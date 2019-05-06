package com.springBoot.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc Base64编解码 apache.commons.codec.binary.Base64
 * @date 2019/4/29 029 12:58
 */
public final class Base64Util {

	/**
	 * encode 编码
	 */
	public static String encode(String code) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		return base64.encodeToString(code.getBytes("UTF-8"));
	}

	/**
	 * decode 解码
	 */
	public static String decode(String code) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		return new String(base64.decode(code), "UTF-8");
	}

}
