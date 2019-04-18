package com.asiainfo.insidejvm;

import java.io.*;
import java.util.Properties;

/**
 * 
 * Java字节流与字符流: 
 *  InputStream和OutputStream提供了面向byte的I/O功能.
 *  Reader和Writer则提供了Unicode兼容的面向字符的I/O功能。
 *  
 *  常用转换：
 *  FileReader——>BufferedReader
 *      BufferedReader in = new BufferedReader(new FileReader("text.txt"));
 *      
 *  InputStream——>InputStreamReader——>BufferedReader
 *      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 *  
 *  FileWriter——>BufferedWriter——>PrintWriter
 *      PrintWriter pw = new PrintWriter(new BufferedWriter("text.out"));
 *      
 *  System.out(PrintStream)——>PrintWriter
 *      PrintWriter pw = new PrintWriter(System.out, true);
 *      
 *  FileOutputStream——>BufferedOutputStream——>PrintStream
 *      PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("text.out")));
 *      
 *  String——>byte[]——>ByteArrayInputStream——>DataInputStream
 *      DataInputStream in = new DataInputStream(new ByteArrayInputStream(str.getBytes()));
 *      
 *  FileInputStream——>BufferedInputStream——>DataInputStream
 *      DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("Data.txt")));
 *      
 *  FileOutputStream——>BufferedOutputStream——>DataOutputStream
 *      DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Data.txt")));
 *      
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:57:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IOStreamExample {

	public static void main(String[] args) {
		
		BufferedReader in = null;
		BufferedWriter out = null;
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		FileInputStream fis = null;
		try {
			//写入字符文件
			out = new BufferedWriter(new FileWriter("IOStreamExample.out"));
			String str = "IOStreamExample.out 中的内容。";
			out.write(str);
			out.newLine();
			out.write("换行内容。");
			out.flush();
			out.close();
			
			//读取字符文件
			in = new BufferedReader(new FileReader("IOStreamExample.out"));
			StringBuffer sb = new StringBuffer();
			while ((str = in.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
			in.close();
			System.out.print(sb.toString());
			
			//复制byte流文件
			bin = new BufferedInputStream(new FileInputStream("IOStream.jpg"));
			bout = new BufferedOutputStream(new FileOutputStream("IOStreamOut.jpg"));
			int i = 0;
			byte[] buf = new byte[1024];
			while ((i = bin.read(buf)) != -1) {
				bout.write(buf, 0, i);
			}
			bout.flush();
			bin.close();
			bout.close();	
			
			//properties 读取配置文件
			Properties prop = new Properties();
			fis = new FileInputStream("info.properties");
			prop.load(fis);
			fis.close();

			//键盘录入的最常见写法。
			//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
		    close(bin, bout, in, out, fis);
		}
	}
	
	// 流关闭工具方法
	protected static void close(Closeable... closeables) {
	    if (closeables != null) {
	        for (Closeable closeable : closeables) {
    	        try {
    	            closeable.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
	        }
	    }
	}
}
