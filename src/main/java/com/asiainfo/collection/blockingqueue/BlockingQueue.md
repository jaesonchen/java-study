
## BlockingQueue

- BlockingQueue：不接受 null 元素。试图 add、put 或 offer 一个 null 元素时，某些实现会抛出 NullPointerException。null 被用作指示 poll 操作失败的警戒值。

- BlockingQueue：实现主要用于生产者-使用者队列，但它另外还支持 Collection 接口。

- BlockingQueue：接口的实现类都必须是线程安全的，实现类一般通过使用内部锁或其他形式的并发控制来保证线程安全。

- BlockingQueue：可以是限定容量的。remainingCapacity()方法用于返回剩余可用容量，对于没有容量限制的BlockingQueue实现，该方法总是返回Integer.MAX_VALUE。
    

```
public interface BlockingQueue<E> extends Queue<E> {

    // 在不违反容量限制的情况下将指定元素插入队列，如果成功返回true, 如果使用具有容量限制的队列时，失败会抛出IllegalStateException。
    // 往限定了长度的队列中设置值，推荐使用offer()方法。
    boolean add(E e);

    // 将给定的元素插入队列中，如果成功返回true, 否则返回false。null在某些方法里具有特定含义，所以不允许null元素，否则抛出NullPointerException。
    boolean offer(E e);

    // 将给定的元素插入队列中，容量不足时可以等待指定时间间隔再插入一次，如果成功返回true, 否则返回false。可中断
    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    // 将给定的元素插入队列中，如果队列中没有多余的空间，该方法会一直阻塞，直到队列中有多余的空间。可中断
    void put(E e) throws InterruptedException;

    // 检索并删除队列的头元素，如果队列为空，线程会一直阻塞，直到队列中有元素，并且该方法成功取得了元素。可中断
    E take() throws InterruptedException;

    // 从队列中检索并删除此队列的头元素，如果队列为空等待指定时间间隔，队列为空时返回null。可中断
    E poll(long timeout, TimeUnit unit) throws InterruptedException;

    // 队列中剩余的空间。
    int remainingCapacity();

    // 从队列中移除指定的元素。
    boolean remove(Object o);

    // 判断队列中是否拥有该元素。
    public boolean contains(Object o);

    // 删除此队列中的所有可用元素并将它们添加到给定集合中。此操作可能比重复轮询此队列更有效。
    int drainTo(Collection<? super E> c);

    // 最多从中删除给定数量的可用元素并将它们添加到给定集合中
    int drainTo(Collection<? super E> c, int maxElements);
}
```
    
BlockingQueue 具有 4 组不同的方法用于插入、移除以及对队列中的元素进行检查。如果请求的操作不能得到立即执行的话，每个方法的表现也不同。
    
| 抛出异常  |  特殊值 | 阻塞 | 超时 |
| :---: | :---: | :---:| :---: |
| 插入 | add(e) | offer(e)  |  put(e) | offer(e, time, unit) |
| 移除 | remove()  |  poll() | take()  poll(time, unit) |
| 检查 | element() |  peek() | 不可用 | 不可用 |
    

四组不同的行为方式： 

- 如果试图的操作无法立即执行，抛一个异常。

- 特定值，如果试图的操作无法立即执行，返回一个特定的值(典型的是 true / false)。

- 阻塞，如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行。

- 超时，如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行，但等待时间不会超过给定值。返回一个特定值(典型的是 true / false)。
    

## BlockingQueue 实现
    
| 队列 | 有界性 | 锁 |  数据结构 |
| :---: | :---: | :---:| :---: |
| ArrayBlockingQueue | bounded(有界) | 加锁 | array |
| LinkedBlockingQueue | optionally-bounded | 加锁  | linkedList |
| PriorityBlockingQueue | unbounded | 加锁  | heap |
| DelayQueue | unbounded | 加锁 | heap |
| SynchronousQueue | bounded | 加锁 | 无 |
| LinkedTransferQueue | unbounded | 加锁  | heap |
| LinkedBlockingDeque | unbounded | 无锁 | heap |
    
- ArrayBlockingQueue：是一个用数组实现的有界阻塞队列，初始化时指定capacity，此队列按照先进先出（FIFO）的原则对元素进行排序。支持公平锁和非公平锁。【注：每一个线程在获取锁的时候可能都会排队等待，如果在等待时间上，先获取锁的线程的请求一定先被满足，那么这个锁就是公平的。反之，这个锁就是不公平的。公平的获取锁，也就是当前等待时间最长的线程先获取锁】

- LinkedBlockingQueue：一个由链表结构组成的有界队列，初始化时指定capacity，默认容量为Integer.MAX_VALUE。此队列按照先进先出的顺序进行排序。

- PriorityBlockingQueue： 一个支持线程优先级排序的无界队列，默认自然排序，也可以自定义实现compareTo()方法来指定元素排序规则，不能保证同优先级元素的顺序。

- DelayQueue： 一个实现PriorityBlockingQueue实现的延迟无界队列，在创建元素时，可以指定多久才能从队列中获取当前元素。只有延时期满后才能从队列中获取元素。（DelayQueue可以运用在以下应用场景：1、缓存系统的设计：可以用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，表示缓存有效期到了。2、定时任务调度。使用DelayQueue保存当天将会执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，从比如TimerQueue就是使用DelayQueue实现的。）

- SynchronousQueue： 一个不存储元素的阻塞队列，每一个put操作必须等待take操作，否则不能添加元素。支持公平锁和非公平锁。SynchronousQueue的一个使用场景是在线程池里。Executors.newCachedThreadPool()就使用了SynchronousQueue，线程池根据需要（新任务到来时）创建新的线程，如果有空闲线程则会重复使用，线程空闲了60秒后会被回收。

- LinkedTransferQueue： 一个由链表结构组成的无界阻塞队列，相当于其它队列，LinkedTransferQueue队列多了transfer和tryTransfer方法。

- LinkedBlockingDeque： 一个由链表结构组成的无界双向阻塞队列。队列头部和尾部都可以添加和移除元素，多线程并发时，可以将锁的竞争最多降到一半。
    



## 线程池与BlockingQueue

从Java5开始，Java提供了自己的线程池 java.util.concurrent.ThreadPoolExecutor。线程池底层的等待队列使用了阻塞队列。
    
```
public ThreadPoolExecutor(int corePoolSize,                 // 核心线程数
                              int maximumPoolSize,          // 最大线程数
                              long keepAliveTime,           // 空闲时间
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,    // 待执行任务等待队列
                              ThreadFactory threadFactory,          // 线程工厂，用于生成线程，标注线程名称等
                              RejectedExecutionHandler handler)     // 拒绝策略，添加任务失败时的处理策略
```
    
### LinkedBlockingQueue

```
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>());
}

public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```
    
### SynchronousQueue

```
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                60L, TimeUnit.SECONDS,
                                new SynchronousQueue<Runnable>());
}
```
    
### DelayedWorkQueue

```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}

public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue());
}
```


