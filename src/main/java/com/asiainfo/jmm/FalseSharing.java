package com.asiainfo.jmm;

/**   
 * @Description: 伪共享和字节填充
 * 
 * @author chenzq  
 * @date 2019年6月12日 下午4:49:55
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class FalseSharing implements Runnable {

    public static int NUM_THREADS = 4;
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    
    private final int arrayIndex;
    private static VolatileLong[] longs;
    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }
    
    public static void main(final String[] args) throws Exception {
        
        System.out.println("starting....");
        
        longs = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
        
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }
    
    private static void runTest() throws InterruptedException {
        
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
    }

    @Override
    public void run() {
        
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }
    
    public static class VolatileLongPadding {
        public long p1, p2, p3, p4, p5, p6; // 填充
    }
    //@sun.misc.Contended
    //public final static class VolatileLong { // 打开注释以测试没有填充的性能
    public final static class VolatileLong extends VolatileLongPadding {
        public volatile long value = 0L;
    }
}
