package com.asiainfo.designpattern.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 通过对象组合的方式，Bridge模式把两个角色之间的继承关系改为了耦合的关系，从而使这两者可以从容自若的各自独立的变化，这也是Bridge模式的本意。
 * bridge是在设计之初的模式，adapter是针对已有系统的代码。
 * jdbc api使用bridge模式，将jdbc转换为odbc再调用native方法。
 * 
 * Facade模式注重简化接口，Adapter模式注重转换接口，Bridge模式注重分离接口（抽象）与其实现，Decorator模式注重稳定接口的前提下为对象扩展功能。
 * 
 * public interface Implementor {
 *      public void operationImpl();
 * }
 * class ConcreteImplementorA implements Implementor {
 *      @Override
 *      public void operationImpl() {
 *          // 真正的实现
 *          System.out.println("具体实现A");
 *      }
 * }
 * class ConcreteImplementorB implements Implementor {
 *      @Override
 *      public void operationImpl() {
 *          // 真正的实现
 *          System.out.println("具体实现B");
 *      }
 * }
 * public abstract class Abstraction {
 *      protected Implementor implementor;
 *      public Abstraction(Implementor implementor) {
 *          this.implementor = implementor;
 *      }
 *      public abstract void operation();
 * }
 * class RefinedAbstraction extends Abstraction {
 *
 *      public RefinedAbstraction(Implementor implementor) {
 *          super(implementor);
 *      }
 *      @Override
 *      public void operation() {
 *          //do someting 
 *          super.implementor.operationImpl();
 *          //do other thing
 *      }
 * }
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:29:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Bridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bridge.class);
    
    public static void main(String[] args) {

        LOGGER.info("The knight receives an enchanted sword.");
        Sword enchantedSword = new Sword(new SoulEatingEnchantment());
        enchantedSword.wield();
        enchantedSword.swing();
        enchantedSword.unwield();

        LOGGER.info("The valkyrie receives an enchanted hammer.");
        Hammer hammer = new Hammer(new FlyingEnchantment());
        hammer.wield();
        hammer.swing();
        hammer.unwield();
    }
    
    // 主体对象
    interface Weapon {
        void wield();
        void swing();
        void unwield();
        Enchantment getEnchantment();
    }
    // 桥接对象
    interface Enchantment {
        void onActivate();
        void apply();
        void onDeactivate();
    }
    
    // 具体实现：组合代替继承
    static class Sword implements Weapon {

        final Logger logger = LoggerFactory.getLogger(getClass());
        
        private Enchantment enchantment;
        public Sword(Enchantment enchantment) {
          this.enchantment = enchantment;
        }
        
        @Override
        public void wield() {
            logger.info("The sword is wielded.");
            enchantment.onActivate();
        }
        @Override
        public void swing() {
            logger.info("The sword is swinged.");
            enchantment.apply();
        }
        @Override
        public void unwield() {
            logger.info("The sword is unwielded.");
            enchantment.onDeactivate();
        }
        @Override
        public Enchantment getEnchantment() {
            return enchantment;
        }
    }
    // 具体实现：组合代替继承
    static class Hammer implements Weapon {

        final Logger logger = LoggerFactory.getLogger(getClass());
        private Enchantment enchantment;
        public Hammer(Enchantment enchantment) {
          this.enchantment = enchantment;
        }
        @Override
        public void wield() {
            logger.info("The hammer is wielded.");
            enchantment.onActivate();
        }
        @Override
        public void swing() {
            logger.info("The hammer is swinged.");
            enchantment.apply();
        }
        @Override
        public void unwield() {
            logger.info("The hammer is unwielded.");
            enchantment.onDeactivate();
        }
        @Override
        public Enchantment getEnchantment() {
            return enchantment;
        }
    }
    // 桥接对象
    static class SoulEatingEnchantment implements Enchantment {

        final Logger logger = LoggerFactory.getLogger(getClass());
        
        @Override
        public void onActivate() {
            logger.info("开始杀戮.");
        }
        @Override
        public void apply() {
            logger.info("这个魔法吞噬敌人的灵魂.");
        }
        @Override
        public void onDeactivate() {
            logger.info("嗜血慢慢消失.");
        }
    }
    // 桥接对象
    static class FlyingEnchantment implements Enchantment {

        final Logger logger = LoggerFactory.getLogger(getClass());
        
        @Override
        public void onActivate() {
            logger.info("开始淡淡地发光.");
        }
        @Override
        public void apply() {
            logger.info("飞到敌人身上，最后回到主人的手上.");
        }
        @Override
        public void onDeactivate() {
            logger.info("光芒渐渐消失.");
        }
    }
}
