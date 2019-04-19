package com.springBoot.service;

import com.springBoot.entity.User;

import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 注册账号服务接口
 * @date 2019/4/20 020 0:56
 */
public interface RegisterService {
	// 判断username是否存在
	User findByUserName(String userName);

	// 注册账号
	Map<String, String> registerAccount(String username, String password);
}
