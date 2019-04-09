package com.springBoot.utils.config.ShiroCas;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
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
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

	@Value("${credentialsMatcher.algorithmName}")
	private String algorithmName;

	@Value("${credentialsMatcher.iterations}")
	private int iterations;

	@Value("${cas.service.loginUrl}")
	private String loginUrl;

	@Value("${cas.service.loginSuccessUrl}")
	private String loginSuccessUrl;

	@Value("${cas.service.unauthorizedUrl}")
	private String unauthorizedUrl;

	@Value("${cas.server-url}")
	private String casServerUrlPrefix;

	@Value("${cookie.cipherKey}")
	private String cookieCipherKey;

	@Value("${cookie.maxAge}")
	private int cookieMaxAge;

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
	public MyShiroCasRealm myShiroCasRealm() {
		MyShiroCasRealm shiroCasRealm = new MyShiroCasRealm();
		// 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
		shiroCasRealm.setAuthenticationCachingEnabled(true);
		// 启用授权缓存，即缓存AuthorizationInfo信息，默认false
		shiroCasRealm.setAuthorizationCachingEnabled(true);
		// 配置自定义密码比较器
		shiroCasRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		logger.info("##################密码验证器使用HashedCredentialsMatcher");
		return shiroCasRealm;
	}

	/**
	 * cookie对象 rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// 记住我cookie生效时间30天 ,单位秒
		simpleCookie.setMaxAge(cookieMaxAge);
		return simpleCookie;
	}

	/**
	 * cookie管理对象 rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode(cookieCipherKey));
		return cookieRememberMeManager;
	}

	/**
	 * 注册单点登出listener
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
		bean.setListener(new SingleSignOutHttpSessionListener());
		bean.setEnabled(true);
		return bean;
	}

	/**
	 * 注册单点登出filter
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FilterRegistrationBean singleSignOutFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setName("singleSignOutFilter");
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casServerUrlPrefix);
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		bean.setFilter(singleSignOutFilter);
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

	/**
	 * 该类可以保证实现了org.apache.shiro.util.Initializable接口的shiro对象的init或者是destory方法被自动调用，
	 * 而不用手动指定init-method或者是destory-method方法
	 * 注意：如果使用了该类，则不需要手动指定初始化方法和销毁方法，否则会出错
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 下面两个配置主要用来开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroCasRealm myShiroCasRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		// 设置realm
		manager.setRealm(myShiroCasRealm);
		// 用户授权/认证信息Cache, 采用EhCache 缓存
		manager.setCacheManager(getEhCacheManager());
		// 指定 SubjectFactory
		manager.setSubjectFactory(new CasSubjectFactory());
		// 注入记住我管理器
		manager.setRememberMeManager(rememberMeManager());
		return manager;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	/**
	 * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
	 */
	/*@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}*/

	/**
	 * CAS过滤器
	 */
	@Bean(name = "casFilter")
	public CasFilter getCasFilter() {
		CasFilter casFilter = new CasFilter();
		casFilter.setName("casFilter");
		casFilter.setEnabled(true);
		casFilter.setLoginUrl(loginUrl);
		casFilter.setSuccessUrl(loginSuccessUrl);
		// 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
		casFilter.setFailureUrl(loginUrl); // 我们选择认证失败后再打开登录页面
		return casFilter;
	}

	/**
	 * 配置密码比较器
	 * 由于我们的密码校验交给shiro的SimpleAuthenticationInfo进行处理
	 */
	@Bean(name = "credentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法，这里使用MD5算法
		hashedCredentialsMatcher.setHashAlgorithmName(algorithmName);
		// 散列次数，比如散列两次，相当于md5(md5(""))
		hashedCredentialsMatcher.setHashIterations(iterations);
		return hashedCredentialsMatcher;
	}

	/**
	 * ShiroFilter
	 * <p>
	 * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
	 * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, CasFilter casFilter) {

		logger.info("##################读取权限规则，加载到shiroFilter中##################");
		// String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + "/cas";
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
		// logut请求采用logout filter

		// 2.不拦截的请求
		filterMap.put("/js/**", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/framework/**", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/login", "anon");
		filterMap.put("/error", "anon");
		filterMap.put("/error/**", "anon");

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
