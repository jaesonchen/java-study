package com.asiainfo.proxy;

public class BusinessImpl implements Business {
    
	private String id = "default";
	public BusinessImpl() {}
	public BusinessImpl(String id) {
		this.id = id;
	}
	
	@Override
	public void service() {
		System.out.println("service.id = " + this.id);
		//自调用测试
		this.execute();
	}

	@Override
	public void execute() {
		System.out.println("execute.id = " + this.id);
	}
}
