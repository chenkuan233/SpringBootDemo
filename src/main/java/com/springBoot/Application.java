package com.springBoot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @SpringBootApplication 相当于@Configuration + @EnableAutoConfiguration + @ComponentScan, 自动扫描同级包（及子包）
 * // @EnableAutoConfiguration
 * // @ComponentScan(basePackages = {"com.springBoot.controller.helloController"})
 * @ServletComponentScan 注解，Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无需其他代码
 * @PropertySource 读取resources目录下的application.yml
 * @MapperScan 扫描 Mapper
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
@MapperScan("com.springBoot.mapper")
public class Application {

	public static void main(String[] args) {

		// 解决集成elasticsearch时，netty冲突后初始化client时还会抛出异常 java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication.run(Application.class, args);
	}
}
