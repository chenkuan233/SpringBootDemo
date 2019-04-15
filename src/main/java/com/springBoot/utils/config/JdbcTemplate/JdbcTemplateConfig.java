package com.springBoot.utils.config.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/12 012 10:54
 */
@Configuration
public class JdbcTemplateConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	@Singleton
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
}
