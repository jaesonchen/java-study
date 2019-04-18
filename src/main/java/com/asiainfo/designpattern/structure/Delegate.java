package com.asiainfo.designpattern.structure;

/**
 * 代理模式：代理类与业务类实现同一个业务接口，代理类通过构造器参数持有业务类对象。
 *          代理类在实现业务方法时，调用真正的业务对象方法实现业务逻辑，并可以在业务方法中进行权限控制以及添加切面逻辑，从而实现对业务类的代理。
 * 
 * 意图: 为其他对象提供一种代理以控制对这个对象的访问。 
 * 
 * 远程代理（Remote Proxy）为一个位于不同的地址空间的对象提供一个本地的代理对象(rpc动态代理)。
 * 虚拟代理（Virtual Proxy）根据需要创建开销很大的对象。如果需要创建一个资源消耗较大的对象，先创建一个消耗相对较小的对象来表示，真实对象只在需要时才会被真正创建。 
 * 保护代理（Protection Proxy）控制对原始对象的访问。保护代理用于对象应该有不同的访问权限的时候。
 * 
 * 延迟加载  用代理模式实现延迟加载的一个经典应用就在 Hibernate 框架里面。
 * 指针引用  是指当调用真实的对象时，代理处理另外一些事。比如计算真实对象的引用次数。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:30:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Delegate {

    public static void main(String[] args) {
        
        IService proxy = new ProtectionProxy(new ServiceImpl());
        proxy.execute();
        proxy.view();

        proxy = new ProtectionProxy(new ServiceImpl(), 1);
        proxy.execute();
        proxy.view();
        
        System.out.println("===========================");
        IService virtualProxy = new VirtualProxy();
        virtualProxy.execute();
    }

    interface IService {
        public void execute();
        public void view();
    }
    
    //业务实现
    static class ServiceImpl implements IService {
        
        public ServiceImpl() {
            System.out.println("ServiceImpl is create");
        }
        @Override
        public void execute() {
            System.out.println("ServiceImpl.service is running......");
        }
        @Override
        public void view() {
            System.out.println("ServiceImpl.view is running......");
        }
    }
    
    // 委托代理
    static class DelegateProxy implements IService {
        
        private IService service;
        public DelegateProxy(IService service) {
            this.service = service;
        }

        @Override
        public void execute() {
            service.execute();
        }
        @Override
        public void view() {
            service.view();
        }
    }
    
    //保护代理
    static class ProtectionProxy implements IService {
        
        private IService service;
        private int permission;
        public ProtectionProxy(IService service) {
            this.service = service;
        }
        public ProtectionProxy(IService service, int permission) {
            this.service = service;
            this.permission = permission;
        }

        @Override
        public void execute() {
            if (this.permission > 0) {
                System.out.println("ProtectionProxy.execute is calling ......");
                this.service.execute();
            } else {
                System.out.println("ProtectionProxy.execute is calling(no permission) ......");
            }
        }
        @Override
        public void view() {
            System.out.println("ProtectionProxy.view is calling ......");
            this.service.view();
        }
    }
    
    //虚拟代理
    static class VirtualProxy implements IService {
        
        private IService service;
        public VirtualProxy() {}
        
        @Override
        public void execute() {
            System.out.println("VirtualProxy.execute is calling ......");
            if (this.service == null) {
                this.service = new ServiceImpl();
            }
            this.service.execute();
        }
        @Override
        public void view() {
            System.out.println("VirtualProxy.view is calling ......");
            if (this.service == null) {
                this.service = new ServiceImpl();
            }
            this.service.view();
        }
    }
}
