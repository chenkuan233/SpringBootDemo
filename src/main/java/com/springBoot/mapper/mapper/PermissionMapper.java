package com.springBoot.mapper.mapper;

import com.springBoot.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/3/25 025 15:45
 */
public interface PermissionMapper {
	List<Permission> findByRoleId(@Param("roleId") Long roleId);
}
