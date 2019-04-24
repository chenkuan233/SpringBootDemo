package com.springBoot.utils.config.dataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 主数据源 配置
 * @date 2019/4/23 023 11:44
 */
@Configuration
@MapperScan(basePackages = "com.springBoot.mapper.mapper", sqlSessionTemplateRef = DataSourceUtil.sqlSessionTemplate_default)
public class DataSourceConfig {

	// @Primary:主数据源

	/**
	 * data source
	 */
	@Bean(name = DataSourceUtil.dataSourceName_default)
	@ConfigurationProperties(prefix = DataSourceUtil.configPropertiesPrefix_default)
	@Primary
	public DataSource dataSource() {
		//return DataSourceBuilder.create().build();
		//return DruidDataSourceBuilder.create().build();
		return new AtomikosDataSourceBean();
	}

	/**
	 * 配置SQL Session工厂
	 */
	@Bean(name = DataSourceUtil.sqlSessionFactory_default)
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier(DataSourceUtil.dataSourceName_default) DataSource dataSource) throws Exception {
		return DataSourceUtil.getSqlSessionFactory(dataSource, DataSourceUtil.mapperResourcesPath_default, DataSourceUtil.entityPackage_default);
	}

	/**
	 * 事务管理 使用Atomikos做分布式统一事务管理
	 */
	/*@Bean(name = DataSourceUtil.transactionManager_default)
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier(DataSourceUtil.dataSourceName_default) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}*/

	/**
	 * 配置Sql Session模板
	 */
	@Bean(name = DataSourceUtil.sqlSessionTemplate_default)
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier(DataSourceUtil.sqlSessionFactory_default) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
