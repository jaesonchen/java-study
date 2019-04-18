package com.asiainfo.designpattern.architecture.retry;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午11:15:37
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DatabaseNotAvailableException extends DataAccessException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public DatabaseNotAvailableException(String msg) {
        super(msg);
    }
}
