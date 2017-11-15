package com.asiainfo.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月15日  下午3:39:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class MD5Util {

    /**
     * md5加密
     * 
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptMD5(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("md5").digest(data);
    }
    
    /**
     * md5加密
     * 
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptMD5Hex(byte[] data) throws NoSuchAlgorithmException {
        return BytesToHex.bytesToHex(encryptMD5(data));
    }
}
