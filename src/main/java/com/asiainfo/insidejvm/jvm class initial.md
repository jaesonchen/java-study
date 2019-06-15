# 类的初始化
类的初始化阶段是类加载过程的最后一步，在准备阶段，类变量已赋过一次默认值；而在初始化阶段，则是根据通过程序制定的主观计划去初始化类变量和其他资源，从代码角度：初始化阶段是JVM执行类构造器<clinit>()方法的过程。
    
- <clinit>()方法是由编译器自动收集类中的所有静态变量的赋值动作和静态语句块(static{}块)中的语句合并产生的, 编译器收集的顺序是由语句在源文件中出现的顺序所决定的, 静态语句块中只能访问定义在静态语句块之前的变量, 定义在它之后的変量, 在前面的静态语句块可以赋值, 但是不能访问。

- <clinit>()方法与类实例的构造函数 (<init>()方法)不同，它不需要显式地调用父类构造器, JVM会保证在子类的<clinit>()方法执行之前, 父类的<clinit>()方法已经执行完毕, 因此在JVM中第一个被执行的<clinit>()方法的类肯定是 java.lang.Object。

- 由于父类的<clinit>()方法先执行，也就意味着父类中定义的静态语句块要优先于子类的变量赋值操作。

- <clinit>()方法对于类或接口来说并不是必须的, 如果一个类中没有静态语句块, 也没有对变量的赋值动作, 那么编译器可以不为这个类生成<clinit>()方法。

- 接口中不能使用静态语句块，但仍然有变量初始化的赋值操作（实际上是static final修饰的常量）, 因此接口与类一样都会生成<clinit>()方法。 但接口与类不同的是, 执行接口的<clinit>()方法不需要先执行父接口的<clinit>()方法。只有当父接口中定义的变量被使用时, 父接口才会被初始化。 另外, 接口的实现类在初始化时也一样不会执行接口的<clinit>()方法。

- 虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确地加锁和同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的<clinit>()法,其他线程部需要阻塞等待，直到活动线程执行<clinit>()方法完毕。Holder单例模式使用的就是该特性。
    

## 初始化的时机
虚拟机规范严格规定了有且只有四种情况必须立即对类进行初始化：

- 遇到new、getstatic、putstatic、invokestatic这四条字节码指令时，如果类还没有进行过初始化，则需要先触发其初始化。生成这四条指令最常见的Java代码场景是：
    - 创建一个类的新实例时（在字节码中执行new指令，不明确的创建：反射、clone和反序列化）；Unsafe.allocateInstance不会触发<init>()，但会触发<clinit>()
    - 读取或设置一个类的静态字段时（在字节码中执行getstatic或putstatic），被final修饰，编译期可确定结果并已放入常量池的静态字段除外
    - 调用一个类的静态方法时（在字节码中执行invokestatic指令）

- 使用Java.lang.refect包的方法对类进行反射调用时(Class.newInstance())，如果类还没有进行过初始化，则需要先触发其初始化。

- 当初始化一个类的时候，如果发现其父类还没有进行初始化，则需要先触发其父类的初始化。

- 当虚拟机启动时，被标明为启动类的类(main方法所在的类)。

- 当使用JDK1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果是REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化。
    
虚拟机规定只有这几种情况才会触发类的初始化，称为对一个类进行主动引用，除此之外所有引用类的方式都不会触发其初始化，称为被动引用。
    
    
## 通过子类引用父类中的静态字段
对于静态字段，只有直接定义这个字段的类才会被初始化。
    
```
class Father {
    public static int m = 33;
    static {
        System.out.println("父类被初始化");
    }
}
class Child extends Father {
    static {
        System.out.println("子类被初始化");
    }
}
public class StaticTest {
    public static void main(String[] args) {
        System.out.println(Child.m);
    }
}
```
    
## 常量
编译期可预知结果的常量，会被放入常量池中，本质上没有直接引用到定义该常量的类，因此不会触发类的初始化。
    
```
class Const {
    public static final String NAME = "我是常量";
    static {
        System.out.println("初始化Const类");
    }
}
public class FinalTest {
    public static void main(String[] args) {
        System.out.println(Const.NAME);
    }
}
```
    
在编译阶段将常量的值“我是常量”存储到了调用它的类FinalTest的常量池中，对常量Const.NAME的引用实际上转化为了FinalTest类对自身常量池的引用。也就是说，实际上FinalTest的Class文件之中并没有Const类的符号引用入口，这两个类在编译成Class文件后就不存在任何联系了。
    

## 通过数组定义来引用类

```
class Const {
    static {
        System.out.println("初始化Const类");
    }
}
 
public class ArrayTest {
    public static void main(String[] args) {
        Const[] con = new Const[5];
    }
}
```
    
但这段代码里触发了另一个名为[LConst;的类的初始化，它是一个由虚拟机自动生成的、直接继承于java.lang.Object的子类，创建动作由字节码指令newarray触发，这是一个对数组引用类型的初初始化，而该数组中的元素仅仅包含一个对Const类的引用，并没有对其进行初始化。
    

# 实例初始化
Java编译器为它编译的每一个类都至少生成一个实例初始化方法。在class文件中这个实例初始化方法被称为<init>。针对源码中类的每一个构造方法，java编译器都产生一个<init>()方法。
    
- 如果类没有明确声明任何构造方法，编译器默认产生一个无参数的构造方法；在class文件中创建一个<init>()方法，对应它的默认构造方法。

- 如果构造方法中通过明确的this()调用另一个构造方法。它对应的<init>()方法由两部分组成：一个与this()参数相同的<init>()方法调用；实现了对应构造方法的方法体的字节码。

- 如果构造方法不是通过this()调用开始的（代码第一行不是this()），而且这个类型不是Object，<init>()方法则由三部分组成：一个超类的无参<init>()方法调用（如果是Object，这项不存在）；任意实例变量初始化语句的字节码；实现了对应构造方法的方法体的字节码。

- 如果构造方法通过明确的super()开始（super和this不能同时存在，他们都必须是构造器的第一行代码），<init>()方法则由三部分组成：对应参数类型的超类<init>()方法；任意实例变量初始化语句字节码；实现了对应构造方法的的方法体的字节码。

- 对于除Object外的每一个类，<init>()方法都必须从另一个<init>()方法调用开始（本类或者超类的<init>()方法）。

- <init>()方法不允许捕捉由它们所调用的<init>()方法抛出的任何异常；如果超类的<init>()方法被意外中止了，那么子类的<init>()方法也必须同样被意外中止。
    

