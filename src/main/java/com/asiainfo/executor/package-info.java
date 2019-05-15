/**   
 * - 合理利用线程池能够带来三个好处。
 *  1. 降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
 *  2. 提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。
 *  3. 提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。
 * 
 * - 线程池的创建
    new  ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, milliseconds, runnableTaskQueue, handler);
 * - 创建一个线程池需要输入几个参数： 
 * corePoolSize（线程池的基本大小）：当提交一个任务到线程池时，线程池会创建一个线程来执行任务，即使其他空闲的基本线程能够执行新任务也会创建线程，
 *    -  等到需要执行的任务数大于线程池基本大小时就不再创建。如果调用了线程池的 prestartAllCoreThreads 方法，线程池会提前创建并启动所有基本线程。
 * runnableTaskQueue（任务队列）：用于保存等待执行的任务的阻塞队列。 可以选择以下几个阻塞队列。
 *      - ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按 FIFO（先进先出）原则对元素进行排序。
 *      - LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按 FIFO （先进先出） 排序元素，吞吐量通常要高于 ArrayBlockingQueue。
 *                            Executors.newFixedThreadPool() 使用了这个队列。
 *      - SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQueue，
 *                         Executors.newCachedThreadPool 使用了这个队列。
 *      - PriorityBlockingQueue：一个具有优先级的无限阻塞队列。
 * maximumPoolSize（线程池最大大小）：线程池允许创建的最大线程数。如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。
 *    -  值得注意的是如果使用了无界的任务队列这个参数就没什么效果。
 * ThreadFactory：用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
 * RejectedExecutionHandler（饱和策略）：当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。
 *    - 这个策略默认情况下是 AbortPolicy，表示无法处理新任务时抛出异常。JDK1.5 提供了四种策略，也可以根据应用场景需要来实现 RejectedExecutionHandler 
 *    - 接口自定义策略。如记录日志或持久化不能处理的任务。
 *      - AbortPolicy：直接抛出异常。
 *      - CallerRunsPolicy：使用调用者所在线程来运行任务。
 *      - DiscardOldestPolicy：丢弃队列里第一个等待任务，并执行当前任务。
 *      - DiscardPolicy：不处理，丢弃掉。
 * keepAliveTime（线程活动保持时间）：线程池的工作线程空闲后，保持存活的时间。所以如果任务很多，并且每个任务执行的时间比较短，可以调大这个时间，提高线程的利用率。
 * TimeUnit（线程活动保持时间的单位）：可选的单位有天（DAYS），小时（HOURS），分钟（MINUTES），毫秒 (MILLISECONDS)，微秒 (MICROSECONDS, 千分之一毫秒) 和毫微秒 (NANOSECONDS, 千分之一微秒)。
 * 
 * - 向线程池提交任务
 * - 可以使用 execute 提交的任务，但是 execute 方法没有返回值，所以无法判断任务是否被线程池执行成功。
    threadsPool.execute(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        }});
 * - 也可以使用 submit 方法来提交任务，它会返回一个 future, 那么我们可以通过这个 future 来判断任务是否执行成功。
    Future<Object> future = executor.submit(harReturnValuetask);
 * 
 * - 线程池的关闭
 * - 可以通过调用线程池的 shutdown 或 shutdownNow 方法来关闭线程池，它们的原理是遍历线程池中的工作线程，
 * - 然后逐个调用线程的 interrupt 方法来中断线程，所以无法响应中断的任务可能永远无法终止。
 * 
 * 
 * - 线程池处理流程
 * 1. 首先线程池判断基本线程池是否已满？没满，创建一个工作线程来执行任务。满了，则进入下个流程。
 * 2. 其次线程池判断工作队列是否已满？没满，则将新提交的任务存储在工作队列里。满了，则进入下个流程。
 * 3. 最后线程池判断整个线程池是否已满？没满，则创建一个新的工作线程来执行任务，满了，则交给饱和策略来处理这个任务。
 * 
 * - 工作线程
 * - 线程池创建线程时，会将线程封装成工作线程 Worker，Worker 在执行完任务后，还会无限循环获取工作队列里的任务来执行。
 * 
 * @author chenzq  
 * @date 2019年5月13日 下午4:56:53
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.executor;