package com.asiainfo.concurrent;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * volatile关键字是一个轻量级的线程同步，虽然volatile不执行互斥访问，
 * 但可以保证任何一个线程在读取变量域值的时候都将看到最近刚刚被写入的值。
 */
public class ConcurrentExample1 {

	private static volatile boolean stopRequested;
	
	public static void main(String[] args) throws Exception {
		
		Thread bgThread = ThreadPoolUtils.getInstance().newThread(new Runnable() {
			@Override
			public void run() {
				long i = 0L;
				while (!stopRequested) {
					i++;
				}
				System.out.println("i = " + i);
			}
		});
		
		bgThread.start();
		Thread.sleep(1000);
		stopRequested = true;
	}
}
