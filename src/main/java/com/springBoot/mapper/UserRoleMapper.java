package com.springBoot.mapper;

import com.springBoot.entity.UserRole;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/3/25 025 11:45
 */
public interface UserRoleMapper {

	Long findRoleIdByUserId(@Param("userId") Long userId);
}
