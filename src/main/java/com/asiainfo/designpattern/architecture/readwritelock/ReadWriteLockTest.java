package com.asiainfo.designpattern.architecture.readwritelock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReadWriteLock 模式实现
 * 
 * @author       zq
 * @date         2018年4月19日  下午4:16:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ReadWriteLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteLockTest.class);
    
    public static void main(String[] args) {

        ExecutorService executeService = Executors.newFixedThreadPool(10);
        ReadWriteLock lock = new SynchronizedReadWriteLock();
        
        // Start writers
        IntStream.range(0, 5)
            .forEach(i -> executeService.submit(new WriteTask(lock.writeLock(), "Writer-" + i)));
        LOGGER.info("Writers added...");

        // Start readers
        IntStream.range(0, 5)
            .forEach(i -> executeService.submit(new ReadTask(lock.readLock(), "Reader" + i)));
        LOGGER.info("Readers added...");
        
        executeService.shutdown();
        try {
            executeService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Error waiting for ExecutorService shutdown", e);
        }
    }

    static class ReadTask implements Runnable {

        final Logger logger = LoggerFactory.getLogger(getClass());
        
        private Lock readLock;
        private String id;
        public ReadTask(Lock readLock, String id) {
            this.readLock = readLock;
            this.id = id;
        }
        
        @Override
        public void run() {

            Thread.currentThread().setName("ReadTask-" + id);
            readLock.lock();
            try {
                read();
            } catch (InterruptedException e) {
                logger.info("InterruptedException when reading", e);
            } finally {
                readLock.unlock();
            }
        }
        
        protected void read() throws InterruptedException {
            
            logger.info("begin read");
            long time = ThreadLocalRandom.current().nextLong(100);
            Thread.sleep(time);
            logger.info("finish after reading {}ms", time);
        }
    }
    
    static class WriteTask implements Runnable {

        final Logger logger = LoggerFactory.getLogger(getClass());
        
        private Lock writeLock;
        private String id;
        public WriteTask(Lock writeLock, String id) {
            this.writeLock = writeLock;
            this.id = id;
        }
        
        @Override
        public void run() {

            Thread.currentThread().setName("WriteTask-" + id);
            writeLock.lock();
            try {
                write();
            } catch (InterruptedException e) {
                logger.info("InterruptedException when writing", e);
            } finally {
                writeLock.unlock();
            }
        }
        
        protected void write() throws InterruptedException {
            
            logger.info("begin write");
            long time = ThreadLocalRandom.current().nextLong(1000);
            Thread.sleep(time);
            logger.info("finish after writing {}ms", time);
        }
    }
}
