package com.asiainfo.fileservice.format;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月16日  下午6:20:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Any2EmptyFormatService implements IopFormatService {

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.fileservice.format.IopFormatService#format(java.lang.Object)
	 */
	@Override
	public String format(Object obj) {
		return "";
	}
}
