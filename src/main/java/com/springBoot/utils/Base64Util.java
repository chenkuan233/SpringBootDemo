package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc Base64编解码 apache.commons.codec.binary.Base64
 * @date 2019/4/29 029 12:58
 */
@Slf4j
public final class Base64Util {

	/**
	 * encode 编码
	 */
	public static String encode(String code) {
		Base64 base64 = new Base64();
		try {
			return base64.encodeToString(code.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * decode 解码
	 */
	public static String decode(String code) {
		Base64 base64 = new Base64();
		try {
			return new String(base64.decode(code), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
