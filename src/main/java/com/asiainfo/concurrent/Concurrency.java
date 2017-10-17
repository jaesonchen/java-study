package com.asiainfo.concurrent;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * 
 * 同步访问共享的可变数据：当多个线程共享可变数据的时候，每个读或者写数据的线程都必须进行同步。
 * 
 * 		关键字synchronized可以保证在同一时刻，只有一个线程可以执行对象的某个方法或者代码库。
 * 		正确地使用同步可以保证没有任何方法会看到对象处于不一致的状态中。
 * 		java规范保证读或者写一个变量是原子性(atomic)，除非这个变量是long或者double。
 * 		java规范保证线程在读取原子数据的时候，不会看到任意的数值，但是它并不保证一个线程写入的值对于另一个线程将是可见的。
 * 		为了提高性能，在读写原子数据的时候应该避免使用同步，这个建议是非常危险而且错误的。
 * 		volatile关键字不执行互斥访问，但它可以保证任何一个线程在读取共享域的时候都将看到最近刚刚被写入的值。
 * 
 * ***************************************************************************
 * 避免过度同步：在一个被同步的方法或代码块内部，永远不要放弃对客户端的控制。
 * 
 * 		在一个被同步的区域内部，不要调用设计成要被覆盖的方法或者是客户都以函数对象的形式提供的方法。
 * 		可以通过将外来方法的调用移出同步的代码块来解决问题。
 *  	通常你应该在同步区域内做尽可能少的工作。
 * 
 * ***************************************************************************
 * 并发工具优先于wait和notify：
 * 		java1.5提供了更高级的并发工具，可以完成以前必须在wait和notify上必须手写代码来完成的工作。
 * 		java.util.concurrent中的并发集合为标准的接口（List、Map）提供了高性能的并发实现。
 * 		应该优先使用ConcurrentHashMap而不是Collections.sychronizedMap或者Hashtable。
 * 		应该优先使用内部同步的并发集合，而不是使用外部同步的集合。
 * 		始终应该使用wait循环模式来调用wait，永远不要再循环之外调用wait。
 * 		总是应该使用notifyAll代替notify，可以避免来自不相关线程的意外或者恶意等待。
 * 
 * ***************************************************************************
 * 线程安全性：
 * 		不可变的：类的实例是不可变的，不需要外部同步。如String。
 * 		无条件的线程安全：类的实例是可变的，但有着足够的内部同步，它的实例可以被并发使用，无需外部同步。如Ramdom、ConcurrentHashMap。
 * 		有条件的线程安全：除了有些方法并发访问需要外部同步外，安全级别与无条件的相同。如Collections.sychronized包装返回的集合，它的Iterator要求外部同步。
 * 		非线程安全：客户端必须自己实现外部同步来调用每个方法。如ArrayList、HashMap。
 * 
 * ***************************************************************************
 * 谨慎使用延迟初始化：如果出于性能的考虑而需要对静态域使用延迟初始化，使用lazy initialization holder class模式。
 * 现代的VM在初始化holder class的时候，同步域的访问，一旦这个类被初始化，VM将修补代码，以便后续对该域的访问不会导致同步。
 * 
 * ***************************************************************************
 * 
 * @author       zq
 * @date         2017年10月16日  下午5:29:38
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Concurrency {
	
	//方法一：使用volatile关键字，保证能看到其他线程修改的结果。
	private static volatile boolean stopRequested;

	//方法二：使用同步synchronized关键字，可以看到其他线程修改的结果
	private static synchronized boolean stopRequested() {
		return stopRequested;
	}
	private static synchronized void requestStop() {
		stopRequested = true;
	}
	
	//lazy initialization holder class模式
	private static class FieldHolder {
		static final FieldType INSTANCE = new FieldType();
	}
	//不需要使用synchronized同步
	static FieldType getField() { return FieldHolder.INSTANCE; }
	
	public static void main(String[] args) throws InterruptedException {
		Thread backgroundThread = ThreadPoolUtils.getInstance().newThread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (!stopRequested()) {
					i++;
				}
				System.out.println("i=" + i);
			}
		});
		backgroundThread.start();
		Thread.sleep(1000);
		requestStop();
	}

}
class FieldType {}
