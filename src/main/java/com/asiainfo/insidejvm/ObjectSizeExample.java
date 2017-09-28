package com.asiainfo.insidejvm;

import static net.sourceforge.sizeof.SizeOf.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

/*
 * -javaagent:SizeOf.jar -XX:+UseCompressedOops
 */
@SuppressWarnings("unused")
public class ObjectSizeExample {

	public static void main(String[] args) {
		
		List<String> list = getStringList();
		System.out.println("List<Long>=" + deepSizeOf(getLongList()));
		System.out.println("Set<Long>=" + deepSizeOf(getLongSet()));
		/*System.out.println("List<String>=" + deepSizeOf(list));
		System.out.println("List<Long>=" + deepSizeOf(getLongList()));
		System.out.println("Set<Long>=" + deepSizeOf(getLongSet()));
		
		System.out.println("Map<nocache>=" + deepSizeOf(convert2Map(list)));
		System.out.println("Map<cache>=" + deepSizeOf(convertToMap(list)));*/
		
		System.out.println("Map<BitMap2Object>=" + deepSizeOf(convertToBitMap(list)));
		System.out.println("Map<BitSet>=" + deepSizeOf(convertToBitSet(list)));
		System.out.println("Map<BitSet1>=" + deepSizeOf(convertToBitSet1(list)));
		System.out.println("Map<BitSet2>=" + deepSizeOf(convertToBitSet2(list)));
		System.out.println("Map<Bit>=" + deepSizeOf(convertToBitMap1(list)));
		
		List<Short> list1 = new ArrayList<Short>();
		List<Short> list2 = new ArrayList<Short>();
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 10000; j++) {
				list1.add(Short.valueOf((short) j));
				list2.add(ShortCachePool.valueOf((short) j));
			}
		}
		System.out.println("List<Short1>=" + deepSizeOf(list1));
		System.out.println("List<Short2>=" + deepSizeOf(list2));
		System.out.println("Short=" + deepSizeOf(new Short("1002")));
		
		/*
		System.out.println(sizeOf(new Integer(1)));
		System.out.println(deepSizeOf(new Integer(1)));
        System.out.println(sizeOf(new String("a")));
        System.out.println(sizeOf(new char[1]));
        System.out.println(sizeOf(new long[1]));
        
        System.out.println("sizeOf(new Object())=" + sizeOf(new Object()));  
        System.out.println("sizeOf(new A())=" + sizeOf(new A()));  
        System.out.println("sizeOf(new B())=" + sizeOf(new B()));  
        System.out.println("sizeOf(new B2())=" + sizeOf(new B2()));  
        System.out.println("sizeOf(new B[3])=" + sizeOf(new B[3]));
        System.out.println("sizeOf(new C())=" + sizeOf(new C()));  
        //System.out.println("fullSizeOf(new C())=" + fullSizeOf(new C()));  
        System.out.println("sizeOf(new D())=" + sizeOf(new D()));  
        //System.out.println("fullSizeOf(new D())=" + fullSizeOf(new D()));  
        System.out.println("sizeOf(new int[3])=" + sizeOf(new int[3]));  
        System.out.println("sizeOf(new Integer(1)=" + sizeOf(new Integer(1)));  
        System.out.println("sizeOf(new Integer[0])=" + sizeOf(new Integer[0]));  
        System.out.println("sizeOf(new Integer[1])=" + sizeOf(new Integer[1]));  
        System.out.println("sizeOf(new Integer[2])=" + sizeOf(new Integer[2]));  
        System.out.println("sizeOf(new Integer[3])=" + sizeOf(new Integer[3]));  
        System.out.println("sizeOf(new Integer[4])=" + sizeOf(new Integer[4]));  
        System.out.println("sizeOf(new A[3])=" + sizeOf(new A[3]));  
        System.out.println("sizeOf(new E())=" + sizeOf(new E()));*/
	}
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 = 16 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 4 + padding/4 = 24 
     */  
    static class A {
        int a;
    }
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 + 4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 4 + 4 = 24 
     */  
    static class B {
        int a;
        int b;
    }
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 + 4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 8 + 4 + padding/4 = 32 
     */  
    static class B2 {
        int b2a;
        Integer b2b;
    }
  
    /** 
     * 不考虑对象头： 
     * 4 + 4 + 4 * 3 + 3 * sizeOf(B) 
     */  
    static class C extends A {
        int ba;
        B[] as = new B[3];
  
        C() {
            for (int i = 0; i < as.length; i++) {
                as[i] = new B();
            }
        }
    }
  
    static class D extends B {  
        int da;  
        Integer[] di = new Integer[3];  
    }  
  
    /** 
     * 会算上A的实例字段 
     */  
    static class E extends A {  
        int ea;  
        int eb;  
    }
    
    private static List<Long> getLongList() {
    	
    	List<Long> result = new ArrayList<Long>();
    	
    	long num = 13522587602L;
    	
    	for (int i = 0; i < 10000; i++) {
    		
    		for (int j = 0; j < 100; j++)
    			result.add(num++);
    	}
    	
    	return result;
    }
    
    private static List<String> getStringList() {
    	
    	List<String> result = new ArrayList<String>();
    	
    	//long num = 13522587602L;
    	int num = 1350000000;
    	
    	for (int i = 0; i < 100; i++) {
    		
    		//num = 13522587;
    		for (int j = 0; j < 10000; j++)
    			result.add("" + num++ + "1");
    	}
    		
    	
    	return result;
    }
 
    private static Set<Long> getLongSet() {
    	
    	Set<Long> result = new HashSet<Long>();
    	
    	long num = 13522587602L;
    	
    	for (int i = 0; i < 10000; i++) {
    		
    		for (int j = 0; j < 100; j++)
    			result.add(num++);
    	}
    	
    	return result;
    }
    
    private static Map<Short, Set<Integer>> convert2MapSet(List<String> list) {
    	
    	Map<Short, Set<Integer>> result = new HashMap<Short, Set<Integer>>();
    	Short brand;
    	Set<Integer> set;
    	for (String str : list) {
    		
    		brand = new Short(str.substring(0, 4));
    		set = result.get(brand);
			if (set == null || set.isEmpty()) {
				set = new HashSet<Integer>();
				result.put(brand, set);
			}
			set.add(new Integer(str.substring(4)));
    	}
    	
    	return result;
    }
    
	private static Map<Short, Map<Short, Set<Short>>> convert2Map(List<String> list) {
		
		Map<Short, Map<Short, Set<Short>>> result = new HashMap<Short, Map<Short, Set<Short>>>();
		
		Short brand;
		Short first;
		Short second;
		Map<Short, Set<Short>> map;
		Set<Short> set;
		for (String str : list) {
			
			brand = new Short(str.substring(0, 3));
			first = new Short(str.substring(3, 7));
			second = new Short(str.substring(7));
			
			map = result.get(brand);
			if (map == null || map.isEmpty()) {
				
				map = new HashMap<Short, Set<Short>>();
				result.put(brand, map);
			}

			set = map.get(first);
			if (set == null || set.isEmpty()) {
				
				set = new HashSet<Short>();
				map.put(first, set);
			}
			
			set.add(second);
		}
		
		return result;
	}
	//use cache
	private static Map<Short, Map<Short, Set<Short>>> convertToMap(List<String> list) {
		
		Map<Short, Map<Short, Set<Short>>> result = new HashMap<Short, Map<Short, Set<Short>>>();
		
		Short brand;
		Short first;
		Short second;
		Map<Short, Set<Short>> map;
		Set<Short> set;
		for (String str : list) {
			
			brand = ShortCachePool.valueOf(str.substring(0, 3));
			first = ShortCachePool.valueOf(str.substring(3, 7));
			second = ShortCachePool.valueOf(str.substring(7));
			
			map = result.get(brand);
			if (map == null || map.isEmpty()) {
				
				map = new HashMap<Short, Set<Short>>();
				result.put(brand, map);
			}

			set = map.get(first);
			if (set == null || set.isEmpty()) {
				
				set = new HashSet<Short>();
				map.put(first, set);
			}
			
			set.add(second);
		}
		
		return result;
	}

	private static Map<Short, BitSet> convertToBitSet(List<String> list) {
		
		Map<Short, BitSet> result = new HashMap<Short, BitSet>();
		
		Short key;
		int value;
		BitSet set;
		for (String str : list) {
			
			key = ShortCachePool.valueOf(str.substring(0, 4));
			value = Integer.valueOf(str.substring(4));
			
			set = result.get(key);
			if (set == null || set.isEmpty()) {
				
				set = new BitSet();
				result.put(key, set);
			}
			
			set.set(value);
		}
		
		return result;
	}
	private static Map<Short, BitSet> convertToBitSet1(List<String> list) {
		
		Map<Short, BitSet> result = new HashMap<Short, BitSet>();
		
		Short brand;
		int phone;
		BitSet set;
		for (String str : list) {
			
			brand = Short.valueOf(str.substring(1, 5));
			phone = Integer.valueOf(str.substring(5));
			
			set = result.get(brand);
			if (set == null || set.isEmpty()) {
				
				set = new BitSet();
				result.put(brand, set);
			}
			
			set.set(phone);
		}
		
		return result;
	}
	private static Map<Integer, BitSet> convertToBitSet2(List<String> list) {
		
		Map<Integer, BitSet> result = new HashMap<Integer, BitSet>();
		
		Integer brand;
		int phone;
		BitSet set;
		for (String str : list) {
			
			brand = Integer.valueOf(str.substring(1, 7));
			phone = Integer.valueOf(str.substring(7));
			
			set = result.get(brand);
			if (set == null || set.isEmpty()) {
				
				set = new BitSet();
				result.put(brand, set);
			}
			
			set.set(phone);
		}
		
		return result;
	}
	private static Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> convertToBitMap(List<String> list) {
		
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> h1 = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>(1);
		
		for (String str : list) {
			
			byte v1 = Byte.valueOf(str.substring(1, 3));
			short v2 = Short.valueOf(str.substring(3, 7));
			short v3 = Short.valueOf(str.substring(7));
			Short2ObjectOpenHashMap<BitSet> h2 = h1.get(v1);
			if (h2 == null) {
				h2 = new Short2ObjectOpenHashMap<BitSet>(1);
				h1.put(v1, h2);
			}
			BitSet h3 = h2.get(v2);
				h3 = new BitSet(10000);
			h3.set(v3);
			h2.put(v2, h3);
		}
		
		return h1;
	}
	private static Short2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> convertToBitMap1(List<String> list) {
		
		Short2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> h1 = new Short2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>();
		
		for (String str : list) {
			
			short v1 = Short.valueOf(str.substring(0, 4));
			short v2 = Short.valueOf(str.substring(4, 8));
			short v3 = Short.valueOf(str.substring(8));
			Short2ObjectOpenHashMap<BitSet> h2 = h1.get(v1);
			if (h2 == null) {
				h2 = new Short2ObjectOpenHashMap<BitSet>(1);
				h1.put(v1, h2);
			}
			BitSet h3 = h2.get(v2);
				h3 = new BitSet(1000);
			h3.set(v3);
			h2.put(v2, h3);
		}
		
		return h1;
	}
}
