package com.springBoot.service.impl;

import com.springBoot.entity.Man;
import com.springBoot.service.WriteToMysqlService;
import com.springBoot.utils.config.jdbcTemplate.JdbcTemplateConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 使用JdbcTemplate写数据
 * @date 2019/4/4 004 15:31
 */
@Service("writeToMysqlService")
public class WriteToMysqlServiceImpl implements WriteToMysqlService {

	// 默认数据源
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 指定第2数据源
	// @Autowired()
	// @Qualifier(JdbcTemplateConfig.jdbcTemplate_chen2)
	@Resource(name = JdbcTemplateConfig.jdbcTemplate_chen2)
	private JdbcTemplate jdbcTemplateChen2;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void writeMan(List<Man> manList) {
		if (CollectionUtils.isNotEmpty(manList)) {
			try {
				String sql = "insert into t_man(name,nick) values(?,?)";
				jdbcTemplate.batchUpdate(sql, manList, 1000, (ps, t) -> {
					ps.setString(1, t.getName());
					ps.setString(2, t.getNick());
				});

				//throw new Exception(); // 手动抛出异常 测试事务
			} catch (Exception e) {
				// 手动事务回滚
				// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new RuntimeException("数据源1运行异常");
			}
		}
	}

	//第2数据源
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void writeManDB2(List<Man> manList) {
		if (CollectionUtils.isNotEmpty(manList)) {
			try {
				String sql = "insert into t_man(name,nick) values(?,?)";
				jdbcTemplateChen2.batchUpdate(sql, manList, 1000, (ps, t) -> {
					ps.setString(1, t.getName());
					ps.setString(2, t.getNick());
				});
			} catch (Exception e) {
				// 手动事务回滚
				// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new RuntimeException("数据源2运行异常");
			}
		}
	}

}
