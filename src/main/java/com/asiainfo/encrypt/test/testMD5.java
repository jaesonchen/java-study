package com.asiainfo.encrypt.test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.asiainfo.encrypt.MD5Util;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年1月15日  下午2:24:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class testMD5 {

    /** 
     * TODO
     * 
     * @param args
     * @throws UnsupportedEncodingException 
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println(MD5Util.encryptMD5Hex("bj8618701595221".getBytes("utf-8")));
    }
}
