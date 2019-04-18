package com.asiainfo.designpattern.builder;

/**
 * SimpleFactory缺点: 每当增加一种产品，工厂类的方法中就要多一种if选择情况，如果在产品数量非常多的情况下，这无疑让整个类变得非常臃肿。
 * 
 * 
 * @author       zq
 * @date         2018年4月26日  下午5:02:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SimpleFactory {
    
    public static void main(String[] args) {

        FruitFactory factory = new FruitFactory();
        Fruit fruit = factory.newInstance("apple");
        System.out.println(fruit.getName());
        
        fruit = factory.newInstance("banana");
        System.out.println(fruit.getName());
        
        fruit = factory.newInstance("unknown");
    }
    
    interface Fruit {
        String getName();
    }
    static class Apple implements Fruit {
        @Override
        public String getName() {
            return "apple";
        }
    }
    static class Banana implements Fruit {
        @Override
        public String getName() {
            return "banana";
        }
    }
    
    // simple factory
    static class FruitFactory {
        
        public Fruit newInstance(String name) {
            
            if ("apple".equals(name)) {
                return new Apple();
            } else if ("banana".equals(name)) {
                return new Banana();
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
