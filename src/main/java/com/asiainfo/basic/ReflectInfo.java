package com.asiainfo.basic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月16日  下午2:33:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component("reflect")
public class ReflectInfo extends ReflectParent implements ReflectInterface {

	public static final String FLAG = "FLAG";
	private String name;
	private int age;
	
	public ReflectInfo() {
		this("jaeson");
	}
	public ReflectInfo(String name) {
		this(name, 20);
	}
	private ReflectInfo(String name, int age) {
		super(name, age);
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	
	public void greeting(String greeting) {
		System.out.println(greeting + " " + this.name);
		this.privateMethod();
	}
	
	private void privateMethod() {}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Class<?> clazz = ReflectInfo.class;
		clazz = new ReflectInfo().getClass();
		//ClassNotFoundException
		clazz = Class.forName("com.asiainfo.reflect.ReflectInfo");
		//classloader
		System.out.println(clazz.getClassLoader().getClass());
		//全限定类名
		System.out.println(clazz.getName());
		//类名
		System.out.println(clazz.getSimpleName());
		//类的修饰符
		System.out.println(clazz.getModifiers());
		System.out.println(Modifier.isAbstract(clazz.getModifiers()));
		System.out.println(Modifier.isPublic(clazz.getModifiers()));
		//包
		System.out.println(clazz.getPackage().getName());
		//父类
		System.out.println(clazz.getSuperclass().getName());
		//父接口：只返回当前类所实现的接口
		System.out.println(clazz.getInterfaces()[0].getName());
		//public构造器
		for (Constructor<?> con : clazz.getConstructors()) {
			System.out.println(con);
		}
		System.out.println("====================================");
		//所有构造器
		for (Constructor<?> con : clazz.getDeclaredConstructors()) {
			System.out.println(con);
		}
		System.out.println("====================================");
		//public方法：包含继承的方法
		for (Method method : clazz.getMethods()) {
			System.out.println(method);
		}
		System.out.println("====================================");
		//声明的方法：包含继承的public方法(不包含Object的方法)
		for (Method method : clazz.getDeclaredMethods()) {
			System.out.println(method);
		}
		System.out.println("====================================");
		//public field：包含父类
		for (Field field : clazz.getFields()) {
			System.out.println(field);
		}
		System.out.println("====================================");
		//声明的field
		for (Field field : clazz.getDeclaredFields()) {
			System.out.println(field);
		}
		System.out.println("====================================");
		//注解
		for (Annotation anno : clazz.getAnnotations()) {
			System.out.println(anno);
		}
	}
}

interface ReflectInterface {}
class ReflectParent {
	
	public String parentName;
	private int parentAge;

	ReflectParent() {}
	public ReflectParent(String name, int age) {
		this.parentName = name;
		this.parentAge = age;
	}
	
	public String getParentName() {
		return parentName;
	}
	public int getParentAge() {
		return parentAge;
	}
	
	@SuppressWarnings("unused")
	private void parentPrivate() {}
	protected void parentProtected() {}
	public void parentPublic() {}
}
