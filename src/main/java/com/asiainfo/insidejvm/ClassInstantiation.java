package com.asiainfo.insidejvm;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:34:45
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("unused")
public class ClassInstantiation {

	public static void main(String[] args) 
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, CloneNotSupportedException {
		
		Example obj1 = new Example("new");
		
		Class<Example> myClass = Example.class;
		Example obj2 = myClass.newInstance();
		
		Example obj3 = (Example) obj1.clone();
	}
}

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:34:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SuppressWarnings("all")
class Example implements Cloneable {
	Example() {
		System.out.println("create by newInstance()");
	}
	Example(String msg) {
		System.out.println("create by " + msg);
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}