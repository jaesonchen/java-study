package com.asiainfo.fileservice.parse;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午2:09:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class String2IntParseServiceImpl implements IopParseService<Integer> {

	private IopParseService<?> delegate = null;

	/* 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @see com.asiainfo.fileservice.parse.IopParseService#parse(java.lang.String)
	 */
	@Override
	public Integer parse(String str) {
		
		Object result = (null == this.delegate) ? str : this.delegate.parse(str);
		try {
			return Integer.parseInt(String.valueOf(result));
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
