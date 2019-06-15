package com.asiainfo.insidejvm;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @Description: 自定义sizeOf方法，开启/关闭 压缩指针时，引用的大小不一致
 * @VM args: -javaagent:SizeOf.jar -XX:+UseCompressedOops
 * 
 * @author chenzq  
 * @date 2019年3月23日 下午2:13:25
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class MySizeOf {

    static Instrumentation inst;

    // 通过 -javaagent:xxx.jar 注入代理对象
    public static void premain(String agent, Instrumentation inst) {
        MySizeOf.inst = inst;
    }

    /**
     * @Description: 计算当前对象占用空间，不包括引用对象大小
     * @author chenzq
     * @date 2019年3月23日 下午2:30:25
     * @param o
     * @return
     */
    public static long sizeOf(Object o) {
       if(inst == null) {
          throw new IllegalStateException("Can not access instrumentation environment.\n" +
             "Please check if jar file containing SizeOfAgent class is \n" +
             "specified in the java's \"-javaagent\" command line argument.");
       }
       return inst.getObjectSize(o);
    }

   /**
    * @Description: 递归计算当前对象占用空间总大小，包括当前类和超类的字段大小以及字段引用对象大小
    * @throws IllegalAccessException 
    * @throws IllegalArgumentException 
    */
    public static long fullSizeOf(Object obj) throws IllegalArgumentException, IllegalAccessException {
       Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
       Stack<Object> stack = new Stack<Object>();
       long result = internalSizeOf(obj, stack, visited);
       while (!stack.isEmpty()) {//通过栈进行遍历  
          result += internalSizeOf(stack.pop(), stack, visited);
       }
       visited.clear();
       return result;  
    }
    
    //字符串池对象是需要跳过的
    private static boolean skipObject(Object obj, Map<Object, Object> visited) {
       if (obj instanceof String && obj == ((String) obj).intern()) {
          return true;
       }
       return (obj == null) || visited.containsKey(obj);
    }

    // 计算对象本身大小，把引用对象放入stack
    private static long internalSizeOf(Object obj, Stack<Object> stack, Map<Object, Object> visited) throws IllegalArgumentException, IllegalAccessException {
        
        //跳过常量池对象、跳过已经访问过的对象
        if (skipObject(obj, visited)) { 
           return 0;
       }
       //将当前对象放入已访问map中 
       visited.put(obj, null);
       long size = 0;
       //对象本身大小
       size += sizeOf(obj);
       Class<?> clazz = obj.getClass();
       // 数组 
       if (clazz.isArray()) {
           if (clazz.getName().length() != 2) {// skip primitive type array  [I , [F 基本类型名字长度是2
              int length =  Array.getLength(obj);
              for (int i = 0; i < length; i++) {
                 stack.add(Array.get(obj, i));
              }
           }
       // 遍历所有字段
       } else {
           while (clazz != null) {
               Field[] fields = clazz.getDeclaredFields();
               for (Field field : fields) {
                   if (Modifier.isStatic(field.getModifiers())    //静态不计 
                           || field.getType().isPrimitive()) {    //基本类型不重复计 （因为基本关键字在调用java默认提供的方法就已经计算过了）
                       continue;
                   }
                   field.setAccessible(true);
                   Object fieldValue = field.get(obj);
                   if (fieldValue == null) {
                       continue;
                   }
                   stack.add(fieldValue);
               }
               clazz = clazz.getSuperclass();
           }
       }
       return size;
   }
}