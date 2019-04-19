package com.springBoot.controller;

import com.springBoot.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 文件上传controller
 * @date 2019/4/19 019 17:39
 */
@Slf4j
@Controller
public class UploadController {

	@Value("${upload.filePath}")
	private String filePath;

	/**
	 * 文件上传
	 *
	 * @param file file名称不可更改
	 * @return filePath
	 * @throws IOException
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
		// 得到上传时的文件名
		String fileName = file.getOriginalFilename();
		// 开始上传
		fileName = UploadUtil.upload(file.getBytes(), filePath, fileName);
		if (fileName != null) {
			log.info(filePath + fileName + " 上传成功");
		}
		return filePath + fileName;
	}

}
