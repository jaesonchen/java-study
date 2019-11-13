
## PriorityBlockingQueue
PriorityBlockingQueue是一种无界阻塞队列，在构造的时候可以指定队列的初始容量。具有如下特点：
    
- PriorityBlockingQueue与之前介绍的阻塞队列最大的不同之处就是：它是一种优先级队列，也就是说元素并不是以FIFO的方式出/入队，而是以按照权重大小的顺序出队；

- PriorityBlockingQueue是真正的无界队列（仅受内存大小限制），它不像ArrayBlockingQueue那样构造时必须指定最大容量，也不像LinkedBlockingQueue默认最大容量为Integer.MAX_VALUE；

- 由于PriorityBlockingQueue是按照元素的权重进行排序，所以队列中的元素必须是可以比较的，也就是说元素必须实现Comparable接口；或者在构造器里指定Comparator

- 由于PriorityBlockingQueue无界队列，所以插入元素永远不会阻塞线程，默认容量为11；

- PriorityBlockingQueue底层是一种基于数组实现的二叉堆结构。

- PriorityBlockingQueue内部也是利用了ReentrantLock来保证并发访问时的线程安全。PriorityBlockingQueue只有一个条件等待队列——notEmpty，因为构造时不会限制最大容量且会自动扩容，所以插入元素并不会阻塞，仅当队列为空时，才可能阻塞“出队”线程。
    
![LinkedBlockingQueue](../../../../resources/images/collection/PriorityBlockingQueue.png) 
    

## put / offer

![LinkedBlockingQueue](../../../../resources/images/collection/PriorityBlockingQueue-put.png) 
    
![LinkedBlockingQueue](../../../../resources/images/collection/PriorityBlockingQueue-put1.png) 
    
```
/**
 * 向队列中插入指定元素.
 * 由于队列是无界的，所以不会阻塞线程.
 */
public void put(E e) {
    offer(e);   // never need to block
}
 
public boolean offer(E e) {
    if (e == null)
        throw new NullPointerException();
 
    final ReentrantLock lock = this.lock;   // 加锁
    lock.lock();
 
    int n, cap;
    Object[] array;
    while ((n = size) >= (cap = (array = queue).length))    // 队列已满, 则进行扩容
        tryGrow(array, cap);
 
    try {
        Comparator<? super E> cmp = comparator;
        if (cmp == null)    // 比较器为空, 则按照元素的自然顺序进行堆调整
            siftUpComparable(n, e, array);
        else                // 比较器非空, 则按照比较器进行堆调整
            siftUpUsingComparator(n, e, array, cmp);
        size = n + 1;       // 队列元素总数+1
        notEmpty.signal();  // 唤醒一个可能正在等待的"出队线程"
    } finally {
        lock.unlock();
    }
    return true;
}

/**
 * 将元素x插入到array[k]的位置.
 * 然后按照元素的自然顺序进行堆调整——"上浮"，以维持"堆"有序.
 * 最终的结果是一个"小顶堆".
 */
private static <T> void siftUpComparable(int k, T x, Object[] array) {
    Comparable<? super T> key = (Comparable<? super T>) x;
    while (k > 0) {
        int parent = (k - 1) >>> 1;     // 相当于(k-1)除2, 就是求k结点的父结点索引parent
        Object e = array[parent];
        if (key.compareTo((T) e) >= 0)  // 如果插入的结点值大于父结点, 则退出
            break;
 
        // 否则，交换父结点和当前结点的值
        array[k] = e;
        k = parent;
    }
    array[k] = key;
}

private void tryGrow(Object[] array, int oldCap) {
    lock.unlock();  // 扩容和入队/出队可以同时进行, 所以先释放全局锁
    Object[] newArray = null;
    if (allocationSpinLock == 0 &&
            UNSAFE.compareAndSwapInt(this, allocationSpinLockOffset,
                    0, 1)) {    // allocationSpinLock置1表示正在扩容
        try {
            // 计算新的数组大小
            int newCap = oldCap + ((oldCap < 64) ?
                    (oldCap + 2) :
                    (oldCap >> 1));
            if (newCap - MAX_ARRAY_SIZE > 0) {    // 溢出判断
                int minCap = oldCap + 1;
                if (minCap < 0 || minCap > MAX_ARRAY_SIZE)
                    throw new OutOfMemoryError();
                newCap = MAX_ARRAY_SIZE;
            }
            if (newCap > oldCap && queue == array)
                newArray = new Object[newCap];  // 分配新数组
        } finally {
            allocationSpinLock = 0;
        }
    }
    if (newArray == null)   // 扩容失败（可能有其它线程正在扩容，导致allocationSpinLock竞争失败）
        Thread.yield();
    
    lock.lock();            // 获取全局锁(因为要修改内部数组queue)
    if (newArray != null && queue == array) {
        queue = newArray;   // 指向新的内部数组
        System.arraycopy(array, 0, newArray, 0, oldCap);
    }
}
```
    


## take / poll

![LinkedBlockingQueue](../../../../resources/images/collection/PriorityBlockingQueue-take.png) 
    
![LinkedBlockingQueue](../../../../resources/images/collection/PriorityBlockingQueue-take1.png) 
    
```
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return dequeue();
    } finally {
        lock.unlock();
    }
}

/**
 * 出队一个元素.
 * 如果队列为空, 则阻塞线程.
 */
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();   // 获取全局锁
    E result;
    try {
        while ((result = dequeue()) == null)    // 队列为空
            notEmpty.await();                   // 线程在noEmpty条件队列等待
    } finally {
        lock.unlock();
    }
    return result;
}
 
private E dequeue() {
    int n = size - 1;   // n表示出队后的剩余元素个数
    if (n < 0)          // 队列为空, 则返回null
        return null;
    else {
        Object[] array = queue;
        E result = (E) array[0];    // array[0]是堆顶结点, 每次出队都删除堆顶结点
        E x = (E) array[n];         // array[n]是堆的最后一个结点, 也就是二叉树的最右下结点
        array[n] = null;
        Comparator<? super E> cmp = comparator;
        if (cmp == null)
            siftDownComparable(0, x, array, n);
        else
            siftDownUsingComparator(0, x, array, n, cmp);
        size = n;
        return result;
    }
}

/**
 * 堆的"下沉"调整.
 * 删除array[k]对应的结点,并重新调整堆使其有序.
 *
 * @param k     待删除的位置
 * @param x     待比较的健
 * @param array 堆数组
 * @param n     堆的大小
 */
private static <T> void siftDownComparable(int k, T x, Object[] array, int n) {
    if (n > 0) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        int half = n >>> 1;           // 相当于n除2, 即找到索引n对应结点的父结点
        while (k < half) {
            /**
             * 下述代码中:
             * c保存k的左右子结点中的较小结点值 
             * child保存较小结点对应的索引
             */
            int child = (k << 1) + 1; // k的左子结点
            Object c = array[child];
 
            int right = child + 1;    // k的右子结点
            if (right < n && ((Comparable<? super T>) c).compareTo((T) array[right]) > 0)
                c = array[child = right];
            
            if (key.compareTo((T) c) <= 0)
                break;
            array[k] = c;
            k = child;
        }
        array[k] = key;
    }
}
```



