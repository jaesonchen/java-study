package com.asiainfo.datastructure;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年12月26日  上午11:21:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface Map<K, V> {

    int size();
    boolean isEmpty();
    boolean containsKey(Object key);
    V get(Object key);
    V put(K key, V value);
    V remove(Object key);
    void clear();
    Set<K> keySet();
    List<V> values();
    Set<Map.Entry<K, V>> entrySet();
    
    interface Entry<K,V> {
        K getKey();
        V getValue();
        V setValue(V value);
    }
}
