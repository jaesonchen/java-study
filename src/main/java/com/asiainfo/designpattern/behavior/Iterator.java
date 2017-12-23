package com.asiainfo.designpattern.behavior;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:35:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Iterator {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        MyList<String> list = new MyList<>();
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
    }

    interface MyIterator<E> {
        
        boolean hasNext();
        E previous();
        E next();
        E first();
        E last();
    }
    
    static class MyList<E> {
        
        int size;
        E[] elements;
        
        @SuppressWarnings("unchecked")
        public MyList() {
            elements = (E[]) new Object[8];
        }
        @SuppressWarnings("unchecked")
        public MyList(int initialCapacity) {
            elements = (E[]) new Object[initialCapacity];
        }
        public boolean isEmpty() {
            return size == 0;
        }
        public int size() {
            return size;
        }
        public E[] toArray() {
            return Arrays.copyOf(elements, size);
        }
        public void add(E t) {
            ensureCapacity(size + 1);
            elements[size++] = t;
        }
        public E get(int index) {
            
            rangeCheck(index);
            return elements[index];
        }
        public E remove(int index) {

            rangeCheck(index);
            E oldValue = elements[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elements, index + 1, elements, index, numMoved);
            }
            elements[--size] = null;
            return oldValue;
        }
        public boolean remove(Object obj) {
            
            int index = indexOf(obj);
            if (index >= 0) {
                remove(index);
                return true;
            }
            return false;
        }
        public boolean contains(Object obj) {
            return indexOf(obj) >= 0;
        }
        protected int indexOf(Object obj) {
            
            for (int i = 0; i < size; i++) {
                if (null == obj && null == elements[i] || null != obj && obj.equals(elements[i])) {
                    return i;
                }
            }
            return -1;
        }
        protected void rangeCheck(int index) {
            if (index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }
        protected void ensureCapacity(int minCapacity) {
            if (minCapacity > elements.length) {
                grow(minCapacity);
            }
        }
        protected void grow(int minCapacity) {
            
            int oldCapacity = elements.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            newCapacity = newCapacity < minCapacity ? minCapacity : newCapacity;
            elements = Arrays.copyOf(this.elements, newCapacity);
        }
        
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
                    return elements[--cursor];
                }
                @Override
                public E next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return elements[cursor++];
                }
                @Override
                public E first() {
                    if (size == 0) {
                        throw new NoSuchElementException();
                    }
                    return elements[0];
                }
                @Override
                public E last() {
                    if (size == 0) {
                        throw new NoSuchElementException();
                    }
                    return elements[size - 1];
                }
            };
        }
    }
}
