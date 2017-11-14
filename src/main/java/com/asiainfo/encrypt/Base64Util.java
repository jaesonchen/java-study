package com.asiainfo.encrypt;

import java.util.Base64;

/**
 * java8 BASE64算法实现加解密
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:37:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Base64Util {

	/**
	 * base64算法加密
	 * 
	 * @param data
	 * @return
	 */
	public static String base64Encrypt(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	/**
	 * 
	 * url加密
	 * 
	 * @param data
	 * @return
	 */
    public static String base64UrlEncrypt(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data);
    }
    
    /**
     * mime加密
     * 
     * @param data
     * @return
     */
    public static String base64MimeEncrypt(byte[] data) {
        return Base64.getMimeEncoder().encodeToString(data);
    }

	/**
	 * base64算法解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] base64Decrypt(String data) throws Exception {
		return Base64.getDecoder().decode(data);
	}

    /**
     * url解密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] base64UrlDecrypt(String data) throws Exception {
        return Base64.getUrlDecoder().decode(data);
    }
    
    /**
     * mime解密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] base64MimeDecrypt(String data) throws Exception {
        return Base64.getMimeDecoder().decode(data);
    }
    
	/**
	 * base64算法解密
	 * 
	 * @param data
	 * @param encode
	 * @return
	 * @throws Exception
	 */
    public static String base64Decrypt(String data, String encode) throws Exception {
        return new String(base64Decrypt(data), encode);
    }
    
    /**
     * url解密
     * 
     * @param data
     * @param encode
     * @return
     * @throws Exception
     */
    public static String base64UrlDecrypt(String data, String encode) throws Exception {
        return new String(base64UrlDecrypt(data), encode);
    }
    
    /**
     * mime解密
     * 
     * @param data
     * @param encode
     * @return
     * @throws Exception
     */
    public static String base64MimeDecrypt(String data, String encode) throws Exception {
        return new String(base64MimeDecrypt(data), encode);
    }
}
