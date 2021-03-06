package com.springBoot.controller;

import com.google.gson.Gson;
import com.springBoot.utils.*;
import com.springBoot.utils.config.applicationContext.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 文件上传controller
 * @date 2019/4/19 019 17:39
 */
@Slf4j
@Controller
public class UploadController {

	@Value("${upload.tempDir}")
	private String tempDir;

	private Gson gson = new Gson();

	/**
	 * 文件上传
	 *
	 * @param file file名称不可更改
	 * @return filePath
	 */
	@ResponseBody
	@RequestMapping("/upload/{serviceName}/{funcName}")
	public String upload(@RequestParam("file") MultipartFile file, @PathVariable String serviceName, @PathVariable String funcName) throws Exception {
		// 判断serviceBean是否存在
		List<String> beanNames = SpringBeanUtil.getBeanNames();
		if (!beanNames.contains(serviceName)) {
			log.error("请求无对应服务: " + serviceName + "." + funcName);
			return gson.toJson(Response.returnData(-1, "请求无对应服务: " + serviceName + "." + funcName));
		}

		// 获取serviceBean
		Object service = SpringBeanUtil.getBean(serviceName);
		Class<?> clazz = service.getClass();

		// 获取funcName所在的接口下的Method
		Method method = ServiceUtil.getMethod(clazz, funcName);
		if (method == null) {
			log.error("请求无对应方法: " + serviceName + "." + funcName);
			return gson.toJson(Response.returnData(-1, "请求无对应方法: " + serviceName + "." + funcName));
		}

		// 获取上传文件原始文件名
		String fileName = file.getOriginalFilename();
		String filePath = tempDir + DateUtil.date() + "/" + NumberUtil.getRandomUUID() + "/";

		// 将MultipartFile转为File缓存文件
		File tempFile = FileUtil.bytesToFile(file.getBytes(), filePath, fileName);
		if (tempFile == null) {
			log.error(filePath + fileName + "缓存文件写入失败");
			return gson.toJson(Response.returnData(-1, file.getOriginalFilename() + "上传失败"));
		}

		Object[] params = new Object[]{tempFile};
		// 执行方法（method) 在指定对象(target)上，使用指定参数(args)
		Object obj = method.invoke(service, params);

		log.info(fileName + " 上传成功");
		return gson.toJson(Response.returnData(0, obj));
	}
}
