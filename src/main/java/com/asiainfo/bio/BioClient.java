package com.asiainfo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月13日  下午1:58:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BioClient {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int port = 8080;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("localhost", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("query");
			out.flush();
			//shutdown output stream trigger eof
			socket.shutdownOutput();
			String response = in.readLine();
			System.out.println(response);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
