package com.springBoot.impl.fix;

import lombok.extern.slf4j.Slf4j;
import quickfix.*;
import quickfix.field.MsgType;
import quickfix.fix44.Email;

/**
 * @author chenkuan
 * @version v1.0
 * @desc MessageCracker是一个工具类，通过继承MessageCracker可以覆盖onMessage方法
 * 通过调用crack回调onMessage中的业务逻辑。所以所有的业务逻辑可以直接写在onMessage 方法中。
 * <p>
 * onCreate –>当一个Fix Session建立是调用
 * onLogon –>当一个Fix Session登录成功时候调用
 * onLogout –>当一个Fix Session退出时候调用
 * fromAdmin–>当收到一个消息,经过一系列检查，合格后，属于Admin 类型时候调用
 * fromApp–>当收到一个消息,经过一系列检查，合格后，不属于Admin 类型时候调用
 * toAdmin–>当发送一个admin类型消息调用toApp—>当发送一个非admin(业务类型)消息调用
 * @date 2019/5/9 009 9:53
 */
@Slf4j
public class MyApplication extends MessageCracker implements Application {

	@Override
	protected void onMessage(Message message, SessionID sessionID) {
		try {
			log.info("业务逻辑实现统一写在此方法中");
			String msgType = message.getHeader().getString(35);
			Session session = Session.lookupSession(sessionID);
			log.info("服务器接收到用户信息订阅==" + msgType);
			switch (msgType) {
				case MsgType.LOGON: // 登陆
					session.logon();
					session.sentLogon();
					break;
				case MsgType.HEARTBEAT: // 心跳
					session.generateHeartbeat();
					break;
			}

		} catch (FieldNotFound e) {
			log.error("FieldNotFound", e);
		}
	}

	@Override
	public void onCreate(SessionID sessionId) {
		log.info("服务器启动时候调用此方法创建");
		log.info("sessionId: " + sessionId);
	}

	@Override
	public void onLogon(SessionID sessionId) {
		log.info("客户端登陆成功时候调用此方法");
	}

	@Override
	public void onLogout(SessionID sessionId) {
		log.info("客户端断开连接时候调用此方法");
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		log.info("发送会话消息时候调用此方法");
		log.info("message: " + message);
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		log.info("发送业务消息时候调用此方法");
		log.info("message: " + message);
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("接收会话类型消息时调用此方法");
		log.info("message: " + message);
		try {
			crack(message, sessionId);
		} catch (UnsupportedMessageType | FieldNotFound | IncorrectTagValue e) {
			log.error("接收会话类型消息错误", e);
		}
	}

	// 接收业务消息时调用此方法
	@Override
	public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectTagValue, UnsupportedMessageType {
		log.info("接收业务消息时调用此方法");
		log.info("message: " + message);
		crack(message, sessionId);
	}

	@Handler
	public void myEmailHandler(Email email, SessionID sessionID) {
		// handler implementation
	}

}
