package com.springBoot.utils.configs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 要实现对象的缓存，定义自己的序列化和反序列化器
 * @date 2019/3/26 026 10:44
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private Class<T> clazz;

	public FastJsonRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		// 将字节数组转为字符串
		String str = new String(bytes, DEFAULT_CHARSET);
		// 格式化字符串为json字符串
		str = new Gson().toJson(new JsonParser().parse(str));
		// json字符串转为相应clazz对象
		return (T) JSON.parseObject(str, clazz);
	}
}
