package com.springBoot.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springBoot.entity.Permission;
import com.springBoot.entity.Role;
import com.springBoot.entity.User;
import com.springBoot.mapper.CommonMapper.UserCommonMapper;
import com.springBoot.mapper.PermissionMapper;
import com.springBoot.mapper.RoleMapper;
import com.springBoot.mapper.UserMapper;
import com.springBoot.mapper.UserRoleMapper;
import com.springBoot.repository.UserRepository;
import com.springBoot.service.UserService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.MD5Util;
import com.springBoot.utils.MessageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenkuan
 * @version v1.0
 * @desc userService层
 * @date 2019/1/17 017 14:19
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserCommonMapper userCommonMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<User> findAllUser() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		Optional<User> opt = userRepository.findById(id);
		return opt.orElse(null);
	}

	@Override
	public List<User> findByIdIn(List<Long> ids) {
		return userRepository.findByIdIn(ids);
	}

	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public User findByUserNameAndPassword(String userName, String password) {
		return userRepository.findByUserNameAndPassword(userName, MD5Util.md5(password));
	}

	@Override
	public void saveOrUpdateUser(User user) {
		if (user.getId() == null) {
			user.setRegDate(DateUtil.date());
			user.setRegTime(DateUtil.time());
			user.setPassword(MD5Util.md5(user.getPassword())); // MD5加密
			logger.info("新增用户:" + user.getUserName());
		} else {
			logger.info("更新用户:" + user.getUserName());
		}
		userRepository.save(user);
	}

	// mybatis查询，PageHelper分页
	@Override
	public List<User> findAllMapper() {
		PageHelper.startPage(1, 3);
		List<User> list = userMapper.findAll();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return list;
	}

	// mybatis查询 ByUserName
	@Override
	public User findByUserNameMapper(String userName) {
		return userMapper.findByUserName(userName);
	}

	// mybatis查询 ByUserNameAndPassword
	@Override
	public User findByUserNameAndPasswordMapper(String userName, String password) {
		return userMapper.findByUserNameAndPassword(userName, MD5Util.md5(password));
	}

	// 通用mapper 查询，PageHelper分页
	@Override
	public List<User> findAllMyMapper() {
		PageHelper.startPage(1, 5);
		List<User> list = userCommonMapper.selectAll();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return list;
	}

	// 通用mapper 插入User
	@Override
	public void saveUserMyMapper(User user) {
		if (user != null) {
			user.setRegDate(DateUtil.date());
			user.setRegTime(DateUtil.time());
			user.setPassword(MD5Util.md5(user.getPassword())); // MD5加密
			userCommonMapper.insert(user);
			logger.info("新增用户:" + user.getUserName());
		}
	}

	// 通用mapper 更新User
	@Override
	public void updateUserMyMapper(User user) {
		userCommonMapper.updateByPrimaryKey(user);
		logger.info("更新用户:" + user.getUserName());
	}

	// 通用mapper 修改用户密码
	@Override
	public Map<String, String> updatePasswordMyMapper(Long id, String oldPassword, String newPassword) {
		User user = userCommonMapper.selectByPrimaryKey(id);
		if (user == null) {
			logger.error("密码修改失败：用户不存在");
			return MessageUtil.message("1", "用户不存在");
		}
		// 校验原密码
		if (!user.getPassword().equals(MD5Util.md5(oldPassword))) {
			logger.error("密码修改失败：用户原密码校验错误");
			return MessageUtil.message("2", "用户原密码校验错误");
		}
		// 原密码与新密码是否相同
		newPassword = MD5Util.md5(newPassword);
		if (user.getPassword().equals(newPassword)) {
			logger.error("密码修改失败：用户密码未改变");
			return MessageUtil.message("3", "用户密码未改变");
		}
		// 修改为新密码
		user.setPassword(newPassword);
		userCommonMapper.updateByPrimaryKey(user);
		logger.info("用户" + user.getUserName() + "密码修改成功");
		return MessageUtil.message("0", "密码修改成功");
	}

	// mybatis查询 findRoleIdByUserId
	@Override
	public Long findRoleIdByUserId(Long userId) {
		return userRoleMapper.findRoleIdByUserId(userId);
	}

	// 通过id 查找 role
	@Override
	public Role findRoleById(Long id) {
		return roleMapper.findById(id);
	}

	// 通过roleId 查找 permission_name
	@Override
	public Set<String> findPermissionNamesByRoleId(Long roleId) {
		List<Permission> permissionList = permissionMapper.findByRoleId(roleId);
		if (CollectionUtils.isEmpty(permissionList)) return null;
		Set<String> set = new HashSet<>();
		permissionList.forEach(x -> set.add(x.getPermissionName()));
		return set;
	}
}
