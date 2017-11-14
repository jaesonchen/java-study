package com.asiainfo.encrypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 
 * TODO
 * 
 * @author       zq
 * @date         2017年11月14日  上午11:06:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RSAUtil {

	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	/**
	 * 生成RSA的公钥和私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
	    
	    Map<String, Object> keyMap = new HashMap<String, Object>();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		//512-65536 & 64的倍数
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	/**
	 * 获得公钥
	 * 
	 * @param keyMap
	 * @return
	 */
	public static RSAPublicKey getPublicKey(Map<String, Object> keyMap) {
		return (RSAPublicKey) keyMap.get(PUBLIC_KEY);
	}
	
	/**
	 * 获得私钥
	 * 
	 * @param keyMap
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(Map<String, Object> keyMap) {
		return (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, RSAPublicKey publicKey) throws Exception {
	    
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey) throws Exception {
	    
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
}
