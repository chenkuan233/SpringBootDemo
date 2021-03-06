package com.springBoot.service;

import com.springBoot.entity.Role;
import com.springBoot.entity.User;
import com.springBoot.utils.Pageable;
import com.springBoot.utils.annotation.PageQuery;

import java.util.List;
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
	Object findAllMapper(Pageable pageable);

	@PageQuery
	Object findAllMapperDB2(Pageable pageable);

	// mybatis 根据id删除
	void deleteMapper(int id);

	// mybatis查询
	User findByUserNameMapper(String userName);

	// 通用mapper 插入
	void saveUserMyMapper(User user);

	// 第2数据源 插入User
	void saveUserMapperDB2(User user);

	// mybatis查询 ByUserNameAndPassword
	Long findRoleIdByUserId(Long userId);

	// 通过id 查找 role
	Role findRoleById(Long id);

	// 通过roleId 查找 permission_name
	Set<String> findPermissionNamesByRoleId(Long roleId);
}
