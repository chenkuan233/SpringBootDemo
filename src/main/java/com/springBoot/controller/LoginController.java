package com.springBoot.controller;

import com.google.gson.Gson;
import com.springBoot.entity.User;
import com.springBoot.utils.Response;
import com.springBoot.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenkuan
 * @version v1.0
 * @desc loginController
 * @date 2019/4/1 001 9:09
 */
@Slf4j
@Controller
public class LoginController {

	@Value("${login.loginUrl}")
	private String loginUrl;

	@Value("${login.indexUrl}")
	private String indexUrl;

	private Gson gson = new Gson();

	/**
	 * 登录请求
	 */
	@GetMapping("/login")
	public String doLogin() {
		return "redirect:" + loginUrl;
	}

	/**
	 * 登出请求
	 */
	@GetMapping("/logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		// shiro 默认登出 自动清除权限、session、cookie等
		subject.logout();
		log.info(request.getRemoteAddr() + " 安全登出");
		return "redirect:" + loginUrl;
	}

	/**
	 * loginController登录服务
	 *
	 * @param request  request
	 * @param response response
	 */
	@PostMapping("/login")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Boolean rememberMe = StringUtils.isNotEmpty(request.getParameter("rememberMe"));
		String host = request.getRemoteAddr(); // 获得客户端的ip地址

		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe, host);
		Subject subject = SecurityUtils.getSubject();
		String message = "";
		try {
			log.info(username + "进行登录验证..验证开始");
			subject.login(token);
		} catch (UnknownAccountException e) {
			log.error(username + "进行登录验证..验证未通过，用户不存在");
			message = "用户不存在";
		} catch (AuthenticationException e) {
			log.error(username + "进行登录验证..验证未通过，密码输入错误");
			message = "密码输入错误";
		} catch (Exception e) {
			log.error("登录服务异常", e);
			token.clear();
			return gson.toJson(Response.returnData(-1, "服务异常"));
		}

		// 验证是否登录成功
		if (subject.isAuthenticated()) {
			log.info(username + " " + host + " 登录成功");
			// 将当前用户存入session
			User user = (User) subject.getPrincipal();
			// 去掉密码和加密盐保护安全
			user.setPassword(null);
			user.setCredentialsSalt(null);
			SessionUtil.saveUserInfo(user);
			return gson.toJson(Response.returnData(0, new Response("0", user)));
		} else {
			token.clear();
			return gson.toJson(Response.returnData(0, new Response("-1", message)));
		}
	}
}
