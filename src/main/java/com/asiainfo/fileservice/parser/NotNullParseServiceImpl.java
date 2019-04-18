package com.asiainfo.fileservice.parser;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:25:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class NotNullParseServiceImpl implements ParseService<String> {

	private ParseService<?> delegate = null;

	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public String parse(String str) {
		
		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		if (null == result) {
			throw new ParseException(ErrorCodes.RECORD_RESULTCODE_ERROR_VALUE, "字段不能为空!");
		}
		return String.valueOf(result);
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
