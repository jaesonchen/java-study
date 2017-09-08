package com.asiainfo.insidejvm.oom;

public class StackOverflow {

	private static int count = 0;
    /**
     * @Described：栈溢出测试StackOverflowError
     * @VM args:-Xss128k
     * @VM args:-Xss1024k
     * @VM args:-verbose:gc -XX:+PrintGCDetails
     */  
    public static void main(String[] args) {
    	
        //recursiveInvoke();
    	recursiveInvoke(1, 1);

    }
    
    public static void recursiveInvoke(int param1, int param2) {
    	int local1 = param1 + 1;
    	int local2 = param2 + 2;
    	count++;
    	System.out.println(count);
        recursiveInvoke(local1, local2);
    }
    
    public static void recursiveInvoke() {
    	count++;
    	System.out.println(count);
        recursiveInvoke();
    }
}
