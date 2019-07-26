package com.springBoot.impl.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenkuan
 * @version v1.0
 * @desc websocket测试
 * @date 2019/7/26 026 15:40
 */
@Slf4j
@ServerEndpoint("/websocket/{username}")
public class WebSocket {

	private static int onlineCount = 0;
	private static Map<String, WebSocket> clients = new ConcurrentHashMap<>();
	private Session session;
	private String username;

	@OnOpen
	public void onOpen(@PathParam("username") String username, Session session) {
		this.username = username;
		this.session = session;
		addOnlineCount();
		clients.put(username, this);
		log.info(username + " 已连接");
	}

	@OnClose
	public void onClose() {
		subOnlineCount();
		clients.remove(username);
		log.info(username + " 断开连接");
	}

	@OnMessage
	public void onMessage(String message) {
		JSONObject jsonTo = JSONObject.parseObject(message);
		if (!"All".equals(jsonTo.get("To"))) {
			sendMessageTo("给" + jsonTo.get("To") + "的测试消息", jsonTo.get("To").toString());
		} else {
			sendMessageAll("给所有人的测试消息");
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.error("websocket错误", error);
	}

	//给指定客户端发送消息
	public void sendMessageTo(String message, String toClient) {
		for (WebSocket item : clients.values()) {
			if (item.username.equals(toClient))
				item.session.getAsyncRemote().sendText(message);
		}
	}

	//给所有客户端发送消息
	public void sendMessageAll(String message) {
		for (WebSocket item : clients.values()) {
			item.session.getAsyncRemote().sendText(message);
		}
	}

	//获取在线连接数
	public static int getOnlineCount() {
		return onlineCount;
	}

	//获取所有客户端
	public static Map<String, WebSocket> getClients() {
		return clients;
	}

	//在线连接数 +1
	private static synchronized void addOnlineCount() {
		onlineCount++;
	}

	//在线连接数 -1
	private static synchronized void subOnlineCount() {
		onlineCount--;
	}
}
