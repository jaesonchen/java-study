package com.asiainfo.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月5日  上午10:13:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ValueWrapper<V> implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private long timestamp = System.currentTimeMillis();
	private V value;
	
	public ValueWrapper() {}
	public ValueWrapper(V value) {
		this.value = value;
	}
	
	/**
	 * 是否失效
	 * 
	 * @param expire
	 * @param unit
	 * @return
	 */
	public boolean isValid(long expire, TimeUnit unit) {
	    long millis = unit.toMillis(expire);
	    return (this.timestamp + millis) > System.currentTimeMillis();
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
    public V getValue() {
		return value;
	}
	@SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz) {
        return (T) value;
    }
	public void setValue(V value) {
		this.value = value;
	}
	@SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ValueWrapper)) {
            return false;
        }
        ValueWrapper<V> target = (ValueWrapper<V>) obj;
        return this.timestamp == target.timestamp && this.value.equals(target.value);
    }
    @Override
    public int hashCode() {
        
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
	@Override
	public String toString() {
		return "ValueWrapper [timestamp=" + timestamp + ", value=" + value + "]";
	}
}
