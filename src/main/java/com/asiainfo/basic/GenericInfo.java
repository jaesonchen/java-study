package com.asiainfo.basic;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * @Description: TODO
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
		
		//Type所有类型指代的有：原始类型 (raw types)【对应Class】，参数化类型 (parameterized types)【对应ParameterizedType】，
		//数组类型 (array types)【对应GenericArrayType】，类型变量 (type variables)【对应TypeVariable】，基本数据类型(primitive types)【对应Class】
		
		//ParameterizedType: 表示一种参数化的类型，比如List<T> 获取参数化类型<>中的实际类型 Type[] getActualTypeArguments()
		
		//GenericArrayType: 表示一种元素类型是参数化类型(ParameterizedType)或者类型变量(TypeVariable)的数组类型, 
		//获取泛型数组中元素的类型Type getGenericComponentType()
		
		//TypeVariable: 是各种类型变量的公共父接口, 获取类型变量的泛型限定的上边界的类型 Type[] getBounds(),
		//没有定义上边界，默认是Object(只能使用extends 定义多边界)
		
		//WildcardType: 代表一种通配符类型表达式，比如?, ? extends Number, ? super Integer
		//PECS表示producer-extends，consumer-super
		//获取泛型限定的上边界的类型Type[] getUpperBounds() 没有定义上边界，默认是Object
		//获取泛型限定的下边界的类型Type[] getLowerBounds() 没有定义下边界，默认是null
		
		//class GenericClazz<T, K extends Comparable<? super T> & Serializable> {}
		for (TypeVariable<?> tv : GenericClazz.class.getTypeParameters()) {
			if (tv.getBounds()[0] instanceof Class) {
				System.out.println("TypeVariable=" + tv.getTypeName() + ", Class=" + tv.getBounds()[0]);
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

		//通过父类或者父接口获得参数类型的实际类型
		GenericElement generic = new GenericElement(11);
		ParameterizedType type = (ParameterizedType) generic.getClass().getGenericInterfaces()[0];
		System.out.println("actualTypeArguments=" + type.getActualTypeArguments()[0]);
		System.out.println("actualTypeArguments=" + generic.getClass().getGenericSuperclass());
		
		//反射获得方法返回值和参数类型的实际类型
		Method method = GenericInfo.class.getMethod("actualTypeArguments", GenericClazz.class);
		type = (ParameterizedType) method.getGenericParameterTypes()[0];
		System.out.println("method raw type=" + type.getRawType());
		System.out.println("method actualTypeArguments=" + type.getActualTypeArguments()[0]);
		type = (ParameterizedType) method.getGenericReturnType();
		System.out.println("method returntype raw type=" + type.getRawType());
		System.out.println("method returntype actualTypeArguments=" + type.getActualTypeArguments()[0]);
		
		//raw type原始类型
		//一种规定了generic type参数，但是引用的时候却没有指明的一种引用。
		//一个包含raw type的数组。
		//一个非静态的raw type的成员R，并且r没有继承或者实现父类或者接口。
		//简单来说，就是声明了generic type T，但是用的时候却没有指定T
		//List list = new ArrayList<>();
		//List[] listArr = new List[0];

		//泛型的限制
	    //基本类型不能作为类型参数。
		//类型检查只能用原始类型。t instanceof List<String>  //ERROR
	    //不能实例化类型变量。	T t = new T() //ERROR
	    //不能实例化参数化类型的数组。List<Integer>[] p = new List<Integer>[5] //ERROR
	    //不能定义静态实例变量和静态方法。private static T t  //ERROR
		
	}
	
	
	//通过方法获取泛型参数的实际类型
	public Comparable<Integer> actualTypeArguments(GenericClazz<Integer, GenericElement> generic) {
		return null;
	}
	
	//类型推断和捕获转换
	interface Box<T> {
		T get();
		void put(T t);
	}
	public void rebox(Box<?> box) {
		reboxHelper(box);
	}
	public <V> void reboxHelper(Box<V> box) {
		box.put(box.get());
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
