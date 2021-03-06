package com.springBoot.utils.config.shiroCas;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc shiro+cas配置
 * @date 2019/3/28 028 13:07
 */
@Slf4j
@SuppressWarnings("unchecked")
@Configuration
public class ShiroCasConfig {

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

	@Value("${cookie.name}")
	private String cookieName;

	@Value("${cookie.token}")
	private String cookieToken;

	@Value("${cookie.maxAge}")
	private int cookieMaxAge;

	@Value("${server.servlet.session.timeout}")
	private long sessionTimeOut;

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
		log.info("##################密码验证器使用HashedCredentialsMatcher");
		return shiroCasRealm;
	}

	/**
	 * cookie对象 rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = cookieName
		SimpleCookie simpleCookie = new SimpleCookie(cookieName);
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
		// cookieName cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode(cookieToken));
		return cookieRememberMeManager;
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
	 * 配置sessionDAO
	 */
	@Bean
	public MemorySessionDAO sessionDAO() {
		return new MemorySessionDAO();
	}

	/**
	 * 配置sessionManager
	 * DefaultWebSessionManager：用于Web环境的实现，可以替代ServletContainerSessionManager，自己维护着会话
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//注入sessionDao
		sessionManager.setSessionDAO(sessionDAO());
		//设置全局会话超时时间,单位毫秒(默认1800000)
		sessionManager.setGlobalSessionTimeout(60000 * sessionTimeOut);
		//session有效性检测时间间隔,单位毫秒
		sessionManager.setSessionValidationInterval(60000 * 30);
		//是否开启session定时扫描，校验session是否有效
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//删除失效session
		sessionManager.setDeleteInvalidSessions(true);
		return sessionManager;
	}

	/**
	 * securityManager 可以管理其他组件
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroCasRealm myShiroCasRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		// 设置realm
		manager.setRealm(myShiroCasRealm);
		// 用户授权/认证信息Cache, 采用EhCache缓存
		manager.setCacheManager(getEhCacheManager());
		// 注入session管理器
		manager.setSessionManager(sessionManager());
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
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {

		log.info("##################读取权限规则，加载到shiroFilter中##################");
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

		// 添加自定义Filter到shiroFilter中
		Map<String, Filter> myFilters = new LinkedHashMap<>();
		//myFilters.put("casFilter", casFilter);
		myFilters.put("myUserFilter", new MyUserFilter()); // 继承UserFilter
		shiroFilterFactoryBean.setFilters(myFilters);

		// 加载shiroFilter权限控制规则
		Map<String, String> filterMap = loadShiroFilterMap();
		log.info("##################加载shiroFilter权限控制规则:" + filterMap.toString());
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

		log.info("##################Shiro拦截器工厂类注入成功##################");
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
		//filterMap.put("/cas", "casFilter");

		// 2.不拦截的请求 一般为静态资源、登录登出
		filterMap.put("/css/**", "anon");
		filterMap.put("/error/**", "anon"); // 错误页面
		filterMap.put("/framework/**", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/**.js", "anon"); // 所有js文件
		filterMap.put("/login", "anon"); // 登录请求服务
		filterMap.put("/pages/login/**", "anon"); // 登录页面
		filterMap.put("/pages/register/**", "anon"); // 注册页面
		filterMap.put("/service/registerService/*", "anon"); // 注册请求服务
		filterMap.put("/download", "anon"); // 下载服务

		// 登出请求 shiro的默认登出也会清理用户的session信息,并且也会清理掉redis中缓存的用户 身份认证和权限认证的相关信息
		// filterMap.put("/logout", "logout"); // 使用自定义的登出
		filterMap.put("/logout", "anon");

		// 3.拦截的请求（从本地数据库获取或者从casserver获取(webservice,http等远程方式)，看你的角色权限配置在哪里）
		filterMap.put("/user", "authc"); // 需要登录
		filterMap.put("/user/**", "roles[\"admin\"]"); // 需要登录，且用户角色为admin
		// filterMap.put("/user/add/**", "authc,roles[\"admin\"]"); // 需要登录，且用户角色为admin
		// filterMap.put("/user/delete/**", "authc,perms[\"user:delete\"]"); // 需要登录，且用户有权限为user:delete

		// 4.登录过的不拦截
		// filterMap.put("/**", "user");
		filterMap.put("/**", "myUserFilter"); // 使用自定义拦截器

		// 5.拦截所有的请求
		// filterMap.put("/**", "authc");

		return filterMap;
	}
}
