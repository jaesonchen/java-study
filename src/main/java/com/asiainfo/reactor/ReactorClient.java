package com.asiainfo.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月21日  上午11:35:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReactorClient {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			InetSocketAddress addr = new InetSocketAddress("localhost", 8080);
			SocketChannel sc = SocketChannel.open();
			sc.connect(addr);
			if (sc.finishConnect()) {
				String request = "query";
				ByteBuffer buff = ByteBuffer.wrap(request.getBytes("utf-8"));
				sc.write(buff);
				sc.shutdownOutput();
				
				ByteBuffer readBuff = ByteBuffer.allocate(1024);
				sc.read(readBuff);
				readBuff.flip();
				System.out.println("INFO >>> " + new String(readBuff.array(), "utf-8"));
			} else {
				System.out.println("connect failed.");
			}
			sc.close();
		} catch(IOException ex) {}
	}
}
