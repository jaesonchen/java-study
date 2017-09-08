package com.asiainfo.rpc.test;

import com.asiainfo.rpc.RpcClient;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午11:32:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcClientTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		IHelloService service = new RpcClient().refer(IHelloService.class, "localhost", 20880);
		System.out.println(service.hello("jaesonchen"));
	}
}
