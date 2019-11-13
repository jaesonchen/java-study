package com.asiainfo.collection.blockingqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月23日 下午9:23:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class DelayQueueDemo {

    public static void main(String[] args) {
        
        DelayQueue<Order> queue = new DelayQueue<>();

        Thread c1 = new Thread(new Consumer(queue), "consumer-1");
        Thread c2 = new Thread(new Consumer(queue), "consumer-2");
        Thread p1 = new Thread(new Producer(queue), "producer-1");

        c1.start();
        c2.start();
        p1.start();
    }
    
    static class Producer implements Runnable {
        
        private final ThreadLocalRandom random = ThreadLocalRandom.current();
        private final DelayQueue<Order> queue;
        private final AtomicLong seq = new AtomicLong(1);

        public Producer(DelayQueue<Order> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            while (true) {
                Order order = new Order(seq.getAndIncrement(), random.nextInt(10) + 1);
                this.queue.offer(order);
                System.out.println(Thread.currentThread().getName() + " produce order: " + order);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 
                }
            }
        }
    }
    
    static class Consumer implements Runnable {
        
        private final DelayQueue<Order> queue;

        public Consumer(DelayQueue<Order> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    Order order = this.queue.take();
                    System.out.println(Thread.currentThread().getName() + " consume order: " + order);
                    Thread.yield();
                } catch (InterruptedException e) {
                    // 
                }
            }
        }
    }
    
    static class Order implements Delayed {

        long id;
        long timeout;
        long expire;
        public Order(long id, long timeout) {
            this.id = id;
            this.timeout = timeout;
            this.expire = System.currentTimeMillis() + this.timeout * 1000L;
        }
        
        @Override
        public int compareTo(Delayed other) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "Order [id=" + id + ", timeout=" + timeout + ", expire=" + expire + "]";
        }
    }
}
