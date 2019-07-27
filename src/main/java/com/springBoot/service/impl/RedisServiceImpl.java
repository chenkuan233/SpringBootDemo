package com.springBoot.service.impl;

import com.springBoot.entity.User;
import com.springBoot.service.RedisService;
import com.springBoot.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author chenkuan
 * @version v1.0
 * @desc redis测试
 * @date 2019/3/26 026 12:32
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public boolean saveString(String key, String value) {
		return redisUtil.set(key, value);
	}

	@Override
	public boolean saveUser(String key, User user) {
		return redisUtil.set(key, user);
	}

	@Override
	public boolean saveList(String key, List<User> userList) {
		return redisUtil.set(key, userList);
	}

	@Override
	public Object getByKey(String key) {
		return redisUtil.get(key);
	}

	@Override
	public boolean hasKey(String key) {
		return redisUtil.hasKey(key);
	}

	@Override
	public void delByKey(String key) {
		redisUtil.del(key);
	}

	@Override
	public Set<String> getKeysByPrefix(String prefix) {
		return redisUtil.getKeysByPrefix(prefix);
	}
}
