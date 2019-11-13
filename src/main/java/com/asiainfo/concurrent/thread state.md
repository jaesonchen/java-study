
## 线程状态

- 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。实现Runnable接口和继承Thread可以得到一个线程类，new一个实例出来，线程就进入了初始状态。
    
- 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
    
1. 就绪状态只是说你资格运行，调度程序没有挑选到你，你就永远是就绪状态。
2. 调用线程的start()方法，此线程进入就绪状态。
3. 当前线程sleep()方法结束，其他线程join()结束，等待用户输入完毕，某个线程拿到对象锁，这些线程也将进入就绪状态。
4. 当前线程时间片用完了，调用当前线程的yield()方法，当前线程进入就绪状态。
5. 锁池里的线程拿到对象锁后，进入就绪状态。
6. 线程调度程序从可运行池中选择一个线程作为当前线程时线程所处的状态。这也是线程进入运行状态的唯一一种方式。
    
- 阻塞(BLOCKED)：阻塞状态是线程阻塞在进入synchronized关键字修饰的方法或代码块(获取锁)时的状态。

- 等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。处于这种状态的线程不会被分配CPU执行时间，它们要等待被显式地唤醒，否则会处于无限期等待的状态。

- 超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。处于这种状态的线程不会被分配CPU执行时间，不过无须无限期等待被其他线程显示地唤醒，在达到一定时间后它们会自动唤醒。

- 终止(TERMINATED)：表示该线程已经执行完毕。
    
1. 当线程的run()方法完成时，或者主线程的main()方法完成时，我们就认为它终止了。这个线程对象也许是活的，但是，它已经不是一个单独执行的线程。线程一旦终止了，就不能复生。
2. 在一个终止的线程上调用start()方法，会抛出java.lang.IllegalThreadStateException异常。
    


## 线程状态转换

![thread-state-switch](../../../../resources/images/concurrent/thread-state-switch.png)  
    


## 等待队列

- Thread.sleep(long millis)，一定是当前线程调用此方法，当前线程进入TIMED_WAITING状态，但不会释放已经持有的对象锁，millis后线程自动苏醒进入就绪状态。作用：给其它线程执行机会的最佳方式。

- Thread.yield()，一定是当前线程调用此方法，当前线程放弃获取的CPU时间片，但不会释放已经持有的对象锁，由运行状态变为就绪状态，让调度再次选择线程。作用：让相同优先级的线程轮流执行，但并不保证一定会轮流执行。实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。Thread.yield()不会导致阻塞。该方法与sleep()类似，只是不能由用户指定暂停多长时间。

- Thread.join()/Thread.join(long millis)，当前线程里调用其它线程t的join方法，当前线程进入WAITING/TIMED_WAITING状态，当前线程不会释放已经持有的对象锁。线程t执行完毕或者millis时间到，当前线程一般情况下进入RUNNABLE状态，也有可能进入BLOCKED状态（因为join是基于wait实现的）。

- obj.wait()，当前线程调用对象的wait()方法，当前线程释放对象锁，必须持有对象锁。依靠notify()/notifyAll()唤醒或者wait(long timeout) timeout时间到自动唤醒。

- obj.notify()，唤醒在此对象监视器上等待的单个线程，选择是任意性的，必须持有对象锁。notifyAll()唤醒在此对象监视器上等待的所有线程。

- LockSupport.park()/LockSupport.parkNanos(long nanos)/LockSupport.parkUntil(long deadlines), 当前线程进入WAITING/TIMED_WAITING状态。对比wait方法,不需要获得锁就可以让线程进入WAITING/TIMED_WAITING状态，需要通过LockSupport.unpark(Thread thread)唤醒。
    


