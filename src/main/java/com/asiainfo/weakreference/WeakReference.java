package com.asiainfo.weakreference;

import java.util.WeakHashMap;

/**
 * @Description: 目的是为了优化JVM，使JVM中的垃圾回收器GC更智能的回收“无用”的对象。
 * 				  与其它Map最主要的区别就在于其KEY是弱引用类型。
 * 
 * @author       zq
 * @date         2017年8月23日  下午2:42:52
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class WeakReference {

	/** 
	 * @Description: 弱引用测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
        WeakHashMap<KeyHolder, ValueHolder> weakMap = new WeakHashMap<KeyHolder, ValueHolder>();
        KeyHolder kh = new KeyHolder();
        ValueHolder vh = new ValueHolder();
        
        weakMap.put(kh, vh);
        while (true) {
            for (KeyHolder key : weakMap.keySet()) {
                System.out.println(key + " : " + weakMap.get(key));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("map.size=" + weakMap.size());
            //这里把kh设为null，这样一来就只有弱引用指向kh指向的对象
            kh = null;
            System.gc();
        }
	}
}

class KeyHolder {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("I am over from key");
        super.finalize();
    }
}
class ValueHolder {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("I am over from value");
        super.finalize();
    }
}
