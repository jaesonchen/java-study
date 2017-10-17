package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

import com.asiainfo.util.ServiceUtil;
import com.asiainfo.util.ThreadPoolUtils;

/**
 * 
 * 运行查看线程状态
 * 	NEW
 * 	RUNNABLE
 * 	BLOCKED
 * 	WAITING
 * 	TIME_WAITING
 * 	TERMINATED
 * 
 * 使用jstack工具查看示例代码运行时的线程信息
 * >jps     --列出当前用户的所有java进程
 * ...
 * >jstack  --用于打印出给定的java进程ID或core file或远程调试服务的Java堆栈信息
 * ...
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:19:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadStateExample {
	
	public static void main(String[] args) {
		
		ThreadPoolUtils.getInstance().newThread(new TimeWaiting(), "TimeWaitingThread").start();
		ThreadPoolUtils.getInstance().newThread(new Waiting(), "WaitingThread").start();
		//使用两个Blocked线程，一个获取锁成功，另一个被阻塞
		ThreadPoolUtils.getInstance().newThread(new Blocked(),"BlockedThread-1").start();
		ThreadPoolUtils.getInstance().newThread(new Blocked(),"BlockedThread-2").start();
	}
	
	//该线程不断进行睡眠
	static class TimeWaiting implements Runnable {
		@Override
		public void run() {
			while(true) {
				ServiceUtil.waitFor(10, TimeUnit.SECONDS);
			}
		}
	}
	
	//该线程在Waiting.class实例上等待
	static class Waiting implements Runnable {
		@Override
		public void run() {
			while(true) {
				synchronized (Waiting.class) {
					try {
						Waiting.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//该线程在Blocked.class实例上加锁后，不会释放该锁
	static class Blocked implements Runnable {
		@Override
		public void run() {
			synchronized (Blocked.class) {
				while(true) {
					ServiceUtil.waitFor(1, TimeUnit.SECONDS);
				}
			}
		}
	}
}
