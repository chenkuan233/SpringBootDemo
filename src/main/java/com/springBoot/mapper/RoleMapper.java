package com.springBoot.mapper;

import com.springBoot.entity.Role;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/3/25 025 14:39
 */
public interface RoleMapper {
	Role findById(@Param("id") Long id);
}
