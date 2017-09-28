package com.asiainfo.basic;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月18日  下午12:26:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class EnumExample {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		for (WeekDay day : WeekDay.values()) {
            System.out.println(day.name() + " " + day.getChnName());
        }
		
		double x = 1.11;
		double y = 2.01;
		for (Operation op : Operation.values()) {
			System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
		}
		System.out.println(Operation.valueOf("MINUS").name());
		System.out.println(Operation.valueOf("MINUS").getSymbol());
		System.out.println(Operation.valueOf("MINUS").ordinal());
		
		printName(WeekDay.FRIDAY);
	}
	
	public static void printName(WeekDay day) {
		
		switch (day) {
        	case MONDAY: //无需使用WeekDay进行引用
        		System.out.println(day.getChnName());
        		break;
            case TUESDAY:
            	System.out.println(day.getChnName());
                break;
            case WEDNESDAY:
            	System.out.println(day.getChnName());
                break;
            case FRIDAY:
            	 System.out.println(day.getChnName());
                 break;
            case SATURDAY:
            	System.out.println(day.getChnName());
            	break;
            case SUNDAY:
            	System.out.println(day.getChnName());
            	break;
            default:
            	System.out.println("error day!");
            	break;
        }
    }
}

enum WeekDay {
	
    MONDAY("星期一"), 
    TUESDAY("星期二"), 
    WEDNESDAY("星期三"), 
    THURSDAY("星期四"), 
    FRIDAY("星期五"), 
    SATURDAY("星期六"), 
    SUNDAY("星期日");

    private String chnName;

    /**
     * 私有构造,防止被外部调用
     * @param desc
     */
    private WeekDay(String chnName) {
        this.chnName = chnName;
    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     * @return
     */
    public String getChnName(){
        return chnName;
    }
}

/*final class com.asiainfo.reflect.WeekDay extends java.lang.Enum<com.asiainfo.reflect.WeekDay> {
	
  public static final com.asiainfo.reflect.WeekDay MONDAY;
  public static final com.asiainfo.reflect.WeekDay TUESDAY;
  public static final com.asiainfo.reflect.WeekDay WEDNESDAY;
  public static final com.asiainfo.reflect.WeekDay THURSDAY;
  public static final com.asiainfo.reflect.WeekDay FRIDAY;
  public static final com.asiainfo.reflect.WeekDay SATURDAY;
  public static final com.asiainfo.reflect.WeekDay SUNDAY;
  static {};
  public java.lang.String getChnName();
  public static com.asiainfo.reflect.WeekDay[] values();
  public static com.asiainfo.reflect.WeekDay valueOf(java.lang.String);
}*/

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

interface Food {
	
	enum Appetizer implements Food {
	    SALAD, SOUP, SPRING_ROLLS;
	}
	
	enum Coffee implements Food {
	    BLACK_COFFEE, ESPRESSO, LATTE, TEA;
	}
}
