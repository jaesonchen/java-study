package com.asiainfo.jdbc.util;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月3日  上午11:48:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ConvertUtil {
	
	public static String list2String(List<String> list, String seperator) {
		
		if (null == list || list.isEmpty()) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			result.append(list.get(i));
			if (i < (list.size() - 1)) {
				result.append(seperator);
			}
		}
		return result.toString();
	}
	
	public static String array2String(String[] arr, String seperator) {
		
		if (null == arr || arr.length == 0) {
			return null;
		}
		return list2String(Arrays.asList(arr), seperator);
	}
	
	public static String clob2String(java.sql.Clob clob) {
		
		if (null == clob) {
			return null;
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(clob.getCharacterStream());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		String str = null;
		StringBuilder sb = new StringBuilder();
		try {
			while (null != (str = br.readLine())) {
				sb.append(str);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}
	
	public static Date datestring2Date(String datestr, String format) {
		
		if (StringUtils.isEmpty(datestr) || StringUtils.isEmpty(format)) {
			return null;
		}

		try {
			return new SimpleDateFormat(format).parse(datestr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static java.sql.Timestamp datestring2SqlTimestamp(String datestr, String format) {
		
		Date date = datestring2Date(datestr, format);
		return null == date ? null : new java.sql.Timestamp(date.getTime());
	}
	
	public static String removeDuplicate(String origin) {
		
		if (StringUtils.isEmpty(origin)) {
			return origin;
		}
		List<String> result = new ArrayList<String>();
		String[] ids = origin.split(",");
		for (String id : ids) {
			if (!result.contains(id)) {
				result.add(id);
			}
		}
		return list2String(result, ",");
	}
}
