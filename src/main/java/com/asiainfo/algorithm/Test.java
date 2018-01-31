package com.asiainfo.algorithm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年1月8日  下午3:06:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Test {

    /** 
     * TODO
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/java/com/asiainfo/algorithm/slopeone.data")));
        long item = 10001L;
        Random random = new Random();
        for (int i = 1; i < 1000; i++) {
            for (int j = 0; j < 10; j++) {
                writer.write("user" + i + "\t" + (item + random.nextInt(10)) + "\t" + (double) (1 + random.nextInt(50)) / 10);
                writer.newLine();
            }
        }
        writer.flush();
        writer.close();
    }
}
