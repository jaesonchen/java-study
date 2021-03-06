package com.asiainfo.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description: Atomic 原子类基于Unsafe的getAndAddInt、compareAndSwapInt实现。
 *               Atomic封装的值域使用了volatile，private volatile int value; 因此Atomic类在效率上是低于直接用volatitle的
 * 
 * 
 * @author       zq
 * @date         2017年9月3日  下午1:19:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AtomicIntegerExample {

	private AtomicInteger atomicI = new AtomicInteger(0);
	private int i = 0;

	public static void main(String[] args) throws InterruptedException {
		
		final AtomicIntegerExample cas = new AtomicIntegerExample();
		List<Thread> list = new ArrayList<>();
		for (int j = 0; j < 100; j++) {
			list.add(ThreadPoolUtils.getInstance().newThread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						cas.count();
						cas.safeCount();
					}
				}
			}));
		}
		for (Thread t : list) {
			t.start();
		}
		// 等待所有线程执行完成
		for (Thread t : list) {
			t.join();
		}
		System.out.println(cas.i);
		System.out.println(cas.atomicI.get());
	}

	/**
	 * 使用CAS实现线程安全计数器
	 */
	private void safeCount() {
		for (;;) {
			int i = atomicI.get();
			boolean suc = atomicI.compareAndSet(i, ++i);
			if (suc) {
				break;
			}
		}
	}

	/**
	 * 非线程安全计数器
	 */
	private void count() {
		i++;
	}
}
