package com.asiainfo.basic;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * @Description: java 泛型介绍
 * 
 * @author       zq
 * @date         2017年9月12日  下午2:38:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class GenericInfo {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {

		//Type：java语言中所有类型的公共父接口  --> Class
		
		//Type所有类型指代的有：原始类型 (raw types)【对应Class】，基本数据类型(primitive types)【对应Class】，
	    //                    参数化类型 (parameterized types)【对应ParameterizedType】，
		//                    类型变量 (type variables)【对应TypeVariable】，
	    //                    数组类型 (array types)【对应GenericArrayType】
	    //                    ? 通配符类型【对应WildcardType】
		
		//ParameterizedType: 表示一种参数化的类型，比如List<T> 获取参数化类型<>中的实际类型 Type[] getActualTypeArguments()
	    //                   ArrayList<Map<String, Object>> --> Map<String, Object> (ParameterizedType)
	    //                   ArrayList<E> --> E (TypeVariable)
        //                   ArrayList<String> --> String (Class)
        //                   ArrayList<? extends Number> --> ? extends Number (WildcardType)
	    //                   ArrayList<E[]> --> E[] (GenericArrayType)
	    
		//GenericArrayType: 表示一种元素类型是参数化类型(ParameterizedType)或者类型变量(TypeVariable)的数组类型, 比如T[]，List<T>[]，
	    //                  <>不能出现在数组的初始化中，即new数组之后不能出现<>，否则javac无法通过。
		//                  获取泛型数组中元素的类型Type getGenericComponentType()
	    //                  String[] --> String.class (Class)
	    //                  E[] --> E (TypeVariable)
	    //                  ArrayList[] --> ArrayList (ParameterizedType)
	    //                  E[][] --> E[] (GenericArrayType)
		
		//TypeVariable: 是各种类型变量的公共父接口, 获取类型变量的泛型限定上边界的类型 Type[] getBounds(),
		//              没有定义上边界，默认是Object(只能使用extends & 定义多边界)
		
		//WildcardType: 代表一种通配符类型表达式，比如?, ? extends Number, ? super Integer
		//              PECS表示producer-extends，consumer-super
		//              获取泛型限定的上边界的类型Type[] getUpperBounds() 没有定义上边界，默认是Object
		//              获取泛型限定的下边界的类型Type[] getLowerBounds() 没有定义下边界，默认是null

        //raw type原始类型：
        //    一种规定了generic type参数，但是引用的时候却没有指明的一种引用。
        //    一个包含raw type的数组。
        //    一个非静态的raw type的成员R，并且r没有继承或者实现父类或者接口。
        //    简单来说，就是声明了generic type T，但是用的时候却没有指定T
        //    List list = new ArrayList<>();
        //    List[] listArr = new List[0];

        //泛型的限制
        //基本类型不能作为类型参数。
        //类型检查只能用原始类型。t instanceof List<String>  //ERROR
        //不能实例化类型变量。    T t = new T() //ERROR
        //不能实例化参数化类型的数组。List<Integer>[] p = new List<Integer>[5] //ERROR
        //不能定义静态实例变量和静态方法。private static T t  //ERROR
        // T[] arr = (T[]) new Object[10];
        // T getInstance(Class<T> clazz) {
        //    return clazz.newInstance();
        // }
	    
	    // 捕获转换
	    // 通常情况下，使用原生类型和<?>并没有什么区别，但是有一种情况特别需要使用<?>而不是原生类型，即捕获转换（因为未指定的通配符类型被捕获，并被转换为确切类型）。
	    
		//class GenericClazz<T, K extends Comparable<? super T> & Serializable> {}
		for (TypeVariable<?> tv : GenericClazz.class.getTypeParameters()) {
			if (tv.getBounds()[0] instanceof Class) {
				System.out.println("TypeVariable=" + tv.getTypeName() + ", uppper bound=" + tv.getBounds()[0]);
			}
			else if (tv.getBounds()[0] instanceof ParameterizedType) {
				System.out.println("TypeVariable=" + tv.getTypeName() + ", ParameterizedType=" 
						+ tv.getBounds()[0] + ", " + tv.getBounds()[1]);
				ParameterizedType pType = (ParameterizedType) tv.getBounds()[0];
				System.out.println("raw type=" + pType.getRawType());
				for (Type tp : pType.getActualTypeArguments()) {
					if (tp instanceof ParameterizedType) {
						System.out.println("inner ParameterizedType=" + ((ParameterizedType) tp).getActualTypeArguments()[0]);
					}
					else if (tp instanceof GenericArrayType) {
						System.out.println("inner GenericArrayType=" + ((GenericArrayType) tp).getGenericComponentType());
					}
					else if (tp instanceof TypeVariable) {
						System.out.println("inner TypeVariable=" + ((TypeVariable<?>) tp).getBounds()[0]);
					}
					else if (tp instanceof WildcardType) {
						System.out.println("inner WildcardType=" + ((WildcardType) tp).getLowerBounds()[0]);
					}
					else {
						System.out.println("inner Class=" + ((Class<?>) tp).getName());
					}
				}
			}
		}

		//class GenericElement implements Comparable<Integer>, Serializable {}
		GenericElement generic = new GenericElement(11);
		// getGenericInterfaces 接口的参数化类型
		ParameterizedType type = (ParameterizedType) generic.getClass().getGenericInterfaces()[0];
		System.out.println("actualTypeArguments=" + type.getActualTypeArguments()[0]);
		// getGenericSuperclass 父类的参数化类型
		System.out.println("getGenericSuperclass=" + generic.getClass().getGenericSuperclass());
		
		Method method = GenericInfo.class.getMethod("actualTypeArguments", GenericClazz.class);
		// getGenericParameterTypes 方法参数的参数化类型
		type = (ParameterizedType) method.getGenericParameterTypes()[0];
		System.out.println("method raw type=" + type.getRawType());
		System.out.println("method actualTypeArguments=" + type.getActualTypeArguments()[0]);
		// getGenericReturnType 方法返回值的参数化类型
		type = (ParameterizedType) method.getGenericReturnType();
		System.out.println("method returntype raw type=" + type.getRawType());
		System.out.println("method returntype actualTypeArguments=" + type.getActualTypeArguments()[0]);
		
		// 捕获转换，当一个容器由一个特定的类型参数转换为原型或者是通配符’?’时，可以通过先用一个使用’?’的方法保存容器，再将容器作为参数传递给泛型方法。
		Box<?> box = new Box<Integer>(1);
		rebox(box);
	}

    public Comparable<Integer> actualTypeArguments(GenericClazz<Integer, GenericElement> generic) {
        return null;
    }
	
	//类型推断和捕获转换
	static class Box<T> {
	    T t;
	    Box(T t) { this.t = t; }
		T get() { return t; }
		void put(T t) { this.t = t; }
	}
	public static void rebox(Box<?> box) {
		reboxHelper(box);
	}
	// 通过捕获转换获得确切的类型
	public static <V> void reboxHelper(Box<V> box) {
	    V v = box.get();
		System.out.println(v);
	}
}

class GenericClazz<T, K extends Comparable<? super T> & Serializable> {}
class GenericElement implements Comparable<Integer>, Serializable {
	
	int value;
	GenericElement(int value) {
		this.value = value;
	}
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/* 
	 * @Description: TODO
	 * @param o
	 * @return
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Integer o) {
		return this.value - o.intValue();
	}
}
