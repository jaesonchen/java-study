package com.asiainfo.concurrent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Random:产生一个伪随机数（通过相同的种子，产生的随机数是相同的）。
 * ThreadLocalRandom:是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
 * 
 * ThreadLocalRandom的主要实现细节：
 *  使用一个普通的long而不是使用Random中的AtomicLong作为seed
 *  不能自己创建ThreadLocalRandom实例，因为它的构造函数是私有的，可以使用它的静态工厂ThreadLocalRandom.current()
 *  它是CPU缓存感知式的，使用8个long虚拟域来填充64位L1高速缓存行
 * 
 * 任何情况下都不要在多个线程间共享一个Random实例，而该把它放入ThreadLocal之中
 * java7在所有情形下都更推荐使用ThreadLocalRandom，它向下兼容已有的代码且运营成本更低
 * 
 * 
 * @author       zq
 * @date         2018年4月18日  下午5:18:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadLocalRandomExample {

    public static void main(String[] args) throws InterruptedException {

        // java.util.Random
        for (int i = 0; i < 4; i++) {
            new Thread(new RandomTask(String.valueOf(i), new Random(100), 1000000)).start();
        }
        TimeUnit.SECONDS.sleep(1);
        
        // java.util.concurrent.ThreadLocalRandom
        for (int i = 0; i < 4; i++) {
            new Thread(new RandomTask(String.valueOf(i), null, 1000000) {

                @Override
                protected Random getRandom() {
                    return ThreadLocalRandom.current();
                }
            }).start();
        }
        TimeUnit.SECONDS.sleep(1);
        
        // ThreadLocal
        final ThreadLocal<Random> rnd = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random(100);
            }
        };
        for (int i = 0; i < 4; i++) {
            new Thread(new RandomTask(String.valueOf(i), null, 1000000) {

                @Override
                protected Random getRandom() {
                    return rnd.get();
                }
            }).start();
        }
        TimeUnit.SECONDS.sleep(1);
    }
    
    static class RandomTask implements Runnable {

        private Random random;
        private long count;
        private String id;
        
        public RandomTask(String id, Random random, long count) {
            this.id = id;
            this.random = random;
            this.count = count;
        }
        
        protected Random getRandom() {
            return random;
        }

        @Override
        public void run() {
            
            Random rd = getRandom();
            long start = System.currentTimeMillis();
            int sum = 0;
            for (long i = 0; i < count; i++) {
                sum += rd.nextInt();
            }
            long time = System.currentTimeMillis() - start;
            System.out.println("Thread #" + id + " Time = " + time / 1000.0 + " sec, sum = " + sum);
        }
    }
}
