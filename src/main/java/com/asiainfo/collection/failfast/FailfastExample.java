package com.asiainfo.collection.failfast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Fail-Fast: 从字面含义看就是“快速失败”，尽可能的发现系统中的错误，使系统能够按照事先设定好的错误的流程执行，对应的方式是“fault-tolerant（错误容忍）”。
 * 			    以JAVA集合（Collection）的快速失败为例，当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件。
 * 
 * Fail-Over: Fail-Over的含义为“失效转移”，是一种备份操作模式，当主要组件异常时，其功能转移到备份组件。其要点在于有主有备，且主故障时备可启用，并设置为主。
 * 			    如Mysql的双Master模式，当正在使用的Master出现故障时，可以拿备Master做主使用。
 * 
 * Fail-Safe: Fail-Safe的含义为“失效安全”，即使在故障的情况下也不会造成伤害或者尽量减少伤害。维基百科上一个形象的例子是红绿灯的“冲突监测模块”当监测到错误
 * 			    或者冲突的信号时会将十字路口的红绿灯变为闪烁错误模式，而不是全部显示为绿灯。
 * 
 * 迭代器的快速失败行为无法得到保证，它不能保证一定会出现该错误，因此，ConcurrentModificationException应该仅用于检测 bug。
 * Java.util包中的所有集合类都是快速失败的，而java.util.concurrent包中的集合类都是安全失败的；
 * 快速失败的迭代器抛出ConcurrentModificationException，而安全失败的迭代器从不抛出这个异常。
 * 
 * @author       zq
 * @date         2017年8月28日  下午4:51:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FailfastExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
        //Creating an ArrayList of integers
        List<Integer> list = new ArrayList<Integer>();
         
        //Adding elements to list
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
         
        //Getting an Iterator from list
        Iterator<Integer> it = list.iterator();
        try {
	        while (it.hasNext()) {
	            
	        	it.next();
	            list.add(6);      //This will throw ConcurrentModificationException
	        }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        //fail-safe
        List<Integer> cpList = new CopyOnWriteArrayList<>();
        cpList.add(1);
        cpList.add(2);
        cpList.add(3);
        cpList.add(4);
        cpList.add(5);
        Iterator<Integer> cpIt = cpList.iterator();
        while (cpIt.hasNext()) {
        	Integer i = cpIt.next();
        	cpList.add(6);
        	System.out.println(i);
        }
        System.out.println(cpList);
	}
}
