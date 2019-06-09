package com.asiainfo.jmm;

/**   
 * CacheLine 命中，符合空间局部性、时间局部性的算法能增加CacheLine的命中，提供程序性能
 * @sun.misc.Contended
 * 
 * @author chenzq  
 * @date 2019年6月9日 下午10:15:11
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class CacheLine {

    static int[][] arr = new int[1024 * 1024][8];
    public static void main(String[] args) {
        
        long start = System.currentTimeMillis();
        int total = 0;
        for (int i = 0; i < 1024 * 1024; i++) {
            for (int j = 0; j < 8; j++) {
                total += arr[i][j];
            }
        }
        System.out.println("time=" + (System.currentTimeMillis() - start) + ", total=" + total);
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 1024 * 1024; j++) {
                total += arr[j][i];
            }
        }
        System.out.println("time=" + (System.currentTimeMillis() - start) + ", total=" + total);
    }
}
