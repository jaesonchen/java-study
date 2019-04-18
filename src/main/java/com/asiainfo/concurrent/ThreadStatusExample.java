package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

import com.asiainfo.util.ServiceUtil;
import com.asiainfo.util.ThreadPoolUtils;

/**
 * 阻塞(block)：阻塞状态是指线程因为某种原因放弃了cpu 使用权，也即让出了cpu timeslice(时间片)，暂时停止运行。
 *             直到线程进入可运行(runnable)状态，才有机会再次获得cpu timeslice 转到运行(running)状态。
 * 阻塞的情况分三种： 
 * (一). 等待阻塞：运行(running)的线程执行o.wait()方法，JVM会把该线程放入等待队列(waitting queue)中。
 * (二). 同步阻塞：运行(running)的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池(lock pool)中。
 * (三). 其他阻塞：运行(running)的线程执行Thread.sleep(long ms)或t.join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。
 *       当sleep()超时、join()等待线程终止或者超时、或者I/O处理完毕、或者被中断(Thread.interrupt())时，线程重新转入可运行(runnable)状态。
 * 
 * Thread.sleep(long millis) 一定是当前线程调用此方法，当前线程进入阻塞，但不释放对象锁，millis后线程自动苏醒进入可运行状态。作用：给其它线程执行机会的最佳方式。
 * Thread.yield() 一定是当前线程调用此方法，当前线程放弃获取的cpu时间片，由运行状态变会可运行状态，让OS再次选择线程。作用：让相同优先级的线程轮流执行，
 *      但并不保证一定会轮流执行。实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。Thread.yield()不会导致阻塞。
 * t.join()/t.join(long millis) 当前线程里调用其它线程的join方法，当前线程阻塞，但不释放对象锁，直到其他线程执行完毕或者millis时间到，当前线程进入可运行状态。
 * obj.wait() 当前线程调用对象的wait()方法，当前线程释放对象锁，进入等待队列。依靠notify()/notifyAll()唤醒或者wait(long timeout)timeout时间到自动唤醒。
 * obj.notify() 唤醒在此对象监视器上等待的单个线程，选择是任意性的。
 * obj.notifyAll() 唤醒在此对象监视器上等待的所有线程。
 * 
 * 运行线程状态
 * 	NEW
 * 	RUNNABLE
 * 	BLOCKED
 * 	WAITING
 * 	TIME_WAITING
 * 	TERMINATED
 * 
 * 使用jstack工具查看示例代码运行时的线程信息
 * >jps     --列出当前用户的所有java进程
 * 
 * >jstack pid  --用于打印出给定的java进程ID或core file或远程调试服务的Java堆栈信息
 * 
 * >jmap pid    --得到运行java程序的内存分配的详细情况。例如实例个数，大小等 
 * 
 * >jstat       --可以观察到classloader，compiler，gc相关信息。可以时时监控资源和性能 
 * 
 * @author       zq
 * @date         2017年9月3日  下午6:19:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadStatusExample {
	
	public static void main(String[] args) {
		
		ThreadPoolUtils.getInstance().newThread(new SleepBlock(), "SleepBlockThread").start();
		ThreadPoolUtils.getInstance().newThread(new Waiting(), "WaitingThread").start();
		//使用两个Blocked线程，一个获取锁成功，另一个被阻塞
		ThreadPoolUtils.getInstance().newThread(new LockPool(), "LockPoolThread-1").start();
		ThreadPoolUtils.getInstance().newThread(new LockPool(), "LockPoolThread-2").start();
	}
	
	//该线程不断进行睡眠
	static class SleepBlock implements Runnable {
		@Override
		public void run() {
			while(true) {
				ServiceUtil.waitFor(10, TimeUnit.SECONDS);
			}
		}
	}
	
	//该线程在Waiting.class上等待，释放该锁
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
	
	//该线程在Blocked.class上加锁后，不会释放该锁
	static class LockPool implements Runnable {
		@Override
		public void run() {
			synchronized (LockPool.class) {
				while(true) {
					ServiceUtil.waitFor(1, TimeUnit.SECONDS);
				}
			}
		}
	}
}
