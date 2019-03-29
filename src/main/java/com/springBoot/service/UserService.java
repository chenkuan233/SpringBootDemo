package com.springBoot.service;

import com.springBoot.entity.Role;
import com.springBoot.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/1/17 017 14:20
 */
public interface UserService {

	List<User> findAllUser();

	User findById(Long id);

	List<User> findByIdIn(List<Long> ids);

	User findByUserName(String userCode);

	void saveOrUpdateUser(User user);

	// mapper查询，PageHelper分页
	List<User> findAllMapper();

	// mybatis查询
	User findByUserNameMapper(String userName);

	// mybatis查询，PageHelper分页
	User findByUserNameAndPasswordMapper(String userName, String password);

	// 通用mapper
	List<User> findAllMyMapper();

	// 通用mapper 插入
	void saveUserMyMapper(User user);

	// 通用mapper 更新User
	void updateUserMyMapper(User user);

	// 通用mapper 更新User
	Map<String, String> updatePasswordMyMapper(String userName, String oldPassword, String newPassword);

	// mybatis查询 ByUserNameAndPassword
	Long findRoleIdByUserId(Long userId);

	// 通过id 查找 role
	Role findRoleById(Long id);

	// 通过roleId 查找 permission_name
	Set<String> findPermissionNamesByRoleId(Long roleId);
}
