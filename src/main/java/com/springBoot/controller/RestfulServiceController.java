package com.springBoot.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.springBoot.utils.Response;
import com.springBoot.utils.ServiceUtil;
import com.springBoot.utils.config.applicationContext.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 测试用 RestfulServiceController
 * @date 2019/4/20 001 10:04
 */
@Slf4j
@SuppressWarnings("unchecked")
@Controller
public class RestfulServiceController {

	private static Gson gson = new Gson();

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
			value = {"/restfulService/{serviceName}/{funcName}"},
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
			value = {"/restfulService/{serviceName}/{funcName}"},
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
			return gson.toJson(Response.returnData(-1, "请求无对应服务: " + serviceName + "." + funcName));
		}

		// 获取request请求方法参数
		Map<String, String> paramMap = ServiceUtil.getRequestParams(request);

		// 获取serviceBean
		Object service = SpringBeanUtil.getBean(serviceName);

		// 获取指定方法(null：忽略参数; 不能存在方法的重载)
		Method method = ServiceUtil.getMethod(service.getClass(), funcName);
		// Method method = ReflectionUtils.findMethod(service.getClass(), funcName, (Class<?>[]) null);
		if (method == null) {
			log.error("请求无对应方法: " + serviceName + "." + funcName);
			return gson.toJson(Response.returnData(-1, "请求无对应方法: " + serviceName + "." + funcName));
		}

		// 获取该方法的参数, 校验参数
		Parameter[] parameters = method.getParameters();
		if (parameters.length != paramMap.size()) {
			log.error("参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length);
			return gson.toJson(Response.returnData(-1, "参数个数校验失败, " + funcName + "要求参数个数为: " + parameters.length));
		}

		// 请求参数赋值给方法参数
		Object[] params = getMethodParams(paramMap, parameters);

		// 反射执行方法并获取返回结果
		Object obj = ReflectionUtils.invokeMethod(method, service, params);

		return gson.toJson(Response.returnData(0, obj));
	}

	/**
	 * 方法参数赋值 方法参数名: arg0,arg1,..  将json字符串参数转为方法对应参数对象
	 *
	 * @param paramMap   request请求方法传入参数
	 * @param parameters 所请求的方法参数名数组
	 * @return 参数转为Object[]数组
	 * @throws Exception
	 */
	private static Object[] getMethodParams(Map<String, String> paramMap, Parameter[] parameters) throws Exception {
		Object[] params = null;
		if (parameters.length > 0 && paramMap.size() > 0) {
			params = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				if (!paramMap.containsKey(parameters[i].getName())) {
					throw new Exception("方法要求参数" + parameters[i].getName() + "未找到");
				}
				Type type = parameters[i].getParameterizedType();
				String param = paramMap.get(parameters[i].getName());
				if (StringUtils.isEmpty(param)) {
					params[i] = null;
					continue;
				}
				// 方法参数反序列化
				try {
					// 将字符串格式化为json字符串
					String jsonStr = gson.toJson(new JsonParser().parse(param));
					// 反序列化为对象clazz
					params[i] = gson.fromJson(jsonStr, type);
				} catch (Exception e) {
					throw new Exception("json反序列化失败.value=" + param, e);
				}
			}
		}
		return params;
	}

}
