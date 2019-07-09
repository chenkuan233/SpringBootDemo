package com.springBoot.repository;

import com.springBoot.entity.PersonalImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 个人相册repository 只用于保存save
 * @date 2019/7/8 008 17:53
 */
@Repository
public interface PersonalImageRepository extends CrudRepository<PersonalImage, Long> {
}
