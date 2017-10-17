package com.asiainfo.rpc;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月21日  下午4:56:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FastjsonSerializer implements Serializer<Object> {

	static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    static final SerializerFeature[] FEATURES = {SerializerFeature.WriteClassName};
    
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
        try {
            return JSON.toJSONBytes(t, FEATURES);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
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
        try {
            return JSON.parse(bytes);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
	}
}
