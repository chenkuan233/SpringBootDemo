package com.springBoot.impl.kafka;

import com.springBoot.service.kafka.KafkaSendService;
import com.springBoot.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author chenkuan
 * @version v1.0
 * @desc kafka测试
 * @date 2019/7/2 002 11:50
 */
@Slf4j
@Service("kafkaService")
public class KafkaSendServiceImpl implements KafkaSendService {

	@Autowired
	private KafkaTemplate kafkaTemplate;

	//测试发送kafka消息
	@Override
	public Response sendKafka(String message) {
		log.info("kafka消息=" + message);
		try {
			//KafkaTemplate.send()
			//topic：主题名称；partition：要发送消息到哪个分区；timestamp：创建消息的时间；key：消息的键；value：消息的值
			//send方法是异步，一旦将消息保存在等待发送消息的缓存中就立即返回，这样就不会阻塞去等待每一条消息的响应。可以使用ListenableFuture.cancle()方法去取消消息的发送
			ListenableFuture future = kafkaTemplate.send("test", message);
			log.info("发送kafka成功.");
			return new Response("0", "发送kafka成功");
		} catch (Exception e) {
			log.info("发送kafka失败.");
			return new Response("-1", "发送kafka失败");
		}
	}

	//测试发送kafka消息
	@Override
	public Response sendKafkaMany(String topic, Integer total) {
		int num = 0;
		log.info("开始发送---topic:" + topic);
		try {
			//topic：主题名称；partition：要发送消息到哪个分区；timestamp：创建消息的时间；key：消息的键；value：消息的值
			for (int i = 0; i < total; i++) {
				kafkaTemplate.send(topic, topic + "--testMessage--" + i);
			}
		} catch (Exception e) {
			num++;
		}
		log.info("发送kafka完成---成功" + (total - num) + ", 失败---" + num);
		return new Response("0", "发送kafka成功");
	}
}
