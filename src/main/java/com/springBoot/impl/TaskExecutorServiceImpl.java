package com.springBoot.impl;

import com.springBoot.entity.Man;
import com.springBoot.service.TaskExecutorService;
import com.springBoot.service.WriteToMysqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 线程处理service
 * @date 2019/4/4 004 14:43
 */
@Slf4j
@Service("taskExecutorService")
public class TaskExecutorServiceImpl implements TaskExecutorService {

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private WriteToMysqlService writeToMysqlService;

	@Override
	public void testTask() {
		for (int i = 0; i < 1; i++) {
			threadPoolTaskExecutor.execute(this::printStr);
		}
		log.info("23333");
	}

	private void printStr() {
		for (int i = 0; i < 22; i++) {
			log.info("test:" + i);
		}
	}

	// 默认数据源
	@Override
	public void writeToMysql() {
		List<Man> manList = new ArrayList<>();
		Man man = null;
		for (int i = 0; i < 100; i++) {
			man = new Man("维维-" + i, "weiwei-" + i);
			manList.add(man);
		}
		threadPoolTaskExecutor.execute(() -> writeToMysqlService.writeMan(manList));
	}

	// 第2数据源
	@Override
	public void writeToMysqlDB2() {
		List<Man> manList = new ArrayList<>();
		Man man = null;
		for (int i = 0; i < 100; i++) {
			man = new Man("维维-" + i, "weiwei-" + i);
			manList.add(man);
		}
		threadPoolTaskExecutor.execute(() -> writeToMysqlService.writeManDB2(manList));
	}

}
