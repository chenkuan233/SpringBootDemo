package com.springBoot.utils.config.httpServer;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenkuan
 * @version v1.0
 * @desc tomcat相关配置
 * @date 2019/7/17 017 18:21
 */
@Configuration
public class TomcatConfig {

	/**
	 * 配置tomcat url允许非法字符
	 */
	@Bean
	public ServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
		return factory;
	}
}
