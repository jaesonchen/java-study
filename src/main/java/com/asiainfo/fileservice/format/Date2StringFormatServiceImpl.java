package com.asiainfo.fileservice.format;

import java.text.SimpleDateFormat;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月10日  下午2:34:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Date2StringFormatServiceImpl implements IopFormatService {

	private String pattern = "yyyyMMddHHmmss";

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.fileservice.format.IopFormatService#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		if (null == obj) {
			return null;
		}
		return new SimpleDateFormat(this.pattern).format(obj);
	}
}
