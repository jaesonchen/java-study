package com.asiainfo.datastructure;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 二叉堆是完全二叉树或者是近似完全二叉树。
 * 所谓完全二叉树，即高度为n的二叉树，其前n-1层必须被填满，第n层也要从左到右顺序填满。
 * 在二叉堆中，所有非叶子结点的值均不大于（或不小于）其左右孩子的值。
 * 
 * 二叉堆是一种最小值先出的数据结构。
 * 堆的意义就在于：最快的找到最大/最小值，在堆结构中插入一个值重新构造堆结构，取走最大/最小值后重新构造堆结构；其时间复杂度为O(logN)；
 * 堆实践中用途不在于排序，其主要用在调度算法中，比如优先级队列、优先级调度，每次取优先级最高的，时间驱动，取时间最小/等待最长的 等等 ，分为最大堆/最小堆。
 * 
 * 二叉堆一般用数组来表示。根节点在数组中的位置是0，第n个位置的子节点分别在2n+1和 2n+2，奇数坐标为者左节点，偶数坐标为根节点或右节点。
 * 
 * 对于二叉堆我们通常有三种操作：插入、删除和修改：
 * 
 * 插入：首先把要插入的元素添加到数组的末尾，将插入的结点与其父结点比较，若小于其父结点的值，则交换两者。重复此操作，直至该结点不比其父结点小，或者该结点成为根结点。
 * 删除：删除队首元素（根结点）后，会得到左右两棵子树，此时将堆中最后一个元素移到堆顶，然后自上而下调整，将该结点与左右孩子结点比较，
 *       如果两个子节点中较小的节点小于该节点，就将它们交换，直到两个子节点都比此顶点大，或者该节点为叶子节点。
 * 修改：向上或者向下
 * 
 * 二叉堆有两种：最大堆和最小堆。
 * 最大堆：父结点的键值总是大于或等于任何一个子节点的键值。
 * 最小堆：父结点的键值总是小于或等于任何一个子节点的键值。
 * 
 * 
 */
public class BinaryHeap<E extends Comparable<E>> implements Collection<E> {
    
    static final int DEFAULT_CAPACITY = 1 << 6;
    Object[] elements;
    int size;
    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }
    
    public BinaryHeap(int initCapacity) {
        this.elements = new Object[initCapacity];
    }

    public BinaryHeap(Collection<E> collection) {
        this(DEFAULT_CAPACITY);
        this.addAll(collection);
    }

    /* 
     * TODO
     * @return
     * @see java.util.Collection#size()
     */
    @Override
    public int size() {
        return this.size;
    }

    /* 
     * TODO
     * @return
     * @see java.util.Collection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* 
     * TODO
     * @param o
     * @return
     * @see java.util.Collection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {

        if (null == o) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(this.elements[i])) {
                return true;
            }
        }
        return false;
    }

    /* 
     * TODO
     * @return
     * @see java.util.Collection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * TODO
     * @return
     * @see java.util.Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        //System.arraycopy(this.elements, 0, dest, 0, this.size);
        return Arrays.copyOf(this.elements, this.size);
    }

    /* 
     * TODO
     * @param a
     * @return
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] dest) {
        
        if (null == dest || dest.length < this.size) {
            return (T[]) Arrays.copyOf(this.elements, this.size, dest.getClass());
        }
        System.arraycopy(this.elements, 0, dest, 0, this.size);
        return dest;
    }

    /* 
     * TODO
     * @param e
     * @return
     * @see java.util.Collection#add(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        
        if (null == e) {
            throw new NullPointerException();
        }
        ensureCapacity(size + 1);
        int i = size;
        // 奇数坐标为左节点，偶数坐标为右节点
        int parent = (i % 2 == 0) ? (i - 2) >> 1 : (i - 1) >> 1;
        while (parent >= 0) {
            if (e.compareTo((E) elements[parent]) >= 0) {
                break;
            }
            elements[i] = elements[parent];
            i = parent;
            parent = (i % 2 == 0) ? (i - 2) >> 1 : (i - 1) >> 1;
        }
        elements[i] = e;
        size++;
        return true;
    }

    //容量检查
    protected void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            grow(minCapacity);
        }
    }
    
    //容量扩展
    protected void grow(int minCapacity) {
        
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        newCapacity = newCapacity < minCapacity ? minCapacity : newCapacity;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    /* 
     * TODO
     * @param c
     * @return
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {

        ensureCapacity(size + c.size());
        for (E e : c) {
            this.add(e);
        }
        return true;
    }
    
    /**
     * @Description: 移除根节点
     * @author chenzq
     * @date 2019年4月6日 下午12:54:46
     * @return
     */
    @SuppressWarnings("unchecked")
    public E remove() {
        
        if (size == 0) {
            throw new NullPointerException();
        }
        size--;
        E result = (E) this.elements[0];
        E element = (E) this.elements[size];
        int i = 0;
        int child = (i << 1) + 1;
        while (child < size) {
            // 存在右节点
            if ((child + 1) < size) {
                child = ((E) elements[child]).compareTo((E) elements[child + 1]) >= 0 ? child + 1 : child;
            }
            // 是否需要交换
            if (element.compareTo((E) elements[child]) <= 0) {
                break;
            }
            // 交换位置
            elements[i] = elements[child];
            i = child;
            child = (i << 1) + 1;
        }
        this.elements[i] = element;
        return result;
    }
    
    /* 
     * TODO
     * @param o
     * @return
     * @see java.util.Collection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("remove unsupported!");
    }


    /* 
     * TODO
     * @param c
     * @return
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll unsupported!");
    }

    /* 
     * TODO
     * @param c
     * @return
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll unsupported!");
    }

    /* 
     * TODO
     * @param c
     * @return
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll unsupported!");
    }

    /* 
     * TODO
     * @see java.util.Collection#clear()
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            this.elements[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public String toString() {
        return String.valueOf(Arrays.asList(this.toArray()));
    }

    public static void main(String[] args) {
        
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(1);
        heap.add(9);
        heap.add(4);
        heap.add(7);
        heap.add(5);
        heap.add(3);
        System.out.println(heap);
        System.out.println(heap.remove());
        System.out.println(heap);
        System.out.println(heap.remove());
        System.out.println(heap);
        System.out.println(heap.remove());
        System.out.println(heap);
        System.out.println(heap.remove());
        System.out.println(heap);
        System.out.println(heap.remove());
    }
}
