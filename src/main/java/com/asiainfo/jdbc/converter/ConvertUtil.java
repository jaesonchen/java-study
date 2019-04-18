package com.asiainfo.jdbc.converter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 转换工具类
 * 
 * @author       zq
 * @date         2017年6月3日  上午11:48:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ConvertUtil {
	
    /**
     * @Description: list转String
     * @author chenzq
     * @date 2019年3月27日 下午6:00:42
     * @param list
     * @param separator
     * @return
     */
	public static String list2String(List<String> list, String separator) {
		if (null == list || list.isEmpty()) {
			return "";
		}
		return StringUtils.join(list, separator);
	}
	
	/**
	 * @Description: 数组转字符串
	 * @author chenzq
	 * @date 2019年3月27日 下午6:03:25
	 * @param array
	 * @param seperator
	 * @return
	 */
	public static String array2String(String[] array, String seperator) {
		if (null == array || array.length == 0) {
			return "";
		}
		return StringUtils.join(array, seperator);
	}
	
    /**
     * @Description: 移除重复的字符串
     * @author chenzq
     * @date 2019年3月27日 下午6:00:10
     * @param origin
     * @return
     */
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
        return StringUtils.join(result, ",");
    }
	
    /**
     * @Description: 读取blob字段
     * @author chenzq
     * @date 2019年3月27日 下午6:21:30
     * @param blob
     * @return
     */
    public static byte[] blob2Byte(java.sql.Blob blob) {
        if (null == blob) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;
        byte[] result = null;
        try {
            byte[] buff = new byte[1024];
            in = blob.getBinaryStream();
            int i;
            while ((i = in.read(buff)) != -1) {
                out.write(buff, 0, i);
            }
            result = out.toByteArray();
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(in, out);
        }
        return result; 
        
    }
    
    /**
     * @Description: 读取Clob字段
     * @author chenzq
     * @date 2019年3月27日 下午6:09:44
     * @param clob
     * @return
     */
	public static String clob2String(java.sql.Clob clob) {
		if (null == clob) {
			return null;
		}
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
		    reader = new BufferedReader(clob.getCharacterStream());
			String str = null;
	        while (null != (str = reader.readLine())) {
                sb.append(str);
            }
	        reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		    close(reader);
		}
		return sb.toString();
	}
	
	// 流关闭方法
	private static void close(Closeable... closeables) {
	    for (Closeable closeable : closeables) {
    	    try {
    	        if (closeable != null) {
    	            closeable.close();
    	        }
    	    } catch (IOException ex) {
    	        // ignore
    	    }
	    }
	}
	
	/**
	 * @Description: 日期解析
	 * @author chenzq
	 * @date 2019年3月27日 下午5:58:23
	 * @param datestr
	 * @param format
	 * @return
	 */
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
	/**
	 * @Description: java.sql.Timestamp解析
	 * @author chenzq
	 * @date 2019年3月27日 下午5:58:50
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static java.sql.Timestamp datestring2SqlTimestamp(String datestr, String format) {
		Date date = datestring2Date(datestr, format);
		return null == date ? null : new java.sql.Timestamp(date.getTime());
	}
}
