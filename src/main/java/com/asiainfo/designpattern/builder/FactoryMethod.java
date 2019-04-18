package com.asiainfo.designpattern.builder;

/**
 * Factory Method: 工厂方法模式是简单工厂模式的一种升级和抽象
 * 工厂模式的好处就在于将工厂和产品之间的耦合降低，将具体产品的构造过程放在了具体工厂类里面。
 * 在以后扩展产品的时候方便很多，只需要添加一个工厂类，一个产品类，就能方便的添加产品，而不需要修改原有的代码。
 * 
 * 意图:定义一个用于创建对象的接口，让子类决定实例化哪一个类。Factory Method 使一个类的实例化延迟到其子类。
 * 适用性:
 *  当一个类不知道它所必须创建的对象的类的时候。 
 *  当一个类希望由它的子类来指定它所创建的对象的时候。 
 *  当类将创建对象的职责委托给多个帮助子类中的某一个，并且你希望将哪一个帮助子类是代理者这一信息局部化的时候。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:33:42
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FactoryMethod {

    public static void main(String[] args) {

        IFactory carFac = new CarFactory();
        IProduct car = carFac.produce();
        car.use();
        
        IFactory busFac = new BusFactory();
        IProduct bus = busFac.produce();
        bus.use();
    }

    interface IProduct {
        void use();
    }
    interface IFactory {
        IProduct produce();
    }
    
    //工厂实现
    static class CarFactory implements IFactory {
        @Override public IProduct produce() {
            System.out.println("Car is producing......");
            return new Car();
        }
    }
    static class BusFactory implements IFactory {
        @Override public IProduct produce() {
            System.out.println("Bus is producing......");
            return new Bus();
        }
    }
    //产品实现
    static class Car implements IProduct {
        @Override public void use() {
            System.out.println("Car is using......");
        }
    }
    static class Bus implements IProduct {
        @Override public void use() {
            System.out.println("Bus is using......");
        }
    }
}
