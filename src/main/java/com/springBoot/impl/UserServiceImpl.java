package com.springBoot.impl;

import com.springBoot.entity.Permission;
import com.springBoot.entity.Role;
import com.springBoot.entity.User;
import com.springBoot.mapper.mapper.PermissionMapper;
import com.springBoot.mapper.mapper.RoleMapper;
import com.springBoot.mapper.mapper.UserMapper;
import com.springBoot.mapper.mapper.UserRoleMapper;
import com.springBoot.mapper.mapper.commonMapper.UserCommonMapper;
import com.springBoot.mapper.mapperChen2.UserChen2Mapper;
import com.springBoot.repository.UserRepository;
import com.springBoot.service.UserService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.Pageable;
import com.springBoot.utils.UserEncryptUtil;
import com.springBoot.utils.annotation.PageQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author chenkuan
 * @version v1.0
 * @desc userService层
 * @date 2019/1/17 017 14:19
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

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

	@Autowired
	private UserEncryptUtil userEncryptUtil;

	@Autowired
	private UserChen2Mapper userChen2Mapper;

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
	public void saveOrUpdateUser(User user) {
		if (user.getId() == null) {
			user.setRegDate(DateUtil.date());
			user.setRegTime(DateUtil.time());
			// 加密
			user = userEncryptUtil.encrypt(user);
			log.info("新增用户:" + user.getUserName());
		} else {
			log.info("更新用户:" + user.getUserName());
		}
		userRepository.save(user);
	}

	// mybatis查询，PageHelper分页
	@PageQuery
	@Override
	public Object findAllMapper(Pageable pageable) {
		List<User> list = userMapper.findAll();
		return list;
	}

	// 第2数据源 查询
	@PageQuery
	@Override
	public Object findAllMapperDB2(Pageable pageable) {
		List<User> list = userChen2Mapper.findAll();
		return list;
	}

	// mybatis 根据id删除
	@Override
	public void deleteMapper(int id) {
		userMapper.deleteUser(id);
	}

	// mybatis查询 ByUserName
	@Override
	public User findByUserNameMapper(String userName) {
		return userMapper.findByUserName(userName);
	}

	// mybatis查询 ByUserNameAndPassword
	/*@Override
	public User findByUserNameAndPasswordMapper(String userName, String password) {
		User user = userCommonMapper.selectOne(new User(userName));
		password = userEncryptUtil.encrypt(password, user.getCredentialsSalt());
		return userMapper.findByUserNameAndPassword(userName, password);
	}*/

	// 通用mapper 查询，PageHelper分页
	/*@Override
	public List<User> findAllMyMapper() {
		PageHelper.startPage(1, 5);
		List<User> list = userCommonMapper.selectAll();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return list;
	}*/

	// 通用mapper 插入User
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUserMyMapper(User user) {
		if (user != null) {
			user.setRegDate(DateUtil.date());
			user.setRegTime(DateUtil.time());
			// 加密
			user = userEncryptUtil.encrypt(user);
			userCommonMapper.insert(user);
			log.info("新增用户:" + user.getUserName());
		}
	}

	// 第2数据源 插入User
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUserMapperDB2(User user) {
		if (user != null) {
			user.setRegDate(DateUtil.date());
			user.setRegTime(DateUtil.time());
			// 加密
			user = userEncryptUtil.encrypt(user);
			userChen2Mapper.saveUser(user);
			log.info("新增用户:" + user.getUserName());
		}
	}

	// 通用mapper 更新User
	/*@Override
	public void updateUserMyMapper(User user) {
		userCommonMapper.updateByPrimaryKey(user);
		log.info("更新用户:" + user.getUserName());
	}*/

	// 通用mapper 修改用户密码
	/*@Override
	public Map<String, String> updatePasswordMyMapper(String userName, String oldPassword, String newPassword) {
		User user = userCommonMapper.selectOne(new User(userName));
		if (user == null) {
			log.error("密码修改失败：用户不存在");
			return Response.message("1", "用户不存在");
		}
		// 校验原密码
		oldPassword = userEncryptUtil.encrypt(oldPassword, user.getCredentialsSalt());
		if (!user.getPassword().equals(oldPassword)) {
			log.error("密码修改失败：用户原密码校验错误");
			return Response.message("2", "用户原密码校验错误");
		}
		// 重新加密
		user = userEncryptUtil.encrypt(user, newPassword);
		userCommonMapper.updateByPrimaryKey(user);
		log.info("用户" + user.getUserName() + "密码修改成功");
		return Response.message("0", "密码修改成功");
	}*/

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
