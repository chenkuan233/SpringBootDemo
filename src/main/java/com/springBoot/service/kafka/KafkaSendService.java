package com.springBoot.service.kafka;

import com.springBoot.utils.Response;

/**
 * @author chenkuan
 * @version v1.0
 * @desc kafka测试消息发送 接口
 * @date 2019/7/2 002 11:51
 */
public interface KafkaSendService {
	//测试发送kafka消息
	Response sendKafka(String message);
}
