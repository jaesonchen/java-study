package com.asiainfo.fileservice.parse;

import org.springframework.util.Assert;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:32:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Null2DefaultParseService implements IopParseService<String> {

	private IopParseService<?> delegate = null;
	private String replaceStr = "";
	
	public Null2DefaultParseService() {}
	public Null2DefaultParseService(String replaceStr) {
		Assert.isTrue(null != replaceStr, "代替字符不能是null");
		this.replaceStr = replaceStr;
	}
	
	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public String parse(String str) {

		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		return (null == result) ? this.replaceStr : String.valueOf(result);
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
