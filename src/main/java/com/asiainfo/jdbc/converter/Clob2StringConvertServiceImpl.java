package com.asiainfo.jdbc.converter;

/**
 * @Description: java.sql.Clob字段转String，用于setter上
 * 
 * @author       zq
 * @date         2017年6月25日  下午12:01:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Clob2StringConvertServiceImpl implements IConvertService<String> {

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#getClazz()
	 */
	@Override
	public Class<?> getClazz() {
		return java.sql.Clob.class;
	}

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#convert(java.lang.Object)
	 */
	@Override
	public String convert(Object obj) {
		return ConvertUtil.clob2String((java.sql.Clob) obj);
	}

	/* 
	 * @Description: TODO
	 * @param delegate
	 * @see com.asiainfo.jdbc.converter.IConvertService#setDelegate(com.asiainfo.jdbc.converter.IConvertService)
	 */
	@Override
	public void setDelegate(IConvertService<?> delegate) {
		throw new UnsupportedOperationException("ClobConverter do not support delegate!");
	}
}
