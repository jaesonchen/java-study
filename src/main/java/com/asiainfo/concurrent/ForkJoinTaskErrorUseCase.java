package com.asiainfo.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * invokeAll的N个任务中，其中N-1个任务会使用fork()交给其它线程执行，但是，它还会留一个任务自己执行，这样，就充分利用了线程池，保证没有空闲的不干活的线程。
 * 
 * 执行compute()方法的线程本身也是一个Worker线程，当对两个子任务调用fork()时，这个Worker线程就会把任务分配给另外两个Worker，
 * 但是它自己却停下来等待不干活了！这样就白白浪费了Fork/Join线程池中的一个Worker线程。
 * 
 * @author       zq
 * @date         2017年9月14日  上午9:33:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ForkJoinTaskErrorUseCase extends RecursiveTask<Integer> {
	
	private static final long serialVersionUID = -1739805877354442869L;

	private static final int THRESHOLD = 10;// 阀值
	private int start;
	private int end;

	public ForkJoinTaskErrorUseCase(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		
		int sum = 0;
		// 如果任务足够小就计算任务
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			// 如果任务大于阀值，就分裂成两个子任务计算
			int middle = (start + end) / 2;
			ForkJoinTaskErrorUseCase leftTask = new ForkJoinTaskErrorUseCase(start, middle);
			ForkJoinTaskErrorUseCase rightTask = new ForkJoinTaskErrorUseCase(middle + 1, end);
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			// 等待子任务执行完，并得到其结果
			int leftResult = leftTask.join();
			int rightResult = rightTask.join();
			// 合并子任务
			sum = leftResult + rightResult;
		}
		return sum;
	}

	public static void main(String[] args) {
		
		ForkJoinPool pool = new ForkJoinPool();
		// 生成一个计算任务
		ForkJoinTaskErrorUseCase task = new ForkJoinTaskErrorUseCase(1, 100);
		// 执行任务
		Future<Integer> result = pool.submit(task);
		try {
			System.out.println(result.get());
		} catch (InterruptedException | ExecutionException e) {}
	}
}
