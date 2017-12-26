package com.asiainfo.datastructure;

import java.util.NoSuchElementException;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年12月25日  下午3:29:07
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LinkedDLNodeList<E> implements List<E> {

    private DLNode<E> head;    //双链表head节点，不包含数据
    private DLNode<E> tail;    //双链表tail节点，不包含数据
    private int size;          //线性表中数据元素的个数
    
    public LinkedDLNodeList() {
        head = new DLNode<E>();
        tail = new DLNode<E>();
        head.setNext(tail);
        tail.setPre(head);
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

        DLNode<E> p = head.getNext();
        int index = 0;
        while (p != null) {
            if (null == obj && null == p.getData() || null != obj && obj.equals(p.getData())) {
                return index;
            }
            p = p.getNext();
            index++;
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

        DLNode<E> node = new DLNode<E>(element);
        DLNode<E> current = node(index);
        current.getPre().setNext(node);
        node.setPre(current.getPre());
        node.setNext(current);
        current.setPre(node);
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

        DLNode<E> node = new DLNode<E>(element);
        tail.getPre().setNext(node);
        node.setPre(tail.getPre());
        node.setNext(tail);
        tail.setPre(node);
        size++;
        return true;
    }

    /* 
     * TODO
     * @param index
     * @return
     * @see com.asiainfo.datastructure.List#get(int)
     */
    @Override
    public E get(int index) {
        return node(index).getData();
    }

    /* 
     * TODO
     * @param index
     * @return
     * @see com.asiainfo.datastructure.List#remove(int)
     */
    @Override
    public E remove(int index) {

        DLNode<E> node = node(index);
        node.getPre().setNext(node.getNext());
        node.getNext().setPre(node.getPre());
        size--;
        return node.getData();
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

        DLNode<E> node = head.getNext();
        while (tail != node) {
            DLNode<E> next = node.getNext();
            node.setNext(null);
            node.setPre(null);
            node.setData(null);
            node = next;
        }
        head.setNext(tail);
        tail.setPre(head);
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
            @Override
            public E previous() {
                if (size == 0 || cursor == 0) {
                    throw new NoSuchElementException();
                }
                return node(--cursor).getData();
            }
            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return node(cursor++).getData();
            }
            @Override
            public E first() {
                if (size == 0) {
                    throw new NoSuchElementException();
                }
                return head.getNext().getData();
            }
            @Override
            public E last() {
                if (size == 0) {
                    throw new NoSuchElementException();
                }
                return tail.getPre().getData();
            }
        };
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.List#toArray()
     */
    @Override
    public E[] toArray() {

        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[size];
        DLNode<E> node = head.getNext();
        int index = 0;
        while (tail != node) {
            result[++index] = node.getData();
            node = node.getNext();
        }
        return result;
    }
    
    //取节点
    protected DLNode<E> node(int index) {
        
        rangeCheck(index);
        DLNode<E> node;
        //from head
        if (index < (size >> 1)) {
            node = head.getNext();
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
        // from tail
        } else {
            node = tail.getPre();
            for (int i = 0; i < (size - index - 1); i++) {
                node = node.getPre();
            }
        }
        return node;
    }
    
    //坐标越界检查
    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        List<String> list = new LinkedDLNodeList<>();
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
