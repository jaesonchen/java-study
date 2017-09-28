package com.asiainfo.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import com.asiainfo.util.ServiceUtil;

/**
 * 
 * 	CyclicBarrier与CountDownLatch区别：
 * 	1、CountDownLatch的计数器只能使用一次，而CyclicBarrier的计数器可以使用reset()方法重置
 * 	2、CyclicBarrier还提供其他有用的方法，如getNumberWaiting方法可以获得阻塞的线程数量。isBroken()方法用来了解阻塞的线程是否被中断
 * 
 * @author       zq
 * @date         2017年9月3日  下午3:37:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CyclicBarrierExample {
	
	static CyclicBarrier barrier = new CyclicBarrier(2, new A());
	
	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("thread one arrive barrier point, " + barrier.getNumberWaiting() + " threads are waiting at barrier!");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println("thread one cross over barrier!");
			}
		}).start();
		
		ServiceUtil.waitFor(100, TimeUnit.MILLISECONDS);
		
		try {
			System.out.println("thread two arrive barrier point, " + barrier.getNumberWaiting() + " threads are waiting at barrier!");
			barrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("thread two cross over barrier!");
	}
	
	static class A implements Runnable {
		@Override
		public void run() {
			System.out.println("command action run only once on barrier point, after "  + barrier.getNumberWaiting() + " threads arrived!");
		}
	}
}
