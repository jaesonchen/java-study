package com.asiainfo.datastructure;

import java.util.NoSuchElementException;

/**
 * 列表链式实现
 * 
 * @author       zq
 * @date         2017年12月25日  下午3:16:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LinkedList<E> implements List<E> {

	private SLNode<E> head;    //单链表head节点，不包含数据
	private SLNode<E> tail;    //队尾节点引用
	private int size;          //线性表中数据元素的个数

	public LinkedList () {
	    head = new SLNode<E>();
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

        SLNode<E> p = head.getNext();
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
        
        SLNode<E> node = new SLNode<E>(element);
        SLNode<E> preNode = preNode(index);
        SLNode<E> nextNode = preNode.getNext();
        preNode.setNext(node);
        node.setNext(nextNode);
        if (nextNode == null) {
            tail = node;
        }
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
        
        SLNode<E> node = new SLNode<E>(element);
        if (size == 0) {
            head.setNext(node);
        } else {
            tail.setNext(node);
        }
        tail = node;
        size++;
        return true;
    }
    
    /* 
     * TODO
     * @param it
     * @see com.asiainfo.datastructure.List#addAll(java.lang.Iterable)
     */
    @Override
    public void addAll(Iterable<? extends E> it) {

        if (null != it) {
            for (E e : it) {
                add(e);
            }
        }
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
        
        SLNode<E> preNode = preNode(index);
        SLNode<E> removeNode = preNode.getNext();
        SLNode<E> nextNode = removeNode.getNext();
        preNode.setNext(nextNode);
        if (nextNode == null) {
            tail = preNode;
        }
        size--;
        return removeNode.getData();
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
        
        tail = null;
        SLNode<E> node = head.getNext();
        head.setNext(null);
        while (null != node) {
            SLNode<E> next = node.getNext();
            node.setNext(null);
            node.setData(null);
            node = next;
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
                return tail.getData();
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
        SLNode<E> node = head.getNext();
        int index = 0;
        while (null != node) {
            result[++index] = node.getData();
            node = node.getNext();
        }
        return result;
    }
    
    //节点的前一个节点
    protected SLNode<E> preNode(int index) {
        
        rangeCheck(index);
        SLNode<E> preNode = head;
        for (int i = 0; i < index; i++) {
            preNode = preNode.getNext();
        }
        return preNode;
    }
    
    //取节点
    protected SLNode<E> node(int index) {
        
        rangeCheck(index);
        SLNode<E> node = head.getNext();
        for (int i = 0; i < index; i++) {
            node = node.getNext();
        }
        return node;
    }
    
    //坐标越界检查
    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    public static void main(String[] args) {
        
        List<String> list = new LinkedList<>();
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