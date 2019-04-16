package com.springBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc HelloWorldController
 * @date 2019/1/17 017 10:33
 */
@Slf4j
@Controller
public class HelloWorldController {

	@GetMapping("/checkAdmin")
	public String home() {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.checkPermission("admin");
		} catch (UnauthorizedException e) {
			log.error("没有足够的权限", e);
		} catch (Exception e) {
			log.error("权限验证出错", e);
		}
		return "redirect:/error/403.html"; // 引入动态页面后，访问static页面需加redirect:重定向
	}

	@GetMapping("/hello")
	public ModelAndView hello(String str) {
		log.info(str);
		return new ModelAndView("index");
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
