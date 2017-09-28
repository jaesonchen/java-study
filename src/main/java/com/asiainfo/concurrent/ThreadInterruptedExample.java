package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 线程状态：New -> Runnable -> Running -> Blocked (WAITING、TIME_WAITING、Blocked) -> Dead
 * 
 * 通过调用线程的interrupt方法对其进行中断操作，当一个线程调用interrupt方法时，线程的中断状态（标识位）将被置位（改变），
 * 这是每个线程都具有的boolean标志，每个线程都应该不时的检查这个标志，来判断线程是否被中断。
 * 线程中断仅仅是设置线程的中断状态位，不会停止线程。
 * 
 * 如果此时线程处于阻塞状态（sleep、wait、jion），会抛出InterruptedException异常。
 * 调用sleep、wait等此类可中断（throw InterruptedException）方法时，一旦方法抛出InterruptedException，
 * 当前调用该方法的线程的中断状态就会被jvm自动清除了，就是说调用该线程的isInterrupted 方法时是返回false。
 * 
 * 只有当前线程才能清除自己的中断位（对应interrupted（）方法）。
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:23:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadInterruptedExample {
	
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
		
		//
		Thread t = new Thread(new Worker());
		t.start();
		Thread.sleep(200);
		t.interrupt();
		System.out.println("Main thread stopped.");
	}
	
	static class Runner implements Runnable {
		
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
	
	static class Worker implements Runnable {
		
        public void run() {
            System.out.println("Worker started.");
              
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            	//The interrupted status of the current thread is cleared when this exception is thrown.
                System.out.println("Worker IsInterrupted: " + Thread.currentThread().isInterrupted());  
            }
            System.out.println("Worker stopped.");
        }
    }
}
