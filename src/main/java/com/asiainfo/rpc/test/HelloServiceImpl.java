package com.asiainfo.rpc.test;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午11:30:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class HelloServiceImpl implements IHelloService {

	/* 
	 * @Description: TODO
	 * @param name
	 * @return
	 * @see com.asiainfo.rpc.test.IHelloService#hello(java.lang.String)
	 */
	@Override
	public String hello(String name) {
		return "hello " + name;
	}
}
