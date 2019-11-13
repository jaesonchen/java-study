
## DelayQueue
DelayQueue是一个支持延时获取元素的无界阻塞队列。底层使用PriorityQueue优先级队列来实现。队列中的元素必须实现Delayed接口，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。
    

DelayQueue的特点：
    
- DelayQueue是无界阻塞队列

- 队列中的元素必须实现Delayed接口，元素过期后才能从队列中取走
    


## 应用场景： 

- 缓存系统的设计：使用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，就表示有缓存到期了。 

- 定时任务调度：使用DelayQueue保存要执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，比如Timer就是使用DelayQueue实现的。
    
- 异步通知的重试：在很多系统中，当用户完成服务调用后，系统有时需要将结果异步通知到用户的某个URI。由于网络等原因，很多时候会通知失败，这个时候就需要一种重试机制。

## Delayed

```
/**
 * Delayed接口，用来标记那些应该在给定延迟时间之后取出的对象。
 * 
 * 此接口的实现必须定义一个 compareTo 方法，该方法提供与此接口的 getDelay 方法一致的排序。
 */
public interface Delayed extends Comparable<Delayed> {

    /**
     * 返回与此对象相关的剩余有效时间，以给定的时间单位表示.
     */
    long getDelay(TimeUnit unit);
}
```
    
可以看到，Delayed接口除了自身的getDelay方法外，还实现了Comparable接口。getDelay方法用于返回对象的剩余有效时间，实现Comparable接口则是为了能够比较两个对象，以便排序。
    
也就是说，如果一个类实现了Delayed接口，当创建该类的对象并添加到DelayQueue中后，只有当该对象的getDalay方法返回的剩余时间≤0时才会出队。
    
另外，由于DelayQueue内部委托了PriorityQueue对象来实现所有方法，所以能以堆的结构维护元素顺序，这样剩余时间最小的元素就在堆顶，每次出队其实就是删除剩余时间≤0的最小元素。
    


## DelayQueue实现

```
public class DelayQueue<E extends Delayed> extends AbstractQueue<E>
        implements BlockingQueue<E> {
    // 可重入锁
    private final transient ReentrantLock lock = new ReentrantLock();
    // 支持优先级的BlockingQueue
    private final PriorityQueue<E> q = new PriorityQueue<E>();
    /**
     * leader线程是首个尝试出队元素（队列不为空）但被阻塞的线程.
     * 该线程会限时等待（队首元素的剩余有效时间），用于唤醒其它等待线程
     */
    private Thread leader = null;
    // 出队线程条件队列, 当有多个线程, 会在此条件队列上等待.
    private final Condition available = lock.newCondition();

    ......
}
```
    
DelayQueue每次只会出队一个过期的元素，如果队首元素没有过期，就会阻塞出队线程，让线程在available这个条件队列上无限等待。
    
为了提升性能，DelayQueue并不会让所有出队线程都无限等待，而是用leader保存了第一个尝试出队的线程，该线程的等待时间是队首元素的剩余有效期。这样，一旦leader线程被唤醒（此时队首元素也失效了），就可以出队成功，然后唤醒一个其它在available条件队列上等待的线程。之后，会重复上一步，新唤醒的线程可能取代成为新的leader线程。这样，就避免了无效的等待，提升了性能。这其实是一种名为“Leader-Follower pattern”的多线程设计模式。
    


### put
    
```
/**
 * 入队一个指定元素e，由于是无界队列, 所以该方法并不会阻塞线程。
 */
public void put(E e) {
    offer(e);
}

public boolean offer(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        q.offer(e);             // 调用PriorityQueue的offer方法
        if (q.peek() == e) {    // 如果入队元素在队首, 则需要唤醒一个出队线程，重新设置leader并调整await时间
            leader = null;
            available.signal();
        }
        return true;
    } finally {
        lock.unlock();
    }
}
```
    
需要注意的是，当首次入队元素时，需要唤醒一个出队线程，因为此时可能已有出队线程在空队列上等待了，如果不唤醒，会导致出队线程永远无法执行。
    

### take
take方法在一个自旋中完成，其实就分为两种情况：
    
1. 队列为空，这种情况直接阻塞出队线程。（在available条件队列等待）

2. 队列非空，要看队首元素的状态（有效期），如果队首元素过期了，那直接出队就行了；如果队首元素未过期，就要看当前线程是否是第一个到达的出队线程（即判断leader是否为空），如果不是，就无限等待，如果是，则限时等待。
    
```
/**
 * 队首出队元素.
 * 如果队首元素（堆顶）未到期或队列为空, 则阻塞线程.
 */
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        for (; ; ) {
            E first = q.peek();     // 读取队首元素
            if (first == null)      // CASE1: 队列为空, 直接阻塞
                available.await();
            else {                  // CASE2: 队列非空
                long delay = first.getDelay(NANOSECONDS);   // 获取队首元素的超时时间
                if (delay <= 0)                             // CASE2.0: 队首元素已过期，出队
                    return q.poll();    // 在finally里唤醒下一个出队线程

                // 执行到此处说明队列非空, 且队首元素未过期
                first = null;           // 避免等待线程持有引用，引发内存泄漏
                if (leader != null)                         // CASE2.1: 已存在leader线程
                    available.await();  // 无限期阻塞当前线程
                else {                                      // CASE2.2: 不存在leader线程
                    Thread thisThread = Thread.currentThread();
                    leader = thisThread;    // 将当前线程置为leader线程
                    try {
                        available.awaitNanos(delay);        // 阻塞当前线程（限时等待剩余有效时间）
                    } finally {
                        if (leader == thisThread)   // 从超时阻塞中醒来 或者 被入队线程唤醒（新入队的元素排在队首）
                            leader = null;          // 如果本身是leader线程，leader设为null，重新自旋
                    }
                }
            }
        }
    } finally {
        if (leader == null && q.peek() != null)             // 出队返回之前，如果不存在leader线程, 则唤醒一个出队线程
            available.signal();
        lock.unlock();
    }
}

public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        E first = q.peek();
        if (first == null || first.getDelay(NANOSECONDS) > 0)
            return null;
        else
            return q.poll();
    } finally {
        lock.unlock();
    }
}
```
    
需要注意，自旋结束后如果leader == null && q.peek() != null，需要唤醒一个等待中的出队线程。
    
leader == null && q.peek() != null的含义就是，没有leader线程但队列中存在元素。leader线程作用之一就是用来唤醒其它无限等待的线程，所以必须要有这个判断。
    























