package com.asiainfo.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES算法
 * DES采用了64位的分组长度和56位的密钥长度，是对称加密的一种。
 * 原理：该算法是一个利用56+8奇偶校验位（第8,16,24,32,40,48,56,64）=64位的密钥对以64位为单位的块数据进行加解密。
 * 
 * 对称加解密算法：通信双方同时掌握一个密钥，加密解密都是由一个密钥完成的。
 * 
 * 公私钥加解密算法：通信双方掌握不同的密钥，不同方向的加解密由不同的密钥完成。
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
