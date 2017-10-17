package com.asiainfo.concurrent;

import com.asiainfo.util.ThreadPoolUtils;

/**
 * 多线程同步 synchronized方法和代码块：每个对象都有一个锁，当线程进入对象的synchronized方法或代码块时，
 * 该线程获得对象的锁，其他线程在访问该对象的任何synchronized方法或代码块时无法获得锁被挂起等待。对象的
 * 其他非synchronized方法不受锁的影响。
 * static synchronized方法的锁时ClassName.class。
 */
public class ProducerAndConsumer {

	public static void main(String[] args) {
		
		Storage storage = new Storage(); 
		Thread producer1 = ThreadPoolUtils.getInstance().newThread(new Producer(storage, "producer1"));  
		Thread producer2 = ThreadPoolUtils.getInstance().newThread(new Producer(storage, "producer2")); 
		Thread producer3 = ThreadPoolUtils.getInstance().newThread(new Producer(storage, "producer3"));  
		Thread consumer1 = ThreadPoolUtils.getInstance().newThread(new Consumer(storage, "Consumer1"));  
		Thread consumer2 = ThreadPoolUtils.getInstance().newThread(new Consumer(storage, "Consumer2"));
		
		producer1.start();
		producer2.start();
		producer3.start();
		consumer1.start(); 
		consumer2.start();
	}
}

//仓库 
class Storage {
	private int index = 0;
	private Product[] products = new Product[20];
	
	public int getSize() {
		return this.index;
	}
	// 生产者往仓库中放入产品  
	public synchronized void push(Product product) {
		
		while (index == products.length) {
			try {
				//仓库已满，等待  
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		//把产品放入仓库 
		products[index++] = product;
		System.out.println(product.getProducer() + " 存入了产品 " + product + " ,仓库共有 " + this.index + "个产品！"); 
		//唤醒等待线程
		notifyAll();
	}

	// 消费者从仓库中取出产品  
	public synchronized Product pop(String consumer) {
		while (index == 0) {
			try {  
				wait();//仓库空，等待  
			} catch (InterruptedException ex) {   
				ex.printStackTrace();  
			}  

		}  
		//从仓库中取产品  
		Product product = products[--index];  
		products[index] = null;  
		System.out.println(consumer + " 消费了产品 " + product + " ,仓库共有 " + this.index + "个产品！");  
		//唤醒等待线程
		notifyAll();  
		return product;
	}
}

//产品类 
class Product {
	private long id;// 产品id  
	private String producer;// 生产者

	public Product(long id, String producer) {  
		this.id = id;
		this.producer = producer;
		System.out.println(this.producer + "生产了产品：" + this.id);
	}

	public long getId() {
		return this.id;
	}
	
	public String getProducer() {
		return this.producer;
	}
	
	@Override  
	public String toString() {
		return "(产品ID：" + this.id + " 生产者：" + this.producer + ")";  
	}
}

//生产者 
class Producer extends Thread {
	private Storage storage;  
	public Producer(Storage storage, String name) {
		super(name);
		this.storage = storage;
	}  

	@Override
	public void run() {
		while (true) {
			storage.push(new Product(new java.util.Date().getTime(), super.getName()));
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {   
				ex.printStackTrace();  
			}
		}
	}
}

//消费者
class Consumer extends Thread {
	
	private Storage storage;
	
	public Consumer(Storage storage, String name) {
		super(name);
		this.storage = storage;
	}  

	@Override  
	public void run() {  
		while (true) {
			storage.pop(super.getName());		
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {   
				ex.printStackTrace();  
			}
		}
	}
}


