package com.asiainfo.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.asiainfo.collection.failfast.CopyOnWriteHashMap;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年3月29日  下午10:35:55
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SimpleCache implements ICache<String, Object> {

	/**缓存对象*/
	private final Map<String, ValueWrapper> objects = new CopyOnWriteHashMap<String, ValueWrapper>();
		
	/* 
	 * @Description: TODO
	 * @param key
	 * @param value
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void put(String key, Object value) {
		this.objects.put(key, new ValueWrapper(value));
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#get(java.lang.Object)
	 */
	@Override
	public Object get(String key) {
		return null == this.objects.get(key) ? null : this.objects.get(key).getValue();
	}

	/* 
	 * TODO
	 * @param key
	 * @param expire
	 * @param unit
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.cache.ICache#get(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Object get(String key, long expire, TimeUnit unit) {
		
		long millis = unit.toMillis(expire);
		ValueWrapper wrapper = this.objects.get(key);
		if (null == wrapper) {
			return null;
		}
		return ((wrapper.getTimestamp() + millis) < System.currentTimeMillis()) ? null : wrapper.getValue();
	}
	
	/* 
	 * @Description: TODO
	 * @param key
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#remove(java.lang.Object)
	 */
	@Override
	public void remove(Object key) {
		this.objects.remove(key);
	}

	/* 
	 * @Description: TODO
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#clear()
	 */
	@Override
	public void clear() {
		this.objects.clear();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return this.objects.keySet();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#values()
	 */
	@Override
	public Collection<Object> values() {

		List<Object> values = new ArrayList<>();
		for (ValueWrapper wrapper : this.objects.values()) {
			values.add(wrapper.getValue());
		}
		return values;
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.objects.containsKey(key);
	}

	/* 
	 * @Description: TODO
	 * @param value
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.objects.containsValue(value);
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.fqc.cache.ICache#size()
	 */
	@Override
	public int size() {
		return this.objects.size();
	}
}
