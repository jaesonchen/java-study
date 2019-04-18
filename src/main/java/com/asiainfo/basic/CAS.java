package com.asiainfo.basic;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @Description: cas 原理
 * 
 *  CAS是一些CPU直接支持的指令，在Java中无锁操作CAS基于以下3个方法实现:
 *      第一个参数o为给定对象，offset为对象内存的偏移量，通过这个偏移量迅速定位字段并设置或获取该字段的值，
 *      expected表示期望值，x表示要设置的值，下面3个方法都通过CAS原子指令执行操作。
 *      
 *      public final native boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);
 *      public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);
 *      public final native boolean compareAndSwapLong(Object o, long offset, long expected, long x);
 * 
 * @author       zq
 * @date         2017年9月18日  下午3:00:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("restriction")
public class CAS {
	
    Unsafe unsafe;
	public CAS() throws Exception {

		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		this.unsafe = (Unsafe) field.get(null);
	}
	
	//1.8新增，给定对象o，根据获取内存偏移量指向的字段，将其增加delta，
	//这是一个CAS操作过程，直到设置成功方能退出循环，返回旧值
	public final int getAndAddInt(Object o, long offset, int delta) {
		
		int v;
		do {
			//获取内存中最新值
			v = this.unsafe.getIntVolatile(o, offset);
			//通过CAS操作
		} while (!this.unsafe.compareAndSwapInt(o, offset, v, v + delta));
		return v;
	}
	
	//1.8新增，操作的long类型
	public final long getAndAddLong(Object o, long offset, long delta) {
		
		long v;
		do {
			v = this.unsafe.getLongVolatile(o, offset);
		} while (!this.unsafe.compareAndSwapLong(o, offset, v, v + delta));
		return v;
	}
	
	//1.8新增，给定对象o，根据获取内存偏移量对于字段，将其 设置为新值newValue，
	//这是一个CAS操作过程，直到设置成功方能退出循环，返回旧值
	public final int getAndSetInt(Object o, long offset, int newValue) {
		
		int v;
		do {
			v = this.unsafe.getIntVolatile(o, offset);
		} while (!this.unsafe.compareAndSwapInt(o, offset, v, newValue));
		return v;
	}

	// 1.8新增，操作的是long类型
	public final long getAndSetLong(Object o, long offset, long newValue) {

		long v;
		do {
			v = this.unsafe.getLongVolatile(o, offset);
		} while (!this.unsafe.compareAndSwapLong(o, offset, v, newValue));
		return v;
	}

	//1.8新增，同上，操作的是引用类型数据
	public final Object getAndSetObject(Object o, long offset, Object newValue) {

		Object v;
		do {
			v = this.unsafe.getObjectVolatile(o, offset);
		} while (!this.unsafe.compareAndSwapObject(o, offset, v, newValue));
		return v;
	}
	 
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws Exception {
		
		CAS cas = new CAS();
		System.out.println("park 5s!");
		cas.unsafe.park(false, 5000000000L);
		System.out.println("after 5s!");
	}
}
