package com.springBoot.utils.config.https;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author chenkuan
 * @version v1.0
 * @desc https配置，增加http访问自动跳转到https
 * @date 2019/3/27 027 15:25
 */
@Configuration
public class HttpsConfig extends WebMvcConfigurerAdapter {

	@Value("${server.port}")
	private int httpsPort;

	@Value("${http.port}")
	private int httpPort;

	/**
	 * 解决跨域问题
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
						"Access-Control-Request-Headers", "accessToken")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
				.allowCredentials(true).maxAge(3600);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	/**
	 * 配置https
	 */
	private Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		// 同时启用http、https两个端口
		connector.setScheme("http");
		// 需要重定向的http端口
		connector.setPort(httpPort);
		connector.setSecure(false);
		// 设置重定向到https端口
		connector.setRedirectPort(httpsPort);
		return connector;
	}
}
