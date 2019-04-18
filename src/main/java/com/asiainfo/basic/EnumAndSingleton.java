package com.asiainfo.basic;

/**
 * 单例和枚举
 * 
 * 枚举序列化是由jvm保证的，每一个枚举类型和定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上，
 * Java做了特殊的规定：在序列化时Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
 * 同时，编译器是不允许任何对这种序列化机制的定制的并禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法，
 * 从而保证了枚举实例的唯一性。
 * 
 * @author       zq
 * @date         2017年9月18日  下午2:13:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class EnumAndSingleton {
    
}

// 枚举单例
enum SingletonEnum {
	/**
	 * single instance
	 */
    INSTANCE;
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

// 饿汉单例
class SingletonHungry implements java.io.Serializable {

    /** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private static SingletonHungry instance = new SingletonHungry();
    private SingletonHungry() {}

    public static SingletonHungry getInstance() {
        return instance;
    }
    protected Object readResolve() {     
    	return instance;     
	}
}

// 懒汉单例
class SingletonLazy implements java.io.Serializable {

    /** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private static volatile SingletonLazy instance;
    private SingletonLazy() {}

    public static synchronized SingletonLazy getInstance() {
        if (instance == null) {
            instance = new SingletonLazy();
        }
        return instance;
    }
    protected Object readResolve() {     
    	return instance;     
	}
}

// 双重检查懒汉单例
class SingletonDoubleCheck implements java.io.Serializable {
	
    /** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private static volatile SingletonDoubleCheck instance = null;
    private SingletonDoubleCheck() {}

    public static SingletonDoubleCheck getSingleton() {
        if (instance == null) {
            synchronized (SingletonDoubleCheck.class) {
                if (instance == null) {
                	instance = new SingletonDoubleCheck();
                    //指令重排序会导致多线程下使用未完成初始化的对象，为避免指令重排序，必须使用volatile
                    //allocate memory
                    //instance(memory)
                    //singleton = memory
                }
            }
        }
        return instance;
    }
    protected Object readResolve() {     
    	return instance;     
	}
}

// Holder模式，由虚拟机保证同步
class Singleton implements java.io.Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private Singleton() {}
    private static class SingletonHolder {
        private static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }
    protected Object readResolve() {     
    	return SingletonHolder.instance;     
	}
}