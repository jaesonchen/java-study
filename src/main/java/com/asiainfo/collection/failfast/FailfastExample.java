package com.asiainfo.collection.failfast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * java Iterator的fail-fast、fail-safe：
 * 
 * Fail-Fast: 从字面含义看就是“快速失败”，尽可能的发现系统中的错误，使系统能够按照事先设定好的错误的流程执行，对应的方式是“fault-tolerant（错误容忍）”。
 * 			    以JAVA集合（Collection）的快速失败为例，当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件。
 * 
 * Fail-Safe: Fail-Safe的含义为“失效安全”，即使在故障的情况下也不会造成伤害或者尽量减少伤害。维基百科上一个形象的例子是红绿灯的“冲突监测模块”当监测到错误
 * 			    或者冲突的信号时会将十字路口的红绿灯变为闪烁错误模式，而不是全部显示为绿灯。
 * 
 * 迭代器(Iterator)的快速失败行为无法得到保证，它不能保证一定会出现该错误，因此，ConcurrentModificationException应该仅用于检测 bug。
 * Java.util包中的所有集合类都是快速失败的(Fail-Fast)，而java.util.concurrent包中的集合类都是安全失败的(Fail-Safe)，比如CopyOnWriteArrayList。
 * 快速失败的迭代器抛出ConcurrentModificationException，而安全失败的迭代器从不抛出这个异常。
 * 
 * 当Iterator、ListIterator执行next、remove、previous、set、add操作时，如果modcount值不一致，表示集合遇到非预期的修改，会直接丢出ConcurrentModificationException异常。
 * 会导致modcount计数增加的操作有add、remove、clear等会导致集合结构化变化的操作，表现为集合的size发生变化，如果是set等替换操作一般不会修改modcount。
 * Iterator本身的add、remove操作会自动更新modcount，不会引发异常。
 * 
 * 
 * @author       zq
 * @date         2017年8月28日  下午4:51:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FailfastExample {

	public static void main(String[] args) {
		
        List<Integer> list = new ArrayList<Integer>();
        //Adding elements to list
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
         
        // fail-fast
        Iterator<Integer> it = list.iterator();
        try {
	        while (it.hasNext()) {
	            list.add(6);      //This will change modCount
	        	it.next();        //This will throw ConcurrentModificationException
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
        	cpList.add(6);
        	System.out.println(cpIt.next());
        }
        System.out.println(cpList);
	}
}
