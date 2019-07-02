package com.springBoot.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 返回消息 0成功
 * @date 2019/2/28 028 14:57
 */
public final class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	//消息码
	private String code;

	//消息内容
	private String msg;

	//数据
	private Object data;

	public Response() {
	}

	public Response(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Response(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
