package com.asiainfo.jdbc.caseinsensitive;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年3月31日  下午5:52:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CaseInsensitiveHashMap implements Map<String, Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> map = new HashMap<String, Object>();
	
	public CaseInsensitiveHashMap(Map<String, Object> map) {
		this.map = map;
	}
	
	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.map.size();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	/* 
	 * @Description: TODO
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		return this.map.get(key);
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object value) {
		return this.map.put(key, value);
	}

	/* 
	 * @Description: TODO
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Object remove(Object key) {
		return this.map.remove(key);
	}

	/* 
	 * @Description: TODO
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		this.map.putAll(m);
	}

	/* 
	 * @Description: TODO
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		this.map.clear();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return this.map.keySet();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Object> values() {
		return this.map.values();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		
		Set<java.util.Map.Entry<String, Object>> result = new HashSet<java.util.Map.Entry<String, Object>>();
		for (java.util.Map.Entry<String, Object> entry : this.map.entrySet()) {
			result.add(new CaseInsensitiveEntry(entry));
		}
		return result;
	}
	
	static class CaseInsensitiveEntry implements java.util.Map.Entry<String, Object> {

		java.util.Map.Entry<String, Object> entry;
		
		CaseInsensitiveEntry(java.util.Map.Entry<String, Object> entry) {
			this.entry = entry;
		}
		
		/* 
		 * @Description: TODO
		 * @return
		 * @see java.util.Map.Entry#getKey()
		 */
		@Override
		public String getKey() {
			return this.entry.getKey() == null ? null : this.entry.getKey().toUpperCase();
		}

		/* 
		 * @Description: TODO
		 * @return
		 * @see java.util.Map.Entry#getValue()
		 */
		@Override
		public Object getValue() {
			return this.entry.getValue();
		}

		/* 
		 * @Description: TODO
		 * @param value
		 * @return
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		@Override
		public Object setValue(Object value) {
			return this.entry.setValue(value);
		}
		
		@Override
        public final String toString() {
            return this.entry.getKey() + "=" + this.entry.getValue();
        }
	}
}
