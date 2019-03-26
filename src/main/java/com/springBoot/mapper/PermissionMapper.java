package com.springBoot.mapper;

import com.springBoot.entity.Permission;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/3/25 025 15:45
 */
public interface PermissionMapper {
	List<Permission> findByRoleId(Long roleId);
}
