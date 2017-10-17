package com.asiainfo.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月16日  下午8:41:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class MyClassLoader extends ClassLoader {

	final String CLASS_NAME = "reflection.MyObject";
	public MyClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {

		if (!CLASS_NAME.equals(name)) {
			return super.loadClass(name);
		}
		try {
			String url = "file:C:/data/projects/tutorials/web/WEB-INF/classes/reflection/MyObject.class";
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			return defineClass(CLASS_NAME, classData, 0, classData.length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
