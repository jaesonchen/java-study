package com.asiainfo.jdbc.converter;

/**
 * @Description: String字段转String[]，用于setter上
 * 
 * @author       zq
 * @date         2017年6月25日  下午1:51:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class String2StringArrayConvertServiceImpl implements IConvertService<String[]> {

	private IConvertService<?> delegate;
	
	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#getClazz()
	 */
	@Override
	public Class<?> getClazz() {
	    return null == this.delegate ? String.class : this.delegate.getClazz();
	}

	/* 
	 * @Description: TODO
	 * @param obj
	 * @return
	 * @see com.asiainfo.jdbc.IConvertService#convert(java.lang.Object)
	 */
	@Override
	public String[] convert(Object obj) {
		Object result = null == this.delegate ? obj : this.delegate.convert(obj);
		return null == result ? null : String.valueOf(result).split(",");
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
