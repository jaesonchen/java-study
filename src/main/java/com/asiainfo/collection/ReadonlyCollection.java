package com.asiainfo.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collections、Arrays 提供的只读集合
 * 
 * @author       zq
 * @date         2017年10月16日  下午12:38:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReadonlyCollection {

	public static void main(String[] args) {
		
		// Collections提供的只能用来迭代的空实现
		List<Object> list = Collections.emptyList();
		Set<Object> set = Collections.emptySet();
		Map<Object, Object> map = Collections.emptyMap();
		try {
		    // UnsupportedOperationException
			list.add(new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
		    // UnsupportedOperationException
			set.add(new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
		    // UnsupportedOperationException
			map.put(new Object(), new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// 可以修改的空集合
		list = new ArrayList<Object>(0);
		list.add(new Object());
		System.out.println(list.size());
		
		// Arrays.asList 返回的是固定大小的list，可以get、set，可以迭代，不能add、remove
		List<String> list1 = Arrays.asList(new String[] {"1", "2", "3"});
		try {
		    list1.set(0, "4");
		    System.out.println(list1.get(0));
		    // UnsupportedOperationException
			list1.add("5");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 可以迭代
		Iterator<String> it = list1.iterator();
		while (it.hasNext()) {
		    System.out.println(it.next());
		}

		// 可以重构为ArrayList
		List<String> list2 = new ArrayList<>(Arrays.asList(new String[] {"1", "2", "3"}));
		list2.add("4");
		System.out.println(list2);
		
		// List.subList 返回的子list 不再是Serializable，rpc应用中需要转换为ArrayList
		List<String> list3 = list2.subList(0, 2);
		System.out.println(list3 instanceof Serializable);
		
		// ArrayList.subList() 只是返回列表的一个视图，不会新建列表，对子列表视图的修改都将影响到父列表。
        List<String> subList = list2.subList(0, 3);
        subList.remove(2);
        System.out.println(list2);
	}
}
