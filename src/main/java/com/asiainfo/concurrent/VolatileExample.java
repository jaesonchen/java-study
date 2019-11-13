package com.asiainfo.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * JDK 5开始，JSR-133增强了volatile语义，禁止了volatile变量与普通变量之间的重排序。
 * 
 * volatile 修饰的变量（包括long、double）保证简单的赋值setField与返回操作getField的原子性，但是不保证++操作的原子性。
 * 
 * - 当写一个 volatile 变量时，JMM 会把该线程对应的工作内存中的共享变量值刷新到主内存。
 * - 当读一个 volatile 变量时，JMM 会把该线程对应的工作内存缓存行置为无效。线程接下来将从主内存中读取共享变量到工作内存。
 * 
 * 
 * volatile写使共享变量的缓存行失效，缓存行里的普通共享变量是否会失效？
 * - 不会使得缓存行里的普通共享变量失效，只有在读volatile共享变量时，才会重新加载整个缓存行，一个缓存行通常是64个字节，
 * - 有很大可能同一个java对象内的volatile和普通共享变量会在同一个缓存行被加载，从而间接实现了普通共享变量的重新加载。
 * 
 * 
 * volatile写使缓存行失效，volatile读每次都会去读主存？
 * - volatile是一种类型修饰符，被volatile声明的变量表示随时可能发生变化，每次使用时，都必须从变量对应的内存地址读取。
 * 
 * 
 * java普通变量写时，是否立即写到主存？
 * - 在多线程的环境下，如果某个线程首次读取共享变量，则首先到主内存中获取该变量，然后存入工作内存中，以后只需要在工作内存中读取该变量即可。
 * - 同样如果对该共享变量执行了写操作，则先将新值写入工作内存中，然后再刷新至主内存中。但是什么时候最新的值会被刷新至主内存中是不太确定的。
 * 
 * 
 * 
 * @author       zq
 * @date         2018年4月27日  上午11:14:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class VolatileExample {
    
    long ll = 0L;
    volatile long num = 0;
    long ss = 0;
    boolean flag = true;
    
    public static void main(String[] args) throws Exception {
        
        final VolatileExample obj = new VolatileExample();

        // normal variable read
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t start.");
                // volatile 读，把普通共享变量一起读入缓存行
                long count = 0;
                // 借助volatile，把普通共享变量一起失效，从而导致从主内存重新加载
                while (obj.ss == 0) {
                    count += obj.num;
                    count++;
                    /*if (count % 1000000 == 0) {
                        System.out.println("count=" + count);
                    }*/
                }
                System.out.println("t end" + ", count=" + count);
            }
        });
        t.start();
        
        System.out.println("obj.flag=" + obj.flag);
        // volatile write
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("s start.");
                obj.flag = false;
                obj.ll = 1;
                obj.ss = 1;
                //obj.num = 1;
                /*// 非volatile变量的写不能保证什么时候刷新到内存
                obj.flag = false;
                while (true && !Thread.currentThread().isInterrupted()) {}*/
                System.out.println("s end.");
            }
        });
        TimeUnit.SECONDS.sleep(1);
        s.start();
        System.out.println("main wait.");
        System.in.read();
        System.out.println("obj.flag=" + obj.flag);

        System.out.println("main end.");
    }
}

