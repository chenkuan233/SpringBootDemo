package com.springBoot.utils.config.shiroCas;

import com.springBoot.utils.RedisUtil;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 获取redisUtil实例
 * @date 2019/7/10 010 9:23
 */
@Component
@DependsOn(value = {"redisUtil", "shiroCasConfig"})
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
		FilterDependsOnBean.isOpenShiro = isOpenShiro;
	}

	@Value("${singleLogon}")
	public void setSingleLogon(Integer singleLogon) {
		if (singleLogon == null) singleLogon = 0;
		FilterDependsOnBean.singleLogon = singleLogon;
	}
}
