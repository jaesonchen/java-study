package com.asiainfo.fileservice;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月18日  下午12:55:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LineSeperator {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (System.getProperty("line.separator").equals("\r\n")) {
		    System.out.println("\\r\\n is for windows");
		} else if (System.getProperty("line.separator").equals("\r")) {
		    System.out.println("\\r is for Mac");
		} else if (System.getProperty("line.separator").equals("\n")) {
		    System.out.println("\\n is for Unix/Linux");
		}
	}
}
