package com.asiainfo.collection.concurrentmap;

/**
 * jdk7
 * ConcurrentHashMap 是一个 Segment 数组，Segment 通过继承 ReentrantLock 来进行加锁，所以每次需要加锁的操作锁住的是一个 segment，
 * 这样只要保证每个 Segment 是线程安全的，也就实现了全局的线程安全。
 * 
 * Segment 数组初始化后不可扩容，每个Segment元素是一个链式哈希表（类似HashMap，使用segment ReentrantLock独占锁），Segment元素可以扩容。
 * 
 * Segment 数组大小默认是 16，理论上，最多可以同时支持 16 个线程并发写，只要它们的操作分别分布在不同的 Segment 上。
 * 
 * ConcurrentHashMap 初始化时只会初始化 Segment[0]，其他分段在首次put到该分段时初始化，使用Segment[0]的table[].length和loadFactor进行初始化。
 * 其他分段初始化时，会使用while循环和cas进行并发控制。
 * 
 * Segment put时，会首先尝试获取独占锁，如果失败则会继续尝试 MAX_SCAN_RETRIES（单核1多核64），超过次数则park线程。
 * 
 * size()操作
 * 最安全的做法，是在统计size的时候把所有Segment的put，remove和clean方法全部锁住，但是这种做法显然非常低效。
 * 因为在累加count操作过程中，之前累加过的count发生变化的几率非常小。
 * ConcurrentHashMap的做法是先尝试2次通过不锁住Segment的方式来统计各个Segment大小，如果统计的过程中，容器的count发生了变化，
 * 则再采用加锁的方式来统计所有Segment的大小。那么ConcurrentHashMap是如何判断在统计的时候容器是否发生了变化呢？使用modCount变量，
 * 在put, remove和clean方法里操作元素前都会将变量modCount进行加1，那么在统计size前后比较modCount是否发生变化，从而得知容器的大小是否发生变化。
 * 
 * 
 * jdk8
 * Java7 HashMap 的介绍，我们知道，查找的时候，根据 hash 值我们能够快速定位到数组的具体下标，
 * 但是之后的话，需要顺着链表一个个比较下去才能找到我们需要的，时间复杂度取决于链表的长度，为 O(n)。
 * 为了降低这部分的开销，在 Java8 中，当链表中的元素超过了 8 个以后，会将链表转换为红黑树，在这些位置进行查找的时候可以降低时间复杂度为 O(logN)。
 * 根据数组元素中，第一个节点数据类型是 Node 还是 TreeNode 来判断该位置下是链表还是红黑树的。
 * 
 * 
 * 在Java8中，为什么要增加红黑树这种数据结构来进行存储，而不是全部使用链表来进行存储呢？ 
 * 1.因为攻击者可以构造大量具有相同hashCode的内容，使其全部放在同一个列表中，这样，在查找的时候，所花费的时间会很长。
 *   这个时候，如果采用红黑树这个结构来进行存储，那么其查找的效率会高很多。 
 * 2.hashCode()函数的计算有时候并不合理，例如重写hashCode函数的时候。如果都映射到同一个位置，那么查找的时间也会很长。
 * 
 * 
 * 集合大小
 * JDK1.7 和 JDK1.8 对 size 的计算是不一样的。 1.7 中是先不加锁计算三次，如果三次结果不一样在加锁。
 * JDK1.8 size 是通过对 baseCount 和 counterCell 进行 CAS 计算，最终通过 baseCount 和 遍历 CounterCell 数组得出 size。
 * JDK 8 推荐使用mappingCount 方法，因为这个方法的返回值是 long 类型，不会因为 size 方法是 int 类型限制最大值。
 * 
 * 
 * 更新指定键中的值
 * ConcurrentHashMap是一个安全的数据结构（get()、put()操作不会破坏数据结构），但是它的并不保证操作值的安全。
 * 想要安全的更新值可以使用replace(key, oldValue, newValue)
 * 
 * 
 * JDK1.7版本的ReentrantLock + Segment + HashEntry -> JDK1.8版本的synchronized + CAS + HashEntry + 红黑树
 * 数据结构：取消了Segment分段锁的数据结构，取而代之的是数组+链表+红黑树的结构。
 * 线程安全机制：JDK1.7采用segment的分段锁机制实现线程安全，JDK1.8采用CAS+Synchronized保证线程安全。
 * 锁的粒度：原来是对需要进行数据操作的Segment加锁，现调整为对每个数组元素加锁（Node）
 * 链表转化为红黑树: 定位结点的hash算法简化会带来弊端,Hash冲突加剧,因此在链表节点数量大于8时，会将链表转化为红黑树进行存储
 * 查询时间复杂度：从原来的遍历链表O(n)，变成遍历红黑树O(logN)。
 * 
 */
public class ConcurrentHashMapExample {

    public static void main(String[] args) {
    }
}
