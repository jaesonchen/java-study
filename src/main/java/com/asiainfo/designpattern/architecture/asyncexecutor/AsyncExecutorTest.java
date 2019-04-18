package com.asiainfo.designpattern.architecture.asyncexecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月26日  下午6:12:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AsyncExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExecutorTest.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        AsyncExecutor executor = new ThreadAsyncExecutor();
        AsyncResult<Integer> asyncResult1 = executor.startProcess(callable(10, 400));
        AsyncResult<String> asyncResult2 = executor.startProcess(callable("test", 300));
        AsyncResult<Integer> asyncResult3 = executor.startProcess(callable(20, 500), callback("Callback result 3"));
        
        Thread.sleep(350);
        LOGGER.info("Some hard work done");
        
        executor.endProcess(asyncResult1);
        executor.endProcess(asyncResult2);
        LOGGER.info("endProcess done");
        
        asyncResult3.await();
    }

    static <T> Callable<T> callable(T value, long delayMillis) {
        return () -> {
            Thread.sleep(delayMillis);
            LOGGER.info("Task completed with: " + value);
            return value;
        };
    }
    static <T> AsyncCallback<T> callback(String name) {
        return (value, ex) -> {
            if (ex.isPresent()) {
                LOGGER.info(name + " failed: " + ex.map(Exception::getMessage).orElse(""));
            } else {
                LOGGER.info(name + ": " + value);
            }
        };
    }
}
