package com.asiainfo.limiting;

/**   
 * @Description: 滑动时间窗口计数限流
 * 
 * @author chenzq  
 * @date 2019年11月12日 下午8:36:25
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class RollingWindow {

    // 时间窗口数
    private final int UNIT = 10;
    // 限流
    private final int LIMIT = 100;
    // 时间窗口长度
    private final long INTERVAL = 6000L;
    // 限流长度
    private final long TOTAL_INTERVAL = UNIT * INTERVAL;
    
    // 起始时间窗口坐标
    private int start;
    // 当前时间窗口坐标
    private int index;
    // 时间窗口数组
    private long[] timestamp;
    // 计数窗口数组
    private int[] reqeust;
    
    public RollingWindow() {
        start = index = 0;
        timestamp = new long[UNIT + 1];
        reqeust = new int[UNIT + 1];
        timestamp[index] = System.currentTimeMillis();
        reqeust[index] = 0;
    }
    
    // 时间窗口计数限流
    public boolean grant() {
        long now = System.currentTimeMillis();
        if ((now - timestamp[start]) > TOTAL_INTERVAL 
                || (now - timestamp[index] > INTERVAL)) {
            rolling();
        }
        int count = countRequest();
        if (count < LIMIT) {
            reqeust[index]++;
            return true;
        }
        return false;
    }
    
    // 滑动时间窗口，无效单元值0
    private void rolling() {
        long now = System.currentTimeMillis();
        // 滑动当前时间窗口
        index = range(++index);
        timestamp[index] = now;
        reqeust[index] = 0;
        // 滑动起始时间窗口
        int i = start;
        while ((now - timestamp[i]) > TOTAL_INTERVAL) {
            timestamp[i] = 0;
            reqeust[i] = 0;
            i = range(++i);
        }
        // 重置起始坐标
        start = i;
    }
    
    // 坐标范围
    private int range(int index) {
        return index < UNIT ? index : 0; 
    }
    
    
    // 计算当前请求总数
    private int countRequest() {
        int total = 0;
        for (int i : reqeust) {
            total += i;
        }
        return total;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
