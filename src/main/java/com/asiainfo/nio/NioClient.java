package com.asiainfo.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月11日  下午12:43:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class NioClient {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("localhost", 8080));
		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		final int max = 10;
		int loop = 0;
		while (true && loop < max) {
			selector.select();
			//Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				if (!key.isValid()) {
					continue;
				}
				if (key.isConnectable()) {
					// a connection was established with a remote server.
					SocketChannel channel = (SocketChannel) key.channel();
					if(channel.isConnectionPending()) {
						channel.finishConnect();
					}
					channel.configureBlocking(false);
					channel.write(ByteBuffer.wrap(new String("query").getBytes("utf-8")));
					//channel.shutdownOutput();//eof
					//channel.register(selector, SelectionKey.OP_WRITE);
					key.interestOps(SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					// a channel is ready for reading
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer buff = ByteBuffer.allocate(1024);
					ByteArrayOutputStream result = new ByteArrayOutputStream();
					int count = 0;
					while ((count = channel.read(buff)) > 0) {
			            buff.flip();
			            result.write(buff.array(), 0, count);
			            buff.clear();
					}

					System.out.println("response from server side：" + new String(result.toByteArray(), "utf-8"));
					++loop;
					Thread.sleep(1000);
					channel.write(ByteBuffer.wrap(new String("query").getBytes("utf-8")));
				}
			}
		}
		selector.close();
		socketChannel.close();
	}
}
