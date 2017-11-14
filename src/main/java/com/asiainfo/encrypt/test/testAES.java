package com.asiainfo.encrypt.test;

import com.asiainfo.encrypt.AESUtil;
import com.asiainfo.encrypt.BytesToHex;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午11:06:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testAES {

	//待加密的原文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
		//获得密钥
		byte[] aesKey = AESUtil.initKey();
		System.out.println("AES 密钥 : " + BytesToHex.bytesToHex(aesKey));
		//加密
		byte[] encrypt = AESUtil.encryptAES(DATA.getBytes(), aesKey);
		System.out.println("AES 加密 : " + BytesToHex.bytesToHex(encrypt));
		
		//解密
		byte[] plain = AESUtil.decryptAES(encrypt, aesKey);
		System.out.println("AES 解密 : " + new String(plain));
	}
}
