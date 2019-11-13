
## ArrayBlockingQueue
ArrayBlockingQueue是一个有界队列，底层使用一个数组实现队列，在构造ArrayBlockingQueue时需要指定容量，也就意味着底层数组一旦创建了，容量就不能改变了，因此ArrayBlockingQueue是一个容量限制的阻塞队列。在队列满时执行入队将会阻塞，在队列为空时出队同样将会阻塞。ArrayBlockingQueue的并发阻塞是通过ReentrantLock和Condition来实现的，出队和入队共用同一把可重入锁ReentrantLock。
    
此类支持对等待的生产者线程和使用者线程进行排序的可选公平策略。默认情况下，不保证是这种排序。然而，通过将公平性 (fairness) 设置为 true 而构造的队列允许按照 FIFO 顺序访问线程。公平性通常会降低吞吐量，但也减少了可变性和避免了“不平衡性”。
    
![blockingqueue-array](../../../../resources/images/collection/ArrayBlockingQueue.png) 
    
```
public class ArrayBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
        
    // 存储队列元素的数组
    final Object[] items;

    // 取队列元素的索引，用于take，poll，peek，remove方法
    int takeIndex;

    // 插入队列元素的索引，用于put，offer，add方法
    int putIndex;

    // 队列元素个数
    int count;

    // 出队入队使用同一把可重入锁 
    final ReentrantLock lock;
    // notEmpty条件对象，由lock创建，用于同步队列非空
    private final Condition notEmpty;
    // notFull条件对象，由lock创建，用于同步队列非满
    private final Condition notFull;

    // 默认非公平锁的阻塞队列 
    public ArrayBlockingQueue(int capacity) {
        this(capacity, false);
    }
    public ArrayBlockingQueue(int capacity, boolean fair) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        this.items = new Object[capacity];
        // 初始化ReentrantLock可重入锁，出队入队使用同一把锁 
        lock = new ReentrantLock(fair);
        // 初始化非空等待条件
        notEmpty = lock.newCondition();
        // 初始化非满等待条件
        notFull =  lock.newCondition();
    }
    public ArrayBlockingQueue(int capacity, boolean fair, Collection<? extends E> c) {
        this(capacity, fair);

        final ReentrantLock lock = this.lock;
        lock.lock();    // 这里加锁是用于保证items数组的可见性，由于指令重排序，可能出现先赋值引用再调用初始化
        try {
            int i = 0;
            try {
                for (E e : c) {
                    checkNotNull(e);
                    items[i++] = e;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new IllegalArgumentException();
            }
            count = i;
            putIndex = (i == capacity) ? 0 : i;
        } finally {
            lock.unlock();
        }
    }
}
```
    

### put
put方法在队列尾部插入元素，在队列满时会阻塞线程，直到被 take / poll线程唤醒，可被中断。
    
```
public void put(E e) throws InterruptedException {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        while (count == items.length)
            // 当队列满时，将当前调用线程挂起，添加到notFull条件队列中等待唤醒
            notFull.await();
        // 入队
        enqueue(e);
    } finally {
        lock.unlock();
    }
}

// 入队操作
private void enqueue(E x) {
    final Object[] items = this.items;
    // 通过putIndex索引对数组进行赋值
    items[putIndex] = x;
    // putIndex索引自增，如果已是最后一个位置，重置 putIndex = 0;
    if (++putIndex == items.length)
        putIndex = 0;
    // 队列总数加1
    count++;
    // 唤醒等待在notEmpty上的take线程
    notEmpty.signal();
}
```

![blockingqueue-array-put](../../../../resources/images/collection/ArrayBlockingQueue-put.png) 
    

### take
take方法检索并删除队列头部元素，在队列为空时会阻塞线程，直到被 put / offer线程唤醒，可被中断。
    
```
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        while (count == 0)
            // 当队列为空时，将当前调用线程挂起，添加到notEmpty条件队列中等待唤醒
            notEmpty.await();
        // 出队
        return dequeue();
    } finally {
        lock.unlock();
    }
}

// 出队
private E dequeue() {
    final Object[] items = this.items;
    // 通过takeIndex获取要出队的元素
    @SuppressWarnings("unchecked")
    E x = (E) items[takeIndex];
    // 将takeIndex索引位置设置为null
    items[takeIndex] = null;
    // takeIndex索引自增，如果已是最后一个位置，重置 takeIndex = 0;
    if (++takeIndex == items.length)
        takeIndex = 0;
    // 队列总数减1
    count--;
    // 同时更新迭代器中的元素
    if (itrs != null)
        itrs.elementDequeued();
    // 唤醒等待在notFull上的put线程
    notFull.signal();
    // 返回出队的元素
    return x;
}
```
    
![blockingqueue-array-take](../../../../resources/images/collection/ArrayBlockingQueue-take.png) 
    

### offer
offer方法在队列尾部插入元素，成功返回true，队列满时返回false。带时间参数时，在notFull上等待指定的时间。
    
```
public boolean offer(E e) {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 队列满时，返回false
        if (count == items.length)
            return false;
        else {
            // 入队
            enqueue(e);
            return true;
        }
    } finally {
        lock.unlock();
    }
}
    
public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
    checkNotNull(e);
    long nanos = unit.toNanos(timeout);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 队列满时，在notFull上等待指定时间
        while (count == items.length) {
            if (nanos <= 0)
                return false;
            nanos = notFull.awaitNanos(nanos);
        }
        // 入队
        enqueue(e);
        return true;
    } finally {
        lock.unlock();
    }
}
```
    

### poll
poll方法检索并删除队列头部元素，在队列为空时返回null。带时间参数时，在notEmpty上等待指定的时间。
    
```
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 队列为空时返回null，非空时出队
        return (count == 0) ? null : dequeue();
    } finally {
        lock.unlock();
    }
}

public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    long nanos = unit.toNanos(timeout);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 队列为空时，在notEmpty上等待指定时间
        while (count == 0) {
            if (nanos <= 0)
                return null;
            nanos = notEmpty.awaitNanos(nanos);
        }
        // 出队
        return dequeue();
    } finally {
        lock.unlock();
    }
}
```
    

### peek
peek方法返回队列头部元素，队列为空时返回null。
    
```
    public E peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 返回队列头部元素
            return itemAt(takeIndex); // null when queue is empty
        } finally {
            lock.unlock();
        }
    }
```
    

