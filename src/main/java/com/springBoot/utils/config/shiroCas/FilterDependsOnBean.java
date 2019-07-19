package com.springBoot.utils.config.shiroCas;

import com.springBoot.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 获取redisUtil实例
 * @date 2019/7/10 010 9:23
 */
@Slf4j
@Component
@DependsOn(value = {"redisUtil", "shiroCasConfig"})
@Singleton
public class FilterDependsOnBean {

	public static RedisUtil redisUtil;

	public static SessionDAO sessionDAO;

	public static SessionManager sessionManager;

	//是否开启shiro
	public static boolean isOpenShiro;

	//单点登录的登陆设备数
	public static int singleLogon;

	@Autowired
	public FilterDependsOnBean(RedisUtil redisUtil, SessionDAO sessionDAO, SessionManager sessionManager) {
		FilterDependsOnBean.redisUtil = redisUtil;
		FilterDependsOnBean.sessionDAO = sessionDAO;
		FilterDependsOnBean.sessionManager = sessionManager;
	}

	@Value("${isOpenShiro}")
	public void setIsOpenShiro(Boolean isOpenShiro) {
		if (isOpenShiro == null) isOpenShiro = true;
		if (isOpenShiro) {
			log.info("######shiro权限认证---开启");
		} else {
			log.info("######shiro权限认证---关闭");
		}
		FilterDependsOnBean.isOpenShiro = isOpenShiro;
	}

	@Value("${singleLogon}")
	public void setSingleLogon(Integer singleLogon) {
		if (singleLogon == null) singleLogon = 0;
		if (singleLogon > 0) {
			log.info("######单点登录---开启，同时允许在线数---" + singleLogon);
		} else {
			log.info("######单点登录---关闭");
		}
		FilterDependsOnBean.singleLogon = singleLogon;
	}
}
