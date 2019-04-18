package com.asiainfo.concurrent;

import java.util.*;

/**
 * 避免在同步中调用外来方法：
 * 为了避免线程的活性失败和安全性失败，在一个同步方法或者代码块中，永远不要放弃对客户端的控制，
 * 即在一个被同步的区域内部，不要调用设计成要被覆盖的方法，或者由客户端以函数对象的形式提供的方法，
 * 否则由于同步块无法控制这些外来的方法，会导致异常、死锁或者数据损坏。
 */
public class ConcurrentExample4 {

	public static void main(String[] args) {
		
		ObservableSet<Integer> set = new ObservableSet<Integer>();
		set.addObserver(new Observer<Integer>() {
			@Override
	    	public void added(ObservableSet<Integer> s, Integer e) {
	    		System.out.println("print = " + e);
	    	}
		});
		
		set.addObserver(new Observer<Integer>() {
			@Override
	    	public void added(ObservableSet<Integer> s, Integer e) {
	    		System.out.println("remove = " + e);
	    		if (e > 30) {
	    			s.removeObserver(this);
	    		}
	    	}
		});
		
		for (int i = 1; i <= 100; i++) {
			set.add(i);
		}
	}
}

//集合观察者  
interface Observer<E> {
	public void added(ObservableSet<E> set, E element);
}
//被观察的集合主题 
@SuppressWarnings("all")
class ObservableSet<E> extends HashSet<E> {
	//观察者集合
	//private final List<SetObserver<E>> observers = new ArrayList<SetObserver<E>>();
	private final List<Observer<E>> observers = new java.util.concurrent.CopyOnWriteArrayList<Observer<E>>();

	//注册观察者
	public void addObserver(Observer<E> observer) {
		synchronized(observers){
			observers.add(observer);
		}
	}
	//取消注册观察者
	public void removeObserver(Observer<E> observer) {
		synchronized(observers) {
			observers.remove(observer);
		}
	}
	//通知集合中添加元素
	private void notifyElementAdded(E element) {
		/*synchronized(observers) {
			for(SetObserver<E> observer : observers) {
				observer.added(this, element);
			}
		}*/
		for (Observer<E> observer : observers) {
			observer.added(this, element);
		}
	}
	@Override
	public boolean add(E element) {
		boolean added = super.add(element);//调用父类方法 
		if (added) {
			notifyElementAdded(element);
		}
		return added;
	}
}