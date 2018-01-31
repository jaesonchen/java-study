package com.asiainfo.algorithm;

/**
 * Sunday算法是从前往后匹配，在匹配失败时关注的是文本串中参加匹配的最末位字符的下一位字符。
 * 如果该字符没有在模式串中出现则直接跳过，即移动位数 = 匹配串长度 + 1；
 * 否则，其移动位数 = 模式串中最右端的该字符到末尾的距离+1。
 * 
 * @author       zq
 * @date         2018年1月5日  下午5:49:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SundayStringMatch {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
