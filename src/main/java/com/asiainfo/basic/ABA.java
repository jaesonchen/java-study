package com.asiainfo.basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * cas(compareAndSet) 会导致aba问题
 * CAS算法实现一个重要前提需要取出内存中某时刻的数据，而在下时刻比较并替换，那么在这个时间差会导致数据的变化。
 *   
 * 
 * AtomicStampedReference原子类是一个带有时间戳的对象引用，在每次修改后，AtomicStampedReference不仅会设置新值而且还会记录更改的时间。
 * 当AtomicStampedReference设置对象值时，对象值以及时间戳都必须满足期望值才能写入成功。
 * 
 * @author       zq
 * @date         2017年9月18日  下午3:39:27
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ABA {

	static AtomicInteger atIn = new AtomicInteger(100);

    //初始化时需要传入一个初始值和初始时间
    static AtomicStampedReference<Integer> atomicStampedR =
            new AtomicStampedReference<Integer>(100, 0);

    static Thread t1 = ThreadPoolUtils.getInstance().newThread(new Runnable() {
        @Override
        public void run() {
            //更新为200
            atIn.compareAndSet(100, 200);
            //更新为100
            atIn.compareAndSet(200, 100);
        }
    });

    static Thread t2 = ThreadPoolUtils.getInstance().newThread(new Runnable() {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // ignore
            }
            boolean flag = atIn.compareAndSet(100, 500);
            System.out.println("flag:" + flag + ", newValue:" + atIn);
        }
    });

    static Thread t3 = ThreadPoolUtils.getInstance().newThread(new Runnable() {
        @Override
        public void run() {
            int time = atomicStampedR.getStamp();
            //更新为102，Integer类型比较的是引用
            atomicStampedR.compareAndSet(100, 102, time, time + 1);
            //更新为100
            int time2 = atomicStampedR.getStamp();
            atomicStampedR.compareAndSet(102, 100, time2, time2 + 1);
        }
    });

    static Thread t4 = ThreadPoolUtils.getInstance().newThread(new Runnable() {
        @Override
        public void run() {
            int time = atomicStampedR.getStamp();
            System.out.println("sleep 前 t4 time:" + time);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean flag = atomicStampedR.compareAndSet(100, 103, time, time + 1);
            System.out.println("flag:" + flag + ", time:" + atomicStampedR.getStamp() + ", newValue:" + atomicStampedR.getReference());
        }
    });
    
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        t3.start();
        t4.start();
        t3.join();
        t4.join();        
	}
}
