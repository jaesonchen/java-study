package com.asiainfo.lambda;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * filter 	排除所有不满足条件的元素，具体条件通过Predicate接口来定义；
 * map 		执行元素的映射转换，具体的映射方式使用Function接口定义；
 * flatMap 	通过另外一种 Stream接口将每个流元素转换成零个或者更多流元素；
 * peek  	对遇到的每个流元素执行一些操作；
 * distinct 根据流元素的equals(..)结果排除所有重复的元素；
 * sorted 	使后续操作中的流元素强制按Comparator定义的比较逻辑排列；
 * limit 	使后续操作只能看到有限数量的元素；
 * substream 使后续操作只能看到某个范围内的元素（使用索引）。
 * 
 * @author       zq
 * @date         2017年9月4日  下午9:44:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StreamExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

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
	}
    
    public static List<Task> getTasks() {
    	
        Task task1 = new Task("Read Java 8 in action", 					TaskType.READING, 	LocalDate.of(2015, Month.SEPTEMBER, 20)).addTag("java").addTag("java8").addTag("books");
        Task task2 = new Task("Write factorial program in Haskell", 	TaskType.CODING, 	LocalDate.of(2015, Month.SEPTEMBER, 20)).addTag("program").addTag("haskell").addTag("functional");
        Task task3 = new Task("Read Effective Java", 					TaskType.READING, 	LocalDate.of(2015, Month.SEPTEMBER, 21)).addTag("java").addTag("books");
        Task task4 = new Task("Write a blog on Stream API", 			TaskType.BLOGGING, 	LocalDate.of(2015, Month.SEPTEMBER, 21)).addTag("writing").addTag("stream").addTag("java8");
        Task task5 = new Task("Write prime number program in Scala", 	TaskType.CODING, 	LocalDate.of(2015, Month.SEPTEMBER, 22)).addTag("scala").addTag("functional").addTag("program");
        return Stream.of(task1, task2, task3, task4, task5).collect(Collectors.toList());
    }
    
    public static List<String> allReadingTasks(List<Task> tasks) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                sorted(Comparator.comparing(Task::getCreatedOn)).
                map(Task::getTitle).
                collect(Collectors.toList());
    }

    public static List<String> allReadingTasksSortedByCreatedOnDesc(List<Task> tasks) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                sorted(Comparator.comparing(Task::getCreatedOn).reversed()).
                map(Task::getTitle).
                collect(Collectors.toList());
    }

    public static List<Task> allDistinctTasks(List<Task> tasks) {
        return tasks.stream().distinct().collect(Collectors.toList());
    }

    public static List<String> topN(List<Task> tasks, int n) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                sorted(Comparator.comparing(Task::getCreatedOn)).
                map(Task::getTitle).
                limit(n).
                collect(Collectors.toList());
    }

    public static long countAllReadingTasks(List<Task> tasks) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                count();
    }

    public static List<String> allDistinctTags(List<Task> tasks) {
        return tasks.stream().flatMap(task -> task.getTags().stream()).distinct().collect(Collectors.toList());
    }

    public static boolean isAllReadingTasksWithTagBooks(List<Task> tasks) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                allMatch(task -> task.getTags().contains("books"));
    }

    public static boolean isAnyReadingTasksWithTagJava8(List<Task> tasks) {
        return tasks.stream().
                filter(task -> task.getType() == TaskType.READING).
                anyMatch(task -> task.getTags().contains("java8"));
    }

    public static String joinAllTaskTitles(List<Task> tasks) {
        return tasks.stream().
                map(Task::getTitle).
                reduce((first, second) -> first + " *** " + second).
                get();
    }
}
