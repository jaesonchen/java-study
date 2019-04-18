package com.asiainfo.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Description: 自定义ClassLoader，用于加载不在classpath中的class
 * 
 * @author       zq
 * @date         2017年9月16日  下午8:41:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class MyClassLoader extends ClassLoader {

	final String CLASS_NAME = "com.asiainfo.classloader.MyObject";
	public MyClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {

		if (!CLASS_NAME.equals(name)) {
			return super.loadClass(name);
		}
		try {
		    //为方便测试，直接读取classes下的项目
		    String url = this.getClass().getClassLoader().getResource("").toString() + name.replace(".", "/") + ".class";
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data;
			while ((data = input.read()) != -1) {
				buffer.write(data);
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
	
	public static void main(String[] args) throws Exception {
	    
	    MyClassLoader loader = new MyClassLoader(MyClassLoader.class.getClassLoader());
	    Class<?> clazz = loader.loadClass("com.asiainfo.classloader.MyObject");
	    Method method = clazz.getMethod("sayHello", new Class<?>[] {String.class});
	    method.invoke(clazz.newInstance(), "jaeson");
	}
}
