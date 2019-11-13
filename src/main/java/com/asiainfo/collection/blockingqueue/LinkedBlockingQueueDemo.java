package com.asiainfo.collection.blockingqueue;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月24日 下午12:31:33
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class LinkedBlockingQueueDemo {
    
    public static void main(String[] args) {

        BlockingQueue<String> q = new LinkedBlockingQueue<>(1000);
        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);
        new Thread(p, "p").start();
        new Thread(c1, "c1").start();
        new Thread(c2, "c2").start();
    }
    
    static class Producer implements Runnable {
        
        private final BlockingQueue<String> queue;
        Producer(BlockingQueue<String> queue) { this.queue = queue; }
        public void run() {
            try {
                while (true) { queue.put(produce()); }
            } catch (InterruptedException ex) { }
        }
        String produce() {
            
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {}
            return UUID.randomUUID().toString();
        }
    }
    
    static class Consumer implements Runnable {

        private final BlockingQueue<String> queue;
        Consumer(BlockingQueue<String> q) { queue = q; }
        public void run() {
            try {
                while (true) { consume(queue.take()); }
            } catch (InterruptedException ex) { }
        }
        void consume(String str) { System.out.println(Thread.currentThread().getName() + ": " + str); }
    }
}
