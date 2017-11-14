package com.asiainfo.encrypt;

/**
 * 字节码转化十六进制
 * 
 * @author       zq
 * @date         2017年11月14日  上午10:40:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class BytesToHex {

    /**
     * byte转16进制字符
     * 
     * @param bt
     * @return
     */
	public static String bytesToHex(byte[] bt) {
	    
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bt.length; i++) {
		    String hex = Integer.toHexString(0xFF & bt[i]);
			if (hex.length() == 1) {
				builder.append("0");
			}
			builder.append(hex);
		}
		return builder.toString();
	}
	
	/**
	 * 16进制字符转byte
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexToBytes(String str) {
	    
	    String hexStr = str.toUpperCase();
	    char[] hexChars = hexStr.toCharArray();
	    int length = hexStr.length() / 2;
	    byte[] d = new byte[length];
	    for (int i = 0; i < length; i++) {
	        int pos = i * 2;
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
	    }
	    return d;
	}
	
	/**
	 * 字符转byte
	 * 
	 * @param c
	 * @return
	 */
	public static byte charToByte(char c) {
	    return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
