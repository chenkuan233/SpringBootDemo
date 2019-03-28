package com.springBoot.utils.config.ShiroCas;

import com.springBoot.entity.User;
import com.springBoot.service.UserService;
import com.springBoot.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenkuan
 * @version v1.0
 * @desc cas 授权 认证
 * @date 2019/3/28 028 12:58
 */
public class MyShiroCasRealm extends CasRealm {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(MyShiroCasRealm.class);

	@Value("${cas.server-url}")
	private String casServerUrlPrefix;

	@Value("${cas.service}")
	private String shiroServerUrlPrefix;

	@PostConstruct
	public void initProperty() {
		// cas server地址
		setCasServerUrlPrefix(casServerUrlPrefix);
		// 客户端回调地址
		setCasService(shiroServerUrlPrefix + "/cas");
	}

	/**
	 * 授权，为当前登录的Subject授予角色和权限
	 * <p>
	 * 本例中该方法的调用时机为需授权资源被访问时
	 * 并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
	 * 如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		logger.info("##################Shiro权限认证##################");

		// 从凭证中获得用户名
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		// 根据用户名查询用户对象
		User user = userService.findByUserNameMapper(username);
		// 查询用户拥有的角色
		/*List<Role> list = roleService.findByUserId(user.getId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Role role : list) {
			// 赋予用户角色
			info.addStringPermission(role.getRole());
		}*/

		user = new User();
		if (user != null) {
			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 给用户添加角色（让shiro去验证）
			Set<String> roleNames = new HashSet<>();
			if ("admin".equals(user.getUserName())) {
				roleNames.add("admin");
			}
			info.setRoles(roleNames);
			return info;
		}
		// 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
		return null;
	}

	/**
	 * 1、CAS认证，验证用户身份
	 * 2、将用户基本信息设置到会话中(不用了,随时可以获取的)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获得当前用户的用户名、密码
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		if (StringUtils.isEmpty(username)) {
			logger.error("token中未找到登录用户名");
			return null;
		}

		// 从数据库中根据用户名查找用户
		User user = userService.findByUserNameMapper(username);
		if (user == null) {
			logger.error("没有在本系统中找到对应的用户信息");
			throw new UnknownAccountException("没有在本系统中找到对应的用户信息");
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(), MD5Util.md5(user.getPassword()), getName());

		// 当验证通过后，将用户信息存入session中
		SecurityUtils.getSubject().getSession().setAttribute("user", user);

		return info;
	}

	/**
	 * 使Shiro支持 UsernamePasswordToken
	 *
	 * @param token
	 * @return
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

}
