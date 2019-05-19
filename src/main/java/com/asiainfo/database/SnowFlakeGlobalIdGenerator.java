package com.asiainfo.database;

/**   
 * SnowFlake global id 生成算法
 * 
 * @author chenzq  
 * @date 2019年5月19日 上午9:11:13
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class SnowFlakeGlobalIdGenerator {

    private static final long WORKID_BITS = 5L;
    private static final long DATACENTERID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKERID = 1L << WORKID_BITS;
    private static final long MAX_DATACENTERID = 1L << DATACENTERID_BITS;
    
    private long workerIdShift = SEQUENCE_BITS;
    private long datacenterIdShift = SEQUENCE_BITS + WORKID_BITS;
    private long timestampLeftShift = SEQUENCE_BITS + WORKID_BITS + DATACENTERID_BITS;
    
    private long twepoch = 1288834974657L;
    private long lastTimestamp = -1L;
    private long sequenceMask = -1L ^ (-1L << 12);

    private long workerId;
    private long datacenterId;
    private long sequence;
    
    public SnowFlakeGlobalIdGenerator(long workerId, long datacenterId, long sequence) {
        
        if (workerId > MAX_WORKERID || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", MAX_WORKERID));
        }
        if (datacenterId > MAX_DATACENTERID || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTERID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }
    
    public synchronized long nextId() {
        
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // 一个毫秒内最多只能有4096个数字
            // 无论传递多少进来，这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // 记录最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 将时间戳左移，放到 41 bit
        // 将机房 id左移放到 5 bit
        // 将机器id左移放到5 bit; 将序号放最后12 bit
        // 最后拼接起来成一个 64 bit的二进制数字，转换成 10 进制就是个 long 型
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
    
    public static void main(String[] args) {
        SnowFlakeGlobalIdGenerator generator = new SnowFlakeGlobalIdGenerator(1, 1, 1);
        for (int i = 0; i < 30; i++) {
            System.out.println(generator.nextId());
        }
    }
}
