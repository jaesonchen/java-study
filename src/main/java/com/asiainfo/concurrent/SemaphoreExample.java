package com.asiainfo.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * 
 * @Description: 用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:27:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SemaphoreExample {
	
	private static final int THREAD_COUNT = 30;
	private static ExecutorService threadPool = ThreadPoolUtils.getInstance().fixedThreadPool(THREAD_COUNT);	
	private static Semaphore semaphore = new Semaphore(10);
	
	public static void main(String[] args) {
		
		for (int i = 0; i < THREAD_COUNT; i++) {
			threadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						semaphore.acquire();
						System.out.println("save data : " + System.currentTimeMillis());
						TimeUnit.SECONDS.sleep(1);
					} catch(InterruptedException e){
					} finally {
						semaphore.release();
					}
				}
			});
		}
		threadPool.shutdown();
	}
}
