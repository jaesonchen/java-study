package com.asiainfo.jdbc.converter;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年7月2日  上午10:38:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Complex2StringConvertServiceImpl implements IConvertService<String> {

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.jdbc.converter.IConvertService#getClazz()
	 */
	@Override
	public Class<?> getClazz() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.jdbc.converter.IConvertService#convert(java.lang.Object)
	 */
	@Override
	public String convert(Object obj) {
		return null == obj ? null : obj.toString();
	}

	/* 
	 * @Description: TODO
	 * @param delegate
	 * @see com.asiainfo.jdbc.converter.IConvertService#setDelegate(com.asiainfo.jdbc.converter.IConvertService)
	 */
	@Override
	public void setDelegate(IConvertService<?> delegate) {
		// TODO Auto-generated method stub

	}
}
