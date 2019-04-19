package com.springBoot.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 返回消息 0成功
 * @date 2019/2/28 028 14:57
 */
public class MessageUtil {

	public static Map<String, Object> returnData(Integer code, String data) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("data", data);
		return map;
	}

	public static Map<String, Object> returnData(Integer code, Object data) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("data", data);
		return map;
	}

	public static Map<String, String> message(String code, String msg) {
		Map<String, String> map = new HashMap<>();
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	public static Map<String, Object> message(String code, String msg, Object data) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;
	}
}
