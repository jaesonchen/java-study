package com.asiainfo.designpattern.architecture.retry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重试模式，可以配置忽略异常以继续尝试
 * 
 * @author       zq
 * @date         2018年4月10日  上午10:59:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Retry<T> implements IService<T> {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    private IService<T> service;
    private int maxAttempts;
    private long delay;
    private Predicate<Exception> ignore;
    private AtomicInteger attempts = new AtomicInteger(0);
    private List<Exception> errors = new ArrayList<>();
    
    //如果开发人员确信某个使用了可变长度参数的方法，在与泛型类一起使用时不会出现类型安全问题，就可以用这个注解进行声明。
    @SafeVarargs
    public Retry(IService<T> service, int maxAttempts, long delay, Predicate<Exception>... ignore) {
        this.service = service;
        this.maxAttempts = maxAttempts;
        this.delay = delay;
        this.ignore = Arrays.stream(ignore).reduce(Predicate::or).orElse(e -> false);
    }
    
    public List<Exception> errors() {
        return Collections.unmodifiableList(this.errors);
    }
    
    public int attempts() {
        return this.attempts.intValue();
    }
    
    /* 
     * TODO
     * @return
     * @throws DataAccessException
     * @see com.asiainfo.designpattern.retry.IService#perform()
     */
    @Override
    public T perform() throws DataAccessException {
        
        while (this.attempts.getAndIncrement() < this.maxAttempts) {
            try {
                return this.service.perform();
            } catch (DataAccessException ex) {
                logger.error("retry error:", ex);
                this.errors.add(ex);
                if (!this.ignore.test(ex)) {
                    throw ex;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(this.delay);
            } catch (InterruptedException e) {}
        }
        throw new DataAccessException("Data Access fail, after " + this.maxAttempts + " attemps.");
    }
}
