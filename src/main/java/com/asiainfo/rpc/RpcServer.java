package com.asiainfo.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午10:11:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcServer implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final int DEAULF_PORT = 20880;
	private int port;
	private Selector selector;
	private ServerSocketChannel ssChannel;

	public RpcServer() throws IOException {
		this(DEAULF_PORT);
	}
	public RpcServer(int port) throws IOException {
		this.port = port;
		this.selector = Selector.open();
		this.ssChannel = ServerSocketChannel.open();
		this.ssChannel.configureBlocking(false);
		this.ssChannel.socket().setReuseAddress(true);
		this.ssChannel.socket().bind(new InetSocketAddress(port));
		SelectionKey sk = this.ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new RpcAcceptor(this.selector, this.ssChannel));
	}
	
	/* 
	 * @Description: 主体运行逻辑
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		logger.info("---->> 监听服务启动, 端口号: {}", this.port);
		try {
			while (!Thread.interrupted()) {
				this.selector.select();
				Set<SelectionKey> selected = this.selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext()) {
					this.dispatch((SelectionKey) (it.next()));
				}
				selected.clear();
			}
		} catch (IOException ex) {
			logger.error("error on dispatch io event", ex);
		}
	}
	
	/**
	 * @Description: TODO
	 * 
	 * @param sk
	 */
	protected void dispatch(SelectionKey sk) {
		
		logger.info("分发io请求： {}", sk);
		Runnable r = (Runnable) (sk.attachment());
		if (r != null) {
			r.run();
		}
	}
}
