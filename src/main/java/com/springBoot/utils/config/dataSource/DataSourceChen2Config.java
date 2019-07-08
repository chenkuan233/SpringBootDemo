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
@MapperScan(basePackages = "com.springBoot.mapper.mapperChen2", sqlSessionTemplateRef = DataSourceUtil.sqlSessionTemplate_chen2)
public class DataSourceChen2Config {

	@Bean(name = DataSourceUtil.dataSourceName_chen2)
	@ConfigurationProperties(prefix = DataSourceUtil.configPropertiesPrefix_chen2)
	public DataSource dataSourceChen2(Environment env) {
		//return DataSourceBuilder.create().build();
		//return DruidDataSourceBuilder.create().build();
		return new AtomikosDataSourceBean();

//		AtomikosDataSourceBean db = new AtomikosDataSourceBean();
//		Properties prop = DataSourceUtil.getXaProperties(env, DataSourceUtil.envXaPropPrefix_chen2);
//		db.setXaDataSourceClassName(env.getProperty(DataSourceUtil.configPropertiesPrefix_chen2 + ".xa-data-source-class-name"));
//		db.setUniqueResourceName(env.getProperty(DataSourceUtil.configPropertiesPrefix_chen2 + ".unique-resource-name"));
//		db.setMinPoolSize(Integer.parseInt(env.getProperty(DataSourceUtil.configPropertiesPrefix_chen2 + ".min-pool-size")));
//		db.setMaxPoolSize(Integer.parseInt(env.getProperty(DataSourceUtil.configPropertiesPrefix_chen2 + ".max-pool-size")));
//		db.setXaProperties(prop);
//		return db;
	}

	@Bean(name = DataSourceUtil.sqlSessionFactory_chen2)
	public SqlSessionFactory sqlSessionFactoryChen2(@Qualifier(DataSourceUtil.dataSourceName_chen2) DataSource dataSource) throws Exception {
		return DataSourceUtil.getSqlSessionFactory(dataSource, DataSourceUtil.mapperResourcesPath_chen2, DataSourceUtil.entityPackage_chen2);
	}

	/*@Bean(name = DataSourceUtil.transactionManager_chen2)
	public DataSourceTransactionManager transactionManagerChen2(@Qualifier(DataSourceUtil.dataSourceName_chen2) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}*/

	@Bean(name = DataSourceUtil.sqlSessionTemplate_chen2)
	public SqlSessionTemplate sqlSessionTemplateChen2(@Qualifier(DataSourceUtil.sqlSessionFactory_chen2) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
