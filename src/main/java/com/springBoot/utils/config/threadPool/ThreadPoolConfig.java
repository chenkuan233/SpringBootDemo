package com.springBoot.utils.config.threadPool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenkuan
 * @version v1.0
 * @desc Spring ThreadPoolTaskExecutor 线程池配置
 * @date 2019/4/4 004 14:07
 */
@Configuration
public class ThreadPoolConfig {

	@Value("${threadpool.core-pool-size}")
	private int corePoolSize;

	@Value("${threadpool.max-pool-size}")
	private int maxPoolSize;

	@Value("${threadpool.queue-capacity}")
	private int queueCapacity;

	@Value("${threadpool.keep-alive-seconds}")
	private int keepAliveSeconds;

	@Value("${threadpool.wait-for-tasks}")
	private boolean waitForTasks;

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		// 核心线程数
		pool.setCorePoolSize(corePoolSize);
		// 最大线程数
		pool.setMaxPoolSize(maxPoolSize);
		// 缓冲队列
		pool.setQueueCapacity(queueCapacity);
		// 空闲时间
		pool.setKeepAliveSeconds(keepAliveSeconds);
		// 设置失败时拒绝策略 CallerRunsPolicy:由调用线程处理该任务
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 当调度器shutdown被调用时等待当前被调度的任务完成
		pool.setWaitForTasksToCompleteOnShutdown(waitForTasks);
		// 初始化
		pool.initialize();
		return pool;
	}
}
