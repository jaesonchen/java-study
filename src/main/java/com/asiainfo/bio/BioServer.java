package com.asiainfo.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月6日  上午10:51:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BioServer implements Runnable {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private int port = 8080;
	
	public BioServer() {}
	public BioServer(int port) {
		this.port = port;
	}

	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(this.port);
			while (!Thread.interrupted()) {
				Socket socket = ss.accept();
				logger.info("connection established, remote address={} ...", socket.getRemoteSocketAddress());
				new Thread(new BioHandler(socket)).start();
			}
        } catch (IOException ex) {
        	logger.error("error on accept connection!", ex);
        } finally {
        	if (null != ss && !ss.isClosed()) {
        		try {
					ss.close();
				} catch (IOException ex) {
					logger.error("error on close socket!", ex);
				}
        	}
        }
	}

	/**
	 * @Description: TODO
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        new Thread(new BioServer(8080)).start();
    }
}
