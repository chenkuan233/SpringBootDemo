package com.springBoot.utils.config.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author chenkuan
 * @version v1.0
 * @desc websocket配置
 * @date 2019/7/27 027 14:09
 */
@Configuration
public class WebSocketConfig {

	//打成war包部署在tomcat中的话不需要这个bean
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
