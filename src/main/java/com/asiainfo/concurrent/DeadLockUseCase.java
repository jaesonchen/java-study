package com.asiainfo.concurrent;

/**
 * @Description: 基本数据类型的包装类不适合作为同步对象
 * 
 * @author       zq
 * @date         2017年9月5日  上午10:50:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DeadLockUseCase {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 100; i++) {
			new Thread(new SynAddRunnable(1,2)).start();
			new Thread(new SynAddRunnable(2,1)).start();
		}
		Thread.sleep(100000);
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
