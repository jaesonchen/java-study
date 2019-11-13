package com.asiainfo.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月19日 下午3:50:32
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class TreeMapDemo {

    public static void main(String[] args) {
        
        TreeMap<String, Integer> treeMap = new TreeMap<>();

        // 新增元素
        treeMap.put("hello", 1);
        treeMap.put("world", 2);
        treeMap.put("my", 3);
        treeMap.put("name", 4);
        treeMap.put("is", 5);
        treeMap.put("jiaboyan", 6);
        treeMap.put("i", 6);
        treeMap.put("am", 6);
        treeMap.put("a", 6);
        treeMap.put("developer", 6);
        System.out.println("treeMap.size()：" + treeMap.size());

        // 遍历元素
        Set<Map.Entry<String, Integer>> entrys = treeMap.entrySet();
        for (Map.Entry<String, Integer> entry : entrys) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        // 获取所有的key
        Set<String> keys = treeMap.keySet();
        for (String key : keys) {
            System.out.println("treeMap.keySet(): " + key);
        }

        // 获取所有的value
        Collection<Integer> values = treeMap.values();
        for (Integer value : values) {
            System.out.println("treeMap.values(): " + value);
        }

        // 读取value
        System.out.println("treeMap.get(\"jiaboyan\") = " + treeMap.get("jiaboyan"));
        System.out.println("treeMap.firstKey() = " + treeMap.firstKey());
        System.out.println("treeMap.lastKey() = " + treeMap.lastKey());
        System.out.println("treeMap.lowerKey(\"jiaboyan\") = " + treeMap.lowerKey("jiaboyan"));
        System.out.println("treeMap.ceilingKey(\"jiaboyan\") = " + treeMap.ceilingKey("jiaboyan"));
        System.out.println("treeMap.subMap(\"a\", \"my\") = " + treeMap.subMap("a", "my"));
        
        // containsKey
        System.out.println("treeMap.containsKey(\"jiaboyan\") = " + treeMap.containsKey("jiaboyan"));
        
        // 删除元素
        System.out.println("treeMap.remove(\"jiaboyan\") = " + treeMap.remove("jiaboyan"));

        // 清空集合
        treeMap.clear();

        // 判断方法
        System.out.println("treeMap.isEmpty() = " + treeMap.isEmpty());
        
    }
}
