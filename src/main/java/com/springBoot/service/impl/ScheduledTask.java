package com.springBoot.service.impl;

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

	/**
	 * cron表达式格式：
	 * <p>
	 * {秒数} {分钟} {小时} {日期} {月份} {星期} {年份(可为空)}
	 * <p>
	 * {秒数}{分钟} ==> 允许值范围: 0~59 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 * <p>
	 * "*" 代表每隔1秒钟触发；
	 * "," 代表在指定的秒数触发，比如"0,15,45"代表0秒、15秒和45秒时触发任务，"30"代表30秒时触发任务
	 * "-"代表在指定的范围内触发，比如"25-45"代表从25秒开始触发到45秒结束触发，每隔1秒触发1次
	 * "/"代表触发步进(step)，"/"前面的值代表初始值(""等同"0")，后面的值代表偏移量，比如"0/20"或者"/20"代表从0秒钟开始，每隔20秒钟触发1次，即0秒触发1次，20秒触发1次，40秒触发1次；"5/20"代表5秒触发1次，25秒触发1次，45秒触发1次；"10-45/20"代表在[10,45]内步进20秒命中的时间点触发，即10秒触发1次，30秒触发1次
	 * <p>
	 * {小时} ==> 允许值范围: 0~23 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常,占位符和秒数一样
	 * {日期} ==> 允许值范围: 1~31 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 * {星期} ==> 允许值范围: 1~7 (SUN-SAT),1代表星期天(一星期的第一天)，以此类推，7代表星期六(一星期的最后一天)，不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 * {年份} ==> 允许值范围: 1970~2099 ,允许为空，若值不合法，调度器将抛出SchedulerException异常
	 * <p>
	 * 注意：除了{日期}和{星期}可以使用"?"来实现互斥，表达无意义的信息之外，其他占位符都要具有具体的时间含义，且依赖关系为：年->月->日期(星期)->小时->分钟->秒数
	 */

	//上传文件缓存文件夹
	@Value("${upload.tempDir}")
	private String tempDir;

	/**
	 * 每天3点，删除今天之前的上传缓存文件
	 */
	@Scheduled(cron = "0 0 3 * * ?")
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
