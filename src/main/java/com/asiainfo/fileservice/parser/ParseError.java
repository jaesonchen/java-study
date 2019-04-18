package com.asiainfo.fileservice.parser;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:19:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ParseError implements Serializable {

	private static final long serialVersionUID = 1L;
	private int row;
	private int column;
	private String errorMessage;
	private String errorCode;
	
	public ParseError() {}
	public ParseError(int row, String errorCode, String errorMessage) {
		this(row, 0, errorCode, errorMessage);
	}
	public ParseError(int row, int column, String errorCode, String errorMessage) {
		this.row = row;
		this.column = column;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	@Override
	public String toString() {
		return "IopParseError [row=" + row + ", column=" + column + ", errorMessage=" + errorMessage + ", errorCode="
				+ errorCode + "]";
	}
}
