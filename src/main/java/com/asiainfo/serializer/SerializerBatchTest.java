package com.asiainfo.serializer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月3日  上午10:59:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SerializerBatchTest {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SerializerBean bean  = new SerializerBean("1001", "chenzq", 37);
		KryoSerializer kryo = new KryoSerializer();
		HessianSerializer hessian = new HessianSerializer();
		FastjsonSerializer fast = new FastjsonSerializer();
		JdkSerializer jdk = new JdkSerializer();
		
		byte[] result;
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			result = jdk.serialize(bean);
			jdk.deserialize(result);
		}
		System.out.println("jdk time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			result = kryo.serialize(bean);
			kryo.deserialize(result);
		}
		System.out.println("kryo time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			result = hessian.serialize(bean);
			hessian.deserialize(result);
		}
		System.out.println("hessian time=" + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			result = fast.serialize(bean);
			fast.deserialize(result);
		}
		System.out.println("fast time=" + (System.currentTimeMillis() - start));
	}
}
