package com.asiainfo.designpattern.structure;

/**
 * 
 * 将一个类的接口转换成客户希望的另外一个接口。Adapter 模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * java io 的字节/字符流 转换使用了adapter模式，InputStreamReader、OutputStreamWriter
 * 
 * 类适配器：Adapter extends Adaptee implements IService
 * 使用场景：当你想使用一个已经存在的类，而它的接口不符合你的需求。
 * 
 * 对象适配器（持有Adaptee对象）：Adapter implements IService
 * 使用场景：你想使用一些已经存在的子类，但是不可能对每一个都进行子类化以匹配它们的接口，对象适配器可以适配它的父亲接口（Adaptee）。
 * 
 * Facade模式注重简化接口，Adapter模式注重转换接口，Bridge模式注重分离接口（抽象）与其实现，Decorator模式注重稳定接口的前提下为对象扩展功能。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:29:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Adapter {

    public static void main(String[] args) {

        IReader objAdapter = new InputStreamReader(new FileInputStream());
        System.out.println(objAdapter.readLine());
        
        objAdapter = new InputStreamReader(new ByteArrayInputStream());
        System.out.println(objAdapter.readLine());
        
        IReader classAdater = new InputStreamAdapter();
        System.out.println(classAdater.readLine());
    }

    interface IInputStream {
        public byte[] read();
    }
    
    interface IReader {
        public String readLine();
    }
    
    //类适配器
    static class InputStreamAdapter extends FileInputStream implements IReader {
        
        public InputStreamAdapter() {
            super();
        }
        @Override public String readLine() {
            
            byte[] buff = super.read();
            return new String(buff);
        }
    }
    
    //对象适配器
    static class InputStreamReader implements IReader {
        
        private IInputStream input;
        public InputStreamReader(IInputStream input) {
            this.input = input;
        }
        
        @Override public String readLine() {
            
            byte[] buff = this.input.read();
            return new String(buff);
        }
    }
    
    //字节流
    static class ByteArrayInputStream implements IInputStream {
        
        @Override public byte[] read() {
            
            byte[] result = new byte[128];
            for(int i = 0, j = result.length; i < j; i++) {
                result[i] = (byte) i;
            }
            return result;
        }
    }
    
    //字节流
    static class FileInputStream implements IInputStream {
        
        @Override public byte[] read() {
            
            byte[] result = new byte[128];
            for(int i = 0, j = result.length; i < j; i++) {
                result[i] = (byte) (i + 32);
            }
            return result;
        }
    }
}
