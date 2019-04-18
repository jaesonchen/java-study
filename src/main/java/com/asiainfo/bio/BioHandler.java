package com.asiainfo.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月6日  上午10:47:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BioHandler implements Runnable {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	final Socket socket;

    public BioHandler(Socket socket) {
        this.socket = socket;
    }
    
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		logger.info("classic multi-thread bio hanlder ...");
        try {
        	//read block until input data is available, end of file is detected, or an exception is thrown
            byte[] output = process(read());
            //write
            write(output);
            //close socket
            this.socket.close();
        } catch (Exception ex) {
        	logger.error("error on read/write socket!", ex);
        } finally {
        	if (null != this.socket && !this.socket.isClosed()) {
        		try {
					this.socket.close();
				} catch (IOException ex) {
					logger.error("error on close socket!", ex);
				}
        	}
        }
	}
	
	//read request
	protected byte[] read() throws IOException {
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		InputStream in = this.socket.getInputStream();
		byte[] buff = new byte[1024];
		int count = 0;
		while ((count = in.read(buff)) != -1) {
			result.write(buff, 0, count);
		}
		return result.toByteArray();
	}
	
	//process incoming message
	protected byte[] process(final byte[] input) throws UnsupportedEncodingException {
		
		logger.info("processing incoming message:{} ...", new String(input, "utf-8"));
		StringBuilder response = new StringBuilder("response message: ").append(System.currentTimeMillis());
		return response.toString().getBytes("utf-8");
	}
	
	//write response after process
	protected void write(final byte[] output) throws IOException {
		
		OutputStream out = this.socket.getOutputStream();
		out.write(output);
		out.flush();
	}
}
