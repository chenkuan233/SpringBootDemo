package com.springBoot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.springBoot.utils.MessageUtil;
import com.springBoot.utils.config.applicationContext.SpringBeanUtil;
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
 * 注：该controller暂不支持调用参数中有List<>类型的方法(可支持List<String>)
 * @date 2019/3/1 001 14:21
 */
@SuppressWarnings("unchecked")
@Controller
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	private Gson gson = new Gson();

	/**
	 * POST请求
	 *
	 * @param request     请求
	 * @param response    响应
	 * @param serviceName 请求服务名
	 * @param funcName    请求方法名
	 * @return 执行方法返回值
	 */
	@RequestMapping(
			value = {"/service/{serviceName}/{funcName}"},
			method = {RequestMethod.POST},
			produces = {"text/plain;charset=UTF-8"})
	@ResponseBody
	public String doPost(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceName, @PathVariable String funcName) {
		return doService(request, response, serviceName, funcName, RequestMethod.POST);
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
	@RequestMapping(
			value = {"/service/{serviceName}/{funcName}"},
			method = {RequestMethod.GET},
			produces = {"text/plain;charset=UTF-8"})
	@ResponseBody
	public String doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String serviceName, @PathVariable String funcName) {
		return doService(request, response, serviceName, funcName, RequestMethod.GET);
	}

	/**
	 * doService 请求处理
	 *
	 * @param request       请求
	 * @param response      响应
	 * @param serviceName   请求服务名
	 * @param funcName      请求方法名
	 * @param requestMethod 请求类型
	 * @return 方法返回值的json字符串
	 */
	private String doService(HttpServletRequest request, HttpServletResponse response, String serviceName, String funcName, RequestMethod requestMethod) {
		try {
			// 判断serviceBean是否存在
			List<String> beanNames = SpringBeanUtil.getBeanNames();
			if (!beanNames.contains(serviceName)) {
				logger.error("请求无对应服务: " + serviceName + "." + funcName);
				response.setHeader("error_code", "104");
				return gson.toJson(MessageUtil.message(-1, "请求无对应服务: " + serviceName + "." + funcName));
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
			Object service = SpringBeanUtil.getBean(serviceName);

			// 获取指定方法(null：忽略参数; 不能存在方法的重载)
			Method method = ReflectionUtils.findMethod(service.getClass(), funcName, (Class<?>[]) null);
			if (method == null) {
				logger.error("请求无对应方法: " + funcName);
				response.setHeader("error_code", "105");
				return gson.toJson(MessageUtil.message(-1, "请求无对应方法: " + funcName));
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
			logger.info("进入服务: " + serviceName + "." + funcName + " 参数: " + gson.toJson(params));
			// 执行方法
			Object obj = ReflectionUtils.invokeMethod(method, service, params);
			// 处理返回数据
			String returnData = gson.toJson(obj);
			logger.info("离开服务: " + serviceName + "." + funcName + " " + requestMethod + " return: " + (returnData == null ? null : returnData.length() > 300 ? returnData.substring(0, 300) + "..." : returnData));

			response.setHeader("error_code", "200");
			return returnData;
		} catch (Exception e) {
			logger.error("serviceController出错", e);
			response.setHeader("error_code", "500");
			return gson.toJson(MessageUtil.message(-1, "serviceController出错" + e));
		}
	}

	/**
	 * 方法参数赋值 方法参数名: arg0,arg1,..  将json字符串参数转为方法对应参数对象
	 *
	 * @param paramMap   request请求方法传入参数
	 * @param parameters 所请求的方法参数名数组
	 * @return 参数转为Object[]数组
	 * @throws Exception
	 */
	private Object[] getMethodParams(Map<String, String> paramMap, Parameter[] parameters) throws Exception {
		Object[] params = null;
		if (parameters.length > 0 && paramMap.size() > 0) {
			params = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				if (!paramMap.containsKey(parameters[i].getName())) {
					throw new Exception("方法要求参数" + parameters[i].getName() + "未找到");
				}
				Class clazz = parameters[i].getType();
				String value = paramMap.get(parameters[i].getName());
				// 方法参数反序列化
				try {
					// 将字符串格式化为json字符串
					String jsonStr = gson.toJson(new JsonParser().parse(value));
					// 反序列化为对象clazz
					params[i] = gson.fromJson(jsonStr, clazz);
				} catch (Exception e) {
					throw new Exception("json反序列化失败.value=" + value, e);
				}
			}
		}
		return params;
	}

}
