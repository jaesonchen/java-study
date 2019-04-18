package com.asiainfo.designpattern.architecture.retry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月10日  上午10:28:53
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class UserServiceImpl implements IService<User> {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    private AtomicInteger count = new AtomicInteger();
    private List<DataAccessException> errors = new ArrayList<>();
    
    public UserServiceImpl() {}
    public UserServiceImpl(DataAccessException... accessExceptions) {
        errors.addAll(Arrays.asList(accessExceptions));
    }
    
    /* 
     * TODO
     * @return
     * @throws DataAccessException
     * @see com.asiainfo.designpattern.retry.IService#perform()
     */
    @Override
    public User perform() throws DataAccessException {
        
        logger.info("user service perform()");
        int index = count.getAndIncrement();
        if (index < this.errors.size()) {
            throw this.errors.get(index);
        }
        return new User("chenzq", 20);
    }
}
