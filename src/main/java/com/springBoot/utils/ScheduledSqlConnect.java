package com.springBoot.utils;

import com.springBoot.utils.config.jdbcTemplate.JdbcTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 定时执行查询数据库，防止断开连接
 * @date 2019/7/6 006 11:52
 */
@Slf4j
@Component
public class ScheduledSqlConnect {

	//默认数据源
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//第2数据源
	@Resource(name = JdbcTemplateConfig.jdbcTemplate_db2)
	private JdbcTemplate jdbcTemplateDB2;

	//默认数据源数据库名称
	@Value("${spring.datasource.db.unique-resource-name}")
	private String defaultBaseName;

	//第2数据源数据库名称
	@Value("${spring.datasource.db2.unique-resource-name}")
	private String db2BaseName;

	//查询sql
	private final String sql = "select 1 from dual";

	//数据源1 执行查询
	private void queryBaseOne() {
		try {
			jdbcTemplate.query(sql, rs -> {
				//log.info(defaultBaseName + "---执行定时查询---result:" + rs.getInt(1));
			});
		} catch (Exception e) {
			log.error(defaultBaseName + "---执行定时查询---出错");
		}
	}

	//数据源2 执行查询
	private void queryBaseTwo() {
		try {
			jdbcTemplateDB2.query(sql, rs -> {
				//log.info(db2BaseName + "---执行定时查询---result:" + rs.getInt(1));
			});
		} catch (Exception e) {
			log.error(db2BaseName + "---执行定时查询---出错");
		}
	}

	//定时任务 初始延时10分钟，每10分钟执行一次
	@Scheduled(initialDelay = 10 * 60 * 1000, fixedDelay = 10 * 60 * 1000)
	public void task() {
		queryBaseOne();
		queryBaseTwo();
	}
}
