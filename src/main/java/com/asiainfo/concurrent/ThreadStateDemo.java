package com.asiainfo.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**   
 * @Description: 线程状态测试
 * 
 * @author chenzq  
 * @date 2019年8月24日 下午3:04:57
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ThreadStateDemo {

    public static void main(String[] args) {

        testNew();
        testRunnable();
        testRunning();
        testSynchronized();
        testWaiting();
        testTimedWaiting();
        testTerminated();
    }
    
    // runnable
    public static void testNew() {
        
        Thread t = new Thread(() -> {});
        System.out.println(t.getState());
    }
    
    // runnable
    public static void testRunnable() {
        
        Thread t = new Thread(() -> {});
        t.start();
        System.out.println(t.getState());
    }

    // running
    public static void testRunning() {
        
        Thread t = new Thread(() -> {
            while (!Thread.interrupted()) {
                
            }
        });
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //
        }
        System.out.println(t.getState());
        t.interrupt();
    }
    
    // blocked, 进入synchronized 方法/代码块获取时monitor lock，或者从Object.wait()中被唤醒时重新获取monitor lock
    public static void testSynchronized() {
        
        Thread t1 = new Thread(() -> {
            service();
        });
        t1.start();
        
        Thread t2 = new Thread(() -> {
            service();
        });
        t2.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //
        }
        System.out.println("t1 is " + t1.getState());
        System.out.println("t2 is " + t2.getState());
    }
    
    public static synchronized void service() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }
    }
    
    // waiting, LockSupport.park() / Object.wait() / Thread.join()
    public static void testWaiting() {
        final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Thread t = new Thread(() -> {
            try {
                queue.take();
            } catch (InterruptedException e) {
                // 
            }
        });
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //
        }
        System.out.println(t.getState());
        queue.offer("hello world");
    }
    
    // timed_waiting, Thread.sleep(timeout) / Object.wait(timeout) / Thread.join(timeout) / LockSupport.parkNanos(timeout) / LockSupport.parkUntil(deadline)
    public static void testTimedWaiting() {
        
        Thread t = new Thread(() -> {
            LockSupport.parkNanos(1L * 1000 * 1000 * 1000);
        });
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //
        }
        System.out.println(t.getState());
    }
    
    // terminated
    public static void testTerminated() {
        
        Thread t = new Thread(() -> {});
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //
        }
        System.out.println(t.getState());
    }
}
