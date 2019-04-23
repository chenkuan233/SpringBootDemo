package com.springBoot.utils.config.dataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 数据源1 配置
 * @date 2019/4/23 023 11:44
 */
@Configuration
@MapperScan(basePackages = "com.springBoot.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfig {

	// @Primary:主数据源

	@Bean(name = DataSourceUtil.dataSourceName_default)
	@ConfigurationProperties(prefix = DataSourceUtil.configPropertiesPrefix_default)
	@Primary
	public DataSource dataSource() {
		// return DataSourceBuilder.create().build();
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = DataSourceUtil.sqlSessionFactory_default)
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier(DataSourceUtil.dataSourceName_default) DataSource dataSource) throws Exception {
		return DataSourceUtil.getSqlSessionFactory(dataSource, DataSourceUtil.mapperResourcesPath_default, DataSourceUtil.entityPackage_default);
	}

	@Bean(name = DataSourceUtil.transactionManager_default)
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier(DataSourceUtil.dataSourceName_default) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = DataSourceUtil.sqlSessionTemplate_default)
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier(DataSourceUtil.sqlSessionFactory_default) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
