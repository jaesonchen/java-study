package com.asiainfo.concurrent;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join框架的核心是ForkJoinPool类，它是对AbstractExecutorService类的扩展。ForkJoinPool实现了工作偷取算法，并可以执行ForkJoinTask任务。
 * 通常继承RecursiveTask(返回计算结果)或者是RecursiveAction(不返回结果)
 * ForkJoinPool 提供了三类方法来调度子任务：
 * 		execute: 异步执行指定的任务。
 * 		invoke 和 invokeAll: 执行指定的任务，等待完成，返回结果。
 * 		submit: 异步执行指定的任务并立即返回一个 Future 对象。
 * 
 * if (当前这个任务工作量足够小)
 *     直接完成这个任务
 * else
 *     将这个任务或这部分工作分解成两个部分分别触发(invoke)这两个子任务的执行，并等待结果
 * 
 * @author       zq
 * @date         2017年9月15日  上午9:34:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ForkJoinTaskExample extends RecursiveTask<Long>{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private Path dir;
	public ForkJoinTaskExample(Path dir) {
		this.dir = dir;
	}

    public static void main(String[] args) {

        Long count = new ForkJoinPool().invoke(new ForkJoinTaskExample(Paths.get("D:/")));
        System.out.println(count.longValue());
    }
    
	/* 
	 * @Description: TODO
	 * @return
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Long compute() {
		
		long count = 0L;
        List<ForkJoinTaskExample> subTasks = new ArrayList<>();

        // 读取目录 dir 的子路径。
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path subPath : ds) {
                if (Files.isDirectory(subPath, LinkOption.NOFOLLOW_LINKS)) {
                    // 对每个子目录都新建一个子任务。
                    subTasks.add(new ForkJoinTaskExample(subPath));
                } else {
                    // 遇到文件，则计数器增加 1。
                    count++;
                }
            }

            if (!subTasks.isEmpty()) {
                // 在当前的 ForkJoinPool 上调度所有的子任务。
                for (ForkJoinTaskExample subTask : invokeAll(subTasks)) {
                    count += subTask.join();
                }
            }
        } catch (IOException ex) {
            return 0L;
        }
        return count;
	}
}
