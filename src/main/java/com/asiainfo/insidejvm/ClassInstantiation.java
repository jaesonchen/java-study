package com.asiainfo.insidejvm;

/**
 * 1. 对象实例化时，通过clone()获得的对象不会调用构造器
 * 2. 通过Unsafe.allocateInstance(Class) 实例化得对象，不会调用构造函数，但会调用类初始化并初始化final字段(构造函数里初始化的final不会被调用)
 *    final字段已初始化的无法修改，未初始化的final字段可以修改多次
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:34:45
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ClassInstantiation {

	public static void main(String[] args) 
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, CloneNotSupportedException {
		
		Example obj1 = new Example("new");
		System.out.println(obj1);
		
		Class<Example> clazz = Example.class;
		Example obj2 = clazz.newInstance();
		System.out.println(obj2);
		
		Example obj3 = (Example) obj1.clone();
		System.out.println(obj3);
	}
}

class Example implements Cloneable {
    
    String msg = "default";
    
	Example() {
		System.out.println("create by newInstance()");
	}
	Example(String msg) {
	    this.msg = msg;
		System.out.println("create by " + msg);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    @Override
    public String toString() {
        return "Example [msg=" + msg + "]";
    }
}