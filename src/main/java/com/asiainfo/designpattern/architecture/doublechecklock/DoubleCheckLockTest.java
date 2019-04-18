package com.asiainfo.designpattern.architecture.doublechecklock;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月19日  上午10:49:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class DoubleCheckLockTest {

    static final Logger logger = LoggerFactory.getLogger(DoubleCheckLockTest.class);
    
    public static void main(String[] args) {
        
        final Inventory<Item> inventory = new Inventory<>(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                while (inventory.addItem(new Item())) {};
            });
        }
        
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Error waiting for ExecutorService shutdown");
        }
    }
    
    static class Item implements Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
    }
}
