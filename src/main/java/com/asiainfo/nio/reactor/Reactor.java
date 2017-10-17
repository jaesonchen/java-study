package com.asiainfo.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月6日  上午11:58:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Reactor implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Selector selector;
	final ServerSocketChannel ssChannel;
	
	public Reactor(int port) throws IOException {
		this.selector = Selector.open();
		this.ssChannel = ServerSocketChannel.open();
		this.ssChannel.socket().bind(new InetSocketAddress(port));
		this.ssChannel.configureBlocking(false);
		SelectionKey sk = this.ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor(this.selector, this.ssChannel));
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			while (!Thread.interrupted()) {
				selector.select();
				//Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					dispatch((SelectionKey) (it.next()));
				}
				selector.selectedKeys().clear();
			}
		} catch (IOException ex) {
			logger.error("error on dispatch io event", ex);
		}
	}
	
	public void dispatch(SelectionKey sk) {
		
		logger.info("dispatch io event {}", sk);
		Runnable r = (Runnable) (sk.attachment());
		if (r != null) {
			r.run();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Reactor(8080).run();
	}
}