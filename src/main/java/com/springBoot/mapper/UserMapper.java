package com.springBoot.mapper;

import com.springBoot.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc mapper
 * @date 2019/3/4 004 19:38
 */
public interface UserMapper {

	User findByUserName(@Param("userName") String userName);

	User findByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

	List<User> findAll();

	Integer saveUser(@Param("user") User user);
}
