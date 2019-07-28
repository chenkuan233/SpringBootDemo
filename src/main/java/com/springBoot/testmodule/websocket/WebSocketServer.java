package com.springBoot.testmodule.websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@Component
public class WebSocketServer {

	private static int onlineCount = 0;
	private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<>();
	private String username;
	private Session session;

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
		JsonArray jsonArray = new JsonParser().parse(message).getAsJsonArray();
		String to = jsonArray.get(0).getAsString();
		String msg = jsonArray.get(1).getAsString();
		if ("All".equals(to)) {
			sendMessageAll(msg);
		} else {
			sendMessageTo(to, msg);
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.error("websocket错误", error);
	}

	//给指定客户端发送消息
	public void sendMessageTo(String toClient, String message) {
		WebSocketServer socketServer = clients.get(toClient);
		if (socketServer != null) {
			socketServer.session.getAsyncRemote().sendText(message);
			log.info("给[" + socketServer.username + "]推送消息：" + message);
		} else {
			if (this.session != null) {
				this.session.getAsyncRemote().sendText(toClient + "不在线，消息未发送");
			}
		}
	}

	//给所有客户端发送消息
	public void sendMessageAll(String message) {
		for (WebSocketServer item : clients.values()) {
			item.session.getAsyncRemote().sendText(message);
			log.info("给[" + item.username + "]推送消息：" + message);
		}
	}

	//获取在线连接数
	public static int getOnlineCount() {
		return onlineCount;
	}

	//获取所有客户端
	public static Map<String, WebSocketServer> getClients() {
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
