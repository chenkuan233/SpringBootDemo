package com.springBoot.utils.config.jdbcTemplate;

import com.springBoot.utils.config.dataSource.DataSourceUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc spring jdbc 配置
 * @date 2019/4/12 012 10:54
 */
@Configuration
public class JdbcTemplateConfig {

	public static final String jdbcTemplate_default = "jdbcTemplate";

	public static final String jdbcTemplate_chen2 = "jdbcTemplate_chen2";

	@Bean(name = jdbcTemplate_default)
	@Singleton
	public JdbcTemplate jdbcTemplate(@Qualifier(DataSourceUtil.dataSourceName_default) DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = jdbcTemplate_chen2)
	@Singleton
	public JdbcTemplate jdbcTemplateChen2(@Qualifier(DataSourceUtil.dataSourceName_chen2) DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
