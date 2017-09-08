package com.asiainfo.insidejvm;

public class PassiveUse {

	public static void main(String[] args) {
		
		System.out.println(NewBaby.hoursOfSleep);
		System.out.println(NewBaby.greeting);
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
	
	static final String greeting = "hello world";
	
	static int hoursOfCry = (int) (Math.random() * 2.0);
	static {
		System.out.println("NewBaby is initialized");
	}
}