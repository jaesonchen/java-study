package com.asiainfo.designpattern.builder;

/**
 * AbstractFactory
 * 抽象工厂模式实际上是工厂方法的一种更加泛化的情况。在抽象工厂模式中，抽象产品可能是一个或多个，从而构成一个或多个产品族。
 * 在只有一个产品族的情况下，抽象工厂模式就是工厂方法模式。
 * 
 * @author       zq
 * @date         2018年4月25日  下午10:45:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AbstractFactory {
    
    public static void main(String[] args) {

        IFactory foreign = new ForeignFactory();
        foreign.createCar().drive();
        foreign.createFruit().eat();
        
        IFactory national = new NationalFactory();
        national.createCar().drive();
        national.createFruit().eat();
    }

    interface Car {
        void drive();
    }
    interface Fruit {
        void eat();
    }
    interface IFactory {
        Car createCar();
        Fruit createFruit();
    }
    
    //工厂实现
    static class ForeignFactory implements IFactory {
        @Override
        public Car createCar() {
            return new Benz();
        }
        @Override
        public Fruit createFruit() {
            return new Banana();
        }
    }
    static class NationalFactory implements IFactory {
        @Override
        public Car createCar() {
            return new Hongqi();
        }
        @Override
        public Fruit createFruit() {
            return new Apple();
        }
    }
    //产品实现
    static class Benz implements Car {
        @Override public void drive() {
            System.out.println("benz is driving......");
        }
    }
    static class Hongqi implements Car {
        @Override public void drive() {
            System.out.println("hongqi is driving......");
        }
    }
    static class Banana implements Fruit {
        @Override
        public void eat() {
            System.out.println("Banana from USA.");
        }
    }
    static class Apple implements Fruit {
        @Override
        public void eat() {
            System.out.println("Apple from national.");
        }
    }
}
