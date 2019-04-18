package com.asiainfo.fileservice.formatter;

import java.text.NumberFormat;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月16日  下午6:22:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Double2StringFormatServiceImpl implements FormatService {

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.fileservice.format.IopFormatService#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		try {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(4);
			return format.format(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "0.0";
	}
}
