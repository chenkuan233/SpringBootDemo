package com.springBoot.service.impl;

import com.springBoot.service.TestService;
import com.springBoot.utils.PropertiesUtil;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/3 003 10:42
 */
@Service("testService")
public class TestServiceImpl implements TestService {

	private static Properties properties = PropertiesUtil.getProperties();

	@Override
	public String getProperties() {
		return properties.getProperty("http.port");
	}
}
