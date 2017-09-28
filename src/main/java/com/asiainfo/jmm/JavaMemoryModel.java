package com.asiainfo.jmm;

/**
 * Java内存模型：
 * JMM决定一个线程对共享变量的写入何时对另一个线程可见。从抽象的角度来看，JMM定义了线程和主内存之间的抽象关系：
 * 线程之间的共享变量存储在主内存（main memory）中，每个线程都有一个私有的本地内存（local memory），本地内存中存储了该线程以读/写共享变量的副本。
 * 本地内存是JMM的一个抽象概念，并不真实存在。它涵盖了缓存，写缓冲区，寄存器以及其他的硬件和编译器优化。
 * 
 * 			线程A					线程B
 * 
 * 			  ^						  ^
 * 			  |						  |
 * 			  V						  V
 * 
 * 		  本地内存A				   本地内存B
 * 		 共享变量的拷贝		        共享变量的拷贝
 * 
 * 			  ^						  ^
 * 			  |			JMM控制		  |
 * 			  V						  V		
 *  
 * 						主内存
 * 			共享变量1、共享变量2、共享变量3 ...
 * 
 * 
 * 线程A与线程B之间如要通信的话，必须要经历下面2个步骤：
 * 1. 首先，线程A把本地内存A中更新过的共享变量刷新到主内存中去。
 * 2. 然后，线程B到主内存中去读取线程A之前已更新过的共享变量。 
 * 
 * JMM通过控制主内存与每个线程的本地内存之间的交互，来为java程序员提供内存可见性保证。
 * 
 * 在JVM内部，Java内存模型把内存分成了两部分：线程栈区stack和堆区heap。
 * 线程栈包含了当前线程执行的方法调用相关信息，线程栈包含了当前方法的所有局部变量信息，所有原始类型(boolean,byte,short,char,int,long,float,double)的
 * 局部变量都直接保存在线程栈当中，对于它们的值各个线程之间都是独立的。对于原始类型的局部变量，一个线程可以传递一个副本给另一个线程，当它们之间是无法共享的。
 * 
 * 堆区包含了Java应用创建的所有对象信息，不管对象是哪个线程创建的，其中的对象包括原始类型的封装类（如Byte、Integer、Long等等）。
 * 不管对象是属于一个成员变量还是方法中的局部变量，它都会被存储在堆区。
 * 一个局部变量如果是原始类型，那么它会被完全存储到栈区。 一个局部变量也有可能是一个对象的引用，这种情况下，这个局部引用会被存储到栈中，但是对象本身仍然存储在堆区。
 * 对于一个对象的成员方法，这些方法中包含局部变量，仍需要存储在栈区，即使它们所属的对象在堆区。 
 * 对于一个对象的成员变量，不管它是原始类型还是包装类型，都会被存储到堆区。
 * Static类型的变量以及类本身相关信息都会随着类本身存储在堆区。
 * 
 * 
 * 
 * 硬件内存架构：
 * 
 * 			CPU						 CPU	
 * 		 CPU 寄存器				  CPU 寄存器
 * 
 * 			  ^						  ^
 * 			  |						  |
 * 			  V						  V
 * 		 CPU Cache				  CPU Cache
 * 		  Memory					Memory
 * 
 * 			  ^						  ^
 * 			  |						  |
 * 			  V						  V
 * 
 * 				  RAM - Main Memory
 * 
 * 现代计算机一般都有2个以上CPU，而且每个CPU还有可能包含多个核心。因此，如果我们的应用是多线程的话，这些线程可能会在各个CPU核心中并行运行。
 * 在CPU内部有一组CPU寄存器，也就是CPU的储存器。CPU操作寄存器的速度要比操作计算机主存快的多。
 * 在主存和CPU寄存器之间还存在一个CPU缓存，CPU操作CPU缓存的速度快于主存但慢于CPU寄存器。某些CPU可能有多个缓存层（一级缓存和二级缓存）。
 * 计算机的主存也称作RAM，所有的CPU都能够访问主存，而且主存比上面提到的缓存和寄存器大很多。
 * 
 * 当一个CPU需要访问主存时，会先读取一部分主存数据到CPU缓存，进而在读取CPU缓存到寄存器。
 * 当CPU需要写数据到主存时，同样会先flush寄存器到CPU缓存，然后再在某些节点把缓存数据flush到主存。
 * 
 * 
 * 
 * Java内存模型和硬件架构之间的桥接:
 * Java内存模型和硬件内存架构并不一致。硬件内存架构中并没有区分栈和堆，从硬件上看，不管是栈还是堆，大部分数据都会存到主存中，
 * 当然一部分栈和堆的数据也有可能会存到CPU寄存器中。
 * 
 * 当对象和变量存储到计算机的各个内存区域时，必然会面临一些问题，其中最主要的两个问题是：
 * 1. 共享对象对各个线程的可见性
 * 	    当多个线程同时操作同一个共享对象时，如果没有合理的使用volatile和synchronization关键字，一个线程对共享对象的更新有可能导致其它线程不可见。
 * 	  volatile 关键字可以保证变量会直接从主存读取，而对变量的更新也会直接写到主存。volatile原理是基于CPU内存屏障指令实现的
 * 2. 共享对象的竞争现象
 * 	   多个线程共享一个对象，如果它们同时修改这个共享对象，这就产生了竞争现象。
 *   synchronized代码块可以保证同一个时刻只能有一个线程进入代码竞争区，synchronized代码块也能保证代码块中所有变量都将会从主存中读，
 *   当线程退出代码块时，对所有变量的更新将会flush到主存，不管这些变量是不是volatile类型的。
 *   
 *   
 * 
 * 支撑Java内存模型的基础原理：
 * 1. 指令重排序
 *    在执行程序时，为了提高性能，编译器和处理器会对指令做重排序。但是，JMM确保在不同的编译器和不同的处理器平台之上，通过插入特定类型的Memory Barrier来
 *    禁止特定类型的编译器重排序和处理器重排序，为上层提供一致的内存可见性保证。
 *    a. 编译器优化重排序：编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。
 *    b. 指令级并行重排序：现代处理器采用了指令级并行技术（Instruction-Level Parallelism， ILP）来将多条指令重叠执行。
 *                       如果不存在数据依赖性，处理器可以改变语句对应机器指令的执行顺序。
 *    c. 内存系统重排序：由于处理器使用缓存和读写缓冲区，这使得加载和存储操作看上去可能是在乱序执行。
 *    从java源代码到最终实际执行的指令序列，会分别经历下面三种重排序：
 *    
 *    java源代码  -> 编译器优化重排序 -> 指令级并行重排序 -> 内存系统重排序 -> 最终指向的指令序列
 *    
 *    这些重排序都可能会导致多线程程序出现内存可见性问题。
 *    对于编译器，JMM的编译器重排序规则会禁止特定类型的编译器重排序。
 *    对于处理器重排序，JMM的处理器重排序规则会要求java编译器在生成指令序列时，插入特定类型的内存屏障（memory barriers，intel称之为memory fence）指令，
 *    通过内存屏障指令来禁止特定类型的处理器重排序（不是所有的处理器重排序都要禁止）。
 *    JMM属于语言级的内存模型，它确保在不同的编译器和不同的处理器平台之上，通过禁止特定类型的编译器重排序和处理器重排序，为程序员提供一致的内存可见性保证。
 *    
 *    数据依赖性: 如果两个操作访问同一个变量，其中一个为写操作，此时这两个操作之间存在数据依赖性。 
 *    			 编译器和处理器不会改变存在数据依赖性关系的两个操作的执行顺序，即不会重排序。
 * 	  as-if-serial: 不管怎么重排序，单线程下的执行结果不能被改变，编译器、runtime和处理器都必须遵守as-if-serial语义。
 * 
 * 2. 内存屏障（Memory Barrier ）
 * 	    通过内存屏障可以禁止特定类型处理器的重排序，从而让程序按我们预想的流程去执行。内存屏障，又称内存栅栏，是一个CPU指令，
 * 	    它是一条这样的指令：
 * 	  a. 保证特定操作的执行顺序。
 * 	  b. 影响某些数据（或则是某条指令的执行结果）的内存可见性。
 * 
 * 	    编译器和CPU能够重排序指令，保证最终相同的结果，尝试优化性能。插入一条Memory Barrier会告诉编译器和CPU：不管什么指令都不能和这条Memory Barrier指令重排序。
 *    Memory Barrier所做的另外一件事是强制刷出各种CPU cache，如一个Write-Barrier（写入屏障）将刷出所有在Barrier之前写入 cache 的数据，
 *    因此，任何CPU上的线程都能读取到这些数据的最新版本。
 *    
 *    如果一个变量是volatile修饰的，JMM会在写入这个字段之后插进一个Write-Barrier指令，并在读这个字段之前插入一个Read-Barrier指令。
 *    这意味着，如果写入一个volatile变量，就可以保证：
 *    a. 一个线程写入变量a后，任何线程访问该变量都会拿到最新值。
 *    b. 在写入变量a之前的写入操作，其更新的数据对于其他线程也是可见的。因为Memory Barrier会刷出cache中的所有先前的写入。
 *    
 *    JMM把内存屏障指令分为下列四类：
 *    屏障类型					指令示例							说明
 *    LoadLoad Barriers			Load1; LoadLoad; Load2			确保Load1数据的装载，之前于Load2及所有后续装载指令的装载。
 *    StoreStore Barriers		Store1; StoreStore; Store2		确保Store1数据对其他处理器可见（刷新到内存），之前于Store2及所有后续存储指令的存储。
 *    LoadStore Barriers		Load1; LoadStore; Store2		确保Load1数据装载，之前于Store2及所有后续的存储指令刷新到内存。
 *    StoreLoad Barriers		Store1; StoreLoad; Load2		确保Store1数据对其他处理器变得可见（指刷新到内存），之前于Load2及所有后续装载指令的装载。
 *    															StoreLoad会使该屏障之前的所有内存访问指令（存储和装载指令）完成之后，才执行该屏障之后的内存访问指令。  
 *    StoreLoad Barriers是一个“全能型”的屏障，它同时具有其他三个屏障的效果。现代的多处理器大都支持该屏障（其他类型的屏障不一定被所有处理器支持）。
 *    执行该屏障开销会很昂贵，因为当前处理器通常要把写缓冲区中的数据全部刷新到内存中（buffer fully flush）。
 *    需要的屏障			第二步
 *    第一步				Normal Load			Normal Store		Volatile Load/Monitor Enter				Volatile Store/Monitor Exit
 *    Normal Load																						LoadStore
 *    Normal Store																						StoreStore
 *    Load/Enter		LoadLoad			LoadStore			LoadLoad								LoadStore
 *    Store/Exit												StoreLoad								StoreStore
 *    
 *    happens-before： 从jdk5开始，java使用新的JSR-133内存模型，基于happens-before的概念来阐述操作之间的内存可见性。
 *    在JMM中，如果一个操作的执行结果需要对另一个操作可见，那么这两个操作之间必须要存在happens-before关系，这个的两个操作既可以在同一个线程，也可以在不同的两个线程中。
 * 	    与程序员密切相关的happens-before规则如下：
 *    a. 程序顺序规则：一个线程中的每个操作，happens-before于该线程中任意的后续操作。
 *    b. 监视器锁规则：对一个锁的解锁操作，happens-before于随后对这个锁的加锁操作。
 *    c. volatile域规则：对一个volatile域的写操作，happens-before于任意线程后续对这个volatile域的读。
 *    d. 传递性规则：如果 A happens-before B，且 B happens-before C，那么A happens-before C。
 *    注意：两个操作之间具有happens-before关系，并不意味前一个操作必须要在后一个操作之前执行！仅仅要求前一个操作的执行结果，对于后一个操作是可见的，
 *         且前一个操作按顺序排在后一个操作之前。
 *    线程上调用start()方法happens before这个线程启动后的任何操作。
 *    一个线程中所有的操作都happens before从这个线程join()方法成功返回的任何其他线程。
 *    （注意思是其他线程等待一个线程的jion()方法完成，那么，这个线程中的所有操作happens before其他线程中的所有操作）
 * 
 * 
 * volatile和synchronized的区别:
 * volatile本质是在告诉jvm当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取； synchronized则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
 * volatile仅能使用在变量级别；synchronized则可以使用在变量、方法、和类级别的。
 * volatile仅能实现变量的修改可见性，不能保证原子性；而synchronized则可以保证变量的修改可见性和原子性。
 * volatile不会造成线程的阻塞；synchronized可能会造成线程的阻塞。
 * volatile标记的变量不会被编译器优化；synchronized标记的变量可以被编译器优化。
 * 写入一个volatile字段和释放监视器有相同的内存影响，而且读取volatile字段和获取监视器也有相同的内存影响。
 * 
 * 同步保证了一个线程在同步块之前或者在同步块中的一个内存写入操作以可预知的方式对其他有相同监视器的线程可见。
 * 当我们退出了同步块，我们就释放了这个监视器，这个监视器有刷新缓冲区到主内存的效果，因此该线程的写入操作能够为其他线程所见。
 * 在我们进入一个同步块之前，我们需要获取监视器，监视器有使本地处理器缓存失效的功能，因此变量会从主存重新加载，于是其它线程对共享变量的修改对当前线程来说就变得可见了。
 * 
 * 
 * 
 * @author       zq
 * @date         2017年9月17日  上午9:35:56
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JavaMemoryModel {
	 
	int a, b;
	volatile int v, u;

	void f() {
		
		int i, j;
		i = a;	// load a
		j = b;	// load b
		i = v;	// load v
			  	// LoadLoad
		j = u;	// load u
				// LoadStore
		a = i;	// store a
		b = j;	// store b
				// StoreStore
		v = i;	// store v	
				// StoreStore
		u = j;	// store u
				// StoreLoad
		i = u;	// load u
				// LoadLoad
				// LoadStore
		j = b;	// load b
		a = i;	// store a
	}
}

class X {
	
	int a;
	volatile int v;
	void f() {
		
		int i;
		synchronized (this) { // enter EnterLoad EnterStore
			i = a;// load a
			a = i;// store a
		}// LoadExit StoreExit exit ExitEnter

		synchronized (this) {// enter ExitEnter
			synchronized (this) {// enter
			}// EnterExit exit
		}// ExitExit exit ExitEnter ExitLoad

		i = v;// load v
		synchronized (this) {// LoadEnter enter
		} // exit ExitEnter ExitStore

		v = i; // store v
		synchronized (this) { // StoreEnter enter
		} // EnterExit exit
	}
}

