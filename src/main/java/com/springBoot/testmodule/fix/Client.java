package com.springBoot.testmodule.fix;

import lombok.extern.slf4j.Slf4j;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;

import java.util.Date;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 客户端
 * @date 2019/5/9 009 14:14
 */
@Slf4j
public class Client {

	public static void main(String[] args) throws ConfigError, SessionNotFound, InterruptedException {
		Application application = new MyApplication();

		SessionSettings settings = new SessionSettings("quickfix-client.properties");
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		Initiator initiator = new SocketInitiator(application, storeFactory, settings, logFactory, messageFactory);
		initiator.start();

		SessionID sessionID = new SessionID("FIX.4.4", "chenClient", "chenServer");
		for (int i = 0; i < 5; i++) {
			NewOrderSingle order = new NewOrderSingle();
			order.set(new ClOrdID("5678"));
			order.set(new Account("100"));
			order.set(new HandlInst('1'));
			order.set(new OrderQty(45.00));
			order.set(new Price(25.40));
			order.set(new Symbol("USD/EUR"));
			order.set(new Side(Side.BUY));
			order.set(new TransactTime(new Date()));
			order.set(new OrdType(OrdType.LIMIT));
			order.setField(new StringField(1001, "测试字段"));
			// 发送消息
			Session.sendToTarget(order, sessionID);
			Thread.sleep(5000); // 延时5秒再次发送
		}
	}

}
