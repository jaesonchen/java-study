package com.asiainfo.reactor;

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
 * @date         2017年8月11日  下午4:34:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Acceptor implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Selector selector;
	final ServerSocketChannel ssChannel;
	
	public Acceptor(Selector selector, ServerSocketChannel ssChannel) {
		logger.info("in Acceptor constructor...");
		this.selector = selector;
		this.ssChannel = ssChannel;
	}

	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		logger.info("{} accept...", this.selector);
		try {
			 SocketChannel channel = this.ssChannel.accept();
			 if (channel != null) {
				 logger.info("{} clientChannel not null...", this.selector);
				 new Handler(this.selector, channel);
			 } else {
				 logger.info("{} clientChannel is null...", selector);
			 }
		} catch (IOException ex) {
			logger.error("error on handle connection...", ex);
		}
	}
}
