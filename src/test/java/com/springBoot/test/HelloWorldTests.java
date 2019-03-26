package com.springBoot.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/1/17 017 11:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mvc;

	@Before
	public void setUpMockMvc() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 初始化MockMvc对象
	}

	@Test
	public void findUser() throws Exception {
		String userCode = "[{\"userCode\":\"123\"}]";
		mvc.perform(MockMvcRequestBuilders.get("/user/findUser").accept(MediaType.APPLICATION_JSON_UTF8).content(userCode.getBytes()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void saveUser() throws Exception {
		String userJson = "{\"userCode\":\"345\",\"password\":\"345\",\"userName\":\"敏敏\",\"email\":\"345@qq.com\"}";
		mvc.perform(MockMvcRequestBuilders.get("/user/saveUser")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(userJson.getBytes()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}
}
