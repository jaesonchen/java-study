package com.asiainfo.insidejvm;

import java.io.*;

/**
 * 
 * Java字节流与字符流的区别:字节流主要用于处理二进制文件，不能直接处理Unicode字符。
 * 文本文件是指用ASCII、Unicode、UTF-8编码格式来存贮的。
 * 二进制文件就是用01二进制编码来存贮文件。可执行文件，图象文件，word,excel,ppt这些"带格式"的文档都应该以二进制对象存储。
 * InputStream和OutputStream提供了很多面向byte的I/O功能.
 * Reader和Writer则提供了Unicode兼容的面向字符的I/O功能。
 	常用转换：
	
	FileReader——>BufferedReader
	
		BufferedReader in = new BufferedReader(new FileReader("text.txt"));
	
	InputStream——>InputStreamReader——>BufferedReader
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	FileWriter——>BufferedWriter——>PrintWriter
	
		PrintWriter pw = new PrintWriter(new BufferedWriter("text.out"));
	
	System.out(PrintStream)——>PrintWriter
	
		PrintWriter pw = new PrintWriter(System.out,true);
	
	FileOutputStream——>BufferedOutputStream——>PrintStream
	
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("text.out")));

	String——>byte[]——>ByteArrayInputStream——>DataInputStream
	
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(str.getBytes()));
	
	FileInputStream——>BufferedInputStream——>DataInputStream
	
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("Data.txt")));
	
	
	FileOutputStream——>BufferedOutputStream——>DataOutputStream
	
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Data.txt"))); 
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
		try {
			
			//写入字符文件
			out = new BufferedWriter(new FileWriter("IOStreamExample.out"));
			String str = "IOStreamExample.out 中的内容。\n换行内容。";
			out.write(str);
			out.newLine();
			out.write("不换行写入");
			out.flush();
			out.close();
			
			//读取字符文件
			in = new BufferedReader(new FileReader("IOStreamExample.out"));
			str = null;
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
			int by = 0;
			byte[] buf = new byte[1024];
			while ((by = bin.read(buf)) != -1){
				bout.write(buf, 0, by);
			}
			bout.flush();
			bin.close();
			bout.close();	
			
			//properties 读取配置文件
			//Properties prop = new Properties();
			//FileInputStream fis = new FileInputStream("info.txt");
			//prop.load(fis);  //将流中的数据加载进集合。
			
			//append 文件续写
			//FileWriter(String fileName, boolean append) 
			//FileOutputStream(String name, boolean append)

			//键盘录入的最常见写法。
			//BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
			//BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(System.out));
			
			//编码：字符串变成字节数组：String-->byte[];  str.getBytes(charsetName);
			//解码：字节数组变成字符串：byte[] -->String: new String(byte[],charsetName);
			
		} catch(Exception ex) {
			System.out.println(ex);
		} finally {
			
			try {
				
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("读取关闭失败");
			}
			
			try {
				
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("写入关闭失败");
			}
		
			try {
				
				if(bin != null) {
					bin.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("读取关闭失败");
			}
			
			try {
				
				if(bout != null) {
					bout.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("写入关闭失败");
			}	
			
		}	
			
	}
}
