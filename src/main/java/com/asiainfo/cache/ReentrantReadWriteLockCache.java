package com.asiainfo.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @Description: 基于ReentrantReadWriteLock读写锁实现缓存功能
 * 
 * @author       zq
 * @date         2017年9月3日  下午1:13:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReentrantReadWriteLockCache {
	
	static Map<String, Object> map = new HashMap<>();
	static ReadWriteLock lock = new ReentrantReadWriteLock();
	static Lock read = lock.readLock();
	static Lock write = lock.writeLock();

	// 获取一个key对应的value
	public static final Object get(String key) {
		
		read.lock();
		try {
			return map.get(key);
		} finally {
			read.unlock();
		}
	}

	// 设置key对应的value，并返回旧的value
	public static final Object put(String key, Object value) {
		
		write.lock();
		try {
			return map.put(key, value);
		} finally {
			write.unlock();
		}
	}

	// 清空所有内容
	public static final void clear() {
		
		write.lock();
		try {
			map.clear();
		} finally {
			write.unlock();
		}
	}
}
