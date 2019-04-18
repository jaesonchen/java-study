package com.asiainfo.concurrent;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * @Description: 基本数据类型的包装类不适合作为同步对象，valueOf自动拆箱/封箱会缓存[-128, 127]的对象，
 *               有可能使得多个不同对象持有的是同一个对象锁，从对象锁演变为Class锁。
 * 
 * @author       zq
 * @date         2017年9月5日  上午10:50:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DeadLockExample {

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 100; i++) {
			ThreadPoolUtils.getInstance().newThread(new SynAddRunnable(1, 2)).start();
			ThreadPoolUtils.getInstance().newThread(new SynAddRunnable(2, 1)).start();
		}
		Thread.sleep(10000);
	}
	
	static class SynAddRunnable implements Runnable {
		
		int a, b;
		public SynAddRunnable(int a, int b) {
			this.a = a;
			this.b = b;
		}
		@Override
		public void run() {
			synchronized (Integer.valueOf(a)) { //The valueof method will cache number between [-128,127]
				synchronized (Integer.valueOf(b)) {
					System.out.println(a + b);
				}
			}
		}
	}
}
