package com.asiainfo.insidejvm;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:47:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class PassiveUse {

	public static void main(String[] args) {
		
		System.out.println(NewBaby.hoursOfSleep);
		System.out.println(NewBaby.GREETING);
	}
	
	static {
		System.out.println("PassiveUse is initialized");
	}
}
class NewParent {
	
	static int hoursOfSleep = (int) (Math.random() * 3.0);
	static {
		System.out.println("NewParent is initialized");
	}
}
class NewBaby extends NewParent {
	
	static final String GREETING = "hello world";
	
	static int hoursOfCry = (int) (Math.random() * 2.0);
	static {
		System.out.println("NewBaby is initialized");
	}
}