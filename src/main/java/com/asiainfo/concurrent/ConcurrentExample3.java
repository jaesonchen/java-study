package com.asiainfo.concurrent;

import java.util.concurrent.atomic.AtomicLong;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * 
 * 线程安全性失败：使用volatile时要确保变量必须是原子性操作，否则产生安全性失败。
 * 如果第二个线程在第一个线程读取旧值和写回新值期间读取这个域，则第二个线程就会与第一个线程一起看到同一个值，并返回相同的序列号。
 * 
 * @author       zq
 * @date         2017年10月16日  下午5:22:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ConcurrentExample3 {

	private static volatile int nextSerialNumber = 0;
	public static int generateSerialNumber() {
		//nextSerialNumber++非原子操作
	    return nextSerialNumber++;
	}
	
	//修正方法：
	//方法一：使用synchronized同步generateSerialNumber()方法，删除volatile关键字。
	private static int nextSerialNumber1 = 0;
	public static synchronized int generateSerialNumber1() {
	    return nextSerialNumber1++;
	}
	
	//方法二：使用原子数据类型。
	private static AtomicLong nextSerialNumber2 = new AtomicLong();
	public static long generateSerialNumber2() {
	    return nextSerialNumber2.getAndIncrement();
	}
	
	public static void main(String[] args) {
		
		ThreadPoolUtils.getInstance().newThread(new TestThread()).start();
		ThreadPoolUtils.getInstance().newThread(new TestThread()).start();
		ThreadPoolUtils.getInstance().newThread(new TestThread()).start();
		
/*		new Thread(new TestThread1()).start();
		new Thread(new TestThread1()).start();
		new Thread(new TestThread1()).start();*/
		
/*		new Thread(new TestThread2()).start();
		new Thread(new TestThread2()).start();
		new Thread(new TestThread2()).start();*/
	}
	
	static class TestThread implements Runnable {
		
		@Override
		public void run() {
			int i = 10000;
			for (; i > 0; i--) {
				generateSerialNumber();
			}
			System.out.println(this.getClass().getSimpleName() + " = " + nextSerialNumber);
		}
	}
	static class TestThread1 implements Runnable {
		
		@Override
		public void run() {
			int i = 10000;
			for (; i > 0; i--) {
				generateSerialNumber1();
			}
			System.out.println(this.getClass().getSimpleName() + " = " + nextSerialNumber1);
		}
	}
	static class TestThread2 implements Runnable {
		
		@Override
		public void run() {
			int i = 10000;
			for (; i > 0; i--) {
				generateSerialNumber2();
			}
			System.out.println(this.getClass().getSimpleName() + " = " + nextSerialNumber2);
		}
	}
}
