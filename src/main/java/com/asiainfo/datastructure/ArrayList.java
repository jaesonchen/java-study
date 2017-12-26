package com.asiainfo.datastructure;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 列表数组实现
 * 
 * @author       zq
 * @date         2017年12月25日  上午10:47:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ArrayList<E> implements List<E> {
    
	private final int DEFAULT_CAPACITY = 8;    //数组的默认大小
	private int size;                          //线性表中数据元素的个数
	private Object[] elements;                      //数据元素数组

	public ArrayList() {
	    elements = new Object[DEFAULT_CAPACITY];
	}
	public ArrayList (int initialCapacity) {
	    elements = new Object[initialCapacity];
	}
	
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.List#size()
     */
    @Override
    public int size() {
        return size;
    }
    
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /* 
     * TODO
     * @param obj
     * @return
     * @see com.asiainfo.datastructure.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }
    
    /* 
     * TODO
     * @param obj
     * @return
     * @see com.asiainfo.datastructure.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(Object obj) {

        for (int i = 0; i < size; i++) {
            if (null == obj && null == elements[i] || null != obj && obj.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }
    
    /* 
     * TODO
     * @param index
     * @param element
     * @see com.asiainfo.datastructure.List#add(int, java.lang.Object)
     */
    @Override
    public void add(int index, E element) {

        rangeCheck(index);
        ensureCapacity(size + 1);
        int numMoved = size - index;
        System.arraycopy(elements, index, elements, index + 1, numMoved);
        elements[index] = element;
        size++;
    }
    
    /* 
     * TODO
     * @param element
     * @return
     * @see com.asiainfo.datastructure.List#add(java.lang.Object)
     */
    @Override
    public boolean add(E element) {

        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }
    
    /* 
     * TODO
     * @param index
     * @return
     * @see com.asiainfo.datastructure.List#get(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        
        rangeCheck(index);
        return (E) elements[index];
    }
    
    /* 
     * TODO
     * @param index
     * @return
     * @see com.asiainfo.datastructure.List#remove(int)
     */
    @Override
    public E remove(int index) {

        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return oldValue;
    }
    
    /* 
     * TODO
     * @param obj
     * @return
     * @see com.asiainfo.datastructure.List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object obj) {

        int index = indexOf(obj);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }
    
    /* 
     * TODO
     * @see com.asiainfo.datastructure.List#clear()
     */
    @Override
    public void clear() {
        
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }
    
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.List#iterator()
     */
    @Override
    public MyIterator<E> iterator() {

        return new MyIterator<E>() {
            
            int cursor;
            @Override
            public boolean hasNext() {
                return cursor != size;
            }
            @SuppressWarnings("unchecked")
            @Override
            public E previous() {
                if (size == 0 || cursor == 0) {
                    throw new NoSuchElementException();
                }
                return (E) elements[--cursor];
            }
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[cursor++];
            }
            @SuppressWarnings("unchecked")
            @Override
            public E first() {
                if (size == 0) {
                    throw new NoSuchElementException();
                }
                return (E) elements[0];
            }
            @SuppressWarnings("unchecked")
            @Override
            public E last() {
                if (size == 0) {
                    throw new NoSuchElementException();
                }
                return (E) elements[size - 1];
            }
        };
    }
    
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.List#toArray()
     */
    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        return (E[]) Arrays.copyOf(elements, size);
    }

    //坐标越界检查
    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
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
    
    public static void main(String[] args) {
        
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("112");
        list.add("113");
        list.add("114");
        list.add(null);
        list.add("116");
        list.add("117");
        list.add("118");
        list.add("119");
        list.add("120");
        
        System.out.println(list.contains("113"));
        System.out.println(list.contains(null));
        System.out.println(list.contains("130"));
        System.out.println(list.remove(3));
        System.out.println(list.remove(null));
        System.out.println(list.remove("117"));
        System.out.println(list.remove("130"));
        
        MyIterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        for (String str : list) {
            System.out.println(str);
        }
    }
}
