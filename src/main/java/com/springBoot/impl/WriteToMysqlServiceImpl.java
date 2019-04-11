package com.springBoot.impl;

import com.springBoot.entity.Man;
import com.springBoot.service.WriteToMysqlService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 使用JdbcTemplate写数据
 * @date 2019/4/4 004 15:31
 */
@Service("writeToMysqlService")
public class WriteToMysqlServiceImpl implements WriteToMysqlService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void writeMan(List<Man> manList) {
		if (CollectionUtils.isNotEmpty(manList)) {
			try {
				String sql = "insert into t_man(name,nick) values(?,?)";
				jdbcTemplate.batchUpdate(sql, manList, 1000, (ps, t) -> {
					ps.setString(1, t.getName());
					ps.setString(2, t.getNick());
				});
			} catch (Exception e) {
				// 手动事务回滚
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
	}
}
