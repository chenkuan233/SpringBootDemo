package com.springBoot.utils.config.TransactionManagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 配置事务管理器
 * @date 2019/4/11 011 15:57
 */
// @Configuration
public class TransactionConfig {

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
