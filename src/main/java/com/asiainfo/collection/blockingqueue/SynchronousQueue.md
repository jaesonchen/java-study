
## SynchronousQueue
不像ArrayBlockingQueue或LinkedListBlockingQueue，SynchronousQueue内部并没有数据缓存空间，你不能调用peek()方法来看队列中是否有数据元素，因为数据元素只有当你试着取走的时候才可能存在，遍历队列的操作也是不允许的。队列头元素是第一个排队要插入数据的线程，而不是要交换的数据。数据是在配对的生产者和消费者线程之间直接传递的，并不会将数据缓冲数据到队列中。
    
Java 6的SynchronousQueue的实现采用了一种性能更好的无锁算法 — 扩展的 Dual stack and Dual queue算法。性能比Java5的实现有较大提升。竞争机制提供可选的公平策略（默认非公平模式）。非公平模式通过栈（LIFO）实现，公平模式通过队列（FIFO）实现。使用的数据结构是双重队列（Dual queue）和双重栈（Dual stack）。FIFO通常用于支持更高的吞吐量，LIFO则支持更高的线程局部存储（TLS）。
    
![SynchronousQueue](../../../../resources/images/collection/SynchronousQueue.png) 
    
SynchronousQueue一个典型应用场景是在线程池 Executors.newCachedThreadPool() 就使用了它，这个构造使线程池根据需要（新任务到来时）创建新的线程，如果有空闲线程则会重复使用，线程空闲了60秒后会被回收。
    

## SynchronousQueue的特性
- 使用了双重队列（Dual queue）和双重栈（Dual stack）存储数据，队列中的每个节点都可以是一个生产者或是消费者。
- 通过自旋和 LockSupport 的 park/unpark 实现阻塞，在高争用环境下，自旋可以显著提高吞吐量。
- SynchronousQueue没有容量。与其他BlockingQueue不同，SynchronousQueue是一个不存储元素的BlockingQueue。每一个put操作必须要等待一个take操作，否则不能继续添加元素，反之亦然。
- 因为没有容量，所以对应 peek, contains, clear, isEmpty … 等方法其实是无效的。例如clear是不执行任何操作的，contains始终返回false，peek始终返回null。
- SynchronousQueue分为公平和非公平，默认情况下采用非公平性访问策略（TransferStack），当然也可以通过构造函数来设置为公平性访问策略（fair = true）。
- 若使用 TransferQueue, 则队列中永远会存在一个空节点。
    

## SynchronousQueue的方法
- peek() 永远返回null。 
- put() 往queue放进去一个element以后就一直wait直到有其他thread进来把这个element取走。 
- offer() 往queue里放一个element后立即返回，如果碰巧这个element被另一个thread取走了，offer方法返回true，否则返回false。 
- offer(long timeout, TimeUnit unit) 往queue里放一个element但是等待指定的时间后才返回，返回的逻辑和offer()方法一样。 
- take() 取出并且remove掉queue里的element（认为是在queue里的。。。），取不到东西他会一直等。 
- poll() 取出并且remove掉queue里的element（认为是在queue里的。。。），只有到碰巧另外一个线程正在往queue里offer数据或者put数据的时候，该方法才会取到东西。否则立即返回null。 
- poll(long timeout, TimeUnit unit) 等待指定的时间然后取出并且remove掉queue里的element,其实就是再等其他的thread来往里塞。 
- peek() 永远返回null。 
- isEmpty() 永远是true。 
- remainingCapacity() 永远是0。 
- remove() / removeAll() 永远是false。 
- iterator() 永远返回空，因为里面没东西。 
    

## 内部类

- Transferer：内部抽象类，只有一个transfer方法。SynchronousQueue的put和take被统一为一个方法（transfer方法），因为在双重队列 / 栈数据结构中，put和take操作是对称的，所以几乎所有代码都可以合并。

- TransferStack：继承了内部抽象类 Transferer，实现了transfer方法，用于非公平模式下的队列操作，数据按照LIFO的顺序。内部通过单向链表 SNode 实现的双重栈。

- TransferQueue：继承了内部抽象类 Transferer，实现了transfer方法，用于公平模式下的队列操作，数据按照FIFO的顺序。内部通过单向链表 QNode 实现的双重队列。

- SNode：是双重栈的实现，内部除了基础的链表指针和数据外，还维护了一个int型变量mode，它是实现双重栈的关键字段，有三个取值：0 代表消费者节点（take）,1 代表生产者节点（put），2 代表节点已被匹配。此外还有一个match引用，用于匹配时标识匹配的节点，节点取消等待后match引用指向自身。

- QNode：是双重队列的实现，通过isData实现双重队列。
    


## TransferStack 配对

- 栈为空，或栈顶结点类型与当前入队结点相同。这种情况，调用线程会阻塞；
- 栈顶结点还未配对成功，且与当前入队结点可以配对。这种情况，生成mode= 11 / 10 的节点入栈，与原栈顶节点进行配对；
- 栈顶结点正在配对中(mode= 11 / 10)。这种情况，不进行入栈，寻找下一个结点进行配对。
    
put 操作：
    
![SynchronousQueue](../../../../resources/images/collection/SynchronousQueue-put.png) 
    
take 操作：
    
![SynchronousQueue](../../../../resources/images/collection/SynchronousQueue-take.png) 
    
配对后：
    
![SynchronousQueue](../../../../resources/images/collection/SynchronousQueue-result.png) 
    

### put

```
/**
 * 入队指定元素e.
 * 如果没有另一个线程进行出队操作, 则阻塞该入队线程.
 */
public void put(E e) throws InterruptedException {
    if (e == null) throw new NullPointerException();
    if (transferer.transfer(e, false, 0) == null) {
        Thread.interrupted();
        throw new InterruptedException();
    }
}
```
    

### take

```
/**
 * 出队一个元素.
 * 如果没有另一个线程进行出队操作, 则阻塞该入队线程.
 */
public E take() throws InterruptedException {
    E e = transferer.transfer(null, false, 0);
    if (e != null)
        return e;
    Thread.interrupted();
    throw new InterruptedException();
}
```
    

### offer

```
public boolean offer(E e) {
    if (e == null) throw new NullPointerException();
    return transferer.transfer(e, true, 0) != null;
}
    
public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
    if (e == null) throw new NullPointerException();
    if (transferer.transfer(e, true, unit.toNanos(timeout)) != null)
        return true;
    if (!Thread.interrupted())
        return false;
    throw new InterruptedException();
}
```
    

### poll

```
public E poll() {
    return transferer.transfer(null, true, 0);
}
    
public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    E e = transferer.transfer(null, true, unit.toNanos(timeout));
    if (e != null || !Thread.interrupted())
        return e;
    throw new InterruptedException();
}
```
    


### transfer

```
/**
 * 入队/出队一个元素.
 */
E transfer(E e, boolean timed, long nanos) {
    SNode s = null; // 新创建的结点
    // 入参 e==null, 说明当前是出队线程（消费者）, 否则是入队线程（生产者）
    // 入队线程创建一个DATA结点, 出队线程创建一个REQUEST结点
    int mode = (e == null) ? REQUEST : DATA;

    for (; ; ) {    // 自旋
        SNode h = head; // 栈顶
        if (h == null || h.mode == mode) {          // CASE1: 栈为空 或 栈顶结点类型与当前mode相同，同一类型操作，说明当前没有反向操作的线程      
            if (timed && nanos <= 0) {              // case1.1: 限时等待的情况，比如offer / poll 不带时间参数
                if (h != null && h.isCancelled())   // 栈顶节点状态是cancel （s = s.match），则丢弃栈顶head指向下一个节点，继续自旋
                    casHead(h, h.next);
                else
                    return null;                    // 如果栈为空直接返回null，如果栈顶mode相同且不是cancell状态，直接返回null
            } else if (casHead(h, s = snode(s, e, h, mode))) {  // case1.2 将当前结点压入栈，入栈失败就自旋
                                                                // snode 将next指向h，s == null 新建节点，s != null，设置next = head和mode
                SNode m = awaitFulfill(s, timed, nanos);        // 阻塞当前调用线程，等待被匹配线程唤醒，并返回s.match
                if (m == s) {                                   // 阻塞过程中被中断，s = s.match说明线程被中断，需要清理节点
                    clean(s);   // 清理head开始，状态为cancel的节点
                    return null;
                }

                // 此时m为配对结点，s是被配对节点，如果配对节点刚好是栈顶节点，则将栈顶的配对节点出栈
                if ((h = head) != null && h.next == s)
                    casHead(h, s.next);

                // 入队线程null, 出队线程返回配对结点的值
                return (E) ((mode == REQUEST) ? m.item : s.item);
            }
            // 执行到此处说明入栈失败(多个线程同时入栈导致CAS操作head失败),则进入下一次自旋继续执行

        } else if (!isFulfilling(h.mode)) {          // CASE2: 栈顶结点还未配对成功，mode是 0 或者 1
            if (h.isCancelled())                     // case2.1: 元素取消情况（因中断或超时）的处理
                casHead(h, h.next);                  // 丢弃cancel的节点
            else if (casHead(h, s = snode(s, e,
                h, FULFILLING | mode))) {           // case2.2: 将当前结点压入栈中，mode是二进制 11 或者10
                for (; ; ) {                // 自旋，进行配对
                    SNode m = s.next;       // s.next指向原栈顶结点（也就是与当前结点匹配的结点）
                    if (m == null) {        // m==null说明带匹配的节点被其它线程抢先匹配了, 则跳出循环, 重新下一次自旋
                        casHead(s, null);   // head 指向null
                        s = null;           // 丢弃当前配对状态s节点，继续自旋，重新构建s节点
                        break;
                    }

                    SNode mn = m.next;
                    if (m.tryMatch(s)) {    // 进行结点匹配，cas 带匹配节点m.match = s，并唤醒m.waiter
                        casHead(s, mn);     // 匹配成功, 将匹配的两个结点全部弹出栈
                        return (E) ((mode == REQUEST) ? m.item : s.item);   // 返回匹配值
                    } else                  // 匹配失败，要么被人匹配，要么中断/超时
                        s.casNext(m, mn);   // 移除原待匹配结点，继续自旋，寻找下一个配对节点
                }
            }
        } else {                            // CASE3: 其它线程正在匹配，不入栈，尝试寻找下一个节点进行匹配
            SNode m = h.next;
            if (m == null)                  // 栈顶的next==null, 则直接弹出, 重新进入下一次自旋
                casHead(h, null);
            else {                          // 尝试和其它线程竞争匹配
                SNode mn = m.next;
                if (m.tryMatch(h))
                    casHead(h, mn);         // 匹配成功，直接弹出被匹配节点
                else
                    h.casNext(m, mn);       // 匹配失败（被其它线程抢先匹配成功了），弹出
            }
        }
    }
}

/**
 * 判断m是否已经配对成功.
 */
static boolean isFulfilling(int m) {
    return (m & FULFILLING) != 0;
}

```
    

### SNode

```
static final class SNode {
    volatile SNode next;        // next node in stack
    volatile SNode match;       // the node matched to this
    volatile Thread waiter;     // to control park/unpark
    Object item;                // data; or null for REQUESTs
    int mode;
    
    /**
     * 阻塞当前调用线程, 并将线程信息记录在s.waiter字段上.
     *
     * @param s 等待的结点
     * @return 返回配对的结点 或 当前结点（说明线程被中断了）
     */
    SNode awaitFulfill(SNode s, boolean timed, long nanos) {
        // 超时时间
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        Thread w = Thread.currentThread();
    
        // 性能优化操作（计算自旋次数）
        int spins = (shouldSpin(s) ? (timed ? maxTimedSpins : maxUntimedSpins) : 0);
        for (; ; ) {
            // 判断线程是否被中断了，如果被中断了就取消等待，并设置s.match指向s本身，transfer里会判断，如果节点的s = s.match则进行clean操作
            if (w.isInterrupted())
                s.tryCancel();
            /**
             * s.match 保存当前结点的匹配结点.
             * s.match==null 说明还没有匹配结点
             * s.match==s 说明当前结点s对应的线程被中断了，s.match 由后进来的配对线程设置
             */
            SNode m = s.match;
            if (m != null)
                return m;
            // 定时节点时间到了清除节点
            if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    s.tryCancel();
                    continue;
                }
            }
            // 自旋次数
            if (spins > 0)
                spins = shouldSpin(s) ? (spins - 1) : 0;
            else if (s.waiter == null)  // 还没有匹配结点, 则保存当前线程
                s.waiter = w;           // 保存当前阻塞线程
            else if (!timed)
                // park操作会让线程挂起进入等待状态(Waiting)，需要其他线程调用unpark方法唤醒
                LockSupport.park(this); // 阻塞当前线程
            else if (nanos > spinForTimeoutThreshold)
                // parkNanos操作会让线程挂起进入限期等待(Timed Waiting)，不用其他线程唤醒，时间到了会被系统唤醒
                LockSupport.parkNanos(this, nanos);
        }
    }
    
    /**
     * 尝试将当前结点和s结点配对.
     */
    boolean tryMatch(SNode s) {
        // 设置节点的match
        if (match == null && UNSAFE.compareAndSwapObject(this, matchOffset, null, s)) {
            Thread w = waiter;
            if (w != null) {
                waiter = null;
                LockSupport.unpark(w); // 唤醒当前结点对应的线程
            }
            return true;
        }
        return match == s;      // 配对成功返回true
    }

    // match设置为自身，s.match = s
    void tryCancel() {
        UNSAFE.compareAndSwapObject(this, matchOffset, null, this);
    }
    
    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long itemOffset;
    private static final long nextOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = QNode.class;
            itemOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("item"));
            nextOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("next"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
```



