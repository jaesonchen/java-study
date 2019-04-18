package com.asiainfo.designpattern.structure;

/**
 * 
 * 在不影响目标类其他对象的情况下，以动态、透明的方式给单个对象添加职责，处理那些可以撤消的职责。
 * 多个Decorator和目标类必须实现的是同一个接口或者基类。
 * java io 流转换操作使用的是Decorator模式。
 * 
 * Facade模式注重简化接口，Adapter模式注重转换接口，Bridge模式注重分离接口（抽象）与其实现，Decorator模式注重稳定接口的前提下为对象扩展功能。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:29:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Decorator {

    public static void main(String[] args) {

        IReader reader = new FileReader();
        IReader upper = new UpperReader(new BufferedReader(reader));
        IReader lower = new LowerReader(new BufferedReader(reader));
        String str = null;
        while (true) {
            str = reader.readLine();
            if ("eof".equals(str)) {
                break;
            }
            System.out.println(str);
            
            str = upper.readLine();
            if ("eof".equals(str)) {
                break;
            }
            System.out.println(str);
            
            str = lower.readLine();
            if ("eof".equals(str)) {
                break;
            }
            System.out.println(str);
        }
        System.out.println("eof");
    }

    interface IReader {
        String readLine();
    }
    //目标实现
    static class FileReader implements IReader {

        int index;
        @Override
        public String readLine() {
            return index >= 25 ? "eof" : new String("Line " + (++index));
        }
    }
    
    //装饰类
    static class BufferedReader implements IReader {

        String[] buffer;
        int index;
        int max;
        boolean eof;
        IReader reader;
        public BufferedReader(IReader reader) {
            this.reader = reader;
            this.buffer = new String[8];
            this.index = 0;
            this.max = 0;
            this.eof = false;
        }
        
        @Override
        public String readLine() {
            
            // no buffer
            if (this.index == this.max && !this.eof) {
                System.out.println("read max " + this.buffer.length + " line for buffer!");
                this.index = 0;
                this.max = 0;
                for (int i = 0; i < this.buffer.length; i++) {
                    String str = this.reader.readLine();
                    if ("eof".equals(str)) {
                        this.eof = true;
                        break;
                    }
                    this.buffer[i] = str;
                    this.max++;
                }
            }
            return this.index == this.max && this.eof ? "eof" : this.buffer[this.index++];
        }
    }
    
    //装饰类
    static class UpperReader implements IReader {

        IReader reader;
        public UpperReader(IReader reader) {
            this.reader = reader;
        }
        
        @Override
        public String readLine() {
            String str = this.reader.readLine();
            return "eof".equals(str) ? "eof" : (null == str ? null : str.toUpperCase());
        }
    }
    
    //装饰类
    static class LowerReader implements IReader {

        IReader reader;
        public LowerReader(IReader reader) {
            this.reader = reader;
        }
        
        @Override
        public String readLine() {
            String str = this.reader.readLine();
            return "eof".equals(str) ? "eof" : (null == str ? null : str.toLowerCase());
        }
    }
}
