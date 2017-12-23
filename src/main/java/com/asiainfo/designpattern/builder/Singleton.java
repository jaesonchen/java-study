package com.asiainfo.designpattern.builder;

import java.io.Serializable;

/**
 * 用私有构造器或者枚举类型enum强化singleton:
 * 
 * enum Sigleton 在功能上与静态工厂方法相近，但它更简洁，无偿地提供了序列化机制，绝对防止多次实例化， 即使是在面对复杂的序列化和反射攻击的时候。
 * 
 * 单元素的enum已经成为实现Singleton的最佳方法。
 * 
 * 1.线程安全 2.不会因为序列化而产生新实例 3.防止反射攻击
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:32:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Singleton {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    // enum singleton
    enum EnumSingleton implements Serializable {
        
        INSTANCE;
        private String name;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
    // 饿汉
    static class HungrySingleton implements java.io.Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
        
        private static HungrySingleton instance = new HungrySingleton();
        private HungrySingleton() {}

        public static HungrySingleton getInstance() {
            return instance;
        }
        protected Object readResolve() {     
            return instance;     
        }
    }
    // 懒汉
    static class LazySingleton implements java.io.Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
        private static LazySingleton instance;
        private LazySingleton() {}

        public static synchronized LazySingleton getInstance() {
            
            if (instance == null) {
                instance = new LazySingleton();
            }
            return instance;
        }
        protected Object readResolve() {     
            return instance;     
        }
    }
    // 双重检查
    static class DoubleCheckSingleton implements java.io.Serializable {
        
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
        
        private static volatile DoubleCheckSingleton instance;
        private DoubleCheckSingleton() {}

        public static DoubleCheckSingleton getSingleton() {
            
            if (instance == null) {
                synchronized (DoubleCheckSingleton.class) {
                    if (instance == null) {
                        instance = new DoubleCheckSingleton();
                        //指令重排序会导致多线程下使用未完成初始化的对象，为避免指令重排序，instance必须使用volatile
                        //memory
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
    // Holder 模式
    static class HolderSingleton implements java.io.Serializable {
        
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        private HolderSingleton() {}
        private static class SingletonHolder {
            private static HolderSingleton instance = new HolderSingleton();
        }

        public static HolderSingleton getInstance() {
            return SingletonHolder.instance;
        }
        protected Object readResolve() {
            return SingletonHolder.instance;     
        }
    }
}
