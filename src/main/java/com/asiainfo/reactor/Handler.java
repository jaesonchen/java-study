package com.asiainfo.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月11日  下午4:40:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Handler implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final SocketChannel channel;
	final SelectionKey key;
	State state;
	
	public Handler(Selector selector, SocketChannel channel) throws IOException {
		
		this.state = State.READING;
		this.channel = channel;
		this.channel.configureBlocking(false);
		this.key = this.channel.register(selector, this.state.getOp());
		this.key.attach(this);
		//wakeup用于唤醒阻塞在select方法上的线程，它的实现很简单，在linux上就是创建一 个管道并加入poll的fd集合，
		//wakeup就是往管道里写一个字节，那么阻塞的poll方法有数据可读就立即返回。
		selector.wakeup();
		logger.info("{} connect success...", selector);
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		try {
			switch (state) {
				case READING:
					read();
					break;
				case WRITING:
					write();
					break;
				default:
					logger.error("Unsupported State: " + state + " ! overlap processing with IO...");
			}
		} catch (IOException ex) {
			logger.error("error on handle {}", state);
		}
	}

	public void read() throws IOException {
		
		ByteBuffer input = ByteBuffer.allocate(1024);
		this.channel.read(input);
		input.flip();
		byte[] bt = new byte[input.remaining()];
		logger.info("message length={}", input.remaining());
		input.get(bt);
		logger.info("handle read message={}...", new String(bt, "utf-8"));
		interestOps(State.WRITING);
	}
	
	public void write() throws IOException {

		ByteBuffer output = ByteBuffer.wrap("hello from server side ...".getBytes("utf-8"));
		while (output.hasRemaining()) {
			this.channel.write(output);
		}
		logger.info("handle write ...");
		this.key.cancel();
		this.disconnect();
	}
	
	protected void interestOps(State state) {
		
		logger.info("interest mode {}", state.getOp());
		this.state = state;
		this.key.interestOps(state.getOp());
	}
	
	protected void disconnect() {
		
		try {
			this.channel.close();
		} catch (IOException ex) {
			logger.error("error on close channel", ex);
		}
		logger.info("client Address=【{}】 had already closed!!! ", this.channel.socket().getRemoteSocketAddress());
	}

}
enum State{

	READING(SelectionKey.OP_READ),
	WRITING(SelectionKey.OP_WRITE);
		
	private final int op;
	private State(int operate){
		this.op = operate;
	}
	public int getOp() {
		return op;
	}
}