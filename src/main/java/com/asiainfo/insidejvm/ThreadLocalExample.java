package com.asiainfo.insidejvm;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * 
 * 首先，ThreadLocal 不是用来解决共享对象的多线程访问问题的，一般情况下，通过ThreadLocal.set() 到线程中的对象是该线程自己使用的对象，
 * 其他线程是不需要访问的，也访问不到的。各个线程中访问的是不同的对象。 
 * 另外，说ThreadLocal使得各线程能够保持各自独立的一个对象，并不是通过ThreadLocal.set()来实现的，
 * 而是通过每个线程中的new Thread对象 的操作来创建的对象，每个线程创建一个，不是什么对象的拷贝或副本。
 * 通过ThreadLocal.set()将这个新创建的对象的引用保存到各线程的自己的一个map中，每个线程都有这样一个map，
 * 执行ThreadLocal.get()时，各线程从自己的map中取出放进去的对象，因此取出来的是各自自己线程中的对象，ThreadLocal实例是作为map的key来使用的。 
 * 
 * ThreadLocal ThreadLocalMap Thread
 * Thread类里包含成员变量ThreadLocal.ThreadLocalMap threadLocals = null; 用于保存每个线程的ThreadLocal变量。
 * 在Thread的private void exit() 方法里设置threadLocals = null; 用于线程完成时自动清空ThreadLocal变量。
 * ThreadLocal中的createMap方法：
 * void createMap(Thread t, T firstValue) {  
 * 		t.threadLocals = new ThreadLocalMap(this, firstValue);  
 * } 
 * ThreadLocalMap使用的key为ThreadLocal对象。
 * 同一个ThreadLocal对象维持的变量中，用于保存变量的所有线程的Map的key为同一个ThreadLocal对象。
 * 
 * ThreadLocal.set(T value) {
 *      Thread t = Thread.currentThread();
 *      ThreadLocalMap map = t.threadLocals;
 *      map.set(this, value);
 * }
 * 
 * protected T initialValue()返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。
 * 这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:40:37
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadLocalExample {
	
	public static void main(String[] args) {
		// 指定初始值 
		ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
			@Override
			public Integer initialValue() {
				return 0;
			}
		};
		
		ThreadPoolUtils.getInstance().newThread(new TestThread(threadLocal, 100)).start();
		ThreadPoolUtils.getInstance().newThread(new TestThread(threadLocal, 1000)).start();
		ThreadPoolUtils.getInstance().newThread(new TestThread(threadLocal, 10000)).start();
		
		System.out.println(threadLocal.get());
	}
} 

class TestThread extends Thread {
	
	private ThreadLocal<Integer> local;
	private Integer seq;
	public TestThread(ThreadLocal<Integer> local, Integer seq) {
		this.local = local;
		this.seq = seq;
		//在这里set会出现bug，因为new对象的时候还处于main的线程里，所以会将seq设置到main线程的Thread.threadLocals中
		//this.local.set(seq);
	}
	
	public void remove() {
		local.remove();
	}
	
	@Override
	public void run() {
		int i = 3;
		this.local.set(this.seq);
		while (i > 0) {
			i--;
			System.out.println(Thread.currentThread() + ":" + this.local.get());
			this.seq = this.local.get();
			this.seq += 1;
			this.local.set(this.seq);
		}
	}
}

