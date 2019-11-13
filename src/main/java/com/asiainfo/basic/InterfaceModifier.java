package com.asiainfo.basic;

/**   
 * java interface modifier
 * 
 * @author chenzq  
 * @date 2019年4月30日 下午5:30:15
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class InterfaceModifier {
    
    // 接口作为类内部的成员时可以拥有与field一样的modifier，public/protected/private/abstract/static (final除外)
    protected static abstract interface InnerInterface {}
}

// 外部接口只能是public / abstract
interface OuterInterface {
    // 接口内的field 默认是public static final
    int i1 = 1;
    public final int i2 = 2;
    static final int i3 = 3;
    
    // 接口内的method，要么是public / abstract，要么是拥有实现体的public / default / static
    void m1();
    public abstract void m2();
    // static method 只能用public来修饰
    static void m3() {}
    public static void m4() {}
    // default method 只能用public来修饰
    default void m6() {}
    public default void m7() {}
}