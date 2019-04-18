package com.asiainfo.basic;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description: java 注解
 * 
 * @author       zq
 * @date         2017年9月16日  下午5:28:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@MyAnnotation(name="jasonchen", value="asiainfo")
public class AnnotationInfo {

	@MyFieldAnnotation("serviceCode")
	String serviceCode;
	
	@MyServiceAnnotation("methodAnnotation")
	public void service(@MyServiceAnnotation("paramAnnotation") String code) {}
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, NoSuchFieldException {

		Class<AnnotationInfo> clazz = AnnotationInfo.class;
		//获取类型的所有注解
		for (Annotation annotation : clazz.getAnnotations()) {
			if (annotation instanceof MyAnnotation) {
				MyAnnotation myAnnotation = (MyAnnotation) annotation;
				System.out.println("class name: " + myAnnotation.name());
				System.out.println("class value: " + myAnnotation.value());
			}
		}
		
		//获取类型指定的注解
		MyAnnotation myAnnotation = clazz.getAnnotation(MyAnnotation.class);
		if (null != myAnnotation) {
			System.out.println("class name: " + myAnnotation.name());
			System.out.println("class value: " + myAnnotation.value());
		}
		
		//获取方法的注解
		Method method = clazz.getDeclaredMethod("service", String.class);
		MyServiceAnnotation serviceAnnotation = method.getAnnotation(MyServiceAnnotation.class);
		if (null != serviceAnnotation) {
			System.out.println("method value: " + serviceAnnotation.value());
		}
		
		//获取方法的参数注解
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		int i = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			Class<?> parameterType = parameterTypes[i++];
			for (Annotation annotation : annotations) {
				if (annotation instanceof MyServiceAnnotation) {
					serviceAnnotation = (MyServiceAnnotation) annotation;
					System.out.println("methdo param: " + parameterType.getName());
					System.out.println("methdo value: " + serviceAnnotation.value());
				}
			}
		}
		
		//获取field的注解
		Field field = clazz.getDeclaredField("serviceCode");
		MyFieldAnnotation fieldAnnotation = field.getAnnotation(MyFieldAnnotation.class);
		if (null != fieldAnnotation) {
			System.out.println("field value: " + fieldAnnotation.value());
		}
	}
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyAnnotation {
	public String name();
	public String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@interface MyServiceAnnotation {
	public String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MyFieldAnnotation {
	public String value();
}