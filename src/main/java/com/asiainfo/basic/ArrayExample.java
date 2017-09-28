package com.asiainfo.basic;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月11日  上午10:35:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ArrayExample {

	static class Element {}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		
		//Array反射
		int[] intArray = (int[]) Array.newInstance(int.class, 3);
		Array.set(intArray, 0, 123);
		Array.set(intArray, 1, 456);
		Array.set(intArray, 2, 789);
		for (int i = 0; i < intArray.length; i++) {
			System.out.println("intArray[" + i + "] = " + Array.get(intArray, i));
		}
		Element[] eleArr = new Element[2];
		System.out.println(eleArr.length);
		Element[][] arr = (Element[][]) Array.newInstance(eleArr.getClass(), 5);
		System.out.println(arr.length);

		//原生数组class
		@SuppressWarnings("unused")
		Class<?> intArrayClass = Class.forName("[I");
		//对象数组clazz
		Class<?> stringArrayClass = String[].class;
		stringArrayClass = Class.forName("[Ljava.lang.String;");
		//数组元素类型
		Class<?> stringArrayComponentType = stringArrayClass.getComponentType();
		System.out.println(stringArrayComponentType);
	}
	
	public Class<?> getClass(String className) throws ClassNotFoundException {

		if ("int" .equals(className)) return int .class;
		if ("long".equals(className)) return long.class;
		return Class.forName(className);
	}
	
	/**
	 * 
	 * @param anArray
	 * @return
	 */
	static Map<Object, Integer> countDuplicates(Object anArray) {
		
	    if (!anArray.getClass().isArray()) {
	        throw new IllegalArgumentException("anArray is not an array");
	    }

	    Map<Object, Integer> map = new HashMap<Object, Integer>();
	    int length = Array.getLength(anArray);
	    for (int i = 0; i < length; i++) {
	        Object dup = Array.get(anArray, i);         
	        Integer count = map.get(dup);
	        map.put(dup, count == null ? 1 : count + 1);
	    }
	    return map;
	}
}
