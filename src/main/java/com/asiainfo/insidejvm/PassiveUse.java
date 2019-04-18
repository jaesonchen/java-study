package com.asiainfo.insidejvm;

/**
 * 
 * @Description: 所有的java虚拟机实现必须在每个类或接口被java程序“首次主动使用”时才初始化他们；被动使用不会初始化类，但是有可能会加载类。
 *               如果在加载的过程中，遇到了.class文件的缺失或者存在错误，类加载器只会在首次主动使用它们时才会报错，如果一直没有主动使用，则不会报错。
 *               
 *               主动使用：创建类的实例、访问某个类或者接口的非final类型的静态变量/对该静态变量赋值、调用类的静态方法、反射、初始化一个类的子类、
 *                        jvm启动时被标明为启动类的类（如Java Test、main方法所在的类）。
 *               
 *               
 * @author       zq
 * @date         2017年10月16日  下午4:47:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class PassiveUse {
    
    // main所在的类为主动使用
    static {
        System.out.println("PassiveUse is initialized");
    }
    
    public static void main(String[] args) {
        
        // 访问类的static final成员并且赋值在常量池中，属于被动使用
        System.out.println(PassiveParent.WELCOME);

        // 通过数组定义类引用类，属于被动使用
        @SuppressWarnings("unused")
        PassiveParent[] pArray = new PassiveParent[10];
        
        System.out.println("===========================");
        
        // 访问类的超类的static 成员，对于子类属于被动使用
        System.out.println(PassiveBaby.hoursOfSleep);
	}
}
class PassiveParent {
	
    static final String WELCOME = "welcome to beijing!";
    // 相当于static 初始化块，访问需要在运行时赋值的static final成员属于主动使用
    // static final int MAX_AGE;
    // static { MAX_AGE = (int) Math.random() * 100; }
    static final int MAX_AGE = (int) Math.random() * 100;
	static int hoursOfSleep = 8;
	static {
		System.out.println("PassiveParent is initialized");
	}
}
class PassiveBaby extends PassiveParent {
    static int hoursOfCry = 8;
	static {
		System.out.println("PassiveBaby is initialized");
	}
}