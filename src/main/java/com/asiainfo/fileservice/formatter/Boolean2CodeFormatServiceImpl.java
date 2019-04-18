package com.asiainfo.fileservice.formatter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月17日  下午3:27:38
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Boolean2CodeFormatServiceImpl implements FormatService {

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.fileservice.format.IopFormatService#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		if (null == obj) {
			return "";
		}
		return Boolean.parseBoolean(String.valueOf(obj)) ? "1" : "0";
	}
}
