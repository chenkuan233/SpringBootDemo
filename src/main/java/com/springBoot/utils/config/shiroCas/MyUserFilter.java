package com.springBoot.utils.config.shiroCas;

import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 在访问controller前判断是否登录，如果是ajax请求则返回json，不进行重定向
 * @date 2019/4/28 028 11:15
 */
public class MyUserFilter extends UserFilter {

	/**
	 * 认证失败后
	 *
	 * @param request
	 * @param response
	 * @return false
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if (isAjax(httpServletRequest)) {
			httpServletResponse.setCharacterEncoding("UTF-8");
			// servletResponse.setContentType("application/json");
			httpServletResponse.setStatus(403);
			return false;
		} else {
			return super.onAccessDenied(request, response);
		}
	}

	// 判断request请求是否是Ajax
	private boolean isAjax(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equalsIgnoreCase(header);
	}

}
