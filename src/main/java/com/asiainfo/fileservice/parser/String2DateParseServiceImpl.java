package com.asiainfo.fileservice.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.Assert;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:35:07
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class String2DateParseServiceImpl implements ParseService<Date> {

	private ParseService<?> delegate;
	private String pattern = "yyyyMMddHHmmss";
	
	public String2DateParseServiceImpl() {}
	public String2DateParseServiceImpl(String pattern) {
		Assert.hasLength(pattern, "日期格式不能为null或者空字符串.");
		this.pattern = pattern;
	}
	
	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public Date parse(String str) {
		
		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		try {
			return new SimpleDateFormat(this.pattern).parse(String.valueOf(result));
		} catch (Exception ex) {
			throw new ParseException(ErrorCodes.RECORD_RESULTCODE_ERROR_TYPE, "日期格式不正确，必须是格式为：" + this.pattern + "的字符串！");
		}
	}
	/* 
	 * @Description: TODO
	 * @param delegate
	 * @see com.asiainfo.fileservice.parse.IopParseService#setDelegate(com.asiainfo.fileservice.parse.IopParseService)
	 */
	@Override
	public void setDelegate(ParseService<?> delegate) {
		this.delegate = delegate;
	}
}
