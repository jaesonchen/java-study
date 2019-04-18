package com.asiainfo.encrypt.test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import com.asiainfo.encrypt.BytesToHex;
import com.asiainfo.encrypt.RSAUtil;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午11:09:38
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testRSA {

	//待加密原文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
	    
		Map<String, Object> keyMap = RSAUtil.initKey();
		RSAPublicKey rsaPublicKey = RSAUtil.getPublicKey(keyMap);
		RSAPrivateKey rsaPrivateKey = RSAUtil.getPrivateKey(keyMap);
		System.out.println("RSA PublicKey: " + rsaPublicKey);
		System.out.println("RSA PrivateKey: " + rsaPrivateKey);
		
		byte[] rsaResult = RSAUtil.encrypt(DATA.getBytes(), rsaPublicKey);
		System.out.println("RSA 加密>>>>====" + BytesToHex.bytesToHex(rsaResult));
		
		byte[] plainResult = RSAUtil.decrypt(rsaResult, rsaPrivateKey);
		System.out.println("RSA 解密>>>>====" + new String(plainResult));
		System.out.println("RSA 解密>>>>====" + BytesToHex.bytesToHex(plainResult));
	}
}
