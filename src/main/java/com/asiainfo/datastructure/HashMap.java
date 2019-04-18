package com.asiainfo.datastructure;

import java.util.Objects;

/**
 * key 哈希map实现
 * 
 * @author       zq
 * @date         2017年12月26日  下午2:25:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HashMap<K, V> implements Map<K, V> {
    
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    Node<K, V>[] table;
    int size;
    int threshold;
    final float loadFactor;
    
    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity / loadFactor);
        this.table = (Node<K, V>[]) new Node[threshold];
    }
    
    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Map#size()
     */
    @Override
    public int size() {
        return size;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.datastructure.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return null != getNode(hash(key), key);
    }

    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.datastructure.Map#get(java.lang.Object)
     */
    @Override
    public V get(Object key) {
        
        Node<K, V> node = getNode(hash(key), key);
        return null == node ? null : node.getValue();
    }

    /* 
     * TODO
     * @param key
     * @param value
     * @return
     * @see com.asiainfo.datastructure.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        
        checkCapacity(size + 1);
        int hash = hash(key);
        Node<K, V> node = getNode(hash, key);
        V oldValue = null;
        if (null == node) {
            Node<K, V> first = table[hash % table.length];
            table[hash % table.length] = new Node<K, V>(hash, key, value, null == first ? null : first);
            size++;
        } else {
            oldValue = node.getValue();
            node.setValue(value);
        }
        return oldValue;
    }

    /* 
     * TODO
     * @param map
     * @see com.asiainfo.datastructure.Map#putAll(com.asiainfo.datastructure.Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

        checkCapacity(size + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.datastructure.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(Object key) {
        
        int hash = hash(key);
        if (containsKey(key)) {
            Node<K, V> node = table[hash % table.length];
            Node<K, V> pre = null;
            while (null != node) {
                if (null == key && null == node.getKey() || null != key && key.equals(node.getKey())) {
                    break;
                }
                pre = node;
                node = node.next;
            }
            if (null == pre) {
                table[hash % table.length] = node.next;
            } else {
                pre.next = node.next;
            }
            size--;
            return node.getValue();
        }
        return null;
    }

    /* 
     * TODO
     * @see com.asiainfo.datastructure.Map#clear()
     */
    @Override
    public void clear() {
        
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            Node<K, V> next = null;
            while (null != node) {
                next = node.next;
                node.next = null;
                node = next;
            }
            table[i] = null;
        }
        size = 0;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Map#keySet()
     */
    @Override
    public Set<K> keySet() {

        Set<K> result = new HashSet<>();
        for (Map.Entry<K, V> entry : entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Map#values()
     */
    @Override
    public List<V> values() {

        List<V> result = new ArrayList<>();
        for (Map.Entry<K, V> entry : entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.datastructure.Map#entrySet()
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        
        Set<Map.Entry<K, V>> result = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (null != node) {
                result.add(node);
                node = node.next;
            }
        }
        return result;
    }
    
    protected void checkCapacity(int newCapacity) {
        
        if (newCapacity > this.threshold) {
            int capacity = table.length;
            capacity = capacity << 1;
            this.threshold = (int) (capacity / this.loadFactor) < newCapacity ? newCapacity : (int) (capacity / this.loadFactor);
            resize(this.threshold);
        }
    }
    
    protected void resize(int capacity) {
        
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[(int) (capacity * this.loadFactor)];
        for (Map.Entry<K, V> entry : entrySet()) {
            addToTable(newTable, entry.getKey(), entry.getValue());
        }
        table = newTable;
    }
    
    protected void addToTable(Node<K, V>[] newTable, K key, V value) {
        
        int hash = hash(key);
        Node<K, V> first = newTable[hash % newTable.length];
        newTable[hash % newTable.length] = new Node<K, V>(hash, key, value, null == first ? null : first);
    }
    
    protected Node<K, V> getNode(int hash, Object key) {
        
        Node<K, V> node = table[hash % table.length];
        while (null != node) {
            if (null == key && node.getKey() == null || null != key && key.equals(node.getKey())) {
                break;
            }
            node = node.next;
        }
        return node;
    }
    
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }
    
    static class Node<K, V> implements Map.Entry<K, V> {
        
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override public final K getKey()        { return key; }
        @Override public final V getValue()      { return value; }
        @Override public final String toString() { return key + "=" + value; }
        @Override public V setValue(V value) {
            this.value = value;
            return value;
        }
        @Override public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override public final boolean equals(Object o) {
            
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static void main(String[] args) {
        
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        map.put("key5", "value5");
        map.put("key6", "value6");
        map.put("key7", "value7");
        map.put("key8", "value8");
        map.put("key9", "value9");
        map.put("key10", "value10");
        map.put("key11", "value11");
        map.put("key12", "value12");
        map.put("key13", "value13");
        map.put("key14", "value14");
        map.put("key15", "value15");
        map.put("key16", "value16");
        map.put("key17", "value16");
        map.put("key18", "value16");
        map.put("key19", "value16");
        map.put("key20", "value16");
        map.put("key21", "value16");
        map.put("key22", "value16");
        map.put("key23", "value16");
        map.put("key24", "value16");
        map.put("key25", "value16");
        map.put("key26", "value16");
        map.put("key27", "value16");
        map.put("key28", "value16");
        System.out.println(map.size());
        System.out.println(map.containsKey("key2"));
        System.out.println(map.remove("key10"));
        System.out.println(map.remove("key20"));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry);
        }
    }
}
