package com.asiainfo.concurrent;

/**   
 * @Description: System.out.println 方法是synchronized修饰的，在测试volatile时如果使用该方法打印信息会导致测试结果不准确
 * 
 * @author chenzq  
 * @date 2019年8月25日 下午6:58:05
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Test {

    static boolean flag = true;
    static long count = 0;
    public static void main(String[] args) throws Exception {
        
        Thread t1 = new Thread(() -> {
            while (flag) {
                count++;
            }
            System.out.println("t1 end");
        });
        t1.start();
        Thread.sleep(100);
        Thread t2 = new Thread(() -> {
            flag = false;
            System.out.println("t2 end");
        });
        t2.start();
        
        t1.join();
        System.out.println(count);
    }
}
