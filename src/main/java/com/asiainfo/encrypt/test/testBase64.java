package com.asiainfo.encrypt.test;

import com.asiainfo.encrypt.Base64Util;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:39:56
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testBase64 {

	//待加密明文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
	    
		String base64Result = Base64Util.base64Encrypt(DATA.getBytes("utf-8"));
		System.out.println("base64加密===========>>>>>>> " + base64Result);
		
		String base64Plain = Base64Util.base64Decrypt(base64Result, "utf-8");
		System.out.println("base64解密===========>>>>>>> " + base64Plain);
	}
}
