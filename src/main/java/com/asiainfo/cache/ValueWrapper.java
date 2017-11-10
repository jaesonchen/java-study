package com.asiainfo.cache;

import java.io.Serializable;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月5日  上午10:13:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ValueWrapper implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private long timestamp = System.currentTimeMillis();
	private Object value;
	
	public ValueWrapper() {}
	public ValueWrapper(Object value) {
		this.value = value;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public boolean equals(Object obj) {
		return this.value.equals(obj);
	}
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}
	@Override
	public String toString() {
		return "ValueWrapper [timestamp=" + timestamp + ", value=" + value + "]";
	}
}
