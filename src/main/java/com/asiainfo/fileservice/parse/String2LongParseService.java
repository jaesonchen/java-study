package com.asiainfo.fileservice.parse;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月9日  下午4:05:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class String2LongParseService implements IopParseService<Long> {
	
	private IopParseService<?> delegate = null;

	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public Long parse(String str) {
		
		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		try {
			return Long.parseLong(String.valueOf(result));
		} catch (Exception ex) {
			throw new IopParseException(ErrorCodes.RECORD_RESULTCODE_ERROR_TYPE, "不能包含非数字字符！");
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
