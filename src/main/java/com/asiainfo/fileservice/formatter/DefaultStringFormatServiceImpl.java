package com.asiainfo.fileservice.formatter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月10日  下午2:52:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DefaultStringFormatServiceImpl implements FormatService {

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.fileservice.format.IopFormatService#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		return null == obj ? "" : String.valueOf(obj);
	}
}
