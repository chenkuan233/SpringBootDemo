package com.springBoot.utils.config.transactionManagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 配置事务管理器
 * @date 2019/4/11 011 15:57
 */
@Slf4j
@Configuration
public class TransactionConfig /*implements TransactionManagementConfigurer*/ {

	// private static final Logger log = LoggerFactory.getLogger(TransactionConfig.class);

	/*@Resource(name = "transactionManager")
	private PlatformTransactionManager transactionManager;

	// JPA必须 @Bean(name = "transactionManager")
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager2(EntityManagerFactory dataSource) {
		return new JpaTransactionManager(dataSource);
	}

	@Bean(name = "transactionManager1")
	public PlatformTransactionManager transactionManager1(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}*/

	/*// 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return transactionManager;
	}*/

	@Bean
	public Object testTransactionManager(PlatformTransactionManager platformTransactionManager) {
		log.info("默认事务管理器 -> " + platformTransactionManager.getClass().getName());
		return new Object();
	}
}
