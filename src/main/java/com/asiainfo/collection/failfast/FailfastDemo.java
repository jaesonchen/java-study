package com.asiainfo.collection.failfast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * java Iterator的fail-fast、fail-safe：
 * 
 * Fail-Fast: 从字面含义看就是“快速失败”，尽可能的发现系统中的错误，使系统能够按照事先设定好的错误的流程执行，对应的方式是“fault-tolerant（错误容忍）”。
 * 			    以JAVA集合（Collection）的快速失败为例，当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件。
 * 
 * Fail-Safe: Fail-Safe的含义为“失效安全”，即使在故障的情况下也不会造成伤害或者尽量减少伤害。维基百科上一个形象的例子是红绿灯的“冲突监测模块”当监测到错误
 * 			    或者冲突的信号时会将十字路口的红绿灯变为闪烁错误模式，而不是全部显示为绿灯。
 * 
 * 
 * @author       zq
 * @date         2017年8月28日  下午4:51:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FailfastDemo {

	public static void main(String[] args) throws InterruptedException {
		
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4 }));
         
        // fail-fast 单线程 Iterator
        Iterator<Integer> it = list.iterator();
        try {
	        while (it.hasNext()) {
	            list.add(5);      // This will change modCount
	        	it.next();        // This will throw ConcurrentModificationException
	        }
        } catch (Exception ex) {
        	System.out.println("single thread Iterator fail-fast: " + ex);
        }
        
        // 可以使用 Iterator.remove
        System.out.println("list: " + list);
        it = list.iterator();
        while (it.hasNext()) {
            int n = it.next();
            System.out.println("iterator: " + n);
            if (0 == n) {
                it.remove();
            }
        }
        
        // fail-fast 单线程 foreach
        try {
            for (Integer i : list) {
                System.out.println("foreach: " + i);
                list.remove(list.size() - 1);
            }
        } catch (Exception ex) {
            System.out.println("single thread foreach fail-fast: " + ex);
        }
        
        // fail-fast 多线程
        System.out.println("list: " + list);
        it = list.iterator();
        Thread t = new Thread(() -> {
                System.out.println("t start!");
                list.add(5);
                while (!Thread.currentThread().isInterrupted()) {
                    // 
                }
                System.out.println("t end!");
        });
        t.start();
        TimeUnit.SECONDS.sleep(1); // 等待线程t启动
        try {
            while (it.hasNext()) {
                it.next();
            }
        } catch (Exception ex) {
            System.out.println("multi thread fail-fast: " + ex);
        }
        t.interrupt();
        
        // fail-safe
        List<Integer> cpList = new CopyOnWriteArrayList<>(list);
        Iterator<Integer> cpIt = cpList.iterator(); // 读取数组的snapshot
        while (cpIt.hasNext()) {
        	cpList.add(6); // 每次add都会重新构造一个数组
        	System.out.println(cpIt.next());
        }
        System.out.println("cpList: " + cpList);
	}
}
