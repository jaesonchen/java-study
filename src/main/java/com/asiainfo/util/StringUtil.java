package com.asiainfo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月15日  下午4:54:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StringUtil {

    static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    
    /**
     * 是否为空
     * 
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return null == str || "".equals(str);
    }
    
    /**
     * 不为空
     * 
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * 去除空格
     * 
     * @param str
     * @return
     */
    public static String trim(String str) {
        return null == str ? null : str.trim();
    }
    
    /**
     * reverse
     * 
     * @param str
     * @return
     */
    public static String reverse(String str) {
        return isBlank(str) ? str : new StringBuilder(str).reverse().toString();
    }
    
    /**
     * lower case
     * 
     * @param str
     * @return
     */
    public static String toLowerCase(String str) {
        return isBlank(str) ? str : str.toLowerCase();
    }
    
    /**
     * upper case
     * 
     * @param str
     * @return
     */
    public static String toUpperCase(String str) {
        return isBlank(str) ? str : str.toUpperCase();
    }
    
    /**
     * 正则匹配
     * 
     * @param str
     * @param regex
     * @return
     */
    public static boolean find(String str, String regex) {
        
        if (null == str || null == regex) {
            return false;
        }
        try {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            return m.find();
        } catch (Exception ex) {
            logger.error("正则表达式错误，regex={}, error message:\n{}", regex, ex);
        }
        return false;
    }
}
