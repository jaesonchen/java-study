package com.asiainfo.jdbc.util;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:59:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class NoAnnotationFoundException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private Class<?> annotationClazz;
	
	public NoAnnotationFoundException(Class<?> annotationClazz) {
		super("No " + annotationClazz.getSimpleName() + " Annotation Found");
		this.annotationClazz = annotationClazz;
	}

	public Class<?> getAnnotationClazz() {
		return annotationClazz;
	}
	public void setAnnotationClazz(Class<?> annotationClazz) {
		this.annotationClazz = annotationClazz;
	}

	@Override
	public String toString() {
		return "NoAnnotationFoundException [annotationClazz=" + annotationClazz + "]";
	}
}
