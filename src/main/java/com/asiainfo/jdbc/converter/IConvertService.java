package com.asiainfo.jdbc.converter;

/**
 * @Description: 数据类型转换接口
 * 
 * @author       zq
 * @date         2017年6月25日  上午11:49:57
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IConvertService<T> {

    /**
     * @Description: 要转换的数据类型，通常指向数据库字段类型
     * @author chenzq
     * @date 2019年3月27日 下午6:39:51
     * @return
     */
	Class<?> getClazz();
	
	/**
	 * @Description: 转换方法，通常需要先调用delegate进行转换
	 * @author chenzq
	 * @date 2019年3月27日 下午6:41:43
	 * @param obj
	 * @return
	 */
	T convert(Object obj);
	
	/**
	 * @Description: 支持转换链，如果不支持需要抛出异常
	 * @author chenzq
	 * @date 2019年3月27日 下午6:41:07
	 * @param delegate
	 */
	void setDelegate(IConvertService<?> delegate);
}
