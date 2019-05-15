package com.asiainfo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties 属性文件读取工具类
 * 
 * @author chenzq  
 * @date 2019年5月13日 下午3:46:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class PropertiesUtil {
	
    static final String PROPERTIES_NAME = "config.properties";
    private Properties properties = new Properties();
    
    static class PropertyReaderUtilHolder {
    	static PropertiesUtil INSTANCE = new PropertiesUtil();
    }
    
    public static PropertiesUtil getInstance() {
    	return PropertyReaderUtilHolder.INSTANCE;
    }
    
    private PropertiesUtil() {
    	this(PROPERTIES_NAME);
    }
    
    private PropertiesUtil(String fileName) {
    	
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * key的属性值
     *
     * @param key
     * @return String
     */
    public String getProperty(String key) {
    	return properties.getProperty(key);
    }
    
    /**
     * key的属性值，如果不存在则返回给定的缺省值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        return null == properties.getProperty(key) ? defaultValue : properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }
}
