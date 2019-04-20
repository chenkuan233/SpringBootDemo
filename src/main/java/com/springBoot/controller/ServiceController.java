package com.springBoot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.springBoot.utils.MessageUtil;
import com.springBoot.utils.config.applicationContext.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 通用serviceController
 * 注：该controller暂不支持调用参数中有List<>类型的方法(可支持List<String>)
 * @date 2019/3/1 001 14:21
 */
@Slf4j
@SuppressWarnings("unchecked")
@Controller
public class ServiceController {

	private static Gson gson = new Gson();
	private static JsonParser jsonParser = new JsonParser();

	/**
	 * POST请求
	 *
	 * @param request     请求
	 * @param response    响应
	 * @param serviceName 请求服务名
	 * @param funcName    请求方法名
	 * @return 执行方法返回值
	 */
	@ResponseBody
	@RequestMapping(
			value = {"/service/{serviceName}/{funcName}"},
			method = {RequestMethod.POST},
			produces = {"text/plain;charset=UTF-8"}
	)
	public String doPost(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceName, @PathVariable String funcName) throws Exception {
		return doService(request, response, serviceName, funcName);
	}

	/**
	 * GET请求
	 *
	 * @param request     请求
	 * @param response    响应
	 * @param serviceName 请求服务名
	 * @param funcName    请求方法名
	 * @return 执行方法返回值
	 */
	@ResponseBody
	@RequestMapping(
			value = {"/service/{serviceName}/{funcName}"},
			method = {RequestMethod.GET},
			produces = {"text/plain;charset=UTF-8"}
	)
	public String doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceName, @PathVariable String funcName) throws Exception {
		return doService(request, response, serviceName, funcName);
	}

	/**
	 * doService 请求处理
	 *
	 * @param request     请求
	 * @param response    响应
	 * @param serviceName 请求服务名
	 * @param funcName    请求方法名
	 * @return 方法返回值的json字符串
	 */
	private String doService(HttpServletRequest request, HttpServletResponse response, String serviceName, String funcName) throws Exception {
		// 判断serviceBean是否存在
		List<String> beanNames = SpringBeanUtil.getBeanNames();
		if (!beanNames.contains(serviceName)) {
			log.error("请求无对应服务: " + serviceName + "." + funcName);
			return gson.toJson(MessageUtil.returnData(-1, "请求无对应服务: " + serviceName + "." + funcName));
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
		// 请求参数处理
		String dataJson = paramMap.get("params");
		if (StringUtils.isEmpty(dataJson)) {
			log.error("params请求参数错误");
			return gson.toJson(MessageUtil.returnData(-1, "params请求参数错误"));
		}
		// 转为JsonArray
		JsonArray array = jsonParser.parse(dataJson).getAsJsonArray();

		// 获取serviceBean
		Object service = SpringBeanUtil.getBean(serviceName);
		Class<?> clazz = service.getClass();

		// 获取funcName所在的接口下的Method
		Method method = getMethod(clazz, funcName);
		if (method == null) {
			log.error("请求无对应方法: " + serviceName + "." + funcName);
			return gson.toJson(MessageUtil.returnData(-1, "请求无对应方法: " + serviceName + "." + funcName));
		}

		// 获取该方法的参数
		Parameter[] parameters = method.getParameters();
		if (array.size() != parameters.length) {
			log.error("参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length);
			return gson.toJson(MessageUtil.returnData(-1, "参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length));
		}

		// 请求参数反序列化为对应方法参数类型
		Object[] params = getMethodParams(array, parameters);

		// 执行方法（method) 在指定对象(target)上，使用指定参数(args)
		Object obj = method.invoke(service, params);

		return gson.toJson(MessageUtil.returnData(0, obj));
	}

	/**
	 * 将json字符串参数转为方法对应参数对象
	 *
	 * @param array      request请求方法传入参数
	 * @param parameters 所请求的方法参数名数组
	 * @return 参数转为Object[]数组
	 * @throws Exception
	 */
	private static Object[] getMethodParams(JsonArray array, Parameter[] parameters) throws Exception {
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
				} catch (Exception e) {
					throw new Exception("json反序列化失败.param=" + param, e);
				}
			}
		}
		return params;
	}

	/**
	 * 获取funcName所在的接口下的Method
	 *
	 * @param clazz    目标类.class
	 * @param funcName 访问方法名
	 * @returnd method
	 */
	private static Method getMethod(Class<?> clazz, String funcName) {
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

}
