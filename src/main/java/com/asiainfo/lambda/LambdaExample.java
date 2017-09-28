package com.asiainfo.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 函数接口（Functional Interface）：只包含一个抽象方法的接口，所以也称为SAM（Single Abstract Method）类型的接口。
 * 例如我们比较熟悉的Runnable接口，只定义了唯一的一个抽象方法run()，就是函数接口。
 * 
 * Lambda表达式：实现函数接口，并返回该接口的一个匿名实现类对象的一种简明表达方式。
 * 启动这样一种技术变革的最佳切入点是集合（collection）内部操作并行化。实践表明，Lambda表达式显著地增加代了码可读性和提升了Java语言的表现力。
 * 
 * 特别强调两点：
 * Lambda表达式返回值是一个对象（至少目前还是），该返回对象往往是某个函数接口的匿名实现。
 * Lambda表达式的类型声明规则如下：要么为所有参数声明类型，要么去掉所有参数的类型声明。
 * 
 * 方法引用（Method Reference）： Lambda表达式的一种特殊形式。当一个lambda表达式body中仅仅是调用某个方法，这种情况下，
 * 使用方法引用替代lambda表达式，从形式上直接引用这个方法，这就比在lambda表达式body中引用在形式上更简洁一些。
 * 
 * @author       zq
 * @date         2017年9月4日  下午2:47:53
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class LambdaExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// lambda express
		new Thread(() -> System.out.println("lambda express!")).start();
		
		// Comparator
		List<String> list = new ArrayList<>(Arrays.asList(new String[] {"aaa", "bbb", "ccc"}));
		/*Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});*/
		Collections.sort(list, (o1, o2) -> o2.hashCode() - o1.hashCode());
		System.out.println(list);
		
		// iterator
		List<String> features = new ArrayList<>(Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API"));
		/*for (String feature : features) {
		    System.out.println(feature);
		}*/
		features.forEach(n -> System.out.println(n));
		
		// method referrence (can not modify collection element)
		features.forEach(System.out::println);
		
		List<Integer> namesLength = transform(features, String::length);
		namesLength.forEach(System.out::println);
		
		List<String> upperCaseNames = transform(features, String::toUpperCase);
		upperCaseNames.forEach(System.out::println);
		
		// Predicate
		List<String> languages = new ArrayList<>(Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp"));
		System.out.println("Languages which starts with J :");
	    filter(languages, (str)->str.startsWith("J"));
	    System.out.println("Languages which ends with a ");
	    filter(languages, (str)->str.endsWith("a"));
	    System.out.println("Print all languages :");
	    filter(languages, (str)->true);
	    System.out.println("Print no language : ");
	    filter(languages, (str)->false);
	    System.out.println("Print language whose length greater than 4:");
	    filter(languages, (str)->str.length() > 4);
	    
	    // and()、or()、xor() combine multi Predicate
	    Predicate<String> startsWithJ = (n) -> n.startsWith("J");
	    Predicate<String> fourLetterLong = (n) -> n.length() == 4;
	    languages.stream().filter(startsWithJ.and(fourLetterLong)).forEach((n) -> System.out.print("nName, which starts with 'J' and four letter long is : " + n));
	    
	    // map
	    List<Integer> costBeforeTax = new ArrayList<>(Arrays.asList(100, 200, 300, 400, 500));
	    costBeforeTax.stream().map((cost) -> cost + 12 * cost).forEach(System.out::println);
	    
	    // reduce
	    int bill = costBeforeTax.stream().map((cost) -> cost + 12 * cost).reduce((sum, cost) -> sum + cost).get();
	    System.out.println("Total : " + bill);
	    
	    // filter (return new List)
	    List<String> strList = new ArrayList<>(Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp"));
	    List<String> filtered = strList.stream().filter(x -> x.length()> 4).collect(Collectors.toList());
	    System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);
	    
	    // Collectors
	    List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
	    String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
	    System.out.println(G7Countries);
	    
	    // distinct
	    List<Integer> numbers = new ArrayList<>(Arrays.asList(9, 10, 3, 4, 7, 3, 4));
	    List<Integer> distinct = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
	    System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
	    
	    // aggregate
	    List<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
	    IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
	    System.out.println("Highest prime number in List : " + stats.getMax());
	    System.out.println("Lowest prime number in List : " + stats.getMin());
	    System.out.println("Sum of all prime numbers : " + stats.getSum());
	    System.out.println("Average of all prime numbers : " + stats.getAverage());
	    
	    
	    // deferrence of annonymous & lambda:
	    // this of annonymous class point to annoymous class self
	    // this of lambda point to outer class
	    // java compiler change lambda to private method (invokedynamic )
	    // lambda must referrence final local var
	}
	
	public static void filter(List<String> names, Predicate<String> condition) {
	    /*for(String name: names)  {
	        if(condition.test(name)) {
	            System.out.println(name + " ");
	        }
	    }*/
	    //better
	    names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
	        System.out.println(name + " ");
	    });
	}
	
	public static <T, R> List<R> transform(List<T> list, Function<T, R> fx) {
        List<R> result = new ArrayList<>();
        for (T element : list) {
            result.add(fx.apply(element));
        }
        return result;
    }
}
