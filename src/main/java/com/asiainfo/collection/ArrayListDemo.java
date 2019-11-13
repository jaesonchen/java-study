package com.asiainfo.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月18日 下午2:32:23
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ArrayListDemo {
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        
        List<Integer> arrayList = new ArrayList<Integer>();
        System.out.printf("Before add: arrayList.size() = %d\n", arrayList.size());
        arrayList.add(1);
        arrayList.add(3);
        arrayList.add(5);
        arrayList.add(7);
        arrayList.add(9);
        System.out.printf("After add: arrayList.size() = %d\n", arrayList.size());

        System.out.println("Printing elements of arrayList");
        // 遍历方式
        // 第一种：通过迭代器遍历
        System.out.print("通过迭代器遍历：");
        Iterator<Integer> it = arrayList.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // 第二种：通过索引值遍历
        System.out.print("通过索引值遍历：");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i) + " ");
        }
        System.out.println();

        // 第三种：for循环遍历
        System.out.print("for循环遍历：");
        for (Integer number : arrayList) {
            System.out.print(number + " ");
        }
        System.out.println();

        // toArray用法
        // 第一种方式：返回新数组(最常用)
        Integer[] integer = arrayList.toArray(new Integer[0]);

        // 第二种方式：指定数组(容易理解)
        Integer[] integer1 = new Integer[arrayList.size()];
        arrayList.toArray(integer1);

        // 第三种方式：转为Object[]类型
        Object[] integer2 = arrayList.toArray();

        // 在指定位置添加元素
        arrayList.add(2,2);
        // 删除指定位置上的元素
        arrayList.remove(2);
        // 删除指定元素
        arrayList.remove((Object) 3);
        // 判断arrayList是否元素
        System.out.println("ArrayList contains 5 is: " + arrayList.contains(5));

        // 清空ArrayList
        arrayList.clear();
        // 判断ArrayList是否为空
        System.out.println("ArrayList is empty: " + arrayList.isEmpty());
    }
}
