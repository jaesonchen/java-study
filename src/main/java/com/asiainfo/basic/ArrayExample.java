package com.asiainfo.basic;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: java.lang.reflect.Array 操作数组
 * 
 * @author       zq
 * @date         2017年9月11日  上午10:35:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ArrayExample {

	static class Element {}

	public static void main(String[] args) throws ClassNotFoundException {
		
		// 新建数组
		int[] intArray = (int[]) Array.newInstance(int.class, 3);
		// 设置数组值
		Array.set(intArray, 0, 123);
		Array.set(intArray, 1, 456);
		Array.set(intArray, 2, 789);
		// 获取数组值
		for (int i = 0; i < intArray.length; i++) {
			System.out.println("intArray[" + i + "] = " + Array.get(intArray, i));
		}
		
		Element[] eleArray = new Element[2];
		System.out.println(eleArray.length);
		Element[][] arr = (Element[][]) Array.newInstance(eleArray.getClass(), 5);
		System.out.println(arr.length);

		//原生数组class
		System.out.println(int[].class.equals(Class.forName("[I")));
		
		//对象数组class
		System.out.println(String[].class.equals(Class.forName("[Ljava.lang.String;")));

		//数组元素类型
		Class<?> stringArrayComponentType = String[].class.getComponentType();
		System.out.println(stringArrayComponentType);
	}
	
	// 加载class
	public Class<?> getClass(String className) throws ClassNotFoundException {

		if ("int" .equals(className)) {
			return int.class;
		}
		if ("long".equals(className)) {
			return long.class;
		}
		return Class.forName(className);
	}
	
	// 操作数组
	public Map<Object, Integer> countDuplicates(Object array) {
		
	    if (!array.getClass().isArray()) {
	        throw new IllegalArgumentException("array is not an array");
	    }
	    int length = Array.getLength(array);
	    Map<Object, Integer> map = new HashMap<Object, Integer>(length);
	    for (int i = 0; i < length; i++) {
	        Object dup = Array.get(array, i);         
	        Integer count = map.get(dup);
	        map.put(dup, count == null ? 1 : count + 1);
	    }
	    return map;
	}
}
