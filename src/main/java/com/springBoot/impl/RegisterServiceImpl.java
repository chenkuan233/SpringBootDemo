package com.springBoot.impl;

import com.springBoot.entity.User;
import com.springBoot.mapper.mapper.UserMapper;
import com.springBoot.service.RegisterService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.MessageUtil;
import com.springBoot.utils.UserEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 注册账号
 * @date 2019/4/20 020 0:56
 */
@Slf4j
@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserEncryptUtil userEncryptUtil;

	// 判断username是否存在
	@Override
	public User findByUserName(String userName) {
		return userMapper.findByUserName(userName);
	}

	// 注册账号
	@Override
	public Map<String, String> registerAccount(String username, String password) {
		User user = new User();
		user.setUserName(username);
		user.setPassword(password);
		user = userEncryptUtil.encrypt(user); // 对密码加密并保存加密盐
		user.setRegDate(DateUtil.date());
		user.setRegTime(DateUtil.time());
		try {
			userMapper.saveUser(user);
			log.info("注册用户:" + username);
		} catch (Exception e) {
			log.error(username + "注册失败", e);
			return MessageUtil.message("-1", "注册失败");
		}
		return MessageUtil.message("0", "注册成功");
	}
}
