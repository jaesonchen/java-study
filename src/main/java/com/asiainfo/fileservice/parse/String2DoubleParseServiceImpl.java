package com.asiainfo.fileservice.parse;

import java.text.NumberFormat;

import org.springframework.util.Assert;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午2:20:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class String2DoubleParseServiceImpl implements IopParseService<Double> {

	private IopParseService<?> delegate = null;
	private int fraction = 2;
	
	public String2DoubleParseServiceImpl() {}
	public String2DoubleParseServiceImpl(int fraction) {
		Assert.isTrue(fraction >= 0, "小数点位数不能小于0");
		this.fraction = fraction;
	}

	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public Double parse(String str) {

		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		try {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(this.fraction);
			return Double.parseDouble(format.format(result));
		} catch (Exception ex) {
			throw new IopParseException(ErrorCodes.RECORD_RESULTCODE_ERROR_TYPE, "不能包含非数字字符");
		}
	}
	/* 
	 * @Description: TODO
	 * @param delegate
	 * @see com.asiainfo.fileservice.parse.IopParseService#setDelegate(com.asiainfo.fileservice.parse.IopParseService)
	 */
	@Override
	public void setDelegate(IopParseService<?> delegate) {
		this.delegate = delegate;
	}
}
