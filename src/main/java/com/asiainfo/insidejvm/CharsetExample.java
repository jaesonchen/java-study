package com.asiainfo.insidejvm;

public class CharsetExample {

	public static void main(String[] args) {
		
		try {
			String str = "i am a 中国人";
			//unicode字符集中使用双字节编码
			System.out.println(str.length());	//10
			//特定字符集的编码
			System.out.println(str.getBytes("gb18030").length);	//13
			System.out.println(toHexString(str.getBytes("gb18030")));
			System.out.println(str.getBytes("gb2312").length);	//13
			System.out.println(toHexString(str.getBytes("gb2312")));
			
			System.out.println(java.nio.charset.Charset.defaultCharset());	//utf-8
			System.out.println(str.getBytes().length);			//16
			
			System.out.println(str.getBytes("utf-8").length);	//16
			System.out.println(toHexString(str.getBytes("utf-8")));
			System.out.println(str.getBytes("utf-16").length);	//22
			//0xFEFF表示高位在前的utf-16格式，0xFFFE表示低位在前的utf-16格式
			System.out.println(toHexString(str.getBytes("utf-16")));
			
			System.out.println(str.getBytes("ISO-8859-1").length);	//10
			System.out.println(toHexString(str.getBytes("ISO-8859-1")));
			
			System.out.println("系统默认编码为：" + System.getProperty("file.encoding"));
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	static String toHexString(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			String hex = Integer.toHexString(arr[i] & 0xFF);
			if (hex.length() == 1) {
                hex = "0" + hex;
            }
			sb.append(" ").append(hex);
		}
		return sb.toString();
	}
}
