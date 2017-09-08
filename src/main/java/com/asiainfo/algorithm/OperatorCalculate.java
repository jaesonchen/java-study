package com.asiainfo.algorithm;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class OperatorCalculate {

	public static void main(String[] args) {
		
		String exp = "1 + 100.2 + 2 * 4 + 3 * (4 - 3) / 2";
		System.out.println(calculate(exp));
	}
	
	public static double calculate(String express) {
		
		Map<String, Operator> operatorMap = getOperator();
		Stack numStack = new StackArray();
		Stack operStack = new StackArray();
		StringTokenizer token = new StringTokenizer(express, "+-*/()", true); 
		
		String current;
		while (token.hasMoreTokens()) {
			
			current = token.nextToken().trim();
			if ("".equals(current))
				continue;
			
			if (isNumber(current)) {
				
				numStack.push(Double.valueOf(current));
			} else {
				Operator oper = operatorMap.get(current);
				//运算符+-*/
				if (oper != null) {
					
					while (!operStack.isEmpty() && ((Operator)operStack.peek()).priority() >= oper.priority()) {
						calculate(numStack, operStack);
					}
					
					operStack.push(oper);
				} else {
					//括号()
					if ("(".equals(current)) {
						operStack.push(Operator.LEFT_BRACKET);
					} else if (")".equals(current)) {
						
						while (!"(".equals(((Operator)operStack.peek()).getSymbol())) {
							calculate(numStack, operStack);
						}
						
						operStack.pop();
					} else {
						
						throw new RuntimeException("运算表达式包含[+-*/()]之外的非法运算字符");
					}
				}
			}
		}
		
		while (!operStack.isEmpty()) {
			calculate(numStack, operStack);
		}
		
		return (Double) numStack.pop();
	}
	
	public static boolean isNumber(String str) {
		//数字的正则表达式 
		String numberRegex = "^\\d+(\\.\\d+)?$";
		return Pattern.matches(numberRegex, str);
	}
	
	public static Map<String, Operator> getOperator() {
		return new HashMap<String, Operator>() {
			private static final long serialVersionUID = 7706718608122369958L;
			{
				put("+", Operator.PLUS);
				put("-", Operator.MINUS);
				put("*", Operator.MULTIPLY);
				put("/", Operator.DIVIDE);
			}
		};
	}
	
	/**
	 * 取numStack的最顶上两个数字，operStack的最顶上一个运算符进行运算，然后把运算结果再放到numStack的最顶端 
	 * @param numStack
	 * @param operStack
	 */
	public static void calculate(Stack numStack, Stack operStack) {
		
		// 弹出数字栈最顶上的数字作为运算的第二个数字 
		Double num2 = (Double) numStack.pop();
		// 弹出数字栈最顶上的数字作为运算的第一个数字
		Double num1 = (Double) numStack.pop();
		// 弹出操作栈最顶上的运算符进行计算
		Double result = ((Operator)operStack.pop()).calculate(num1, num2);
		// 把计算结果重新放到队列的末端 
		numStack.push(result);
	}


}

enum Operator {
	PLUS("+") {
		
		@Override
		public int priority() { return 1; }

		@Override
		public double calculate(double x, double y) { return x + y; }
	},
	MINUS("-") {
		
		@Override
		public int priority() { return 1; }
		
		@Override
		public double calculate(double x, double y) { return x - y; }
	},
	MULTIPLY("*") {
		
		@Override
		public int priority() { return 2; }
		
		@Override
		public double calculate(double x, double y) { return x * y; }
	},
	DIVIDE("/") {
		
		@Override
		public int priority() { return 2; }
		
		@Override
		public double calculate(double x, double y) { return x / y; }
	},
	LEFT_BRACKET("(") {
		@Override
		public int priority() { return 0; }
		
		@Override
		public double calculate(double x, double y) { return Double.NaN; }
	},
	RIGHT_BRACKET(")") {
		@Override
		public int priority() { return 0; }
		
		@Override
		public double calculate(double x, double y) { return Double.NaN; }
	};
	
	private final String symbol;
	private Operator(String symbol) {
		this.symbol = symbol;
	}
	public String getSymbol() {
		return this.symbol;
	}
	
	@Override
	public String toString() { return this.symbol; }
	
	public abstract int priority();
	public abstract double calculate(double x, double y);
}