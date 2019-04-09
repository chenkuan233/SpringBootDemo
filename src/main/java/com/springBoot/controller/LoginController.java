package com.springBoot.controller;

import com.springBoot.entity.User;
import com.springBoot.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenkuan
 * @version v1.0
 * @desc loginController
 * @date 2019/4/1 001 9:09
 */
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Value("${cas.service.loginUrl}")
	private String loginUrl;

	@GetMapping("/login")
	public String doLogin() {
		return loginUrl;
	}

	/**
	 * loginController
	 *
	 * @param request  request
	 * @param response response
	 * @param modelMap thymeleaf视图
	 */
	@RequestMapping(
			value = {"/login"},
			method = {RequestMethod.POST}
	)
	public String doLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Boolean rememberMe = StringUtils.isNotEmpty(request.getParameter("rememberMe"));
		String host = request.getRemoteAddr(); // 获得客户端的ip地址

		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe, host);
		Subject subject = SecurityUtils.getSubject();
		try {
			logger.info(username + "进行登录验证..验证开始");
			subject.login(token);
			logger.info(username + "进行登录验证..验证通过");
		} catch (UnknownAccountException e) {
			logger.error(username + "进行登录验证..验证未通过，未知账户");
			response.setHeader("errCode", "101");
			modelMap.addAttribute("errMsg", "未知账户");
		} catch (AuthenticationException e) {
			logger.error(username + "进行登录验证..验证未通过，账号密码不匹配");
			response.setHeader("errCode", "102");
			modelMap.addAttribute("errMsg", "账号密码不匹配");
		} catch (Exception e) {
			logger.error("登录出错", e);
			response.setHeader("errCode", "500");
			modelMap.addAttribute("errMsg", "登录出错");
		}

		// 验证是否登录成功
		if (subject.isAuthenticated()) {
			logger.info(username + " " + host + " 登录成功");
			// 将当前用户存入session
			Session session = subject.getSession();
			User user = userService.findByUserNameMapper(username);
			session.setAttribute("user", user);
			response.setHeader("errCode", "200");
			return "redirect:/index.html";
		} else {
			token.clear();
			return loginUrl;
		}
	}

}
