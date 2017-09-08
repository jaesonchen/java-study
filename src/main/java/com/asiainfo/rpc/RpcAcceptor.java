package com.asiainfo.rpc;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午10:38:37
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcAcceptor implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Selector selector;
	final ServerSocketChannel ssChannel;
	
	public RpcAcceptor(Selector selector, ServerSocketChannel ssChannel) {
		logger.info("new RpcAcceptor...");
		this.selector = selector;
		this.ssChannel = ssChannel;
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		logger.info("{} 处理连接请求accept...", this.selector);
		try {
			 SocketChannel channel = this.ssChannel.accept();
			 if (channel != null) {
				 logger.info("{} 连接完成...", this.selector);
				 new RpcHandler(this.selector, channel);
			 } else {
				 logger.info("{} 连接出现异常，未能建立连接...", selector);
			 }
		} catch (IOException ex) {
			logger.error("error on handle connection...", ex);
		}
	}
}
