package com.asiainfo.designpattern.architecture.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  下午12:38:47
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RetryTest {

    private static Logger logger = LoggerFactory.getLogger(RetryTest.class);
    
    public static void main(String[] args) {

        noErrors();
        
        try {
            errorNoRetry();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
        }
        
        errorWithIgnoreRetry();
        
        try {
            errorWithRetry();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void noErrors() {
        
        IService<User> service = new UserServiceImpl();
        logger.info("noErrors:{}", service.perform());
    }
    
    public static void errorNoRetry() throws DataAccessException {
        
        IService<User> service = new UserServiceImpl(new UserNotFoundException("user not found!"));
        logger.info("errorNoRetry:{}", service.perform());
    }
    
    public static void errorWithIgnoreRetry() throws DataAccessException {
        
        IService<User> service = new UserServiceImpl(new DatabaseNotAvailableException("db not available!"));
        Retry<User> retry = new Retry<>(
                service, 
                3, 
                1000, 
                e -> DatabaseNotAvailableException.class.isAssignableFrom(e.getClass()));
        
        logger.info("errorWithRetry:result={}, attemps={}, errors={}", retry.perform(), retry.attempts(), retry.errors());
    }
    
    public static void errorWithRetry() throws DataAccessException {
        
        IService<User> service = new UserServiceImpl(
                new DatabaseNotAvailableException("db1 not available!"),
                new DatabaseNotAvailableException("db2 not available!"), 
                new UserNotFoundException("user not found!"));
        Retry<User> retry = new Retry<>(
                service, 
                3, 
                1000, 
                e -> DatabaseNotAvailableException.class.isAssignableFrom(e.getClass()));
        
        logger.info("errorWithRetry:result={}, attemps={}, errors={}", retry.perform(), retry.attempts(), retry.errors());
    }
}
