package com.asiainfo.algorithm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * slopeone数据文件模拟
 * 
 * @author       zq
 * @date         2018年1月8日  下午3:06:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SlopeOneTest {

    public static void main(String[] args) throws Exception {
        
        Map<String, Double> map = new HashMap<>();
        long item = 10001L;
        Random random = new Random();
        for (int i = 1; i < 10000; i++) {
            for (int j = 0; j < 10; j++) {
                map.put("user" + i + "_" + (item + random.nextInt(10)), (double) (1 + random.nextInt(50)) / 10);
            }
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/java/com/asiainfo/algorithm/slopeone.data")));
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String[] keys = entry.getKey().split("_");
            writer.write(keys[0] + "\t" + keys[1] + "\t" + entry.getValue().doubleValue());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
