package com.asiainfo.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
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
 * @date         2017年8月22日  上午10:43:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcHandler implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	SocketChannel channel;
	SelectionKey key;
	Serializer<Object> serializer = new JdkSerializer();
	
	public RpcHandler(Selector selector, SocketChannel channel) throws IOException {
		
		this.channel = channel;
		this.channel.configureBlocking(false);
		this.key = this.channel.register(selector, SelectionKey.OP_READ);
		this.key.attach(this);
		selector.wakeup();
		logger.info("{} 绑定请求处理器...", selector);
	}
	
	/* 
	 * @Description: 获取请求数据，调用相关服务接口并通过socket发送返回结果
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			RpcRequest request = this.receiveRequest(this.channel);
			logger.info("---->>接收到请求 : {}", request);
			Object target = BeanFactory.getBean(request.getInterfaceName());
			if (target != null) {
				Class<?> clazz = Class.forName(request.getInterfaceName());
				Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
				Object response = method.invoke(target, request.getParameterValues());
				this.sendResponse(this.channel, response);
			}
		} catch (Exception ex) {
			logger.error("error on handle request!", ex);
		} finally {
			try {
				if (this.channel != null) {
					this.channel.close();
				}
			} catch (Exception ex) {
				logger.error("error on close socketChannel!", ex);
			}
		}
	}

	/**
	 * @Description: 处理接收请求
	 * 
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
	protected RpcRequest receiveRequest(SocketChannel socketChannel) throws IOException {
		
		logger.info("接收请求参数: {}...", socketChannel);
		RpcRequest request = null;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			byte[] bytes;
			int size = 0;
			while ((size = socketChannel.read(buffer)) != -1) {
				buffer.flip();
				bytes = new byte[size];
				buffer.get(bytes);
				byteStream.write(bytes);
				buffer.clear();
			}
			request = (RpcRequest) this.serializer.deserialize(byteStream.toByteArray());
		} finally {
			try {
				byteStream.close();
			} catch (Exception ex) {
				logger.error("error on close ByteArrayOutputStream!", ex);
			}
		}
		return request;
	}
	
	/**
	 * @Description: 返回处理结果
	 * 
	 * @param socketChannel
	 * @param response
	 * @throws IOException
	 */
	protected void sendResponse(SocketChannel socketChannel, Object response) throws IOException {
		
		logger.info("返回处理结果: {} ...", response);
		byte[] bytes = this.serializer.serialize(response);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		while (buffer.hasRemaining()) {
			socketChannel.write(buffer);
		}
	}
}
