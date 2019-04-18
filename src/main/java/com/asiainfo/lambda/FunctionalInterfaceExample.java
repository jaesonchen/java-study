package com.asiainfo.lambda;

/**
 * @FunctionalInterface
 * 1、该注解只能标记在"有且仅有一个抽象方法"的接口上。
 * 
 * 2、JDK8接口中的static和default方法，都不算是抽象方法。
 * 
 * 3、接口默认继承java.lang.Object，所以如果接口显示声明覆盖了Object中方法，那么也不算抽象方法。
 * 
 * 4、该注解不是必须的，如果一个接口符合"函数式接口"定义，那么加不加该注解都没有影响。加上该注解能够更好地让编译器进行检查。
 *    如果编写的不是函数式接口，但是加上了@FunctionInterface，那么编译器会报错。
 * 
 * @author       zq
 * @date         2018年4月12日  下午3:10:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FunctionalInterfaceExample {

    // 函数化接口
    @FunctionalInterface
    public interface IAction {
     
        // 抽象方法 
        public void perform();
        // java.lang.Object中的方法不是抽象方法 
        public boolean equals(Object obj);
        // default不是抽象方法 
        public default void defaultMethod() {}
    }
    // 带参数函数接口
    @FunctionalInterface
    public interface IActionParam {
        public void perform(String str);
    }
    // 有返回值函数接口
    @FunctionalInterface
    public interface IActionReturn {
        public String perform(String str1, String str2);
    }
    
    // consumer
    public void execute(IAction action) {
        action.perform();
    }
    // consumer，带参数
    public void execute(IActionParam action, String str) {
        action.perform(str);
    }
    // consumer，有返回值
    public String execute(IActionReturn action, String str1, String str2) {
        return action.perform(str1, str2);
    }
    
    public static void main(String[] args) {
        
        FunctionalInterfaceExample example = new FunctionalInterfaceExample();
        example.execute(() -> System.out.println("hello world"));
        example.execute((str) -> System.out.println("hello " + str), "jaeson");
        System.out.println(example.execute((str1, str2) -> str1 + " " + str2, "hello", "chenzq"));
    }
}
