package com.asiainfo.datastructure;

/**
 * 基于HashMap的Set实现
 * 
 * @author       zq
 * @date         2017年12月26日  下午2:25:37
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HashSet<E> implements Set<E> {

    private static final Object DEFAULT_VALUE = new Object();
    private HashMap<E, Object> map;
    
    public HashSet() {
        map = new HashMap<>();
    }
    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }
    
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Set#size()
     */
    @Override
    public int size() {
        return map.size();
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /* 
     * TODO
     * @param o
     * @return
     * @see com.asiainfo.datastructure.Set#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Set#iterator()
     */
    @Override
    public MyIterator<E> iterator() {
        
        return new MyIterator<E>() {
            
            HashMap.Node<E, Object>[] table = map.table;
            HashMap.Node<E, Object> next;
            int index;
            {
                next = null;
                index = 0;
                if (map.size() > 0) {
                    do {} while (index < table.length && (next = table[index++]) == null);
                }
            }
            
            @Override
            public boolean hasNext() {
                return null != next;
            }

            @Override
            public E previous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public E next() {
                
                HashMap.Node<E, Object> current = next;
                next = null == current ? null : current.next;
                if (next == null) {
                    do {} while (index < table.length && (next = table[index++]) == null);
                }
                return null == current ? null : current.getKey();
            }

            @Override
            public E first() {
                throw new UnsupportedOperationException();
            }

            @Override
            public E last() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Set#toArray()
     */
    @Override
    public E[] toArray() {
        
        Set<E> keys = map.keySet();
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[keys.size()];
        int i = 0;
        for (E e : keys) {
            result[i++] = e;
        }
        return result;
    }

    /* 
     * TODO
     * @param e
     * @return
     * @see com.asiainfo.datastructure.Set#add(java.lang.Object)
     */
    @Override
    public boolean add(E e) {
        return null == map.put(e, DEFAULT_VALUE);
    }

    /* 
     * TODO
     * @param it
     * @return
     * @see com.asiainfo.datastructure.Set#addAll(java.lang.Iterable)
     */
    @Override
    public boolean addAll(Iterable<? extends E> it) {
        
        for (E e : it) {
            map.put(e, DEFAULT_VALUE);
        }
        return true;
    }
    
    /* 
     * TODO
     * @param o
     * @return
     * @see com.asiainfo.datastructure.Set#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        return DEFAULT_VALUE == map.remove(o);
    }

    /* 
     * TODO
     * @see com.asiainfo.datastructure.Set#clear()
     */
    @Override
    public void clear() {
        map.clear();
    }
    
    public static void main(String[] args) {
        
        Set<String> set = new HashSet<>();
        set.add("value1");
        set.add("value2");
        set.add("value3");
        set.add("value4");
        set.add("value5");
        set.add("value6");
        set.add("value7");
        set.add("value8");
        set.add("value3");
        
        System.out.println(set.size());
        for (String str : set) {
            System.out.println(str);
        }
    }
}
