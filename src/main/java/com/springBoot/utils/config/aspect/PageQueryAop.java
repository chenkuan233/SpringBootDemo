package com.springBoot.utils.config.aspect;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springBoot.utils.Pageable;
import com.springBoot.utils.annotation.PageQuery;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc PageHelper分页查询切面Aop
 * @date 2019/4/22 022 10:26
 */
@Slf4j
@Aspect
@Component
public class PageQueryAop {

	@Around("@annotation(pageQuery)")
	public Object pageQuery(ProceedingJoinPoint joinPoint, PageQuery pageQuery) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class<?> returnType = signature.getMethod().getReturnType();
		if (returnType != Object.class) {
			log.error("@PageQuery注解分页，方法返回值类型应为Object");
			throw new Throwable("@PageQuery注解分页，方法返回值类型应为Object");
		}
		Object[] args = joinPoint.getArgs();
		Integer pageNum = null;
		Integer pageSize = null;
		// 获取参数中的Pageable分页参数
		if (args.length > 0) {
			for (Object arg : args) {
				if (arg != null && arg.getClass() == Pageable.class) {
					Pageable pageable = (Pageable) arg;
					pageNum = pageable.getPageNum();
					pageSize = pageable.getPageSize();
					break;
				}
			}
		}
		// 执行分页
		if (pageNum == null || pageSize == null) {
			log.error("@PageQuery注解分页，Pageable分页参数为null");
			throw new Throwable("@PageQuery注解分页，Pageable分页参数为null");
		}
		try {
			PageHelper.startPage(pageNum, pageSize);
			Object result = joinPoint.proceed(args);
			// 建议自己实现返回类型，官方自带的返回数据太冗余了
			return new PageInfo<>((List<?>) result);
		} finally {
			// 保证线程变量被清除
			if (PageHelper.getLocalPage() != null) {
				PageHelper.clearPage();
			}
		}
	}

}
