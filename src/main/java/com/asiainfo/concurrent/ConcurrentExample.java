package com.asiainfo.concurrent;

/**
 * 对于共享的可变数据访问如果不同步，则会引起意想不到的结果。
 * 没有同步共享变量的访问，虚拟机将循环代码：
 *	while (!done) {
 * 		i++;
 *	}
 *  优化为：
 *	if (!done) {
 * 		while (true) { 
 *  		i++;
 *  	}
 *  }
*/
public class ConcurrentExample {

	private static boolean stopRequested;
	
	public static void main(String[] args) throws Exception {
		
		Thread bgThread = new Thread(new Runnable() {
			public void run() {
				long i = 0L;
				while (!stopRequested)
					i++;
				System.out.println("i = " + i);
			}
		});
		
		bgThread.start();
		Thread.sleep(1000);
		stopRequested = true;
	}
}
