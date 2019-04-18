package com.asiainfo.concurrent;

import java.util.concurrent.CountDownLatch;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * CountDownLatch: 基于AQS共享锁实现
 * 1、计数器必须大于等于0，等于0的时候，计数器就是零，调用await方法时不会阻塞当前线程。
 * 2、CountDownLatch不能重新初始化或者修改CountDownLatch对象内部计数器的值。
 * 3、一个线程调用countDown方法happen-before，另一个线程调用await方法。
 * 
 * @author       zq
 * @date         2017年9月3日  下午3:34:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CountDownLatchExample {
	
	static CountDownLatch cd = new CountDownLatch(2);
	
	public static void main(String[] args) throws InterruptedException {
		
		ThreadPoolUtils.getInstance().newThread(new Runnable() {
			@Override
			public void run() {
			    try {
			        System.out.println("1");
			    } finally {
			        cd.countDown();
			    }
			}
		}).start();
		
        ThreadPoolUtils.getInstance().newThread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("2");
                } finally {
                    cd.countDown();
                }
            }
        }).start();
		
        cd.await();
		System.out.println("3");
	}
}
