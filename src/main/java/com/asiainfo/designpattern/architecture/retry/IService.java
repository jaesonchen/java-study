package com.asiainfo.designpattern.architecture.retry;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午10:26:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IService<T> {

    T perform() throws DataAccessException;
}
