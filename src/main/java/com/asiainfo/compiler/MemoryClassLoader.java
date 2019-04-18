package com.asiainfo.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: Load class from byte[] which is compiled in memory.
 * 
 * @author       zq
 * @date         2017年9月15日  下午5:16:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class MemoryClassLoader extends URLClassLoader {

	// class name to class bytes:
	Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

	public MemoryClassLoader(Map<String, byte[]> classBytes) {
		super(new URL[0], MemoryClassLoader.class.getClassLoader());
		this.classBytes.putAll(classBytes);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] buf = classBytes.get(name);
		if (buf == null) {
			return super.findClass(name);
		}
		classBytes.remove(name);
		return defineClass(name, buf, 0, buf.length);
	}
}
