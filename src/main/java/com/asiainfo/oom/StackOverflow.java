package com.asiainfo.oom;

/**
 * @Described：栈溢出测试StackOverflowError
 * @VM args:-Xss128k
 * @VM args:-Xss1024k
 * @VM args:-verbose:gc -XX:+PrintGCDetails
 * 
 */
public class StackOverflow {

	private static int count = 0;

    public static void main(String[] args) {
        recursiveInvoke();
    }

    public static void recursiveInvoke() {
    	count++;
    	System.out.println(count);
        recursiveInvoke();
    }
}
