package com.asiainfo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月23日  下午1:50:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class PropertyReaderUtil {
	
    static final String PROPERTIES_NAME = "config.properties";
    private Properties properties = new Properties();
    
    static class PropertyReaderUtilHolder {
    	static PropertyReaderUtil INSTANCE = new PropertyReaderUtil();
    }
    
    public static PropertyReaderUtil getInstance() {
    	return PropertyReaderUtilHolder.INSTANCE;
    }
    
    private PropertyReaderUtil() {
    	this(PROPERTIES_NAME);
    }
    
    private PropertyReaderUtil(String fileName) {
    	
        InputStream is = null;
        try {
            is = PropertyReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
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
     * 通过key获取配置文件的value
     *
     * @param key
     * @return String
     */
    public String getProperty(String key) {
    	return properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }
}
