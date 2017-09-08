package com.asiainfo.insidejvm;

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
@SuppressWarnings("all")
class Example implements Cloneable {
	Example() {
		System.out.println("create by newInstance()");
	}
	Example(String msg) {
		System.out.println("create by " + msg);
	}
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}