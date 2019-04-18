package com.asiainfo.designpattern.architecture.retry;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午11:14:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class UserNotFoundException extends DataAccessException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
