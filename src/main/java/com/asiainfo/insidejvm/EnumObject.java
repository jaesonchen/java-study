package com.asiainfo.insidejvm;

/**
 * 用enum代替int常量：
 * 
 * enum 枚举类型是java1.5引入的新的引用类型。enum本质上是int的值。
 * 经过编译器编译之后产生的是一个 class 文件。该 class 文件经过反编译可以看到实际上是生成了一个类，该类继承了 java.lang.Enum<E>. 
 * 每个枚举项为该枚举类型的一个实例。
 * public final class EnumClass extends java.lang.Enum {
 * 		public static final EnumClass INSTANCE;
 * }
 * 枚举类型是真正的final，客户端不能创建枚举类型的实例，也不能对它进行扩展。
 * 除了完善int枚举模式的不足之处，枚举类型还允许添加任意的方法和域，并实现任意的接口。提供了所有Object方法的高级实现。
 * 为了将数据与枚举常量关联起来，得声明实例field，并编写一个带有参数，并将参数保存在实例field中的构造器。
 * 枚举天生就是不可变的，因此所有的field都应该为final的。
 * ************************************************************
 * 用实例field代替序数：
 * 
 * 所有枚举都有一个ordinal方法，它返回每个枚举常量在枚举类型中的数字位置，常量数组中的下标。
 * ordinal设计用于EnumSet等基于枚举的通用数据结构，除非编写的是这种数据结构，否则应该完全避免使用ordinal。
 * ************************************************************
 * 
 */
public class EnumObject {

	public static void main(String[] args) {
		
		double x = 1.11;
		double y = 2.01;
		for (Operation op : Operation.values())
			System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
		
		System.out.println(Operation.valueOf("MINUS").getSymbol());
		System.out.println(Operation.valueOf("DIVIDE").ordinal());
	}
}

enum Operation {
	PLUS("+") {
		public double apply(double x, double y) { return x + y; }
	},
	MINUS("-") {
		public double apply(double x, double y) { return x - y; }
	},
	TIMES("*") {
		public double apply(double x, double y) { return x * y; }
	},
	DIVIDE("/") {
		public double apply(double x, double y) { return x / y; }
	};
	
	private final String symbol;
	private Operation(String symbol) {
		this.symbol = symbol;
	}
	public String getSymbol() {
		return this.symbol;
	}
	@Override public String toString() { return this.symbol; }
	
	public abstract double apply(double x, double y);
}