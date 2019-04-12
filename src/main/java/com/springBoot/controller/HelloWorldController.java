package com.springBoot.controller;

import com.springBoot.entity.User;
import com.springBoot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc HelloWorldController
 * @date 2019/1/17 017 10:33
 */
@Controller
public class HelloWorldController {

	private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/home")
	public String home() {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.checkPermission("admin");
		} catch (UnauthorizedException e) {
			logger.error("没有足够的权限", e);
		}
		return "index";
	}

	@GetMapping("/hello")
	public ModelAndView hello(String str) {
		logger.info(str);
		return new ModelAndView("index");
	}

	@GetMapping("/findUser")
	public User findUser(String userName) {
		return userService.findByUserName(userName);
	}

	@RequestMapping(
			value = {"/saveUser"},
			method = {RequestMethod.POST},
			produces = {"text/plain;charset=UTF-8"})
	public void saveUser(User user) {
		userService.saveOrUpdateUser(user);
	}

	// 测试 freemarker 页面模板
	@RequestMapping("/ftlIndex")
	public String ftlIndex(Map<String, Object> map) {
		map.put("name", "chenkuan");
		map.put("age", "22");
		map.put("sex", 0);
		return "/freemarker/ftlIndex";
	}
}
