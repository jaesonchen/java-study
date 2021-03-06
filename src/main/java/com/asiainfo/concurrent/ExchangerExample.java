package com.asiainfo.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * Exchanger示例 
 * 用于进行线程间的数据交换。它提供一个同步点，在这个同步点，两个线程可以交换彼此的数据。
 * 
 * 应用场景： 
 * 》可以用于遗传算法： 
 * 》可以用于校对工作：
 * 
 * @author       zq
 * @date         2017年9月3日  下午4:29:45
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExchangerExample {
	
	private static Exchanger<String> exchanger = new Exchanger<>();
	private static ExecutorService threadPool = ThreadPoolUtils.getInstance().fixedThreadPool(2);

	public static void main(String[] args) {
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				String aA = "银行流水A";
				try {
					String x = exchanger.exchange(aA);
					System.out.println("A线程得到的交换数据 : " + x);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				String bB = "银行流水B";
				try {
					String aA = exchanger.exchange(bB);
					System.out.println("A和B数据是否一致 ： " + aA.equals(bB) + " , A录入的是 ： " + aA + " , B录入的是 : " + bB);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		threadPool.shutdown();
	}
}
