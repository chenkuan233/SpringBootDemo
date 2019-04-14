package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 读取 *.properties 配置文件
 * @date 2019/1/10 010 18:09
 */
@Slf4j
public class PropertiesUtil {

	private static Properties properties = new Properties();

	public static Properties getProperties() {
		return getProperties("application.properties");
	}

	public static Properties getProperties(String name) {
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(name);
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(name + "配置文件读取失败", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(name + "配置文件读取流stream关闭失败", e);
				}
			} else {
				log.error(name + "配置文件未读取到内容");
			}
		}
		return properties;
	}
}
