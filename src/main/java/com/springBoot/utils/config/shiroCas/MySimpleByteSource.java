package com.springBoot.utils.config.shiroCas;

import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 继承SimpleByteSource并实现Serializable序列化，避免EhCacheManager缓存时报错
 * @date 2019/3/29 029 15:09
 */
public class MySimpleByteSource extends SimpleByteSource implements Serializable {

	private static final long serialVersionUID = 1L;

	public MySimpleByteSource(String string) {
		super(string);
	}

	public MySimpleByteSource(byte[] bytes) {
		super(bytes);
	}

	public MySimpleByteSource(char[] chars) {
		super(chars);
	}

	public MySimpleByteSource(File file) {
		super(file);
	}

	public MySimpleByteSource(InputStream stream) {
		super(stream);
	}
}
