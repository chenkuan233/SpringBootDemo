package com.springBoot.utils.config.dataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 提取DataSourceConfig公共方法, 常量统一管理
 * @date 2019/4/23 023 15:01
 */
public class DataSourceUtil {

	// ##########默认数据源##########

	// dataSource Bean name
	public static final String dataSourceName_default = "dataSource";
	// sqlSessionFactory Bean name
	public static final String sqlSessionFactory_default = "sqlSessionFactory";
	// transactionManager Bean name
	public static final String transactionManager_default = "transactionManager";
	// sqlSessionTemplate Bean name
	public static final String sqlSessionTemplate_default = "sqlSessionTemplate";
	// 数据源配置路径
	public static final String configPropertiesPrefix_default = "spring.datasource.db";
	// env读取配置路径
	public static final String envXaPropPrefix_default = "spring.datasource.db.xa-properties.";
	// 编译后mapper.xml路径
	public static final String mapperResourcesPath_default = "classpath:mapper/*Mapper.xml";
	// 实体类包
	public static final String entityPackage_default = "com.springBoot.entity";

	// ##########第2数据源##########

	// dataSource Bean name
	public static final String dataSourceName_db2 = "db2DataSource";
	// sqlSessionFactory Bean name
	public static final String sqlSessionFactory_db2 = "db2SqlSessionFactory";
	// transactionManager Bean name
	public static final String transactionManager_db2 = "db2TransactionManager";
	// sqlSessionTemplate Bean name
	public static final String sqlSessionTemplate_db2 = "db2SqlSessionTemplate";
	// 数据源配置路径
	public static final String configPropertiesPrefix_db2 = "spring.datasource.db2";
	// env读取配置路径
	public static final String envXaPropPrefix_db2 = "spring.datasource.db2.xa-properties.";
	// 编译后mapper.xml路径
	public static final String mapperResourcesPath_db2 = "classpath:mapperDB2/*Mapper.xml";
	// 实体类包
	public static final String entityPackage_db2 = "com.springBoot.entity";

	/**
	 * getSqlSessionFactory
	 *
	 * @param dataSource
	 * @param mapperResourcesPath
	 * @param entityPackage
	 * @return
	 * @throws Exception
	 */
	public static SqlSessionFactory getSqlSessionFactory(DataSource dataSource, String mapperResourcesPath, String entityPackage) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		// 设置数据源
		bean.setDataSource(dataSource);
		// 设置编译后mapper.xml扫描路径
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperResourcesPath));
		// 实体类包 用于表字段与实体类自动映射
		bean.setTypeAliasesPackage(entityPackage);
		// 字段映射 驼峰命名
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		bean.setConfiguration(config);
		return bean.getObject();
	}

	/**
	 * 读取多数据源配置属性
	 *
	 * @param env
	 * @param prefix 配置属性路径拼接字符串
	 * @return Properties
	 */
	public static Properties getXaProperties(Environment env, String prefix) {
		Properties props = new Properties();
		props.put("name", env.getProperty(prefix + "name"));
		props.put("url", env.getProperty(prefix + "url"));
		props.put("username", env.getProperty(prefix + "username"));
		props.put("password", env.getProperty(prefix + "password"));
		props.put("driverClassName", env.getProperty(prefix + "driverClassName"));
		props.put("initialSize", env.getProperty(prefix + "initialSize"));
		props.put("minIdle", env.getProperty(prefix + "minIdle"));
		props.put("maxActive", env.getProperty(prefix + "maxActive"));
		props.put("maxWait", env.getProperty(prefix + "maxWait"));
		props.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis"));
		props.put("timeBetweenEvictionRunsMillis", env.getProperty(prefix + "timeBetweenEvictionRunsMillis"));
		props.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements"));
		props.put("maxOpenPreparedStatements", env.getProperty(prefix + "maxOpenPreparedStatements"));
		props.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle"));
		props.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow"));
		props.put("testOnReturn", env.getProperty(prefix + "testOnReturn"));
		props.put("validationQuery", env.getProperty(prefix + "validationQuery"));
		props.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout"));
		props.put("filters", env.getProperty(prefix + "filters"));
		return props;
	}

}
