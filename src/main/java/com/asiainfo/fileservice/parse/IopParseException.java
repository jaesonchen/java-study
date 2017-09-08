package com.asiainfo.fileservice.parse;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:17:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IopParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String message;
	private int column = -1;
	
	public IopParseException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.message = message;
	}
	public IopParseException(String errorCode, Exception ex) {
		super(ex);
		this.errorCode = errorCode;
		this.message = ex.getMessage();
	}
	public IopParseException(int column, String errorCode, String message) {
		super(message);
		this.column = column;
		this.errorCode = errorCode;
		this.message = message;
	}
	public IopParseException(int column, String errorCode, Exception ex) {
		super(ex);
		this.column = column;
		this.errorCode = errorCode;
		this.message = ex.getMessage();
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public String toString() {
		return "IopParseException [errorCode=" + errorCode + ", message=" + message + ", column=" + column + "]";
	}
}
