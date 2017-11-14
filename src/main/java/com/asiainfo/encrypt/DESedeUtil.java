package com.asiainfo.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:58:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DESedeUtil {

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
	    
		//密钥生成器
		KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
		//初始化密钥生成器，可指定密钥长度为112或168，默认168
		keyGen.init(168);   
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
	public static byte[] encryptDESede(byte[] data, byte[] key) throws Exception {
	    
		//恢复密钥
		SecretKey secretKey = new SecretKeySpec(key, "DESede");
		//Cipher加密
		Cipher cipher = Cipher.getInstance("DESede");
		//cipher初始化
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
	public static byte[] decryptDESede(byte[] data, byte[] key) throws Exception {
	    
		//恢复密钥
		SecretKey secretKey = new SecretKeySpec(key, "DESede");
		//Cipher解密
		Cipher cipher = Cipher.getInstance("DESede");
		//初始化cipher
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		//解密
		return cipher.doFinal(data);
	}
}
