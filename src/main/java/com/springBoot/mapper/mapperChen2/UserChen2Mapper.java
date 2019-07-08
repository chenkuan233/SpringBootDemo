package com.springBoot.mapper.mapperChen2;

import com.springBoot.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc mapper
 * @date 2019/3/4 004 19:38
 */
public interface UserChen2Mapper {

	User findByUserName(@Param("userName") String userName);

	User findByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

	List<User> findAll();

	void saveUser(@Param("user") User user);

	void deleteUser(@Param("id") int id);
}
