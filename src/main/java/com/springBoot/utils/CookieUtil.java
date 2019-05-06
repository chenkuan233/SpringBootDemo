package com.springBoot.utils;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 用于获取cookie
 * @date 2019/4/23 023 10:18
 */
public final class CookieUtil {

	/**
	 * 获取指定名称cookie
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = readCookieMap(request);
		return cookieMap.getOrDefault(name, null);
	}

	/**
	 * 将cookie封装成Map
	 */
	private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<>();
		Cookie[] cookies = request.getCookies();
		if (ArrayUtils.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * 删除指定名称cookie
	 */
	public static void removeCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie != null) {
			cookie.setPath("/");
			cookie.setMaxAge(0);
			cookie.setValue(null);
			response.addCookie(cookie);
		}
	}

}
