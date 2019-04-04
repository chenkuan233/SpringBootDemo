package com.springBoot.impl;

import com.springBoot.service.TaskExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 线程处理service
 * @date 2019/4/4 004 14:43
 */
@Service("taskExecutorService")
public class TaskExecutorServiceImpl implements TaskExecutorService {

	private static final Logger logger = LoggerFactory.getLogger(TaskExecutorServiceImpl.class);

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Override
	public void testTask() {
		for (int i = 0; i < 1; i++) {
			threadPoolTaskExecutor.execute(this::printStr);
		}
		logger.info("23333");
	}

	private void printStr() {
		for (int i = 0; i < 22; i++) {
			logger.info("test:" + i);
		}
	}
}
