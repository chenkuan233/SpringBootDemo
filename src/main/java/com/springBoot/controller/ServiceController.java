package com.springBoot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.springBoot.utils.MessageUtil;
import com.springBoot.utils.ServiceUtil;
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
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 通用serviceController
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
		Map<String, String> paramMap = ServiceUtil.getRequestParams(request);
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
		Method method = ServiceUtil.getMethod(clazz, funcName);
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
		Object[] params = ServiceUtil.getMethodParams(array, parameters);

		// 执行方法（method) 在指定对象(target)上，使用指定参数(args)
		Object obj = method.invoke(service, params);

		return gson.toJson(MessageUtil.returnData(0, obj));
	}

}
