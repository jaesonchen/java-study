package com.asiainfo.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法实现
 * 
 * @author       zq
 * @date         2017年11月14日  上午11:03:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AESUtil {

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
	    
		//密钥生成器
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		//初始化密钥生成器，默认128，获得无政策权限后可用192或256
		keyGen.init(128);
		//生成密钥
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptAES(byte[] data, byte[] key) throws Exception {
	    
		//恢复密钥
		SecretKey secretKey = new SecretKeySpec(key, "AES");
		//Cipher加密
		Cipher cipher = Cipher.getInstance("AES");
		//根据密钥对cipher进行初始化
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		//加密
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptAES(byte[] data, byte[] key) throws Exception {
	    
		//恢复密钥生成器
		SecretKey secretKey = new SecretKeySpec(key, "AES");
		//Cipher解密
		Cipher cipher = Cipher.getInstance("AES");
		//根据密钥对cipher进行初始化
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		//解密
		return cipher.doFinal(data);
	}
}
