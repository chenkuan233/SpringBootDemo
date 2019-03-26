package com.springBoot.service;

import com.springBoot.entity.User;

import java.util.Set;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/3/26 026 12:33
 */
public interface RedisService {
	boolean saveString(String key, String value);

	boolean saveUser(String key, User user);

	Object getByKey(String key);

	boolean hasKey(String key);

	void delByKey(String key);

	Set<String> getKeysByPrefix(String prefix);
}
