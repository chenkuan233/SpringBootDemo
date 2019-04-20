package com.springBoot.utils.config.shiroCas;

import org.apache.shiro.util.ByteSource;

import java.io.File;
import java.io.InputStream;

/**
 * @author chenkuan
 * @version v1.0
 * @desc ByteSource工具类 用于处理加密盐salt等
 * @date 2019/3/29 029 15:16
 */
public class ByteSourceUtil {

	public static ByteSource bytes(String string) {
		return new MySimpleByteSource(string);
	}

	public static ByteSource bytes(byte[] bytes) {
		return new MySimpleByteSource(bytes);
	}

	public static ByteSource bytes(char[] bytes) {
		return new MySimpleByteSource(bytes);
	}

	public static ByteSource bytes(File file) {
		return new MySimpleByteSource(file);
	}

	public static ByteSource bytes(InputStream stream) {
		return new MySimpleByteSource(stream);
	}
}
