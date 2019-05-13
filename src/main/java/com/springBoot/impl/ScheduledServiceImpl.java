package com.springBoot.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 定时任务测试
 * @date 2019/5/13 013 17:11
 */
@Slf4j
@Service
public class ScheduledServiceImpl {

	//初始延迟1秒，每隔2秒执行一次
	//@Scheduled(initialDelay = 1000, fixedDelay = 2000)
	public void test1() {
		log.info("当前时间(初始延迟1秒，每隔2秒执行一次): " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}

	//从0秒开始，每隔3秒执行一次
	//@Scheduled(cron="0/3 * * * * ?")
	public void test2() {
		log.info("当前时间(每隔3秒执行一次): " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}

	/**
	 * cron表达式格式：
	 *
	 * {秒数} {分钟} {小时} {日期} {月份} {星期} {年份(可为空)}
	 *
	 * {秒数}{分钟} ==> 允许值范围: 0~59 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 *
	 * "*" 代表每隔1秒钟触发；
	 * "," 代表在指定的秒数触发，比如"0,15,45"代表0秒、15秒和45秒时触发任务，"30"代表30秒时触发任务
	 * "-"代表在指定的范围内触发，比如"25-45"代表从25秒开始触发到45秒结束触发，每隔1秒触发1次
	 * "/"代表触发步进(step)，"/"前面的值代表初始值(""等同"0")，后面的值代表偏移量，比如"0/20"或者"/20"代表从0秒钟开始，每隔20秒钟触发1次，即0秒触发1次，20秒触发1次，40秒触发1次；"5/20"代表5秒触发1次，25秒触发1次，45秒触发1次；"10-45/20"代表在[10,45]内步进20秒命中的时间点触发，即10秒触发1次，30秒触发1次
	 *
	 * {小时} ==> 允许值范围: 0~23 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常,占位符和秒数一样
	 * {日期} ==> 允许值范围: 1~31 ,不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 * {星期} ==> 允许值范围: 1~7 (SUN-SAT),1代表星期天(一星期的第一天)，以此类推，7代表星期六(一星期的最后一天)，不允许为空值，若值不合法，调度器将抛出SchedulerException异常
	 * {年份} ==> 允许值范围: 1970~2099 ,允许为空，若值不合法，调度器将抛出SchedulerException异常
	 *
	 * 注意：除了{日期}和{星期}可以使用"?"来实现互斥，表达无意义的信息之外，其他占位符都要具有具体的时间含义，且依赖关系为：年->月->日期(星期)->小时->分钟->秒数
	 */

}
