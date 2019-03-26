package com.springBoot.controller;

import com.springBoot.entity.User;
import com.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/1/17 017 10:33
 */
@RestController
@RequestMapping("/user")
public class HelloWorldController {

	@Autowired
	private UserService userService;

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
}
