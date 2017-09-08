package com.asiainfo.jdbc.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年7月1日  下午12:12:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class CamelUtil {

	public static String camel2underscore(String camelName) {
		
		if (StringUtils.isEmpty(camelName)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(lowerCaseName(camelName.substring(0, 1)));
		for (int i = 1; i < camelName.length(); i++) {
			String s = camelName.substring(i, i + 1);
			String slc = lowerCaseName(s);
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}
	
	public static String underscore2camel(String underscoreName) {
		
		String[] sections = underscoreName.split("_");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sections.length; i++) {
			String s = sections[i];
			if (i == 0) {
				sb.append(s);
			} else {
				sb.append(capitalize(s));
			}
		}
		return sb.toString();
	}
	
	public static String lowerCaseName(String name) {
		return name.toLowerCase(Locale.US);
	}
	
	public static String capitalize(String str) {
		
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return new StringBuilder()
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}
