package com.springBoot.mapper.CommonMapper;

import com.springBoot.commonMapper.MyMapper;
import com.springBoot.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 继承MyMapper实现通用mapper功能
 * @date 2019/3/15 015 16:11
 */
public interface UserCommonMapper extends MyMapper<User> {

	@Select("select * from t_user")
	List<User> findAll();
}
