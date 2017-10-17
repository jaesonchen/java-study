package com.asiainfo.insidejvm;

/*
 * 缓存0-9999之间的数字的Short对象
 */
public class ShortCachePool {
	
	private static Short[] shortCacheObjects = new Short[10000];
	
	static {
		for (int i = 0; i < 10000; i++) {
			shortCacheObjects[i] = new Short((short) i);
		}
	}
	
	public static Short valueOf(short s) {
		
		if (s < 0 || s > 9999) {
			throw new IndexOutOfBoundsException("ShortCache只缓存0-9999之间的Short对象");
		}
		return shortCacheObjects[s];
	}
	
	public static Short valueOf(String str) {
		
		short s;
		try {
			s = Short.parseShort(str);
		} catch (Exception ex) {
			throw new NumberFormatException("数字转换失败，参数字符串必须是0-9之间的数字");
		}
		
		return valueOf(s);
	}
}
