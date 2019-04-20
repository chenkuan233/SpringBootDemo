package com.springBoot.utils;

import com.springBoot.entity.User;
import com.springBoot.utils.config.shiroCas.ByteSourceUtil;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author chenkuan
 * @version v1.0
 * @desc Shiro密码加密 对密码进行加盐加密
 * @date 2019/3/29 029 10:02
 */
@Component
public class UserEncryptUtil {

	// 随机数生成器
	private static RandomNumberGenerator randomNumber = new SecureRandomNumberGenerator();

	// 散列算法
	@Value("${credentialsMatcher.algorithmName}")
	private String algorithmName;

	// 散列次数
	@Value("${credentialsMatcher.iterations}")
	private int iterations;

	/**
	 * 使用随机盐值对密码进行加密
	 *
	 * @param user 登录用户
	 */
	public User encrypt(User user) {
		user.setCredentialsSalt(randomNumber.nextBytes().toHex());
		String password = new SimpleHash(algorithmName, user.getPassword(), ByteSourceUtil.bytes(user.getCredentialsSalt()), iterations).toHex();
		user.setPassword(password);
		return user;
	}

	/**
	 * 修改密码 使用随机盐值对密码进行加密
	 *
	 * @param user     登录用户
	 * @param password 新密码
	 */
	public User encrypt(User user, String password) {
		user.setCredentialsSalt(randomNumber.nextBytes().toHex());
		String newPassword = new SimpleHash(algorithmName, password, ByteSourceUtil.bytes(user.getCredentialsSalt()), iterations).toHex();
		user.setPassword(newPassword);
		return user;
	}

	/**
	 * 对指定密码和盐进行加密
	 *
	 * @param password        原密码
	 * @param credentialsSalt 加密盐值
	 */
	public String encrypt(String password, String credentialsSalt) {
		return new SimpleHash(algorithmName, password, ByteSourceUtil.bytes(credentialsSalt), iterations).toHex();
	}
}
