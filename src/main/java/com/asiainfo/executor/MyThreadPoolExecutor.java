package com.asiainfo.executor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**   
 * ThreadPoolExecutor 简单实现
 * 
 * @author chenzq  
 * @date 2019年5月13日 下午4:57:08
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyThreadPoolExecutor {

    private BlockingQueue<Runnable> taskQueue = null;
    private List<Worker> workers = new LinkedList<Worker>();
    private AtomicInteger threads = new AtomicInteger(0);
    private volatile boolean isStopped = false;
    private int coreSize;
    private int maxSize;
    private long keepAlive = 60L * 1000;
    private final Lock lock = new ReentrantLock();
    
    public MyThreadPoolExecutor(int coreSize, int maxSize) {
        this(coreSize, maxSize, new LinkedBlockingQueue<>(1024));
    }
    public MyThreadPoolExecutor(int coreSize, int maxSize, BlockingQueue<Runnable> taskQueue) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.taskQueue = taskQueue;
        new EvictIdleTask(this.keepAlive, 3).start();
    }
    
    // 执行线程任务
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        // 小于基本线程数，直接创建Worker
        Worker worker = newWorker(command, this.coreSize);
        if (null != worker) {
            addWorker(worker);
            worker.start();
            return;
        }
        // 放入任务队列
        if (this.taskQueue.offer(command)) {
            return;
        }
        // 创建线程至maxSize
        worker = newWorker(command, this.maxSize);
        if (null != worker) {
            addWorker(worker);
            worker.start();
            return;
        }
        // rejected 异常
        throw new RuntimeException("Rejected by thread pool!");
    }
    
    // 新建worker
    private Worker newWorker(Runnable command, int limit) {
        int current;
        for (;;) {
            current = threads.get();
            if (current >= limit) {
                return null;
            }
            if (threads.compareAndSet(current, current + 1)) {
                return new Worker(command, this.taskQueue);
            }
        }
    }
    
    // 添加到线程池列表中
    private void addWorker(Worker worker) {
        lock.lock();
        try {
            this.workers.add(worker);
        } finally {
            lock.unlock();
        }
    }
    
    // worker 线程
    class Worker extends Thread {
        private BlockingQueue<Runnable> taskQueue = null;
        private Runnable firstTask;
        private volatile long lastUsed = System.currentTimeMillis();
        private volatile boolean isStopped = false;
        
        public Worker(Runnable firstTask, BlockingQueue<Runnable> taskQueue) {
            this.firstTask = firstTask;
            this.taskQueue = taskQueue;
        }
        
        @Override
        public void run() {
            Runnable task = firstTask;
            firstTask = null;
            runTask(task);
            while (!isStopped) {
                try {
                    // block on take
                    task = taskQueue.take();
                    runTask(task);
                    task = null;
                } catch (InterruptedException ie) {
                    // interrupt from take
                }
            }
        }
        // 运行任务
        private void runTask(Runnable task) {
            try {
                task.run();
            } catch (Exception ex) {
                // ignore
            } finally {
                lastUsed = System.currentTimeMillis();
            }
                
        }
        
        public void stopWorker() {
            this.isStopped = true;
            // 从taskQueue.take() 中唤醒
            this.interrupt();
        }
        
        public long getLastUsed() {
            return this.lastUsed;
        }
    }
    
    // 空闲worker检测线程
    class EvictIdleTask extends Thread {
        private long keepAlive;
        private long sleep;
        public EvictIdleTask(long keepAlive, long sleep) {
            this.keepAlive = keepAlive;
            this.sleep = sleep;
        }
        
        @Override
        public void run() {
            final List<Worker> workers = MyThreadPoolExecutor.this.workers;
            final AtomicInteger threads = MyThreadPoolExecutor.this.threads;
            final int coreSize = MyThreadPoolExecutor.this.coreSize;
            List<Worker> idles = new ArrayList<>();
            while (!MyThreadPoolExecutor.this.isStopped) {
                try {
                    for (Worker worker : workers) {
                        // 获取worker的线程状态
                        State state = worker.getState();
                        // stop和terminate的先清除
                        if (worker.isStopped || Thread.State.TERMINATED.equals(state)) {
                            idles.add(worker);
                        }
                    }
                    for (Worker worker : workers) {
                        // 保留coreSize数量的worker
                        if (threads.get() <= (idles.size() + coreSize)) {
                            break;
                        }
                        // 获取worker的线程状态
                        State state = worker.getState();
                        // taskQueue.take() block 状态的线程算空闲
                        if(Thread.State.BLOCKED.equals(state) 
                                || Thread.State.WAITING.equals(state) 
                                || Thread.State.TIMED_WAITING.equals(state)) {
                            // 判断空闲时间是否超过最大空闲时间
                            long idle = System.currentTimeMillis() - worker.getLastUsed();
                            if (idle > this.keepAlive) {
                                idles.add(worker);
                            }
                        }
                    }
                    // 加锁删除idle worker
                    removeIdleWorker(idles);
                    // 检测线程休息一段时间
                    TimeUnit.SECONDS.sleep(this.sleep);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
        // 删除idle worker
        private void removeIdleWorker(List<Worker> idles) {
            final List<Worker> workers = MyThreadPoolExecutor.this.workers;
            final AtomicInteger threads = MyThreadPoolExecutor.this.threads;
            final Lock lock = MyThreadPoolExecutor.this.lock;
            lock.lock();
            try {
                // 删除idle worker
                workers.removeAll(idles);
                // 设置worker状态
                for (Worker worker : idles) {
                    threads.decrementAndGet();
                    worker.stopWorker();
                }
            } catch (Exception ex) {
                // ignore
            } finally {
                lock.unlock();
            }
        }
    }
}
