package com.asiainfo.encrypt.test;

import com.asiainfo.encrypt.BytesToHex;
import com.asiainfo.encrypt.DESedeUtil;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午11:03:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testDESede {

	//待加密原文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
		//获取密钥
		byte[] key = DESedeUtil.initKey();
		System.out.println("DESede 密钥 : " + BytesToHex.bytesToHex(key));
		//加密
		byte[] encrypt = DESedeUtil.encryptDESede(DATA.getBytes(), key);
		System.out.println("DESede 加密 : " + BytesToHex.bytesToHex(encrypt));
		byte[] plain = DESedeUtil.decryptDESede(encrypt, key);
		System.out.println("DESede 解密: " + new String(plain));
	}
}
