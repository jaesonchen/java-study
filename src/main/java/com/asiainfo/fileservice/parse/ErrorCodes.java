package com.asiainfo.fileservice.parse;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月10日  下午4:29:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ErrorCodes implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String RECORD_RESULTCODE_ERROR_TYPE = "4";
	public static final String RECORD_RESULTCODE_ERROR_VALUE = "5";
	public static final String RECORD_RESULTCODE_SUCCESS = "000000000";
	
	public static final String FILE_RESULTCODE_SUCCESS = "00";
	public static final String FILE_RESULTCODE_ERROR_FILENAME = "01";
	public static final String FILE_RESULTCODE_ERROR_FILENOTEXIST = "02";
	public static final String FILE_RESULTCODE_ERROR_FILECANNOTOPEN = "03";
	public static final String FILE_RESULTCODE_ERROR_FILESIZE = "05";
	public static final String FILE_RESULTCODE_ERROR_RECORDCOUNT = "06";
	public static final String FILE_RESULTCODE_ERROR_DATADATE = "07";
	public static final String FILE_RESULTCODE_ERROR_FIELDTYPEERROR = "16";
}
