package com.springBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBootApplication: 相当于@Configuration + @EnableAutoConfiguration + @ComponentScan, 自动扫描同级包（及子包）
 * // @EnableAutoConfiguration
 * // @ComponentScan(basePackages = {"com.springBoot.controller.helloController"})
 * ServletComponentScan: 注解，Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无需其他代码
 * PropertySource: 读取resources目录下的application.yml
 * MapperScan: 扫描Mapper(或在每个mapper上加上@Mapper注解)
 */
@SpringBootApplication
@ServletComponentScan // 开启后Servlet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无需其他代码
@EnableScheduling // 启用定时
@EnableAsync // 启用异步调用
@EnableTransactionManagement // 启用事务管理
// @PropertySource("classpath:application.yml") // 读取指定配置文件
// @MapperScan("com.springBoot.mapper") // Mapper接口扫描路径
public class Application {

	public static void main(String[] args) {

		// 解决集成elasticsearch时，netty冲突后初始化client时还会抛出异常 java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		SpringApplication.run(Application.class, args);
	}
}
