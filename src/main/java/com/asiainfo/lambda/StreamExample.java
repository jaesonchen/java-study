package com.asiainfo.lambda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 并行Streams： parallelStream() 方法在集合和流上实现了并行。它将它们分解成子问题，然后分配给不同的线程进行处理，
 * 这些任务可以分给不同的CPU核心处理，完成后再合并到一起。实现原理主要是使用了fork/join框架。
 * 如果使用不正确的话实际上会使得你的代码运行的更慢。我们进行了一些基准测试，发现要慢15%，甚至可能更糟糕。
 * 假设我们已经运行了多个线程，然后使用.parallelStream() 来增加更多的线程到线程池中，这很容易就超过多核心CPU处理的上限，从而增加了上下文切换次数，使得整体都变慢了。
 * 
 * filter 	排除所有不满足条件的元素，具体条件通过Predicate接口来定义；
 * map 		执行元素的映射转换，具体的映射方式使用Function接口定义；
 * flatMap 	通过另外一种 Stream接口将每个流元素转换成零个或者更多流元素；
 * peek  	对遇到的每个流元素执行一些操作；
 * distinct 根据流元素的equals()结果排除所有重复的元素；
 * sorted 	使后续操作中的流元素强制按Comparator定义的比较逻辑排列；
 * limit 	使后续操作只能看到有限数量的元素；
 * substream 使后续操作只能看到某个范围内的元素（使用索引）。
 * 
 * @author       zq
 * @date         2017年9月4日  下午9:44:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StreamExample {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		List<Task> tasks = getTasks();

		allReadingTasks(tasks).forEach(System.out::println);
		allReadingTasksSortedByCreatedOnDesc(tasks).forEach(System.out::println);
		allDistinctTasks(tasks).forEach(System.out::println);
		topN(tasks, 1).forEach(System.out::println);
		System.out.println(countAllReadingTasks(tasks));
		allDistinctTags(tasks).forEach(System.out::println);
		System.out.println(isAllReadingTasksWithTagBooks(tasks));
		System.out.println(isAnyReadingTasksWithTagJava8(tasks));
		System.out.println(joinAllTaskTitles(tasks));
		parallelStream(tasks);
		parallelStreamWithPool(tasks).get();
	}
    
    public static List<Task> getTasks() {
    	
        Task task1 = new Task("Read Java 8 in action", 					TaskType.READING, 	LocalDate.of(2015, Month.SEPTEMBER, 20))
                .addTag("java").addTag("java8").addTag("books");
        Task task2 = new Task("Write factorial program in Haskell", 	TaskType.CODING, 	LocalDate.of(2015, Month.SEPTEMBER, 20))
                .addTag("program").addTag("haskell").addTag("functional");
        Task task3 = new Task("Read Effective Java", 					TaskType.READING, 	LocalDate.of(2015, Month.SEPTEMBER, 21))
                .addTag("java").addTag("books");
        Task task4 = new Task("Write a blog on Stream API", 			TaskType.BLOGGING, 	LocalDate.of(2015, Month.SEPTEMBER, 21))
                .addTag("writing").addTag("stream").addTag("java8");
        Task task5 = new Task("Write prime number program in Scala", 	TaskType.CODING, 	LocalDate.of(2015, Month.SEPTEMBER, 22))
                .addTag("scala").addTag("functional").addTag("program");
        return Stream.of(task1, task2, task3, task4, task5).collect(Collectors.toList());
    }
    
    // collect
    public static List<String> allReadingTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .sorted(Comparator.comparing(Task::getCreatedOn))
                .map(Task::getTitle)
                .collect(Collectors.toList());
    }

    // sorted
    public static List<String> allReadingTasksSortedByCreatedOnDesc(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .sorted(Comparator.comparing(Task::getCreatedOn).reversed())
                .map(Task::getTitle)
                .collect(Collectors.toList());
    }

    // distinct
    public static List<Task> allDistinctTasks(List<Task> tasks) {
        return tasks.stream().distinct().collect(Collectors.toList());
    }

    // limit
    public static List<String> topN(List<Task> tasks, int n) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .sorted(Comparator.comparing(Task::getCreatedOn))
                .map(Task::getTitle)
                .limit(n)
                .collect(Collectors.toList());
    }

    // count
    public static long countAllReadingTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .count();
    }

    // distinct
    public static List<String> allDistinctTags(List<Task> tasks) {
        return tasks.stream()
                .flatMap(task -> task.getTags().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    // allMatch
    public static boolean isAllReadingTasksWithTagBooks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .allMatch(task -> task.getTags().contains("books"));
    }

    // anyMatch
    public static boolean isAnyReadingTasksWithTagJava8(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getType() == TaskType.READING)
                .anyMatch(task -> task.getTags().contains("java8"));
    }

    // reduce 汇总
    public static String joinAllTaskTitles(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getTitle)
                .reduce((first, second) -> first + " *** " + second)
                .get();
    }
    
    // Java 8 并行流（parallel stream）采用共享线程池，对性能造成了严重影响。可以包装流来调用自己的线程池解决性能问题。
    // 在 JVM 的后台，使用通用的 fork/join 池来完成上述功能，该池是所有并行流共享的。默认情况，fork/join 池会为每个处理器分配一个线程。
    public static void parallelStream(List<Task> tasks) {
        tasks.parallelStream()
            .map(task -> task.getId())
            .forEach(System.out::println);
    }
    
    // 使用独立的ForkJoinPool
    public static ForkJoinTask<?> parallelStreamWithPool(List<Task> tasks) {
        
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        return forkJoinPool.submit(() -> {
            tasks.parallelStream()
                .map(task -> task.getId())
                .forEach(System.out::println);
        });
    }
}
