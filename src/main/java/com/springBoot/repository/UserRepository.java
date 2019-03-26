package com.springBoot.repository;

import com.springBoot.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/1/17 017 14:15
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findByIdIn(List<Long> ids);

	User findByUserName(String userName);

	User findByUserNameAndPassword(String userName, String password);
}
