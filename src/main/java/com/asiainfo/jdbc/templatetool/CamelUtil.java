package com.asiainfo.jdbc.templatetool;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 下划线、驼峰 转换
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:12:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CamelUtil {

    /**
     * @Description: 驼峰转下划线
     * @author chenzq
     * @date 2019年3月27日 下午4:57:43
     * @param camelName
     * @return
     */
	public static String camel2underscore(String camelName) {
		
		if (StringUtils.isEmpty(camelName)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		//类名的第一个字母是大写
		sb.append(Character.toLowerCase(camelName.charAt(0)));
		for (int i = 1; i < camelName.length(); i++) {
		    char ch = camelName.charAt(i);
		    if (Character.isUpperCase(ch)) {
		        sb.append("_");
		    }
		    sb.append(Character.toLowerCase(ch));
		}
		return sb.toString();
	}
	
	/**
	 * @Description: 下划线转驼峰
	 * @author chenzq
	 * @date 2019年3月27日 下午4:57:52
	 * @param underscoreName
	 * @return
	 */
	public static String underscore2camel(String underscoreName) {
		
	    if (StringUtils.isEmpty(underscoreName)) {
	        return "";
	    }
	    StringBuilder sb = new StringBuilder();
		String[] sections = underscoreName.toLowerCase().split("_");
		for (int i = 0; i < sections.length; i++) {
			String s = sections[i];
			if (i == 0) {
				sb.append(s);
			} else {
				sb.append(capitalize(s));
			}
		}
		return sb.toString();
	}

	// 字符串第一个字母转大写
	private static String capitalize(String str) {
		
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return new StringBuilder()
            .append(Character.toUpperCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}
