package com.springBoot.utils.config.aspect;

import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 用来记录每个接口方法的执行时间
 * @date 2019/3/2 002 15:22
 */
@Aspect
@Component
public class TimeInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(TimeInterceptor.class);

	private Gson gson = new Gson();

	// service层的统计耗时切面，类型必须为final String类型的,注解里要使用的变量只能是静态常量类型的
	private static final String POINT = "execution(public * com..impl..*.*(..))";

	/**
	 * 统计方法执行耗时Around环绕通知
	 */
	@Around(POINT)
	public Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable, Exception {
		// 获取服务、方法名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

		// 定义返回对象、得到方法需要的参数
		Object obj = null;
		Object[] args = joinPoint.getArgs();

		logger.info("进入服务: " + methodName + " 参数: " + gson.toJson(args));

		// 开始计时
		long startTime = System.currentTimeMillis();

		// 执行方法
		obj = joinPoint.proceed(args);

		// 结束计时
		long endTime = System.currentTimeMillis();

		// 耗时
		long diffTime = endTime - startTime;

		logger.info("离开服务: " + methodName + " 耗时: " + diffTime + "ms");

		return obj;
	}
}
