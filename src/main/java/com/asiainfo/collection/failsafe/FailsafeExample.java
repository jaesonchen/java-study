package com.asiainfo.collection.failsafe;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月28日  下午4:52:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FailsafeExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
        //Creating a ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
         
        //Adding elements to map
        map.put("ONE", 1);
        map.put("TWO", 2);
        map.put("THREE", 3);
        map.put("FOUR", 4);
         
        //Getting an Iterator from map
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
        	
            String key = (String) it.next();
            System.out.println(key + " : " + map.get(key));
            map.put("FIVE", 5);     //This will not be reflected in the Iterator
        }
	}
}
