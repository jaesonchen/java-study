package com.asiainfo.concurrent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 从JDK 5开始，JSR-133增强了volatile语义，禁止了volatile变量与普通变量之间的重排序。
 * 
 * Happens-before规则：一个volatile变量的写操作发生在这个volatile变量随后的读操作之前。
 *  它的含义是当一个线程执行写入volatile变量的操作后，另一个线程执行读取volatile变量的操作，
 *  此时jmm会将第一个线程写volatile之前的所有共享变量(包括非volatile)的修改刷新到主存中，并将修改过的内容的缓存置为无效，导致其他CPU去主存读取。
 *  从而保证了共享变量的可见性。
 *  
 *  volatile 修饰的变量（包括long、double）保证简单的赋值setField与返回操作getField的原子性，但是不保证++操作的原子性。
 * 
 * @author       zq
 * @date         2018年4月27日  上午11:14:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class VolatileExample {

    volatile static int num = 0;
    static boolean flag = true;
    static boolean synFlag = true;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        // volatile read
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                
                System.out.println("t start.");
                int i = 0;
                int count = 0;
                // 这里使用共享变量，借助volatile的读刷新缓存
                while (VolatileExample.flag) {
                    // 必须有volatile读操作才能触发happens-before规则，使本地内存缓存失效，否则无法跳出循环
                    i = VolatileExample.num;
                    count++;
                }
                System.out.println("t end." + " i=" + i + ", count=" + count);
            }
        });
        t.start();
        // 第一个线程先运行到条件判断
        TimeUnit.MILLISECONDS.sleep(100);
        // write cache flush
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                
                System.out.println("s start.");
                // 普通共享变量的修改由于volatile读操作也会立即写入主存里，如果没有volatile读操作则不会立即刷新到主缓存
                VolatileExample.flag = false;
                while (true && !Thread.currentThread().isInterrupted()) {}
                System.out.println("s end.");
            }
        });
        s.start();
        System.in.read();
        s.interrupt();
       
        // synchrozied
        Thread syn = new Thread(new Runnable() {
            @Override
            public void run() {
                
                System.out.println("syn start.");
                int count = 0;
                while (getFlag()) {
                    count++;
                }
                System.out.println("syn end. count=" + count);
            }
        });
        syn.start();
        TimeUnit.SECONDS.sleep(1);
        setFlag(false);
        System.out.println("main end.");
    }
    public synchronized static boolean getFlag() {
        return synFlag;
    }
    public synchronized static void setFlag(boolean flag) {
        VolatileExample.synFlag = flag;
    }
}

