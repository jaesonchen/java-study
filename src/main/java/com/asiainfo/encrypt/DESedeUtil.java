package com.asiainfo.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * DESede：针对des算法进行了改进，有了三重des算法（DESede）。针对des算法的密钥长度较短以及迭代次数偏少问题做了相应改进，提高了安全强度。
 *             不过desede算法处理速度较慢，密钥计算时间较长，加密效率不高问题使得对称加密算法的发展不容乐观。
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
