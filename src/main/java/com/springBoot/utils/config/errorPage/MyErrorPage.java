package com.springBoot.utils.config.errorPage;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 自定义错误映射页面
 * @date 2019/4/3 003 17:46
 */
@Configuration
public class MyErrorPage {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return (factory -> factory.addErrorPages(
				new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html"),
				new ErrorPage(HttpStatus.FORBIDDEN, "/error/403.html"))
		);
	}
}
