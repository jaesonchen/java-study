/**
 * 
 */
package com.asiainfo.insidejvm;

/**
 * @author zq
 * Java规范有这样的规则：
 * 1.高位转低位需要强制转换  
 * 2.低位转高位自动转. 
 * short s1 = 1; short s2 = 2; s1 = s1 + s2;有什么错? 通过 + 运算后s1 自动转为int 型，无法赋值给short类型的s1，编译不通过。
 * (x = x + i)表达式使用的是简单赋值操作符(=)，而(x += i)表达式使用的是复合赋值操作符。
 * Java语言规范规定，复合赋值(E1 op= E2)等价于简单赋值(E1 = (T) ((E1) op (E2)))，其中T是E1的类型。
 * 复合赋值表达式自动地将所执行计算的结果转型为其左侧变量的类型。
 */
public class AutoTypeConversion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		short s1 = 1;
		
		s1 = (short) (s1 + 1);		//简单类型  
		//s1 = s1 + 1;				//编译错误，无法将int结果自动转换为short
		short s2 = 1; 
		s2 += 1;					//复合类型,复合赋值操作符+=,   
		System.out.println(s1 + ";" + s2);
	}
}

