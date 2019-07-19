package com.springBoot.service;

import com.springBoot.entity.User;
import com.springBoot.utils.Response;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/7/19 019 11:47
 */
public interface PersonalService {
	//修改个人密码
	Response changePassword(User user);
}
