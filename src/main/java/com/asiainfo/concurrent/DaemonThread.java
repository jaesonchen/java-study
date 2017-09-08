package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

import com.asiainfo.util.ServiceUtil;

/**
 * java中的守护线程(Daemon Thread) 指的是一类特殊的Thread，其优先级特别低(低到甚至可以被JVM自动终止)，
 * 通常这类线程用于在空闲时做一些资源清理类的工作，比如GC线程，如果JVM中所有非守护线程（即：常规的用户线程）都结束了，
 * 守护线程会被JVM中止，想想其实也挺合理，没有任何用户线程了，自然也不会有垃圾对象产生，GC线程也没必要存在了。
 * 
 * 当一个Java虚拟机中不存在非daemon线程的时候，java虚拟机将会退出
 * daemon属性需要在线程start之前设置，不能在启动线程之后设置
 * 
 * 注意：在构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源的逻辑
 * 
 * @author liujianzhu
 * @date 2016年7月18日 下午9:35:33
 *
 */
public class DaemonThread {
	
	public static void main(String[] args) {
		
		Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
		thread.setDaemon(true);
		thread.start();
		//ServiceUtil.waitFor(1, TimeUnit.SECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	System.out.println("JVM Exit!");
            }
        });
	}
	
	static class DaemonRunner implements Runnable {
		@Override
		public void run() {
			try{
				System.out.println("DaemonThread run start.");
				ServiceUtil.waitFor(1, TimeUnit.SECONDS);
				System.out.println("DaemonThread run end.");
			} finally {
				System.out.println("DaemonThread finally run.");
			}
		}
	}
}
