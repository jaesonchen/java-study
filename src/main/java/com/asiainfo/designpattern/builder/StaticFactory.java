package com.asiainfo.designpattern.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 考虑用静态工厂方法代替构造器：
 * 
 * 静态工厂方法返回的对象所属的类，在编写包含该静态工厂方法的类时可以不必存在。
 * 这种灵活的静态工厂方法构成了服务提供者框架（Server Provider Framework）的基础。
 * 服务提供者框架是指这样一个系统：多个服务提供者实现一个服务，系统为服务提供者的客户端提供多个实现，并把它们从多个实现中解耦出来。
 * 
 * @author       zq
 * @date         2017年12月22日  下午2:57:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class StaticFactory {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        Factory.registerProvider(new IProvider() {
            @Override
            public IService newInstance() {
                return new IService() {
                    @Override
                    public void execute() {
                        System.out.println("service execute!");
                    }};
            }});
        Factory.newInstance().execute();
    }

    interface IService {
        public void execute();
    }
    interface IProvider {
        IService newInstance();
    }

    static class Factory {
        
        private static final String DEFAULT_PROVIDER = "default";
        private static final Map<String, IProvider> providers = new ConcurrentHashMap<String, IProvider>();
        private Factory() {}
        //Provider register API
        public static void registerProvider(IProvider p) {
            registerProvider(DEFAULT_PROVIDER, p);
        }
        public static void registerProvider(String name, IProvider p) {
            providers.put(name, p);
        }
        //Service instance API
        public static IService newInstance() {
            return newInstance(DEFAULT_PROVIDER);
        }
        public static IService newInstance(String name) {
            IProvider p = providers.get(name);
            if (p == null) {
                throw new IllegalArgumentException("No provider register with name: " + name);
            }
            return p.newInstance();
        }
    }
}
