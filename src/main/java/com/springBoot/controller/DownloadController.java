package com.springBoot.controller;

import com.springBoot.utils.Base64Util;
import com.springBoot.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLDecoder;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 下载文件Controller
 * @date 2019/4/27 027 12:06
 */
@Slf4j
@Controller
public class DownloadController {

	/**
	 * 文件下载
	 */
	@ResponseBody
	@RequestMapping("/download")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取参数 解码
		String filePath = request.getParameter("file");
		if (StringUtils.isEmpty(filePath)) {
			return "<h3 style='color: red;text-align: center;'>文件路径为空</h3>";
		}
		filePath = URLDecoder.decode(Base64Util.decode(filePath), "UTF-8");

		// 判断文件是否存在
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return "<h3 style='color: red;text-align: center;'>文件不存在</h3>";
		}

		// 调用下载方法
		FileUtil.downloadFile(request, response, file);

		return "";
	}

}
