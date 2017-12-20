package com.asiainfo.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
	private final Map<String, ValueWrapper<Object>> objects = new CopyOnWriteHashMap<>();

    /* 
     * TODO
     * @param key
     * @param value
     * @see com.asiainfo.cache.ICache#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public void put(String key, Object value) {
        this.objects.put(key, new ValueWrapper<Object>(value));
    }

    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.cache.ICache#get(java.lang.Object)
     */
    @Override
    public ValueWrapper<Object> get(String key) {
        return this.objects.get(key);
    }

    /* 
     * TODO
     * @param key
     * @param expire
     * @param unit
     * @return
     * @see com.asiainfo.cache.ICache#get(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public ValueWrapper<Object> get(String key, long expire, TimeUnit unit) {
        
        long millis = unit.toMillis(expire);
        ValueWrapper<Object> wrapper = this.objects.get(key);
        if (null == wrapper) {
            return null;
        }
        return ((wrapper.getTimestamp() + millis) < System.currentTimeMillis()) ? null : wrapper;
    }

    /* 
     * TODO
     * @param key
     * @see com.asiainfo.cache.ICache#remove(java.lang.Object)
     */
    @Override
    public void remove(String key) {
        this.objects.remove(key);
    }

    /* 
     * TODO
     * @see com.asiainfo.cache.ICache#clear()
     */
    @Override
    public void clear() {
        this.objects.clear();
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.cache.ICache#keySet()
     */
    @Override
    public Set<String> keySet() {
        return this.objects.keySet();
    }

    /* 
     * TODO
     * @param expire
     * @param unit
     * @return
     * @see com.asiainfo.cache.ICache#keySet(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public Set<String> keySet(long expire, TimeUnit unit) {
        
        Set<String> result = new HashSet<>();
        Set<String> keys = this.keySet();
        if (null == keys || keys.isEmpty()) {
            return result;
        }
        for (String key : keys) {
            if (this.containsKey(key, expire, unit)) {
                result.add(key);
            }
        }
        return result;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.cache.ICache#values()
     */
    @Override
    public Collection<ValueWrapper<Object>> values() {
        return this.objects.values();
    }

    /* 
     * TODO
     * @param expire
     * @param unit
     * @return
     * @see com.asiainfo.cache.ICache#values(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public Collection<ValueWrapper<Object>> values(long expire, TimeUnit unit) {

        List<ValueWrapper<Object>> result = new ArrayList<>();
        Collection<ValueWrapper<Object>> values = this.objects.values();
        if (null == values || values.isEmpty()) {
            return result;
        }
        long millis = unit.toMillis(expire);
        for (ValueWrapper<Object> wrapper : values) {
            if ((wrapper.getTimestamp() + millis) > System.currentTimeMillis()) {
                result.add(wrapper);
            }
        }
        return result;
    }

    /* 
     * TODO
     * @param key
     * @return
     * @see com.asiainfo.cache.ICache#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(String key) {
        return this.objects.containsKey(key);
    }

    /* 
     * TODO
     * @param key
     * @param expire
     * @param unit
     * @return
     * @see com.asiainfo.cache.ICache#containsKey(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean containsKey(String key, long expire, TimeUnit unit) {

        if (!this.containsKey(key)) {
            return false;
        }
        ValueWrapper<Object> wrapper = this.get(key, expire, unit);
        return null != wrapper;
    }

    /* 
     * TODO
     * @param value
     * @return
     * @see com.asiainfo.cache.ICache#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        
        if (null == value) {
            return false;
        }
        Collection<ValueWrapper<Object>> values = this.values();
        if (null == values || values.isEmpty()) {
            return false;
        }
        for (ValueWrapper<Object> wrapper : values) {
            if (value.equals(wrapper.getValue())) {
                return true;
            }
        }
        return false;
    }

    /* 
     * TODO
     * @param value
     * @param expire
     * @param unit
     * @return
     * @see com.asiainfo.cache.ICache#containsValue(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean containsValue(Object value, long expire, TimeUnit unit) {
        
        if (null == value) {
            return false;
        }
        Collection<ValueWrapper<Object>> values = this.values(expire, unit);
        if (null == values || values.isEmpty()) {
            return false;
        }
        for (ValueWrapper<Object> wrapper : values) {
            if (value.equals(wrapper.getValue())) {
                return true;
            }
        }
        return false;
    }

    /* 
     * TODO
     * @return
     * @see com.asiainfo.cache.ICache#size()
     */
    @Override
    public int size() {
        return this.objects.size();
    }
}
