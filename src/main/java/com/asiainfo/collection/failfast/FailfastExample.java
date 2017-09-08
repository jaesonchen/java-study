package com.asiainfo.collection.failfast;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Description: TODO
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
        ArrayList<Integer> list = new ArrayList<Integer>();
         
        //Adding elements to list
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
         
        //Getting an Iterator from list
        Iterator<Integer> it = list.iterator();
        
        while (it.hasNext()) {
            
        	it.next();
            
            list.add(6);      //This will throw ConcurrentModificationException
        }
	}
}
