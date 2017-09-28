package com.asiainfo.fileservice.ftp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月13日  下午5:06:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FileUtilExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		File file = new File("D:\\logs\\MCD_GROUP_KHQ99900000271_20170912.txt");
        InputStream in= new java.io.FileInputStream(file);
        byte[] b = new byte[3];
        in.read(b);
        in.close();
        
        String encode = "";
        if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
        	encode = "UTF-8";
            System.out.println(file.getName() + "：编码为UTF-8");
        } else {
        	encode = "GBK";
            System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
        }
		
		System.out.println(System.getProperty("file.encoding")); 
		LineIterator it = FileUtils.lineIterator(new File("D:\\logs\\MCD_GROUP_KHQ99900000271_20170912.txt"), encode);
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
