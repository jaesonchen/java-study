package com.asiainfo.encrypt.test;

import com.asiainfo.encrypt.BytesToHex;
import com.asiainfo.encrypt.DESUtil;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:57:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testDES {

	//待加密原文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
	    
		byte[] desKey = DESUtil.initKey();
		System.out.println("DES Key : " + BytesToHex.bytesToHex(desKey));
		byte[] desReult = DESUtil.encryptDES(DATA.getBytes(), desKey);
		System.out.println("DES 加密 =====>>>>>>> " + BytesToHex.bytesToHex(desReult));
		byte[] plain = DESUtil.decryptDES(desReult, desKey);
		System.out.println("DES 解密 =====>>>>>>> " + new String(plain));
	}
}
