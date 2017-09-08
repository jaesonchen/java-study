package com.asiainfo.fileservice.parse;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:23:56
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IopParseService<T> {
	
	T parse(String str);
	void setDelegate(IopParseService<?> delegate);
}
