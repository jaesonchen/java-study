package com.asiainfo.dynamicproxy;

/**
 * 代理模式的实现
 */

public class BusinessProxy implements Business {

	private Business business;
	
	public BusinessProxy(Business business) {
		this.business = business;
	}

	private void doBefore() {
		System.out.println("before service");
	}
	
	private void doAfter() {
		System.out.println("after service");
	}
	
	@Override
	public void service() {
		
		this.doBefore();
		this.business.service();
		this.doAfter();
	}
	
	@Override
	public void execute() {
		
		this.doBefore();
		this.business.service();
		this.doAfter();
	}
	
	public static void main(String[] args) {
		
		BusinessProxy proxy = new BusinessProxy(new BusinessImpl());
		proxy.service();	
	}
}
