package com.springBoot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.springBoot.utils.MessageUtil;
import com.springBoot.utils.systemUtils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 通用serviceController
 * @date 2019/3/1 001 14:21
 */
@SuppressWarnings("unchecked")
@Controller
public class serviceController {

	private static final Logger logger = LoggerFactory.getLogger(serviceController.class);

	private Gson gson = new Gson();

	/**
	 * 注解RequestMapping中produces属性可以设置返回数据的类型以及编码，可以是json或者xml
	 */
	@RequestMapping(
			value = {"/service/{serviceName}/{funcName}"},
			method = {RequestMethod.POST},
			produces = {"text/plain;charset=UTF-8"})
	@ResponseBody
	public String doService(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceName, @PathVariable String funcName) {
		try {
			// 判断serviceBean是否存在
			List<String> beanNames = SpringContextUtil.getAllBeanNames();
			if (!beanNames.contains(serviceName)) {
				logger.error("请求无对应服务 " + serviceName + "." + funcName);
				response.setHeader("error_code", "104");
				return gson.toJson(MessageUtil.message(-1, "请求无对应服务 " + serviceName + "." + funcName));
			}

			// 获取request请求方法参数
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

			// 获取serviceBean
			Object service = SpringContextUtil.getBean(serviceName);

			// 获取指定方法(null：忽略参数; 不能存在方法的重载)
			Method method = ReflectionUtils.findMethod(service.getClass(), funcName, (Class<?>[]) null);
			if (method == null) {
				logger.error("请求无对应方法 " + funcName);
				response.setHeader("error_code", "104");
				return gson.toJson(MessageUtil.message(-1, "请求无对应方法 " + funcName));
			}

			// 获取该方法的参数, 校验参数
			Parameter[] parameters = method.getParameters();
			if (parameters.length != paramMap.size()) {
				logger.error("参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length);
				return gson.toJson(MessageUtil.message(-1, "参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length));
			}

			// 请求参数赋值给方法参数
			Object[] params = getMethodParams(paramMap, parameters);

			// 反射执行方法并获取返回结果
			logger.info("进入方法：" + serviceName + "." + funcName + " 参数：" + gson.toJson(params));
			Object obj = ReflectionUtils.invokeMethod(method, service, params);
			String returnData = gson.toJson(obj);
			logger.info(serviceName + "." + funcName + " POST return: " + (returnData == null ? null : returnData.length() > 300 ? returnData.substring(0, 300) + "..." : returnData));

			response.setHeader("error_code", "200");
			return gson.toJson(obj);
		} catch (Exception e) {
			logger.error("serviceController出错", e);
			response.setHeader("error_code", "405");
			return gson.toJson(MessageUtil.message(-1, "serviceController出错" + e));
		}
	}

	// 方法参数赋值 方法参数名: arg0,arg1,..  将json字符串参数转为方法对应参数对象
	private Object[] getMethodParams(Map<String, String> paramMap, Parameter[] parameters) throws Exception {
		Object[] params = null;
		if (parameters.length > 0 && paramMap.size() > 0) {
			params = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				Class clazz = parameters[i].getType();
				String value = paramMap.get(parameters[i].getName());
				if (value == null) {
					throw new Exception("获取的方法参数名错误，参数" + parameters[i].getName() + "无法赋值");
				}
				// 判断方法参数类型
				if (isBaseType(clazz)) {
					// 基本数据类型
					try {
						String jsonStr = new JsonParser().parse(value).getAsString();
						params[i] = gson.fromJson(gson.toJson(jsonStr), clazz);
					} catch (Exception e) {
						throw new Exception("json反序列化失败", e);
					}
				} else if (List.class.equals(clazz) && parameters[i].getParameterizedType() instanceof Class) {
					// 集合
					try {
						JsonArray jsonArray = new JsonParser().parse(value).getAsJsonArray();
						params[i] = gson.fromJson(gson.toJson(jsonArray), parameters[i].getParameterizedType());
					} catch (Exception e) {
						throw new Exception("json反序列化失败", e);
					}
				} else {
					// 对象
					try {
						JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
						params[i] = gson.fromJson(gson.toJson(jsonObject), clazz);
					} catch (Exception e) {
						throw new Exception("json反序列化失败", e);
					}
				}
			}
		}
		return params;
	}

	// 判断是否基本数据类型
	private boolean isBaseType(Class clazz) {
		return String.class.equals(clazz) ||
				Long.class.equals(clazz) || Long.TYPE.equals(clazz) ||
				Integer.class.equals(clazz) || Integer.TYPE.equals(clazz) ||
				Double.class.equals(clazz) || Double.TYPE.equals(clazz) ||
				Float.class.equals(clazz) || Float.TYPE.equals(clazz) ||
				Short.class.equals(clazz) || Short.TYPE.equals(clazz);
	}
}
