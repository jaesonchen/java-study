package com.asiainfo.rpc.test;

import java.io.IOException;

import com.asiainfo.rpc.RpcServer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午11:32:02
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcServiceTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		new RpcServer().run();
	}
}
