package com.springBoot.impl.fix;

import lombok.extern.slf4j.Slf4j;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelRequest;

import java.util.Date;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/5/9 009 14:14
 */
@Slf4j
public class Client {

	public static void main(String[] args) throws Exception {
		Application application = new MyApplication();

		SessionSettings settings = new SessionSettings("quickfix-client.properties");
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		Initiator initiator = new SocketInitiator(application, storeFactory, settings, logFactory, messageFactory);
		initiator.start();
		// initiator.stop();

		/*SessionID sessionID = new SessionID("FIX.4.4", "chenClient", "chenServer");
		while (true) {
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
			try {
				Session.sendToTarget(order, sessionID);
				Thread.sleep(3000);
			} catch (SessionNotFound e) {
				log.error("SessionNotFound", e);
			}
		}*/

	}

	public static void sendRequest() throws SessionNotFound {
		OrderCancelRequest message = new OrderCancelRequest(
				new OrigClOrdID("123"),
				new ClOrdID("321"),
				new Side(Side.BUY),
				new TransactTime(new Date())
		);
		message.set(new Text("Cancel My Order!"));

		Session.sendToTarget(message, "CHEN", "SERVER");
	}

}
