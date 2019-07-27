package com.springBoot.service.impl;

import com.springBoot.entity.User;
import com.springBoot.mapper.mapper.UserMapper;
import com.springBoot.repository.UserRepository;
import com.springBoot.service.PersonalService;
import com.springBoot.utils.Response;
import com.springBoot.utils.SessionUtil;
import com.springBoot.utils.UserEncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 个人中心服务
 * @date 2019/7/19 019 11:47
 */
@Service("personalService")
public class PersonalServiceImpl implements PersonalService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEncryptUtil userEncryptUtil;

	//修改个人密码
	@Override
	public Response changePassword(User user) {
		String oldPassword = user.getOldPassword();
		String newPassword = user.getPassword();
		if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword))
			return new Response("-1", "密码不能为空");

		//校验旧密码
		user = userMapper.findByUserName(SessionUtil.getUserName());
		if (user == null)
			return new Response("-1", "未找到当前用户信息");
		oldPassword = userEncryptUtil.encrypt(oldPassword, user.getCredentialsSalt());
		if (!oldPassword.equals(user.getPassword()))
			return new Response("-1", "原密码输入错误");
		if (user.getPassword().equals(userEncryptUtil.encrypt(newPassword, user.getCredentialsSalt())))
			return new Response("-1", "新密码与原密码相同，请重新输入");

		//保存新密码
		user.setPassword(newPassword);
		user = userEncryptUtil.encrypt(user);
		userRepository.save(user);
		return new Response("0", "密码修改成功");
	}
}
