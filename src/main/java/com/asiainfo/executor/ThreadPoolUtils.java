package com.asiainfo.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * ExecutorService 线程池工具类
 * 
 * @author       zq
 * @date         2017年10月16日  下午3:50:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadPoolUtils {

	private ThreadFactory defaultFactory = new CustomThreadFactory();
	private ThreadPoolUtils() {}
	private static class ThreadPoolUtilsHolder {
		static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
	}
	public static ThreadPoolUtils getInstance() {
		return ThreadPoolUtilsHolder.INSTANCE;
	}
	
	public ExecutorService singleThreadPool() {
		return singleThreadPool(defaultFactory);
	}
	// new ThreadPoolExecutor.AbortPolicy() 任务队列满时抛出Rejected 异常
	public ExecutorService singleThreadPool(ThreadFactory factory) {
		//return Executors.newSingleThreadExecutor();
		return new ThreadPoolExecutor(1, 1, 
			    0L, TimeUnit.MILLISECONDS, 
			    new LinkedBlockingQueue<Runnable>(1024), 
			    factory, 
			    new ThreadPoolExecutor.AbortPolicy());
	}
	
	public ExecutorService fixedThreadPool(int corePoolSize) {
		return fixedThreadPool(corePoolSize, defaultFactory);
	}
	// fixed 的core和max一样大
	public ExecutorService fixedThreadPool(int corePoolSize, ThreadFactory factory) {
		//return Executors.newFixedThreadPool(corePoolSize, Executors.defaultThreadFactory());
		return new ThreadPoolExecutor(corePoolSize, corePoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                factory);
	}
	
	public ExecutorService cachedThreadPool() {
		return cachedThreadPool(defaultFactory);
	}
	// cached 使用SynchronousQueue队列，每进入一个任务都会导致队列满，从而新建一个线程
	public ExecutorService cachedThreadPool(ThreadFactory factory) {
		//return Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
				60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				factory);
	}
	
	public ScheduledExecutorService scheduledThreadPool(int corePoolSize) {
		return scheduledThreadPool(corePoolSize, defaultFactory);
	}
	// scheduled 使用DelayedWorkQueue队列
	public ScheduledExecutorService scheduledThreadPool(int corePoolSize, ThreadFactory factory) {
		//return Executors.newScheduledThreadPool(corePoolSize, Executors.defaultThreadFactory());
		return new ScheduledThreadPoolExecutor(corePoolSize, factory);
	}
	
	public Thread newThread(Runnable r) {
		return this.defaultFactory.newThread(r);
	}
	public Thread newThread(Runnable r, String threadName) {
		return new CustomThreadFactory(threadName).newThread(r);
	}
	
	public static void shutdown(ExecutorService service) {
		try {
			if (null != service) {
				service.shutdown();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
