package com.asiainfo.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES算法实现
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:53:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DESUtil {

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
	    
		//密钥生成器
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		//初始化密钥生成器
		keyGen.init(56);
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
	public static byte[] encryptDES(byte[] data, byte[] key) throws Exception {
	    
		//获得密钥
		SecretKey secretKey = new SecretKeySpec(key, "DES");
		//Cipher加密
		Cipher cipher = Cipher.getInstance("DES");
		//初始化cipher
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
	public static byte[] decryptDES(byte[] data, byte[] key) throws Exception {
	    
		//恢复密钥
		SecretKey secretKey = new SecretKeySpec(key, "DES");
		//Cipher解密
		Cipher cipher = Cipher.getInstance("DES");
		//初始化cipher
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		//解密
		return cipher.doFinal(data);
	}
}
