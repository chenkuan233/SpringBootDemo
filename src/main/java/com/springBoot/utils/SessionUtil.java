package com.springBoot.utils;

import com.springBoot.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author chenkuan
 * @version v1.0
 * @desc session工具 当前登陆用户session
 * @date 2019/7/8 008 17:11
 */
@Slf4j
public class SessionUtil {

	public static final String userInfo = "userInfo";

	/**
	 * 存储
	 *
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(key, value);
	}

	/**
	 * 根据key获取
	 *
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Session session = SecurityUtils.getSubject().getSession();
		return session.getAttribute(key);
	}

	/**
	 * 移除指定key
	 *
	 * @param key
	 */
	public static Object remove(String key) {
		Session session = SecurityUtils.getSubject().getSession();
		return session.removeAttribute(key);
	}

	/**
	 * 获取Serializable id
	 *
	 * @return
	 */
	public static Serializable getSessionId() {
		Session session = SecurityUtils.getSubject().getSession();
		return session.getId();
	}

	/**
	 * 获取keys列表
	 *
	 * @return
	 */
	public static Collection<Object> getKeys() {
		Session session = SecurityUtils.getSubject().getSession();
		return session.getAttributeKeys();
	}

	//获取当前登陆用户信息
	public static User getUserInfo() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user == null) {
			//从session中取
			user = (User) get(userInfo);
		}
		return user;
	}

	//获取登陆用户名
	public static String getUserName() {
		User user = getUserInfo();
		if (user == null) {
			log.warn("sessionId：" + getSessionId() + "---下的登录用户信息为null");
			return "";
		}
		return user.getUserName();
	}
}
