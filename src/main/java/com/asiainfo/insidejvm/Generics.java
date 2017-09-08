package com.asiainfo.insidejvm;

import java.util.*;

/**
 * java1.5增加了泛型（Generics）
 * 
 * 请不要在新代码中使用原生态类型：
 * 如果使用原生态类型就失去了泛型在安全性和表述性方面的所有优势。
 * 泛型有子类型化（subtyping）的规则。List<String>是原生态类型List的一个子类型，不是List<Object>的子类型。
 * 无限制的通配符类型(?)：如果要使用泛型，但不确定实际的参数类型，就可以使用一个问号代替。
 * 无限制的通配符类型Set<?>与原生态类型Set的区别：可以将任何元素放入原生态类型中，但不能将任何元素（除了null）放入到Set<?>中，它是类型安全的。
 * 使用原生态的例外情况：Class类必须使用原生态类型（List.class）;instanceof操作符必须使用原生态类型（o instanceof List）。
 * 
 * *************************************************************************
 * 消除非受检的警告：
 * 用泛型编程时会收到许多编译器警告：unchecked cast warnings、unchecked conversion warnings等。
 * 如果无法消除警告，同时可以保证引起警告的代码是类型安全的；可以用@SuppressWarnings("unchecked")消除警告。
 * 应该始终在尽可能小的范围内使用@SuppressWarnings注解。永远不要在整个类上使用，这样可能会掩盖了重要的警告。
 * 
 * *************************************************************************
 * 列表优先于数组：
 * 数组和泛型相比有两个重要的不同点：
 * 首先，数组是协变式的（covariant），如果Sub是Super的子类型，那么Sub[]也是Super[]的子类型。
 * 		相反，泛型是不可变的（invariant），对于任意两个不同的类型Type1和Type2，List<Type1>既不会是List<Type2>的子类型，也不会是超类型。
 * 
 * 其次，数组是具体化的，因此数组会在运行时才知道并检查元素的类型约束。
 * 		泛型是通过擦除（erasure）来实现的，泛型只在编译时强化它们的类型信息，并在运行时丢弃元素类型信息，使泛型可以与没有使用泛型的代码随意互用。
 * 
 * 由于上述这些根本的区别，数组和泛型不能很好地混合使用。
 * new List<String>[] //编译时错误：generic array creation error
 * *************************************************************************
 * 利用有限通配符类型来提升API的灵活性：? extends E 和 ? super E
 * 不要用通配符类型作为泛型方法的返回类型
 * *************************************************************************
 * 泛型通用参数命名规则：T表示任意的类型，E表示结合的元素类型，K和V表示映射的键和值类型，X表示异常，任何类型的序列可以是T、U、V或者T1、T2、T3
 */

public class Generics {
	//无限制的通配符类型
	static int numElementsInCommon(Set<?> s1, Set<?> s2) {
		int result = 0;
		for (Object o : s1)
			if (s2.contains(o))
				result++;
		return result;
	}
	
	// List-based generic reduction
	static <E> E reduce(List<E> list, Function<E> f, E initVal) {
		List<E> snapshot;
		synchronized(list) {
			snapshot = new ArrayList<E>(list);
		}
		E result = initVal;
		for (E e : snapshot)
			result = f.apply(result, e);
		return result;
	}
	
	private Map<Class<?>, Object> favorites =
			new HashMap<Class<?>, Object>();
	public <T> void putFavorite(Class<T> type, T instance) {
		if (type == null)
			throw new NullPointerException("Type is null");
		favorites.put(type, instance);
	}
	public <T> T getFavorite(Class<T> type) {
		return type.cast(favorites.get(type));
	}
	public static void main(String[] args) {
		Generics g = new Generics();
		g.putFavorite(String.class, "Java");
		g.putFavorite(Integer.class, 100086);
		g.putFavorite(Class.class, Generics.class);
		String favoriteString = g.getFavorite(String.class);
		int favoriteInteger = g.getFavorite(Integer.class);
		Class<?> favoriteClass = g.getFavorite(Class.class);
		System.out.printf("%s %d %s%n", favoriteString,
				favoriteInteger, favoriteClass.getSimpleName());

	}
}

interface Function<T> {
	T apply(T arg1, T arg2);
}
