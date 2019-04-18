package com.asiainfo.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.asiainfo.datastructure.ArrayStack;
import com.asiainfo.datastructure.Stack;

/**
 * 应用Stack进行算术运算及Spring SpEL支持
 * 
 * @author       zq
 * @date         2018年1月4日  下午3:57:08
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExpressionCalculate {
    
    public static void main(String[] args) {
        
        String exp = "1 + 100.2 + 2 * 4 + 3 * (4 - 3) / 2";
        System.out.println(calculate(exp).getValue());
        
        // spel
        spel();
    }
    
    // spel 计算表达式时
    public static void spel() {
        ExpressionParser ep = new SpelExpressionParser();
        // 算术运算
        Expression exp = ep.parseExpression("1 + 100.2 + 2 * 4 + 3 * (4 - 3) / 2");
        System.out.println(exp.getValue(Double.class));
        // 关系运算
        exp = ep.parseExpression("1 < 2");
        System.out.println(exp.getValue(Boolean.class));
        // 变量与赋值
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("name", "jaeson");
        System.out.println(ep.parseExpression("#name").getValue(ctx));
    }
    
    /**
     * 计算表达式
     * 
     * @param express
     * @return
     */
    public static OperatorNumber<?> calculate(String express) {
        
        Map<String, Operator> operatorMap = supportOperator();
        Stack<OperatorNumber<?>> numStack = new ArrayStack<>();
        Stack<Operator> operStack = new ArrayStack<>();
        StringTokenizer token = new StringTokenizer(express, "+-*/()", true);
        String current;
        while (token.hasMoreTokens()) {
            current = token.nextToken().trim();
            if ("".equals(current)) {
                continue;
            }
            if (isInteger(current)) {
                numStack.push(new OperatorNumber<Integer>(Integer.valueOf(current)));
            } else if (isDouble(current)) {
                numStack.push(new OperatorNumber<Double>(Double.valueOf(current)));
            } else {
                Operator oper = operatorMap.get(current);
                //运算符+-*/
                if (oper != null) {
                    while (!operStack.isEmpty() && ((Operator) operStack.peek()).priority() >= oper.priority()) {
                        calculate(numStack, operStack);
                    }
                    operStack.push(oper);
                } else {
                    //括号()
                    if ("(".equals(current)) {
                        operStack.push(Operator.LEFT_BRACKET);
                    } else if (")".equals(current)) {
                        while (!"(".equals(((Operator) operStack.peek()).getSymbol())) {
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
        return (OperatorNumber<?>) numStack.pop();
    }
    
    /**
     * 判断是否double
     * 
     * @param str
     * @return
     */
    protected static boolean isDouble(String str) {
        //数字的正则表达式 
        String numberRegex = "^\\d+(\\.\\d+)?$";
        return Pattern.matches(numberRegex, str);
    }
    
    /**
     * 判断是否int
     * @author chenzq
     * @date 2019年4月8日 下午4:26:19
     * @param str
     * @return
     */
    protected static boolean isInteger(String str) {
        //数字的正则表达式 
        String numberRegex = "^\\d+$";
        return Pattern.matches(numberRegex, str);
    }
    
    /**
     * 支持的运算符
     * 
     * @return
     */
    protected static Map<String, Operator> supportOperator() {
        
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
    protected static void calculate(Stack<OperatorNumber<?>> numStack, Stack<Operator> operStack) {
        
        // 弹出数字栈最顶上的数字作为运算的第二个数字 
        OperatorNumber<?> num2 = numStack.pop();
        // 弹出数字栈最顶上的数字作为运算的第一个数字
        OperatorNumber<?> num1 = numStack.pop();
        OperatorNumber<?> result = null;
        // 弹出操作栈最顶上的运算符进行计算
        if (num1.isInteger() && num2.isInteger()) {
            result = new OperatorNumber<Integer>(((Operator) operStack.pop()).calculate(num1.getValue().intValue(), num2.getValue().intValue()));
        } else {
            result = new OperatorNumber<Double>(((Operator) operStack.pop()).calculate(num1.getValue().doubleValue(), num2.getValue().doubleValue()));
        }
        // 把计算结果重新放到队列的末端 
        numStack.push(result);
    }
}

class OperatorNumber<T extends Number> {
    T value;
    public OperatorNumber(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }
    public boolean isInteger() {
        return value instanceof Integer;
    }
}

enum Operator {
    
	PLUS("+") {
		@Override
		public int priority() { return 1; }
		@Override
		public double calculate(double x, double y) { return x + y; }
        @Override
        public int calculate(int x, int y) { return x + y; }
	},
	MINUS("-") {
		@Override
		public int priority() { return 1; }
		@Override
		public double calculate(double x, double y) { return x - y; }
		@Override
        public int calculate(int x, int y) { return x - y; }
	},
	MULTIPLY("*") {
		@Override
		public int priority() { return 2; }
		@Override
		public double calculate(double x, double y) { return x * y; }
		@Override
        public int calculate(int x, int y) { return x * y; }
	},
	DIVIDE("/") {
		@Override
		public int priority() { return 2; }
		@Override
		public double calculate(double x, double y) { return x / y; }
		@Override
        public int calculate(int x, int y) { return x / y; }
	},
	LEFT_BRACKET("(") {
		@Override
		public int priority() { return 0; }
		@Override
		public double calculate(double x, double y) { throw new UnsupportedOperationException(); }
		@Override
        public int calculate(int x, int y) { throw new UnsupportedOperationException(); }
	},
	RIGHT_BRACKET(")") {
		@Override
		public int priority() { return 0; }
		@Override
		public double calculate(double x, double y) { throw new UnsupportedOperationException(); }
		@Override
        public int calculate(int x, int y) { throw new UnsupportedOperationException(); }
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
	public abstract int calculate(int x, int y);
	public abstract double calculate(double x, double y);
}