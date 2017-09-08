package com.asiainfo.jdbc.converter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月25日  上午11:49:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IConvertService<T> {

	Class<?> getClazz();
	
	T convert(Object obj);
	
	void setDelegate(IConvertService<?> delegate);
}
