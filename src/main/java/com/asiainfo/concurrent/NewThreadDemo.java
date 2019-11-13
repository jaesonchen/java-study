package com.asiainfo.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**   
 * @Description: 新建线程的3种方式
 * 
 * @author chenzq  
 * @date 2019年6月7日 下午7:21:43
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class NewThreadDemo {


    public static void main(String[] args) throws Exception {
        Runnable r = () -> { System.out.println("Runnable"); };
        new Thread(r).start();
        
        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread");
            }
        };
        t.start();
        
        Callable<Integer> c = () -> { return 1; };
        FutureTask<Integer> ft = new FutureTask<>(c);
        new Thread(ft).start();
        System.out.println("Callable, Future.get()=" + ft.get());
        System.exit(0);
    }

}
