package com.asiainfo.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午11:06:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcClient {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	Serializer<Object> serializer = new JdkSerializer();
	
	/**
	 * 
	 * @Description: 生成服务代理
	 * 
	 * @param interfaceClass
	 * @param host
	 * @param port
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
		
		if (interfaceClass == null) {
			throw new IllegalArgumentException("Interface class is null");
		}
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class");
		}
		if (StringUtils.isEmpty(host)) {
			throw new IllegalArgumentException("Host is null");
		}
		if (port <= 0 || port > 65535) {
			throw new IllegalArgumentException("Invalid port " + port);
		}

		logger.info("Get remote service {} from server {}:{}", interfaceClass.getName(), host, port);
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), 
				new Class<?>[] {interfaceClass},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						InetSocketAddress addr = new InetSocketAddress(host, port);
						SocketChannel channel = SocketChannel.open();
						channel.connect(addr);
						try {
							if (channel.finishConnect()) {
								RpcRequest request = new RpcRequest();
								request.setInterfaceName(interfaceClass.getName());
								request.setMethodName(method.getName());
								request.setParameterTypes(method.getParameterTypes());
								request.setParameterValues(args);
								ByteBuffer buffer = ByteBuffer.wrap(RpcClient.this.serializer.serialize(request));
								while (buffer.hasRemaining()) {
									channel.write(buffer);
								}
								channel.socket().shutdownOutput();//eof
					            return RpcClient.this.receiveResponse(channel);
							}
						} catch(IOException ex) {
							RpcClient.this.logger.error("error on request remote service!", ex);
						} finally {
							if(channel != null) {
								channel.close();
							}
						}
						return null;
					}
				});
	}	
	
	/**
	 * 
	 * @Description: 接收请求响应
	 * 
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	protected Object receiveResponse(SocketChannel sc) throws Exception {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            byte[] bytes;
            int count = 0;
            while ((count = sc.read(buffer)) != -1) {
                buffer.flip();
                bytes = new byte[count];
                buffer.get(bytes);
                byteStream.write(bytes);
                buffer.clear();
            }
            sc.socket().shutdownInput();
            return this.serializer.deserialize(byteStream.toByteArray());
        } finally {
            try {
            	byteStream.close();
            } catch(Exception ex) {}
        }
	}
}
