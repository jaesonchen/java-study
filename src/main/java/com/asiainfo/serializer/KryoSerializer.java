package com.asiainfo.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月3日  上午10:04:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class KryoSerializer implements Serializer<Object> {

	/* 
	 * @Description: TODO
	 * @param t
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.serializer.Serializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Object t) throws SerializationException {

		if (t == null) {
			return new byte[0];
		}
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Kryo kryo = new Kryo();
            Output output = new Output(baos);
            //kryo.writeObject(output, t);
            kryo.writeClassAndObject(output, t);
            output.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new SerializationException("kryo serialize error" + ex.getMessage());
        } finally {
        	try {
        		baos.close();
        	} catch (IOException ex) {}
        }
	}

	/* 
	 * @Description: TODO
	 * @param bytes
	 * @return
	 * @throws SerializationException
	 * @see com.asiainfo.serializer.Serializer#deserialize(byte[])
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {

        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            Kryo kryo = new Kryo();
            Input input = new Input(bais);
            //return kryo.readObject(input, SerializerBean.class);
            return kryo.readClassAndObject(input);
        } catch (Exception e) {
            throw new SerializationException("kryo deSerialize error" + e.getMessage());
        } finally {
            try {
            	bais.close();
            } catch (IOException e) {}
        }
	}
}
