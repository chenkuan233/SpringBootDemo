package com.springBoot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @SpringBootApplication 相当于@EnableAutoConfiguration + @ComponentScan, 自动扫描同级包（及子包）
 * // @EnableAutoConfiguration
 * // @ComponentScan(basePackages = {"com.springBoot.controller.helloController"})
 * @PropertySource 读取resources目录下的application.yml
 * @MapperScan 扫描 Mapper
 */
@SpringBootApplication
@PropertySource("classpath:application.yml")
@MapperScan("com.springBoot.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
