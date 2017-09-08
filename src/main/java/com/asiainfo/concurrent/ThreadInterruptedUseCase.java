package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @Description: 通过标识位或者中断操作的方式能够使线程终止
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:23:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadInterruptedUseCase {
	
	public static void main(String[] args) throws Exception {
		
		Runner one = new Runner();
		Thread countThread = new Thread(one, "CountTHread 1");
		countThread.start();
		//睡眠1秒，main线程对CountThread进行中断，使CountThread能够感知中断而结束
		TimeUnit.SECONDS.sleep(1);
		countThread.interrupt();

		Runner two = new Runner();
		countThread = new Thread(two, "CountThread 2");
		countThread.start();
		//睡眠1秒，main线程对Runner two进行取消，使CountThread能够感知on为false而结束
		TimeUnit.SECONDS.sleep(1);
		two.cancel();
	}
	
	private static class Runner implements Runnable {
		
		private long i;
		private volatile boolean on = true;
		
		@Override
		public void run() {
			while (on && !Thread.currentThread().isInterrupted()) {
				i++;
			}
			System.out.println("Count i = " + i);
		}
		
		public void cancel() {
			on = false;
		}
	}
}
