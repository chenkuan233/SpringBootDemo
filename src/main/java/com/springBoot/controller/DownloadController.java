package com.springBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

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
	 *
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/download")
	public String download(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String filePath = request.getParameter("filePath");
		if (StringUtils.isEmpty(filePath)) {
			return "文件路径为空";
		}
		// 判断文件是否存在
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return "文件不存在";
		}
		String fileName = file.getName();
		// 告诉浏览器输出内容格式 application/octet-stream，application/force-download
		response.setContentType("application/octet-stream");
		// 设置下载而并不是打开 两种属性：attachment保存，inline直接显示  浏览器点保存后，文件以filename的值命名
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		// 设置文件大小
		response.setContentLength((int) file.length());

		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			OutputStream os = response.getOutputStream();
			int i;
			while ((i = bis.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			log.info(filePath + "下载成功");
			return "下载成功";
		} catch (ClientAbortException e) {
			log.error("客户端中断了连接");
		} catch (IOException e) {
			log.error(filePath + "下载失败", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					log.error("BufferedInputStream流关闭失败", e);
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("FileInputStream流关闭失败", e);
				}
			}
		}
		return "下载失败";
	}

}
