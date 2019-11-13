
## LinkedBlockingQueue
LinkedBlockingQueue是一个基于链表实现的可选容量的阻塞队列，内部维持一个队列，有一个头节点head和一个尾节点last，内部维持两把锁，一个用于入队，一个用于出队，还有锁关联的Condition对象。虽然入队和出队两个操作同时均只能有一个线程操作，但是一个入队线程和一个出队线程可以共同执行，也就意味着可能同时有两个线程在操作队列，为了维持线程安全，LinkedBlockingQueue使用一个AtomicInterger类型的变量count表示当前队列中含有的元素个数，可以确保两个线程之间操作底层队列是线程安全的。
    
![LinkedBlockingQueue](../../../../resources/images/collection/LinkedBlockingQueue.png) 
    
```
public class LinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
        
    // 节点类，用于存储数据
    static class Node<E> {
        E item;
        Node<E> next;

        Node(E x) { item = x; }
    }
    // 容量
    private final int capacity;

    // 元素个数，因为有2个锁，存在竞态条件，使用AtomicInteger
    private final AtomicInteger count = new AtomicInteger(0);

    // 头结点
    private transient Node<E> head;

    // 尾节点
    private transient Node<E> last;

    // 获取并移除元素时使用的锁，如take, poll
    private final ReentrantLock takeLock = new ReentrantLock();

    // notEmpty条件对象，当队列没有数据时用于挂起执行删除的线程
    private final Condition notEmpty = takeLock.newCondition();

    // 添加元素时使用的锁如 put, offer
    private final ReentrantLock putLock = new ReentrantLock();

    // notFull条件对象，当队列数据已满时用于挂起执行添加的线程 
    private final Condition notFull = putLock.newCondition();
    
    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        // 初始化一个空节点作为链表头节点
        last = head = new Node<E>(null);
    }
    
    ......
}
```
    
![LinkedBlockingQueue-node](../../../../resources/images/collection/LinkedBlockingQueue-node.png) 
    

## put

```
public void put(E e) throws InterruptedException {
    if (e == null) throw new NullPointerException();
    int c = -1;
    // 新建节点
    Node<E> node = new Node<E>(e);
    final ReentrantLock putLock = this.putLock;
    final AtomicInteger count = this.count;
    // 获得putLock
    putLock.lockInterruptibly();
    try {
        // 如果队列已满，将调用线程加入notFull的等待队列中
        while (count.get() == capacity) {
            notFull.await();
        }
        // 入队
        enqueue(node);
        // 队列元素个数加1
        c = count.getAndIncrement();
        // 队列未满，唤醒等待在notFull上的put / offer线程
        if (c + 1 < capacity)
            notFull.signal();
    } finally {
        putLock.unlock();
    }
    // 入队前，队列为空，入队后唤醒等待在notEmpty的take / poll线程。
    // 只需要在队列为空时的入队后唤醒一次takeLock锁上的等待线程，其他时候take / poll线程在count > 0时会其他唤醒出队线程。
    if (c == 0)
        signalNotEmpty();
}
// 入队
private void enqueue(Node<E> node) {
    // 新节点插入队列的最后
    last = last.next = node;
}
// 获取takeLock锁，唤醒等待在notEmpty的take / poll线程
private void signalNotEmpty() {
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
}
```
    

## take

```
public E take() throws InterruptedException {
    E x;
    int c = -1;
    final AtomicInteger count = this.count;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lockInterruptibly();
    try {
        // 如果队列为空，将调用线程加入notEmpty的等待队列中
        while (count.get() == 0) {
            notEmpty.await();
        }
        // 出队
        x = dequeue();
        // 队列元素个数减1
        c = count.getAndDecrement();
        // 队列非空，唤醒等待在notEmpty上的take / poll 线程
        if (c > 1)
            notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
    // 出队前，队列处于满状态，唤醒等待在notFull的put / offer线程
    // 只需要在队列满状态的出队后唤醒一次putLock锁上的等待线程，其他时候put / offer线程在count < capacity时会唤醒其他入队线程。
    if (c == capacity)
        signalNotFull();
    return x;
}
// 头节点出队
private E dequeue() {
    Node<E> h = head;
    // 出队的节点
    Node<E> first = h.next;
    h.next = h; // help GC
    // head指向出队的节点
    head = first;
    // 出队节点的值
    E x = first.item;
    // 头节点设为空节点
    first.item = null;
    return x;
}
// 获取putLock锁，唤醒等待在notFull的put / offer线程
private void signalNotFull() {
    final ReentrantLock putLock = this.putLock;
    putLock.lock();
    try {
        notFull.signal();
    } finally {
        putLock.unlock();
    }
}
```
    

## offer

```
public boolean offer(E e) {
    if (e == null) throw new NullPointerException();
    final AtomicInteger count = this.count;
    // 队列已满返回false，不再尝试获取锁
    if (count.get() == capacity)
        return false;
    int c = -1;
    // 新建节点
    Node<E> node = new Node<E>(e);
    final ReentrantLock putLock = this.putLock;
    putLock.lock();
    try {
        // 队列未满，插入节点到队列最后。如果入队后，队列未满则唤醒等待在notFull的其他put / offer 线程
        if (count.get() < capacity) {
            // 节点入队
            enqueue(node);
            // 队列元素个数加1
            c = count.getAndIncrement();
            // 队列未满则唤醒等待在notFull的其他put / offer 线程
            if (c + 1 < capacity)
                notFull.signal();
        }
    } finally {
        putLock.unlock();
    }
    // 入队前，队列为空，入队后唤醒等待在notEmpty的take / poll线程。
    // 只需要在队列为空时的入队后唤醒一次takeLock锁上的等待线程，其他时候take / poll线程在count > 0时会其他唤醒出队线程。
    if (c == 0)
        signalNotEmpty();
    // c 不等初始值 -1就是成功入队
    return c >= 0;
}

public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {

    if (e == null) throw new NullPointerException();
    long nanos = unit.toNanos(timeout);
    int c = -1;
    final ReentrantLock putLock = this.putLock;
    final AtomicInteger count = this.count;
    putLock.lockInterruptibly();
    try {
        // 在notFull上等待指定时间
        while (count.get() == capacity) {
            // 等待指定时间，队列仍然已满返回false
            if (nanos <= 0)
                return false;
            nanos = notFull.awaitNanos(nanos);
        }
        enqueue(new Node<E>(e));
        c = count.getAndIncrement();
        if (c + 1 < capacity)
             notFull.signal();
    } finally {
        putLock.unlock();
    }
    if (c == 0)
        signalNotEmpty();
    return true;
}
```
    


## poll

```
public E poll() {
    final AtomicInteger count = this.count;
    // 队列为空返回null，不再尝试获取锁
    if (count.get() == 0)
        return null;
    E x = null;
    int c = -1;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        // 队列非空，删除头节点。如果出队后，队列非空则唤醒等待在notEmpty的其他take / poll 线程
        if (count.get() > 0) {
            // 出队
            x = dequeue();
            // 队列元素个数减1
            c = count.getAndDecrement();
            // 队列非空，唤醒等待在notEmpty上的take / poll 线程
            if (c > 1)
                notEmpty.signal();
        }
    } finally {
        takeLock.unlock();
    }
    // 出队前，队列处于满状态，唤醒等待在notFull的put / offer线程
    // 只需要在队列满状态的出队后唤醒一次putLock锁上的等待线程，其他时候put / offer线程在count < capacity时会唤醒其他入队线程。
    if (c == capacity)
        signalNotFull();
    return x;
}

public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    E x = null;
    int c = -1;
    long nanos = unit.toNanos(timeout);
    final AtomicInteger count = this.count;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lockInterruptibly();
    try {
        // 在notEmpty上等待指定时间
        while (count.get() == 0) {
            // 等待指定时间，队列仍然为空返回null
            if (nanos <= 0)
                return null;
            nanos = notEmpty.awaitNanos(nanos);
        }
        x = dequeue();
        c = count.getAndDecrement();
        if (c > 1)
        notEmpty.signal();
    } finally {
            takeLock.unlock();
    }
    if (c == capacity)
        signalNotFull();
    return x;
}
```


## peek

```
public E peek() {
    // 队列为空，直接返回null
    if (count.get() == 0)
        return null;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        // 返回第一个节点
        Node<E> first = head.next;
        if (first == null)
            return null;
        else
            return first.item;
    } finally {
        takeLock.unlock();
    }
}
```
    


## LinkedBlockingQueue和ArrayBlockingQueue 的区别
- 队列大小不同。ArrayBlockingQueue初始构造时必须指定大小，而LinkedBlockingQueue构造时既可以指定大小，也可以不指定（默认为Integer.MAX_VALUE，近似于无界）；
- 底层数据结构不同。ArrayBlockingQueue底层采用数组作为数据存储容器，而LinkedBlockingQueue底层采用单链表作为数据存储容器；
- 加锁机制不同。ArrayBlockingQueue使用一把全局锁，即入队和出队使用同一个ReentrantLock锁；而LinkedBlockingQueue进行了锁分离，入队使用一个ReentrantLock锁（putLock），出队使用另一个ReentrantLock锁（takeLock）；
- LinkedBlockingQueue不能指定公平/非公平策略（默认都是非公平），而ArrayBlockingQueue可以指定策略。
