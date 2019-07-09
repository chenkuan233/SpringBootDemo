package com.springBoot.impl;

import com.springBoot.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 定时任务类
 * @date 2019/7/8 008 16:19
 */
@Slf4j
@Component
public class ScheduledTask {

	//上传文件缓存文件夹
	@Value("${upload.tempDir}")
	private String tempDir;

	/**
	 * 每天3点，删除今天之前的上传缓存文件
	 */
	@Scheduled(cron = "* * 3 * * ?")
	public void deleteUploadTemp() {
		try {
			File dir = new File(tempDir);
			if (!dir.exists()) return;
			File[] dirList = dir.listFiles();
			if (dirList == null) return;

			for (File file : dirList) {
				if (file.isDirectory()) {
					if (file.getName().compareTo(DateUtil.date()) < 0)
						FileUtils.deleteDirectory(file);
				}
			}
		} catch (IOException e) {
			log.error("删除上传缓存文件夹失败", e);
		}
	}
}
