package com.asiainfo.jdbc.converter;

import java.text.SimpleDateFormat;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月25日  上午11:51:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Date2StringConvertService implements IConvertService<String> {

	private IConvertService<?> delegate;
	private String pattern = "yyyyMMddHHmmss";
	
	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#getClazz()
	 */
	@Override
	public Class<?> getClazz() {
		return null == this.delegate ? java.sql.Date.class : this.delegate.getClazz();
	}
	
	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#convert(java.lang.Object)
	 */
	@Override
	public String convert(Object obj) {
		try {
			return new SimpleDateFormat(this.pattern).format(null == this.delegate ? obj : this.delegate.convert(obj));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/* 
	 * @Description: TODO
	 * @param delegate
	 * @see com.asiainfo.jdbc.converter.IConvertService#setDelegate(com.asiainfo.jdbc.converter.IConvertService)
	 */
	@Override
	public void setDelegate(IConvertService<?> delegate) {
		this.delegate = delegate;
	}
}
