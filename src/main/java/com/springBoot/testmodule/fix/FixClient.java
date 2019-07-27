package com.springBoot.testmodule.fix;

import org.apache.log4j.PropertyConfigurator;
import quickfix.*;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider.TemplateMapping;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 客户端
 * @date 2019/5/9 009 12:00
 */
public class FixClient {

	private static SocketInitiator initiator = null;

	private static final String SETTING_ACCEPTOR_TEMPLATE = "AcceptorTemplate";
	private static final String SETTING_SOCKET_ACCEPT_ADDRESS = "SocketAcceptAddress";
	private static final String SETTING_SOCKET_ACCEPT_PORT = "SocketAcceptPort";

	public static SocketInitiator getInitiator() {
		return initiator;
	}

	private final Map<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings = new HashMap<>();

	public Map<InetSocketAddress, List<TemplateMapping>> getDynamicSessionMappings() {
		return dynamicSessionMappings;
	}

	public FixClient() {
	}

	/**
	 * 指定配置文件启动
	 *
	 * @param propFile
	 */
	public FixClient(String propFile) throws ConfigError, FieldConvertError {
		// 设置配置文件
		SessionSettings settings = new SessionSettings(propFile);

		// 设置一个APPlication
		Application application = new MyApplication();

		/**
		 * quickfix.MessageStore 有2种实现。quickfix.JdbcStore,quickfix.FileStore .
		 * JdbcStoreFactory 负责创建JdbcStore ，
		 * FileStoreFactory 负责创建FileStorequickfix 默认用文件存储，因为文件存储效率高。
		 */
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);

		LogFactory logFactory = new FileLogFactory(settings);

		MessageFactory messageFactory = new DefaultMessageFactory();

		initiator = new SocketInitiator(application, storeFactory, settings, logFactory, messageFactory);

		// configureDynamicSessions(settings, application, storeFactory, logFactory, messageFactory);
	}

	private void startServer() throws RuntimeError, ConfigError {
		initiator.start();
	}

	public void stop() {
		initiator.stop();
	}

	/**
	 * 被调用的start方法
	 */
	public static void start() throws ConfigError, FieldConvertError {
		FixClient servercom = new FixClient("quickfix-client.properties");
		servercom.startServer();
	}

	/**
	 * 测试本地使用的main方法
	 *
	 * @param args
	 */
	public static void main(String[] args) throws ConfigError, FieldConvertError {
		// 配置LOG日记
		PropertyConfigurator.configure("logback.xml");
		FixClient fixClient = new FixClient("quickfix-client.properties");
		fixClient.startServer();
	}

	/**
	 * <pre>
	 * 以下几个方法是配置动态SessionProvider
	 * FIX 支持同时支持不同的SessionTemple，使用不同的数据处理Provider体现在配置文件中的[session]标签
	 *
	 * @param settings
	 * @param application
	 * @param messageStoreFactory
	 * @param logFactory
	 * @param messageFactory
	 */
	private void configureDynamicSessions(SessionSettings settings,
										  Application application,
										  MessageStoreFactory messageStoreFactory,
										  LogFactory logFactory,
										  MessageFactory messageFactory) throws ConfigError, FieldConvertError {
		// 获取配置文件中的[session]标签集合
		Iterator<SessionID> sectionIterator = settings.sectionIterator();
		while (sectionIterator.hasNext()) {
			SessionID sessionID = sectionIterator.next();
			if (isSessionTemplate(settings, sessionID)) {
				//判断是否使用了AcceptorTemplate
				InetSocketAddress address = getAcceptorSocketAddress(settings, sessionID);
				getMappings(address).add(new TemplateMapping(sessionID, sessionID));
			}
		}

		/*for (Map.Entry<InetSocketAddress, List<TemplateMapping>> entry : dynamicSessionMappings.entrySet()) {
			initiator.setSessionProvider(
					entry.getKey(),
					new DynamicAcceptorSessionProvider(settings, entry
							.getValue(), application, messageStoreFactory,
							logFactory, messageFactory));
		}*/
	}

	private boolean isSessionTemplate(SessionSettings settings, SessionID sessionID) throws ConfigError, FieldConvertError {
		return settings.isSetting(sessionID, SETTING_ACCEPTOR_TEMPLATE) && settings.getBool(sessionID, SETTING_ACCEPTOR_TEMPLATE);
	}

	private List<TemplateMapping> getMappings(InetSocketAddress address) {
		List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
		if (mappings == null) {
			mappings = new ArrayList<>();
			dynamicSessionMappings.put(address, mappings);
		}
		return mappings;
	}

	private InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID sessionID) throws ConfigError, FieldConvertError {
		String acceptorHost = "0.0.0.0";
		if (settings.isSetting(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS)) {
			acceptorHost = settings.getString(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS);
		}
		int acceptorPort = (int) settings.getLong(sessionID, SETTING_SOCKET_ACCEPT_PORT);

		InetSocketAddress address = new InetSocketAddress(acceptorHost, acceptorPort);
		return address;
	}
}
