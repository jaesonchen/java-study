package com.asiainfo.datastructure;

import java.util.Objects;

/**
 * 基于BinaryTree的Map实现
 * 
 * @author       zq
 * @date         2017年12月28日  下午3:37:07
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class TreeMap<K, V> implements Map<K, V> {

    private BinaryTree<TreeNode<K, V>> tree = new BinaryTree<TreeNode<K, V>>();
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        @SuppressWarnings("unchecked")
        BinaryTreeNode<TreeNode<K, V>> node = tree.find(new TreeNode<K, V>((K) key, (V) null));
        return null != node;
    }

    @Override
    public V get(Object key) {
        @SuppressWarnings("unchecked")
        BinaryTreeNode<TreeNode<K, V>> node = tree.find(new TreeNode<K, V>((K) key, (V) null));
        return null == node ? null : node.getData().getValue();
    }

    @Override
    public V put(K key, V value) {
        
        BinaryTreeNode<TreeNode<K, V>> node = tree.find(new TreeNode<K, V>(key, value));
        V oldValue = null;
        if (null != node) {
            oldValue = node.getData().getValue();
            node.getData().setValue(value);
        } else {
            tree.insert(new TreeNode<K, V>(key, value));
            size++;
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        
        @SuppressWarnings("unchecked")
        BinaryTreeNode<TreeNode<K, V>> node = tree.remove(new TreeNode<K, V>((K) key, (V) null));
        if (null != node) {
            size--;
            return node.getData().getValue();
        }
        return null;
    }

    @Override
    public void clear() {
        tree.setRoot(null);
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        
        Set<K> result = new HashSet<>();
        List<BinaryTreeNode<TreeNode<K, V>>> list = tree.midOrder();
        for (BinaryTreeNode<TreeNode<K, V>> node : list) {
            result.add(node.getData().getKey());
        }
        return result;
    }

    @Override
    public List<V> values() {
        
        List<V> result = new ArrayList<>();
        List<BinaryTreeNode<TreeNode<K, V>>> list = tree.midOrder();
        for (BinaryTreeNode<TreeNode<K, V>> node : list) {
            result.add(node.getData().getValue());
        }
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        
        Set<Map.Entry<K, V>>  result = new HashSet<>();
        List<BinaryTreeNode<TreeNode<K, V>>> list = tree.midOrder();
        for (BinaryTreeNode<TreeNode<K, V>> node : list) {
            result.add(node.getData());
        }
        return result;
    }

    static class TreeNode<K, V> implements Map.Entry<K, V>, Comparable<TreeNode<K, V>> {
        
        final K key;
        V value;
        TreeNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override public final K getKey()        { return key; }
        @Override public final V getValue()      { return value; }
        @Override public final String toString() { return key + "=" + value; }
        @Override public V setValue(V value) {
            this.value = value;
            return value;
        }
        @Override public final int hashCode() {
            return Objects.hashCode(key);
        }
        @Override public final boolean equals(Object o) {
            
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey())) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public int compareTo(TreeNode<K, V> obj) {
            return equals(obj) ? 0 : (hashCode() == obj.hashCode() ? 
                    Objects.hashCode(value) - Objects.hashCode(obj.getValue()) : hashCode() - obj.hashCode());
        }
    }
    
    public static void main(String[] args) {
        
        Map<String, String> map = new TreeMap<>();
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
        map.put("key10", "value101");
        System.out.println(map.size());
        System.out.println(map.containsKey("key2"));
        System.out.println(map.remove("key9"));
        System.out.println(map.remove("key20"));
        System.out.println(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry);
        }
    }
}
