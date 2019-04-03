package com.springBoot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 读取 *.properties 配置文件
 * @date 2019/1/10 010 18:09
 */
public class PropertiesUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Properties properties = new Properties();

	public static Properties getProperties() {
		return getProperties("application.properties");
	}

	public static Properties getProperties(String name) {
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(name);
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error(name + "配置文件读取失败", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(name + "配置文件读取流stream关闭失败", e);
				}
			} else {
				logger.error(name + "配置文件未读取到内容");
			}
		}
		return properties;
	}
}
