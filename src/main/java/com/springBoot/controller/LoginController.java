package com.springBoot.controller;

import com.springBoot.entity.User;
import com.springBoot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
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
			method = {RequestMethod.POST},
			produces = {"text/plain;charset=UTF-8"}
	)
	public String doLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String host = request.getRemoteAddr(); // 获得客户端的ip地址

		UsernamePasswordToken token = new UsernamePasswordToken(username, password, host);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			logger.info(username + " " + host + " 登录成功");

			// 当验证通过后，将用户信息存入session中
			User user = userService.findByUserNameMapper(username);
			SecurityUtils.getSubject().getSession().setAttribute("user", user);
			response.setHeader("errCode", "200");
			return "home";
		} catch (UnknownAccountException e) {
			logger.error("账号不存在");
			response.setHeader("errCode", "101");
			modelMap.addAttribute("errMsg", "用户名不存在");
		} catch (AuthenticationException e) {
			logger.error("账号密码校验失败");
			response.setHeader("errCode", "102");
			modelMap.addAttribute("errMsg", "账号密码校验失败");
		} catch (Exception e) {
			logger.error("登录出错", e);
			response.setHeader("errCode", "500");
			modelMap.addAttribute("errMsg", "登录出错");
		}
		return "login";
	}
}
