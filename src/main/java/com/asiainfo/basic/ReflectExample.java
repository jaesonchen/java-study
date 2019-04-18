package com.asiainfo.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: java reflect示例
 * 
 * @author       zq
 * @date         2017年9月16日  下午4:20:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReflectExample {
	
	String name;
	
	public ReflectExample(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String greeting(String greeting) {
		return greeting + " " +  this.name;
	}
	public static String helloworld() {
		return "hello world!";
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchFieldException 
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
		// getConstructor 获取指定签名的构造器
		Constructor<?> con = ReflectExample.class.getConstructor(new Class<?>[] { String.class });
		// getParameterTypes 获取参数类型
		for (Class<?> clazz : con.getParameterTypes()) {
			System.out.println(clazz);
		}
		// newInstance 新建实例对象
		ReflectExample obj = (ReflectExample) con.newInstance(new Object[] { "jaesonchen" });
		System.out.println(obj.getName());
		
		System.out.println("====================================");
		// getDeclaredField 获取字段
		Field f = obj.getClass().getDeclaredField("name");
		System.out.println(f.getType().getSimpleName() + " " + f.getName());
		// set 设置属性
    	f.setAccessible(true);
    	System.out.println("name:" + (String) f.get(obj));
    	f.set(obj, "chenzq");
    	System.out.println("name:" + obj.getName());
    	// invoke 方法调用
    	Method method = obj.getClass().getMethod("greeting", String.class);
    	System.out.println(method.getReturnType());
    	System.out.println(method.invoke(obj, "hello"));
    	// 无参方法
    	method = obj.getClass().getMethod("getName", (Class<?>[]) null);
    	System.out.println(method.invoke(obj, (Object[]) null));
    	// 静态方法
    	method = obj.getClass().getMethod("helloworld", (Class<?>[]) null);
    	System.out.println(method.invoke(null, (Object[]) null));
    	// getter && setter
    	for (Method md : obj.getClass().getMethods()) {
    		if (isGetter(md) || isSetter(md)) {
    			System.out.println(md);
    		}
    	}
	}
	
	// 是否getter方法
	public static boolean isGetter(Method method) {

		if (!method.getName().startsWith("get")) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}
	
	// 是否getter方法
	public static boolean isSetter(Method method) {

		if (!method.getName().startsWith("set")) {
			return false;
		}
		if (method.getParameterTypes().length != 1) {
			return false;
		}
		return true;
	}
}
