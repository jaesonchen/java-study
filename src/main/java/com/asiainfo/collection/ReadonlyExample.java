package com.asiainfo.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月28日  下午4:52:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReadonlyExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Arrays.asList 返回的是只读的list
		List<String> list1 = Arrays.asList(new String[] {"1", "2", "3"});
		try {
			list1.remove(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		List<String> list2 = new ArrayList<>(Arrays.asList(new String[] {"1", "2", "3"}));
		list2.add("4");
		System.out.println(list2);
		
		//List.subList 返回的子list 不再是Serializable，rpc应用中需要保证成ArrayList
		List<String> list3 = list2.subList(0, 2);
		System.out.println(list3 instanceof Serializable);
	}
}
