package com.springBoot.mapper.mapper;

import com.springBoot.entity.PersonalImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/7/9 009 15:19
 */
public interface PersonalImageMapper {

	void deleteByNameAndUrl(@Param("name") String name, @Param("url") String url);

	List<PersonalImage> findByUserName(@Param("userName") String userName);
}
