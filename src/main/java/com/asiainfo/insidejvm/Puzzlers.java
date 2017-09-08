package com.asiainfo.insidejvm;

import java.io.*;
import java.util.*;
import java.math.*;

@SuppressWarnings("all")
public class Puzzlers {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		//在需要精确答案的地方，要避免使用float 和double。
		//并不是所有的小数都可以用二进制浮点数来精确表示的。
		//当一个小数不能用二进制表示时，它被表示成为最接近它的double 值。
		//对于货币计算，要放大使用int、long 或BigDecimal。
		//一定要用BigDecimal(String)构造器，而千万不要用BigDecimal(double)。
		//后一个构造器将用它的参数的“精确”值来创建一个实例。
		System.out.println(2.00 - 1.10);	//0.8999999999999999
		
		//当你在操作很大的数字时，千万要提防溢出——它可是一个缄	默杀手。
		//即使用来保存结果的变量已显得足够大，也并不意味着要产生结果的计
		//算具有正确的类型。当你拿不准时，就使用long 运算来执行整个计算。
		//在运算时使用的是int，在将int结果赋值给long之前，溢出部分被截取，导致结果错误。
		//正确的做法: final long MICROS_PER_DAY = 24L * 60 * 60 * 1000 * 1000;
		final long MICROS_PER_DAY = 24 * 60 * 60 * 1000 * 1000;
		final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
		System.out.println(MICROS_PER_DAY/MILLIS_PER_DAY);		//5
		
		//在long 型字面常量中，一定要用大写的L，千万不要用小写的l。
		//要避免使用单独的一个l 字母作为变量名。
		//数字1 的水平笔划和垂直笔划之间是一个锐角，而与此相对照的是，小写字母l的是一个直角。
		//正确的做法：System.out.println(12345+5432L);
		System.out.println(12345+5432l);	//17777
		
		//混合类型的计算可能会产生混淆，尤其是十六进制和八进制字面常量无需显式的减号符号就可以表示负的数值。
		//为了避免这种窘境，通常最好是避免混合类型的计算。
		//负的十进制常数可以很明确地用一个减号符号来标识。但是十六进制和八进制字面常量并不是这么回事，它们可以具有正的以及负的数值。
		//如果十六进制和八进制字面常量的最高位被置位了，那么它们就是负数。
		System.out.println(Long.toHexString(0x100000000L + 0xcafebabe));	//cafebabe
		
		//通常最好是在条件表达式中使用类型相同的第二和第三操作数。
		//确定条件表达式结果类型的规则:
		//如果第二个和第三个操作数具有相同的类型，那么它就是条件表达式的类型。
		//如果一个操作数的类型是T，T 表示byte、short 或char，而另一个操作数是一个int 类型的常量表达式，
		//它的值是可以用类型T 表示的，那么条件表达式的类型就是T。
		//否则，将对操作数类型运用二进制数字提升，而条件表达式的类型就是第二个和第三个操作数被提升之后的类型。
		char x = 'X';
		int i = 0;
		System.out.println(true ? 88 : x);		//X
		System.out.println(true ? x : 65535);	//X
		System.out.println(true ? x : 65536);	//88
		System.out.println(false ? i : x);		//88
		System.out.println(true ? x : i);		//88
		
		//复合赋值表达式自动地将它们所执行的计算的结果转型为其左侧变量的类型。
		//请不要将复合赋值操作符作用于byte、short 或char 类型的变量上。
		//如果结果的类型比该变量的类型要宽，那么复合赋值操作符将悄悄地执行	一个窄化原始类型转换。
		//复合赋值 E1 op= E2 等价于简单赋值E1 = (T)((E1)op(E2))，其中T 是E1 的类型。
		int i1 = 123456;
		short s = 0;
		s += i1;
		System.out.println(s);		//-7616
		
		//使用字符串连接操作符使用格外小心。+ 操作符当且仅当它的操作数中至
		//少有一个是String 类型时，才会执行字符串连接操作；否则，它执行的就是加法。
		//编译器在计算常量表达式'H'+'a'时，是通过我们熟知的拓宽原始类型转换将两
		//个具有字符型数值的操作数（'H'和'a'）提升为int 数值而实现的。
		System.out.println('H' + 'a');	//169
		System.out.println("2 + 2 = " + 2 + 2);	//2 + 2 = 22
		
		//char 数组不是字符串。要想将一个char 数组转换成一个字符串，就要调用String.valueOf(char[])方法。
		//否则进行+字符串连接操作时会调用Object.toString()。
		System.out.println(new char[] { '1', '2', '3' });			//123
		System.out.println("" + new char[] { '1', '2', '3' });		//[C@191c0b76
		
		//String类型的编译期常量是内存限定的。
		//任何两个String	类型的常量表达式，如果标明的是相同的字符序列，那么它们就用相同的对象引用来表示。
		//+ 操作符，不论是用作加法还是字符串连接操作，它都比 == 操作符的优先级高。
		final String pig = "length: 10";
		final String dog = "length: " + pig.length();
		System.out. println("Animals are equal: " + pig == dog);	//false
		//相当于执行下面的语句：
		System.out. println(("Animals are equal: " + pig) == dog);
		System.out.println("Animals are equal: " + (pig == dog));	//Animals are equal: false
		//正确的做法：
		System.out.println("Animals are equal: " + pig.equals(dog));	//Animals are equal: true
		
		//在字符串和字符字面常量中要优先选择转义字符序列，而不是Unicode 转义字符。
		//除非确实是必需的，否则就不要使用Unicode 转义字符。
		//Java 对在字符串字面常量中的Unicode 转义字	符没有提供任何特殊处理。
		//编译器在将程序解析成各种符号之前，先将Unicode转义字符转换成为它们所表示的字符。
		// \u0022 是双引号的Unicode 转义字符
		System.out.println("a\u0022.length()+\u0022b".length());	//2
		//可以使用转义字符序列来实现: 转义字符序列是一个反斜杠后面紧跟着一个需要转义的字符
		System.out.println("a\".length()+\"b".length());			//14
		
		/**
		 * c:\u2222test
		 */
		//（\ u）表示的是一个Unicode 转义字符的开始。这些字符后面必须紧跟四个十六进制的数字。
		//否则编译器被要求拒绝该程序。Unicode 转义字符必须是良构的，即使是出现在注释中也是如此。
		//要确保字符\ u 不出现在一个合法的Unicode 转义字符上下文之外，
		//即使是在注释中也是如此。在机器生成的代码中要特别注意此问题。
		
		//Unicode 转义字符只有在你要向程序中插入用其他任何方式都
		//无法表示的字符时才是必需的，除此之外的任何情况都不应该避免使用它们。
		//下面Unicode代码相当于：System.out.println("Hello w"+"orld");
		//输出Hello world
		\u0053\u0079\u0073\u0074\u0065\u006d\u002e\u006f\u0075\u0074
		\u002e\u0070\u0072\u0069\u006e\u0074\u006c\u006e\u0028\u0020
		\u0022\u0048\u0065\u006c\u006c\u006f\u0020\u0077\u0022\u002b
		\u0022\u006f\u0072\u006c\u0064\u0022\u0029\u003b
		
		
		//每当你要将一个byte 序列转换成一个String 时，你都在使用某一个字符集，不管你是否显式地指定了它。
		//如果你想让你的程序的行为是可预知的，那么就请你在每次使用字符集时都明确地指定。
		byte bytes[] = new byte[256];
		for (int j = 0; j < 256; j++)
			bytes[j] = (byte) j;
		try {
			//不指定字符集时使用的是平台的缺省字符集，输出是不可预知的
			System.out.println(new String(bytes));
			//正确的用法
			System.out.println(new String(bytes, "ISO-8859-1"));
		} catch (UnsupportedEncodingException ex) {}
		
		
		//String.replaceAll 接受了一个正则表达式作为它的第一个参数，而并非接受了一个字符序列字面常量。
		//正则表达式“.”可以匹配任何单个的字符，要想只匹配句点符号，在正则表达式中的句点必须在其前面添加
		//一个反斜杠（\）进行转义。
		System.out.println(Puzzlers.class.getName().replaceAll(".", "/") + ".class");
		//输出为：/////////////////////////////////////.class
		System.out.println(Puzzlers.class.getName().replaceAll("\\.", "/") + ".class");
		//输出为：com/jaeson/javastudy/puzzler/Puzzlers.class
		
		//在替代字符串中出现的反斜杠会把紧随其后的字符进行转义，从而导致其被按字面含义而处理了。
		//Exception:String index out of range
		try {
			System.out.println(Puzzlers.class.getName().replaceAll("\\.", File.separator) + ".class");
		} catch (StringIndexOutOfBoundsException ex) {
			System.out.println("StringIndexOutOfBoundsException: " + ex.getMessage());
		}
		//使用1.5新的replace方法：它将模式和替代物都当作字面含义的字符串处理。
		System.out.println(Puzzlers.class.getName().replace(".", File.separator) + ".class");
		
		//令人误解的注释和无关的代码会引起混乱。
		//你可以在任何语句前面放置标号(除了注释，语句不能跟标号在同一行)。
		//这个语句标注了一个表达式语句，它是合法的，但是却没什么用处。
		http://www.google.com;
		System.out.println("url的愚弄");
		
		//不管在什么时候，都要尽可能使用熟悉的惯用法和API。
		//一个 char 不是一个 String，而是更像一个 int。
		//Random.nextInt(int)的规范描述道：“返回一个伪随机的、均等地分布在从0
		//（包括）到指定的数值（不包括）之间的一个int 数值”[Java-API]。
		//3个bug：分支2永远不会到达；没有break，总是最后的default为最后内容；
		//StringBuffer没有char参数构造器，new StringBuffer('M'); 调用的是设置缓冲区初始容量的int型构造器。
		Random rnd = new Random();
		StringBuffer word = null;
		switch(rnd.nextInt(2)) {
			case 1: word = new StringBuffer('P');
			case 2: word = new StringBuffer('G');
			default: word = new StringBuffer('M');
		}
		word.append("a");
		word.append('i');
		word.append('n');
		System.out.println(word);	//输出总是ain
		//正确和更优雅的解决办法：
		System.out.println("PGM".charAt(rnd.nextInt(3)) + "ain");
		
		//byte short char 类型在使用int常量赋值时,如果常量值超出类型表示范围需要强制类型转换。否则编译错误。
		//编译错误: int常量128超过byte的表示范围，丢失精度出错。   	byte bValue = 128;
		//编译错误：int常量2147483648超过int的表示范围，越界出错。  	long lValue = 2147483648;
		byte bValue = 127;
		long lValue = 2147483648L;
		
		//原码的得来：(负数的原码，直接把对应正数的最高位改为1)， 原码能够直观的表示一个负数(能直观的把真值显示出来，-1的原码10000001)。
		//补码的设计目的是:使符号位能与有效值部分一起参加运算，从而简化运算规则。使减法运算转换为加法运算。
		//java中byte short int long都是有符号整数，char是无符号整数
		//有符号整数的最高位为符号位：0表示整数、1表示负数。
		//当把二进制转换为十进制时，正整数直接计算结果。 -5 = 原码(10000101) = 反码(01111010)
		//负数的十进制结果为：负数的二进制码取反(^) + 1得到无符号二进制表示，计算无符号二进制的十进制结果，然后在结果前加负号(-)。
		//十进制负数转化为二进制表示：去除负号，将无符号十进制数转为无符号二进制数表示，对二进制码取反(^) + 1得到有符号二进制表示。
		//byte的表示范围为：-128 to 127，其中负数范围-128 to -1, 整数范围0 to 127，各代表2的n-1次方个数。
		//-128的二进制码为10000000，^(10000000) = 01111111，01111111 + 1 = 10000000（无符号二进制），十进制结果为128加负号 = -128。
		//java中的减法转化为加法：5 - 3 实际执行的是 5 + (-3)，00000101 + 11111101 = 00000010 (高位溢出) = 2
		
		//表达式计算顺序都是从左到右
		System.out.println(3 + 5 + " hello");	//8 hello
		System.out.println("hello " + 3 + 5);	//hello 35
		int a = 3;
		int b[] = new int[5];
		b[a] = a = 6;	//相当于b[3] = 6;
		for(i = 0; i < b.length; i++) {
			System.out.println(b[i]);
		}
		a = 6;
		a += (a = 9);
		System.out.println(a);		//15
		
		a = 6;
		a = a + (a = 9);
		System.out.println(a);		//15
		
		a = 6;
		int c = (a = 3) * a;
		System.out.println(c);		//9

		
		//由于系统总是强制地将一个操作数提升到与另一个操作数相匹配的类型，所以混合类型比较总是容易把人搞糊涂。
		//这种转换是不可视的，而且可能不会产生你所期望的结果。
		for (byte d = Byte.MIN_VALUE; d < Byte.MAX_VALUE; d++) {
			if (d == 0x90)
				System.out.print("Joy!");
		}
		//因为byte 是一个有符号类型，所以这个转换执行的是符号扩展，将负的byte 数值提升为了在数字上相等的int数值。
		//正数的符号扩展使用0填充高位，负数的符号扩展使用1填充高位。
		//byte b = (byte)144; b == 144; -> 11111111111111111111111110010000(负数符号扩展) -> -(11111111111111111111111110010000^ + 1) = -(00000000000000000000000001110000) = -112
		//(byte)0x90 -> (byte)00000000000000000000000010010000 -> 10010000(byte负数) -> -(10010000^ + 1) = -(01110000) = -112
		System.out.println((byte)0x90);		//-112
		
		//不要在单个的表达式中对相同的变量赋值超过一次。
		int j = 0;
		for (i = 0; i < 100; i++)
			j = j++;
		System.out.println(j);		//0
		
		//所有的int 变量都是小于或等于Integer.MAX_VALUE 的。因为它被定义为所有int 数值中的最大值。
		//int 不能表示所有的整数。无论你在何时使用了一个整数类型，都要意识到其边界条件。
		//如果其数值下溢或是上溢了，通常最好是使用一个取值范围更大的类型。（整数类型包括byte、char、short、int 和long。）
		//当i 达到Integer.MAX_VALUE，并且再次被执行增量操作时，它就又绕回到了Integer.MIN_VALUE。
		//for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++)
		//正确的做法：for (long i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++)
		
		
		//移位操作符：<<、>>和>>>。移位长度总是介于0 到31 之间，如果左操作数是long 类型的，则介于0 到63 之间。
		//这个移位长度总是对32取余得到，如果左操作数是long 类型的，则对64 取余。
		//如果试图对一个int 数值移位32 位，或者是对一个long 数值移位64 位，都只能返回这个数值自身的值。
		//没有任何移位长度可以让一个int 数值丢弃其所有的32 位，或者是让一个long数值丢弃其所有的64 位。
		System.out.println(100<<32);	//100
		System.out.println(100<<65);	//200
		
		//任何浮点操作，只要它的一个或多个操作数为NaN，那么其结果为NaN。
		System.out.println(Double.NaN != Double.NaN);		//true
		System.out.println(Double.NaN - Double.NaN == 0);	//false
		
		//复合赋值操作符包括*=、/=、%=、+=、-=、<<=、>>=、>>>=、	&=、^=和|= ，在运算时提升到int，
		//赋值时它们可能会自动地执行窄化原始类型转换。
		//死循环：(short)-1 = 0xffff -> 执行符号扩展0xffffffff -> 无符号右移 1位，高位0填充 -> 0x7fffffff -> (short)0x7fffffff -> (short)0xffff = (short)-1
		//short i = -1;
		//while (i != 0) {
		//	i >>>= 1;
		//}
		
		//Java 使用2 的补码的算术运算，它是非对称的。对于每一种有符号的整数类型（int、long、byte 和short），
		//负的数值总是比正的数值多一个，这个	多出来的值总是这种类型所能表示的最小数值。
		//-128 = 10000000(原码=补码), -(-128) -> -(10000000) -> 10000000^ + 1 -> 10000000 = -128
		i = Integer.MIN_VALUE;
		System.out.println(i != 0 && i == -i);		//true
		
		//在5.0 版中，自动包装（autoboxing）和自动反包装（auto-unboxing）被添加到了Java 语言中。
		//（被包装的数字类型有：Byte、Character、Short、Integer、Long、Float 和Double。）
		//Java 的判等操作符（==和!=）在作用于对象引用时，执行的是引用ID 的比较，而不是值的比较。这是为了兼容5.0以前版本。
		//判等操作符在其两个操作数中只有一个是被包装的数字类型，而另一个是原始类型时，执行的确实是数值比较。这种比较在5.0以前是非法的，所以不会出现不兼容。
		//数值比较操作符（>、 >=、 <、 <=）：
		//当两个操作数都是被包装的数字类型时，数值比较操作符和判等操作符的行为存在着根本的差异：
		//数值比较操作符执行的是值比较，而判等操作符执行的是引用标识的比较。
		Integer obj1 = new Integer(0);
		Integer obj2 = new Integer(0);
		System.out.println(obj1 <= obj2 && obj1 >= obj2 && obj1 != obj2);	//true
		
		//会导致精度丢失的三种拓宽原始类型转换:从int 到float、从long 到float、从long 到double。
		//丢失原因：毗邻的浮点数值之间的距离被称为一个ulp，它是“最小单位（unit in the last place）”的首字母缩写词。
		//当float和double足够大时，将一个小于最小单位ulp的浮点数加到一个很大的浮点数上时，将不会改变大的浮点数的值。
		System.out.println((float)2000000000 ==	(float)2000000050);			//true
		
		//在一个try-finally 语句中，finally 语句块总是在控制权离开try 语句块时执行的。
		System.out.println(decision());		//false
		
		//一个方法可以抛出的被检查异常集合是它所适用的所有类型声明要抛出的被检查异常集合的交集，而不是合集。
		Type3 tp = new Type3();
		tp.f();
		
		//重载方法/构造器调用：总是匹配拥有更精确的参数类型的方法和构造器
		new Confusing(null);			//double array
		new Confusing((Object) null);	//Object
		
		//要避免隐藏静态方法
		Dog woofer = new Dog();
		Dog nipper = new Basenji();
		Basenji basen = null;
		woofer.bark();
		nipper.bark();
		((Dog) null).bark();
		((Dog) basen).bark();	//woof woof woof woof 
		
		
		//递归的初始化尝试会直接被忽略掉
		Evis.instance.show();
		System.out.println(Evis.START);
		System.out.println(Evis.start);
		System.out.println(new Evis()._START);
		System.out.println(new Evis()._start);
		
		// instanceof运算符：
		//尽管null 对于每一个引用类型来说都是其子类型，但是instanceof 操作符被定义为在其左操作数为null
		//时返回false。这被证明是实践中非常有用的行为。如果instanceof 告诉你一个对象引用是某个特定类
		//型的实例，那么你就可以将其转型为该类型，并调用该类型的方法，而不用担心会抛出
		//ClassCastException 或NullPointerException 异常。
		//instanceof 操作符有这样的要求：如果两个操作数的类型都是类，其中一个必须是另一个的子类型。
		//所以，new Puzzlers() instanceof String是非法的。
		String str = null;
		System.out.println(str instanceof String);  
		//编译错误：  
		//System.out.println(new ArrayList() instanceof String);  
		//编译通过，但运行期会抛出异常ClassCastException 
		try {
			Puzzlers tc = (Puzzlers) new Object();
		} catch (Exception ex){	
			System.out.println(ex.getMessage());
		}

		//一个本地变量声明看起来像是一条语句，但是从技术上说，它不是；它应该是一
		//个本地变量声明语句（local variable declaration statement）。
		//Java 语言规范不允许一个本地变量声明语句作为一条语句在for、while 或do
		//循环中重复执行。一个本地变量声明作为一条语句只能直接出现在一个语句块中。
		//（一个语句块是由一对花括号以及包含在这对花括展中的语句和声明构成的。）
		//编译错误：
		//for (i = 0; i < 100; i++)
		//	Object obj = new Object();
		//正确的做法
		for (i = 0; i < 100; i++) {
			Object obj = new Object();
		}
		//更好的做法
		for (i = 0; i < 100; i++)
			new Object();
		
		
		
	}
	//无论你何时使用到了取余操作符，都要考虑到操作数和结果的符号。
	//错误的方法，在i为负数时总是返回false
	public static boolean isOdd(int i){
		return i % 2 == 1;
	}
	//正确的方法
	public static boolean isOddCorrect(int i){
		return i % 2 != 0;
	}
	
	//在一个try-finally 语句中，finally 语句块总是在控制权离开try 语句块时执行的。
	//当try 语句块和finally 语句块都意外结束时，在try 语句块中引发意外结束的原因将被丢弃，
	//而整个try-finally 语句意外结束的原因将于finally 语句块意外结束的原因相同。
	//总之，每一个finally 语句块都应该正常结束，除非抛出的是不受检查的异常。
	//千万不要用一个return、break、continue 或throw 来退出一个finally 语句块，
	//并且千万不要允许将一个受检查的异常传播到一个finally 语句块之外去。
	//如果一个catch 子句要捕获一个类型为E 的被检查异常，而其相对应的try 子句不能抛出E 的某种子类型的异常，那么这就是一个编译期错误。
	//但是捕获Exception 或Throwble 的catch 子句是合法的，不管与其相对应的try 子句的内容为何。
	//System.exit 将立即停止所有的程序线程，它并不会使finally 语句块得到调用，但是它在停止VM 之前会执行关闭挂钩操作。
	@SuppressWarnings("all")
	static boolean decision() {
		try {
			return true;
		} finally {
			return false;
		}
	}
	
	//当你处理IO流，在finally 语句块中调用close 方法时，要用一个嵌套的try-catch语句来保护它，以防止IOException 的传播。
	static void copy(String src, String dest) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int n;
			while ((n = in.read(buf)) > 0)
				out.write(buf, 0, n);
		} finally {
			//in可能抛出异常导致out无法关闭。
			//if (in != null) in.close();
			//if (out != null) out.close();
			closeIgnoringException(in);
			closeIgnoringException(out);
		}
	}
	private static void closeIgnoringException(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException ex) {
				// There is nothing we can do if close fails
			}
		}
	}
	
	//在程序中，一个空final 域只有在它是明确未赋过值的地方才可以被赋值。
	static void finalInit() {
		final int fint;
		try {
			fint = finalInitHelper();
		} catch (Exception e) {
			//编译错误：final 变量可能已经被赋值
			//fint = 100;
		}
	}
	private static int finalInitHelper() throws Exception {
		throw new Exception();
	}
	
	//不要对捕获NoClassDefFoundError 形成依赖。语言规范非常仔细地描述	了类初始化是在何时发生的[JLS 12.4.1]，
	//但是类被加载的时机却显得更加不可预测。更一般地讲，捕获Error 及其子类型几乎是完全不恰当的。
	//这些异常是为那些不能被恢复的错误而保留的。
	static void noClassDefine() {
		/*
		Missing m;
		try {
			m = new Missing();
		} catch (NoClassDefFoundError ex) {
			System.out.println("Got it!");
		}
		*/
		try {
			Object m = Class.forName("Missing").newInstance();
		} catch (ClassNotFoundException ex) {
			System.err.println("Got it!");
		} catch (Exception ex) { }
	}
	
}
//一个方法可以抛出的被检查异常集合是它所适用的所有类型声明要抛出的被检查异常集合的交集，而不是合集。
//多个继承而来的throws 子句的交集，将减少而不是增加方法允许抛出的异常数量。
class Type3 implements Type1, Type2 {
	public void f() {
		System.out.println("do not need throws any Exception");
	}
}
interface Type1 {
	void f() throws CloneNotSupportedException;
}
interface Type2 {
	void f() throws InterruptedException;
}
//实例初始化操作是先于构造器的程序体而运行的。实例初始化操作抛出的任何异常都会传播给构造器。
//如果初始化操作抛出的是被检查异常，那么构造器必须声明也会抛出这些异常，但是应该避免这样做，因为它会造成混乱。
//最后，对于我们所设计的类，如果其实例包含同样属于这个类的其他实例，那么对这种无限递归要格外当心。
//有可能会因为递归调用而抛出StackOverflowError，它是Error 的子类型而不是Exception 的子类型，所以catch 子句无法捕获它。
@SuppressWarnings("unused")
class Car {
	private static Class<Engine> engineClass = Engine.class;
	private Engine engine = newEngine();
	private static Engine newEngine() {
		try {
			return (Engine)engineClass.newInstance();
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (InstantiationException e) {
			throw new AssertionError(e);
		}
	}
	public Car() { }
}
class Engine { }

//Java 的重载解析过程是以两阶段运行的。
//第一阶段选取所有可获得并且可应用的方法或构造器。
//第二阶段在第一阶段选取的方法或构造器中选取最精确的一个。
//如果一个方法或构造器可以接受传递给另一个方法或构造器的任何参数，那么我们就说第一个方法比第二个方法缺乏精确性。
//一旦编译器确定了哪些重载版本是可获得且可应用的，它就会选择最精确的一个重载版本。
//如果你确实进行了重载，那么请确保所有的重载版本所接受的参数类型都互不兼容，
//这样，任何两个重载版本都不会同时是可应用的。
class Confusing {
	Confusing(Object o) {
		System.out.println("Object");
	}
	Confusing(double[] dArray) {
		System.out.println("double array");
	}
}

//对静态方法的调用不存在任何动态的分派机制。
//当一个程序调用了一个静态方法时，要被调用的方法都是在编译时刻被选定的，
//而这种选定是基于修饰符的编译期类型而做出的，修饰符的编译期类型就是我们给出的方法调用表达式中圆点左边部分的名字。
//要用类名来修饰静态方法的调用，或者当你在静态方法所属的类中去调用它们时，压根不去修饰这些方法，但是千万不要用一个表达式去修饰它们。
//还有就是要避免隐藏静态方法。
class Dog {
	public static void bark() {
		System.out.print("woof ");
	}
}
class Basenji extends Dog {
	public static void bark() { }
}

//递归的初始化尝试会直接被忽略掉
//类加载和实例初始化时，分配空间并设置缺省值时，会将final变量中使用常量或常量表达式赋值的变量直接设置为常量值而不是缺省值。
//类加载初始化时：
//static final 类型的域在未初始化之前被引用，只有在其初始化表达式是常量或者常量表达式时才是常量值，否则为缺省值。
//static 类型的域在未初始化之前被引用，为缺省值。
//通常在声明之前直接引用会引发编译错误，可以用 ClassName.引用。
//类实例初始化时：
//final 类型的实例域在未初始化之前被引用，只有在其初始化表达式是常量或常量表达式时才是常量值，否则为缺省值。
//普通类型的实例域在未初始化之前被引用，为缺省值。
//通常在声明之前直接引用会引发编译错误，可以用this.引用。
class Evis {
	static Evis instance = new Evis();
	static final String START = new String("START") + " " + Evis.END;
	static final String END = "END";
	static String start = new String("start") + " " + Evis.end;
	static String end = "end";
	final String _START = new String("_START") + " " + this._END;
	final String _END = "_END";
	String _start = new String("_start") + " " + this._end;
	String _end = "_end";
	String MESSAGE;
	String message;
	String _MESSAGE;
	String _message;

	public Evis() {
		
		MESSAGE = "START = " + START + ", END = "  + END;
		this.message = "start = " + start + ", end = "  + end;
		_MESSAGE = "_START = " + _START + ", _END = "  + _END;
		this._message = "_start = " + _start + ", _end = "  + _end;
	}
	public void show() {
		System.out.println();
		System.out.println(this.MESSAGE);
		System.out.println(this.message);
		System.out.println(this._MESSAGE);
		System.out.println(this._message);
	}
}
