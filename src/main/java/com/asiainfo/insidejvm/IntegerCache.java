package com.asiainfo.insidejvm;

import java.lang.reflect.Field;

/**
 * @Description: Byte、Short、Integer 会缓存[-128, 127]的封装数字，Character会缓存[0, 127]的封装字符
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:39:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IntegerCache {
	
	static {
		try {
		    // IntegerCache内部类
			Class<?> intergerCacheClazz = Integer.class.getDeclaredClasses()[0];
			Field cacheField = intergerCacheClazz.getDeclaredField("cache");
			cacheField.setAccessible(true);
			Integer[] caches = (Integer[]) cacheField.get(0); 
			caches[130] = caches[131];
		} catch (Exception e) {}
	}
	
	public static void main(String[] args) {
		System.out.printf("%d", 1 + 1);
		System.out.println();
		System.out.printf("%d", 2);
		System.out.println();
		System.out.printf("%d", new Integer(2));
	}
}
