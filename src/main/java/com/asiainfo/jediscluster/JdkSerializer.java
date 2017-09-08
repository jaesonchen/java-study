package com.asiainfo.jediscluster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月21日  下午5:23:06
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JdkSerializer implements Serializer<Object> {

	/* 
	 * @Description: TODO
	 * @param t
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.rpc.Serializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		
		if (t == null) {
			return new byte[0];
		}
		if (!(t instanceof Serializable)) {
			throw new SerializationException(getClass().getSimpleName() + " requires a Serializable payload " +
					"but received an object of type [" + t.getClass().getName() + "]");
		}
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
			objectOutputStream.writeObject(t);
			objectOutputStream.flush();
			return byteStream.toByteArray();
		} catch (Throwable ex) {
			throw new SerializationException("Failed to serialize object using " + getClass().getSimpleName(), ex);
		}
	}

	/* 
	 * @Description: TODO
	 * @param bytes
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.rpc.Serializer#deserialize(byte[])
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		
        if (bytes == null || bytes.length == 0) {
            return null;
        }
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream objectOutputStream = new ObjectInputStream(byteStream);
			return objectOutputStream.readObject();
		} catch (Throwable ex) {
			throw new SerializationException("Failed to deserialize payload. " +
					"Is the byte array a result of corresponding serialization for " + getClass().getSimpleName() + "?", ex);
		}
	}
}
