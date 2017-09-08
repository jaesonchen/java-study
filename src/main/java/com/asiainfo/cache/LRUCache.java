package com.asiainfo.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 基于LinkedHashMap实现LRU cache
 * 
 * @author       zq
 * @date         2017年8月22日  下午2:04:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LRUCache<K, V> {

	private static final float factor = 0.75f;
	private LinkedHashMap<K, V> map;
	private int total;
	
	/**
	 * 创建LRUCache实例
	 * @param size 缓存中存放的最大数量
	 */
	@SuppressWarnings("serial")
	public LRUCache(int total) {
		this.total = total;
		int capacity = (int) Math.ceil(total / factor) + 1;
		//false=按插入顺序排序、true=按访问顺序排序
		this.map = new LinkedHashMap<K, V>(capacity, factor, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > LRUCache.this.total;
			}
		};
	}
	
	/**
	  * 从缓存中查找节点数据，通过LinkedHashMap实现被访问的节点会成为MRU结点
	  * 
	  * @param key	
	  * @return	key对应的value值，无值为空
	 */
	public synchronized V get(K key) {
		return map.get(key);
	}
	
	/**
	  * 增加结点到缓存中，新加入的节点成为MRU结点。
	  * 如果所加节点在缓存中已存在，则覆盖缓存中值；
	  * 如果缓存中记录数已满，则将LRU结点从缓存中移除
	  * @param key
	  * @param value
	 */
	public synchronized void put(K key,V value) {
		map.put(key, value);
	}
	
	/**
	 * @Description: 清空缓存
	 *
	 */
	public synchronized void clear(){
		map.clear();
	}
	
	/**
	 * @Description: 缓存的结点数
	 * 
	 * @return
	 */
	public synchronized int size() {
		return map.size();
	}
	
	/**
	 * @Description: 返回所有缓存项
	 * 
	 * @return
	 */
	public synchronized Collection<Map.Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K,V>>(map.entrySet());
	}
}
