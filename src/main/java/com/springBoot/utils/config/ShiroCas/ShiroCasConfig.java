package com.springBoot.utils.config.ShiroCas;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc shiro+cas配置
 * @date 2019/3/28 028 13:07
 */
@SuppressWarnings("unchecked")
@Configuration
public class ShiroCasConfig {

	private static final Logger logger = LoggerFactory.getLogger(ShiroCasConfig.class);

	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return em;
	}

	/**
	 * MyShiroCasRealm
	 */
	@Bean
	public MyShiroCasRealm shiroCasRealm(@Value("${credentialsMatcher.algorithmName}") String algorithmName,
										 @Value("${credentialsMatcher.iterations}") int iterations) {
		MyShiroCasRealm shiroCasRealm = new MyShiroCasRealm();
		// 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
		shiroCasRealm.setAuthenticationCachingEnabled(true);
		// 启用授权缓存，即缓存AuthorizationInfo信息，默认false
		shiroCasRealm.setAuthorizationCachingEnabled(true);
		// 配置自定义密码比较器
		shiroCasRealm.setCredentialsMatcher(hashedCredentialsMatcher(algorithmName, iterations));
		logger.info("##################密码验证器使用HashedCredentialsMatcher");
		return shiroCasRealm;
	}

	/**
	 * 注册单点登出listener
	 */
	@Bean
	public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
		bean.setListener(new SingleSignOutHttpSessionListener());
		// bean.setName(""); // 默认为bean name
		bean.setEnabled(true);
		// bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 设置优先级
		return bean;
	}

	/**
	 * 注册单点登出filter
	 */
	@Bean
	public FilterRegistrationBean singleSignOutFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setName("singleSignOutFilter");
		bean.setFilter(new SingleSignOutFilter());
		bean.addUrlPatterns("/*");
		bean.setEnabled(true);
		return bean;
	}

	/**
	 * 注册DelegatingFilterProxy（Shiro）
	 */
	@Bean
	public FilterRegistrationBean delegatingFilterProxy() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		//  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
		filterRegistration.addInitParameter("targetFilterLifecycle", "false");
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		// daap.setUsePrefix(false);
		return daap;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroCasRealm myShiroCasRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(myShiroCasRealm);
		// 用户授权/认证信息Cache, 采用EhCache 缓存
		manager.setCacheManager(getEhCacheManager());
		// 指定 SubjectFactory
		manager.setSubjectFactory(new CasSubjectFactory());
		return manager;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	/**
	 * CAS过滤器
	 */
	@Bean(name = "casFilter")
	public CasFilter getCasFilter(@Value("${cas.service.loginUrl}") String loginUrl) {
		CasFilter casFilter = new CasFilter();
		casFilter.setName("casFilter");
		casFilter.setEnabled(true);
		// 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
		casFilter.setFailureUrl(loginUrl); // 我们选择认证失败后再打开登录页面
		return casFilter;
	}

	/**
	 * 配置密码比较器
	 * 由于我们的密码校验交给shiro的SimpleAuthenticationInfo进行处理
	 */
	@Bean(name = "credentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher(String algorithmName, int iterations) {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法，这里使用MD5算法
		hashedCredentialsMatcher.setHashAlgorithmName(algorithmName);
		// 散列次数，比如散列两次，相当于md5(md5(""))
		hashedCredentialsMatcher.setHashIterations(iterations);
		// 存储散列后的密码是否为16进制
		// hashedCredentialsMatcher.setStoredCredentialsHexEncoded(hexEncoded);
		return hashedCredentialsMatcher;
	}

	/**
	 * ShiroFilter
	 * <p>
	 * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
	 * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager,
											  CasFilter casFilter,
											  @Value("${cas.service.loginUrl}") String loginUrl,
											  @Value("${cas.service.loginSuccessUrl}") String loginSuccessUrl,
											  @Value("${cas.service.unauthorizedUrl}") String unauthorizedUrl) {

		logger.info("##################读取权限规则，加载到shiroFilter中##################");

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面或"/login"映射
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		// 登录成功后要跳转的连接
		shiroFilterFactoryBean.setSuccessUrl(loginSuccessUrl);
		// 设置无权限时跳转的连接
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
		// 添加casFilter到shiroFilter中
		Map<String, Filter> filters = new HashMap<>();
		filters.put("casFilter", casFilter);
		// filters.put("logout",logoutFilter());
		shiroFilterFactoryBean.setFilters(filters);

		// 加载shiroFilter权限控制规则
		Map<String, String> filterMap = loadShiroFilterMap();
		logger.info("##################加载shiroFilter权限控制规则:" + filterMap.toString());
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

		logger.info("##################Shiro拦截器工厂类注入成功##################");
		return shiroFilterFactoryBean;
	}

	/**
	 * 加载shiroFilter权限控制规则（从数据库读取然后配置）,角色/权限信息由MyShiroCasRealm对象提供doGetAuthorizationInfo实现获取来的
	 */
	private Map<String, String> loadShiroFilterMap() {

		Map<String, String> filterMap = new LinkedHashMap<>();

		// authc：该过滤器下的页面必须登录后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
		// anon: 可以理解为不拦截
		// user: 登录了就不拦截
		// roles["admin"] 用户拥有admin角色
		// perms["permission1"] 用户拥有permission1权限
		// filter顺序按照定义顺序匹配，匹配到就验证，验证完毕结束。
		// url匹配通配符支持：? * **,分别表示匹配1个，匹配0-n个（不含子路径），匹配下级所有路径

		// 1.shiro集成cas后，首先添加该规则
		filterMap.put("/cas", "casFilter");
		//logut请求采用logout filter

		// 2.不拦截的请求
		filterMap.put("/js/**", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/framework/**", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/login", "anon");
		filterMap.put("/error", "anon");

		// 登出请求 shiro的默认登出也会清理用户的session信息,并且也会清理掉redis中缓存的用户 身份认证和 权限认证的相关信息
		filterMap.put("/logout", "logout");

		// 3.拦截的请求（从本地数据库获取或者从casserver获取(webservice,http等远程方式)，看你的角色权限配置在哪里）
		filterMap.put("/user", "authc"); // 需要登录
		filterMap.put("/user/add/**", "authc,roles[admin]"); // 需要登录，且用户角色为admin
		filterMap.put("/user/delete/**", "authc,perms[\"user:delete\"]"); // 需要登录，且用户有权限为user:delete

		// 4.登录过的不拦截
		filterMap.put("/**", "user");

		// 5.拦截所有的请求
		// filterMap.put("/**", "authc");

		return filterMap;
	}

}
