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
	@Resource(name = JdbcTemplateConfig.jdbcTemplate_chen2)
	private JdbcTemplate jdbcTemplateChen2;

	//默认数据源数据库名称
	@Value("${spring.datasource.chen.unique-resource-name}")
	private String baseName_default;

	//第2数据源数据库名称
	@Value("${spring.datasource.chen2.unique-resource-name}")
	private String baseName_chen2;

	//查询sql
	private final String sql = "select 1 from dual";

	//数据源1 执行查询
	private void queryBaseOne() {
		try {
			jdbcTemplate.query(sql, rs -> {
				//log.info(baseName_default + "---执行定时查询---result:" + rs.getInt(1));
			});
		} catch (Exception e) {
			log.error(baseName_default + "---执行定时查询---出错");
		}
	}

	//数据源2 执行查询
	private void queryBaseTwo() {
		try {
			jdbcTemplateChen2.query(sql, rs -> {
				//log.info(baseName_chen2 + "---执行定时查询---result:" + rs.getInt(1));
			});
		} catch (Exception e) {
			log.error(baseName_chen2 + "---执行定时查询---出错");
		}
	}

	//定时任务 初始延时10分钟，每30分钟执行一次
	@Scheduled(initialDelay = 10 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
	public void task() {
		queryBaseOne();
		queryBaseTwo();
	}
}
