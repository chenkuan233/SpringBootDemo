package com.springBoot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc serviceController工具类，核心类
 * @date 2019/4/20 020 18:39
 */
public class ServiceUtil {

	private static Gson gson = new Gson();

	/**
	 * 获取request请求方法参数
	 *
	 * @param request HttpServletRequest
	 * @return paramMap
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		if (request.getParameterNames() != null && request.getParameterNames().hasMoreElements()) {
			Enumeration<String> enumList = request.getParameterNames();
			String key = null;
			String value = null;
			while (enumList.hasMoreElements()) {
				key = enumList.nextElement();
				value = request.getParameter(key);
				paramMap.put(key, value);
			}
		}
		return paramMap;
	}

	/**
	 * 获取funcName所在的接口下的Method
	 *
	 * @param clazz    目标类.class
	 * @param funcName 访问方法名
	 * @return method
	 */
	public static Method getMethod(Class<?> clazz, String funcName) {
		Method method = null;

		// 获取该类的所有接口
		Class<?>[] interfaces = null;
		if (clazz.isInterface()) {
			interfaces = new Class[]{clazz};
		} else {
			interfaces = clazz.getInterfaces();
		}

		// 遍历接口
		for (int var1 = 0; var1 < interfaces.length && method == null; var1++) {
			Method[] methods = interfaces[var1].getMethods();
			for (int var2 = 0; var2 < methods.length && method == null; var2++) {
				if (funcName.equals(methods[var2].getName())) {
					method = methods[var2];
				}
			}
		}
		return method;
	}

	/**
	 * 将json字符串参数转为方法对应参数对象
	 *
	 * @param array      request请求方法传入参数
	 * @param parameters 所请求的方法参数名数组
	 * @return 参数转为Object[]数组
	 * @throws Exception
	 */
	public static Object[] getMethodParams(JsonArray array, Parameter[] parameters) throws JsonSyntaxException {
		Object[] params = null;
		if (parameters.length > 0) {
			params = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				Type clazz = parameters[i].getParameterizedType();
				JsonElement param = array.get(i);
				// 方法参数反序列化
				try {
					if (param.isJsonNull()) {
						params[i] = null;
						continue;
					}
					// 反序列化为对象clazz
					params[i] = gson.fromJson(param.toString(), clazz);
				} catch (JsonSyntaxException e) {
					throw new JsonSyntaxException("json反序列化失败.param=" + param, e);
				}
			}
		}
		return params;
	}

}
