package com.springBoot.utils.config.dataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 数据源2 配置
 * @date 2019/4/23 023 11:44
 */
@Configuration
@MapperScan(basePackages = "com.springBoot.mapper.mapperDB2", sqlSessionTemplateRef = DataSourceUtil.sqlSessionTemplate_db2)
public class DataSourceDB2Config {

	@Bean(name = DataSourceUtil.dataSourceName_db2)
	@ConfigurationProperties(prefix = DataSourceUtil.configPropertiesPrefix_db2)
	public DataSource db2DataSource(Environment env) {
		//return DataSourceBuilder.create().build();
		//return DruidDataSourceBuilder.create().build();
		return new AtomikosDataSourceBean();

//		AtomikosDataSourceBean db = new AtomikosDataSourceBean();
//		Properties prop = DataSourceUtil.getXaProperties(env, DataSourceUtil.envXaPropPrefix_db2);
//		db.setXaDataSourceClassName(env.getProperty(DataSourceUtil.configPropertiesPrefix_db2 + ".xa-data-source-class-name"));
//		db.setUniqueResourceName(env.getProperty(DataSourceUtil.configPropertiesPrefix_db2 + ".unique-resource-name"));
//		db.setMinPoolSize(Integer.parseInt(env.getProperty(DataSourceUtil.configPropertiesPrefix_db2 + ".min-pool-size")));
//		db.setMaxPoolSize(Integer.parseInt(env.getProperty(DataSourceUtil.configPropertiesPrefix_db2 + ".max-pool-size")));
//		db.setXaProperties(prop);
//		return db;
	}

	@Bean(name = DataSourceUtil.sqlSessionFactory_db2)
	public SqlSessionFactory db2SqlSessionFactory(@Qualifier(DataSourceUtil.dataSourceName_db2) DataSource dataSource) throws Exception {
		return DataSourceUtil.getSqlSessionFactory(dataSource, DataSourceUtil.mapperResourcesPath_db2, DataSourceUtil.entityPackage_db2);
	}

	/*@Bean(name = DataSourceUtil.transactionManager_db2)
	public DataSourceTransactionManager db2TransactionManager(@Qualifier(DataSourceUtil.dataSourceName_db2) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}*/

	@Bean(name = DataSourceUtil.sqlSessionTemplate_db2)
	public SqlSessionTemplate db2SqlSessionTemplate(@Qualifier(DataSourceUtil.sqlSessionFactory_db2) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
