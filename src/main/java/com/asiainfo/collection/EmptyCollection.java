package com.asiainfo.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmptyCollection {

	public static void main(String[] args) {
		
		//只能用来迭代的空实现
		List<Object> list = Collections.emptyList();
		Set<Object> set = Collections.emptySet();
		Map<Object, Object> map = Collections.emptyMap();

		try {
			list.add(new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			set.add(new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			map.put(new Object(), new Object());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		//可以修改集合的空实现
		list = new ArrayList<Object>(0);
		list.add(new Object());
		System.out.println(list.size());
		
		set = new HashSet<Object>(0);
		set.add(new Object());
		System.out.println(set.size());
		
		map = new HashMap<Object, Object>(0);
		map.put(new Object(), new Object());
		System.out.println(map.size());
		
		//空数组
		@SuppressWarnings("unused")
		Object[] objArray = new Object[0];
	}
}
