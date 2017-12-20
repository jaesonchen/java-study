package com.asiainfo.cache;

/**
 * 缓存管理类，用于初始化并获得客户群缓存，可根据需求构建相应的缓存实现
 * 
 * @author       zq
 * @date         2017年11月7日  上午10:17:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CacheManager {

	private static class CacheHolder {
		static final ICache<String, Object> CACHE = new SimpleCache();
	}
	public static ICache<String, Object> getInstance() {
		return CacheHolder.CACHE;
	}
}