package com.asiainfo.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Description: java 泛型 示例
 * 
 * @author       zq
 * @date         2017年9月16日  下午6:33:07
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class GenericExample {

	private List<String> list;
	public List<String> getList() {
		return this.list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws NoSuchFieldException 
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, NoSuchFieldException {

		Class<GenericExample> clazz = GenericExample.class;
		
		Method method = clazz.getMethod("getList", (Class<?>[]) null);
		// getGenericReturnType 方法返回值泛型类型
		Type returnType = method.getGenericReturnType();
		// ParameterizedType 泛型参数化类型
		if (returnType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) returnType;
			// getActualTypeArguments 返回实际参数类型
			Type[] typeArguments = type.getActualTypeArguments();
			for (Type typeArgument : typeArguments) {
				Class<?> typeArgClass = (Class<?>) typeArgument;
				System.out.println("typeArgClass = " + typeArgClass);
			}
		}
		
		method = clazz.getMethod("setList", List.class);
		// getGenericParameterTypes 方法参数的泛型类型
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		for (Type genericParameterType : genericParameterTypes) {
			if (genericParameterType instanceof ParameterizedType) {
				ParameterizedType aType = (ParameterizedType) genericParameterType;
				Type[] parameterArgTypes = aType.getActualTypeArguments();
				for (Type parameterArgType : parameterArgTypes) {
					Class<?> parameterArgClass = (Class<?>) parameterArgType;
					System.out.println("parameterArgClass = " + parameterArgClass);
				}
			}
		}
		
		Field field = clazz.getDeclaredField("list");
		// getGenericType field泛型参数类型
		Type genericFieldType = field.getGenericType();
		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			for (Type fieldArgType : fieldArgTypes) {
				Class<?> fieldArgClass = (Class<?>) fieldArgType;
				System.out.println("fieldArgClass = " + fieldArgClass);
			}
		}
	}
}