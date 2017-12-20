package com.asiainfo.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存接口，本系统可以使用得缓存都必须实现该接口，或者通过adapter方式实现该接口
 * 
 * @author       zq
 * @date         2017年11月7日  上午10:18:22
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface ICache<K, V> {

	/**
	 * 增加缓存项
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);
	
	/**
	 * 取得缓存项
	 * 
	 * @param key
	 * @return
	 */
	public ValueWrapper<V> get(K key);
	
	/**
	 * 取得缓存项，超过指定时间的返回null
	 * 
	 * @param key
	 * @param expire
	 * @param unit
	 * @return
	 */
	public ValueWrapper<V> get(K key, long expire, TimeUnit unit);
	
	/**
	 * 删除缓存项
	 * 
	 * @param key
	 */
	public void remove(K key);
	
	/**
	 * 清除整个缓存
	 */
	public void clear();
	
	/**
	 * 返回所有key
	 * 
	 * @return
	 */
	public Set<K> keySet();
	
	/**
	 * 返回所有key，排除失效
	 * 
	 * @param expire
	 * @param unit
	 * @return
	 */
	public Set<K> keySet(long expire, TimeUnit unit);
	
	/**
	 * 返回所有value
	 * 
	 * @return
	 */
	public Collection<ValueWrapper<V>> values();
	
	/**
	 * 返回所有value，排除失效
	 * 
	 * @param expire
	 * @param unit
	 * @return
	 */
	public Collection<ValueWrapper<V>> values(long expire, TimeUnit unit);
	
	/**
	 * 是否包含key
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key);
	
	/**
	 * 是否包含key，判断失效
	 * 
	 * @param key
	 * @param expire
	 * @param unit
	 * @return
	 */
	public boolean containsKey(K key, long expire, TimeUnit unit);
	
	/**
	 * 是否包含value
	 * 
	 * @param value
	 * @return
	 */
	public boolean containsValue(V value);
	
	/**
	 * 是否包含value，判断失效
	 * 
	 * @param value
	 * @param expire
	 * @param unit
	 * @return
	 */
	public boolean containsValue(V value, long expire, TimeUnit unit);
	
	/**
	 * 缓存大小
	 * 
	 * @return
	 */
	public int size();
}
