package com.asiainfo.designpattern.architecture.retry;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午11:21:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DataAccessException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataAccessException(String message) {
        super(message);
    }
}
