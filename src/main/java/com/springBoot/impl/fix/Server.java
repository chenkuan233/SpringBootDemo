package com.springBoot.impl.fix;

import quickfix.*;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 服务端
 * @date 2019/5/9 009 14:06
 */
public class Server {

	public static void main(String[] args) throws Exception {
		Application application = new MyApplication();

		SessionSettings settings = new SessionSettings("quickfix-server.properties");
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		Acceptor acceptor = new SocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
		acceptor.start();
		// acceptor.stop();
	}

}
