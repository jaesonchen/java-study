package com.asiainfo.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable接口类似于Runnable，从名字就可以看出来了，但是Runnable不会返回结果，并且无法抛出返回结果的异常，
 * 而Callable功能更强大一些，被线程执行后，可以返回值，这个返回值可以被Future拿到，也就是说，Future可以拿到异步执行任务的返回值.
 * Future有个get方法而获取结果只有在计算完成时获取，否则会一直阻塞直到任务转入完成状态，然后会返回结果或者抛出异常。 
 * 
 * @author       zq
 * @date         2017年9月3日  下午10:08:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CallableFutureExample {

	static class Task implements Callable<String> {

		String name;
		Task(String name) {
			this.name = name;
		}
		
		/* 
		 * @Description: TODO
		 * @return
		 * @throws Exception
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public String call() throws Exception {
			System.out.println("Thread " + name + " execute!");
			return "result=" + name;
		}
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ExecutorService service = Executors.newCachedThreadPool();
		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			futures.add(service.submit(new Task(String.valueOf(i))));
		}
		
		for (Future<String> future : futures) {
			System.out.println(future.get());
		}
	}
}
