package com.springBoot.utils.config.TransactionManagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 配置事务管理器
 * @date 2019/4/11 011 15:57
 */
@Configuration
public class TransactionConfig implements TransactionManagementConfigurer {

	@Resource(name = "transactionManager")
	private PlatformTransactionManager transactionManager;

	// JPA必须 @Bean(name = "transactionManager")
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager2(EntityManagerFactory dataSource) {
		return new JpaTransactionManager(dataSource);
	}

	@Bean(name = "transactionManager1")
	public PlatformTransactionManager transactionManager1(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	// 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return transactionManager;
	}
}
