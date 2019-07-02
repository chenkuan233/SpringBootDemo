package com.springBoot.impl.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 接收kafka消息 测试
 * @date 2019/7/2 002 14:37
 */
@Slf4j
@Component
public class KafkaConsumer {

	//此方法 消费消息
	//topics 主题
	//topicPartitions 分区
	//groupId 用户组
	@KafkaListener(topics = {"test"}, groupId = "test")
	public void listen(ConsumerRecord<String, String> data) {
		String topic = data.topic(); //消费的topic
		log.info("-------------message from topic-------------" + topic);
		log.info("partition(分区):" + String.valueOf(data.partition())); //消费的topic的分区
		log.info("offset(偏移位置):" + String.valueOf(data.offset())); //消费者的位置
		log.info("get message from topic(消息):" + data.value()); //接收到的消息
	}
}
