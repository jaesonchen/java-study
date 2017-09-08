package com.asiainfo.fileservice.parse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月4日  下午1:21:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IopParseResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<T> result = new ArrayList<T>();
	private List<IopParseError> error = new ArrayList<IopParseError>();
	private String fileName;
	private int total;
	
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public List<IopParseError> getError() {
		return error;
	}
	public void setError(List<IopParseError> error) {
		this.error = error;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "IopParseResult [result=" + result + ", error=" + error + ", fileName=" + fileName + ", total=" + total
				+ "]";
	}
}
