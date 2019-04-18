package com.asiainfo.designpattern.structure;

/**
 * 为一个复杂子系统提供一个简单接口时，由于子系统往往因为不断演化而变得越来越复杂，但这种变化不应该影响到客户的调用，此时使用 Facade 模式对外提供一个访问的接口。
 * 
 * Facade模式注重简化接口，Adapter模式注重转换接口，Bridge模式注重分离接口（抽象）与其实现，Decorator模式注重稳定接口的前提下为对象扩展功能。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:21:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Facade {

    public static void main(String[] args) {
        
        Facade facade = new Facade();
        facade.startup();
        System.out.println("=============================");
        facade.shutdown(); 
    }

    private CPU cpu;
    private Memory memory;
    private Disk disk;

    public Facade() {
        cpu = new CPU();
        memory = new Memory();
        disk = new Disk();
    }
    
    public void startup() {
        
        System.out.println("start computer!");
        cpu.startup();
        memory.startup();
        disk.startup();
        System.out.println("start computer finished!");
    }

    public void shutdown() {
        
        System.out.println("shutdown computer!");
        disk.shutdown();
        memory.shutdown();
        cpu.shutdown();
        System.out.println("computer shutdown finished!");
    }
    
    static class CPU {
        public void startup() {
            System.out.println("cpu startup!");
        }
        public void shutdown() {
            System.out.println("cpu shutdown!");
        }
    }
    static class Memory {
        public void startup() {
            System.out.println("memory startup!");
        }
        public void shutdown() {
            System.out.println("memory shutdown!");
        }
    }
    static class Disk {
        public void startup() {
            System.out.println("disk startup!");
        }
        public void shutdown() {
            System.out.println("disk shutdown!");
        }
    }
}