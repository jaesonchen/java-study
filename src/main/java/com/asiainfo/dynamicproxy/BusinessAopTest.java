package com.asiainfo.dynamicproxy;

public class BusinessAopTest extends BusinessImpl {

	@Override
	public void service() {
		
		System.out.println("BusinessAopTest.service begin...");
		super.service();
		System.out.println("BusinessAopTest.service end...");
	}

	@Override
	public void execute() {
		
		System.out.println("BusinessAopTest.execute begin...");
		super.execute();
		System.out.println("BusinessAopTest.execute end...");
	}
	
	public static void main(String[] args) {
		
		Business business = new BusinessAopTest();
		business.service();
	}

}
