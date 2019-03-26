package com.springBoot.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 字符串工具类
 * @date 2019/3/22 022 16:50
 */
public class StringUtil {

	/**
	 * 将null或空字符串转为null,并去掉末尾空格
	 */
	public static String convertToNull(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		return StringUtils.trim(str);
	}

	/**
	 * 左边补上0
	 *
	 * @param str       字符串
	 * @param strLength 长度
	 * @return
	 */
	public static String stringFormat(String str, int strLength) {
		if (null == str || "".equals(str) || "".equals(str.trim()) || 0 == strLength) return null;
		if (str.length() == strLength) return str;
		if (str.length() > strLength) return str.substring(str.length() - strLength, str.length());
		StringBuffer sb = null;
		while (str.length() < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左补0
			str = sb.toString();
		}
		return str;
	}

}
