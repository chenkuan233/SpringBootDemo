package com.springBoot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 日期时间工具类
 * @date 2019/1/15 015 17:09
 */
public final class DateUtil {

	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String DATE_FORMAT_L = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final String DATE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";
	private static final String DATE_TIME_FORMAT_L = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat getSimpleDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	private static Calendar calendar() {
		Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal;
	}

	private static Date weekDay(int week) {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}

	// 获取当前日期时间
	private static Date now() {
		return new Date();
	}

	// 获取日期(yyyyMMdd)
	public static String date() {
		return getSimpleDateFormat(DATE_FORMAT).format(now());
	}

	// 获取日期(yyyy-MM-dd)
	public static String dateWithL() {
		return getSimpleDateFormat(DATE_FORMAT_L).format(now());
	}

	// 获取时间(HH:mm:ss)
	public static String time() {
		return getSimpleDateFormat(TIME_FORMAT).format(now());
	}

	// 获取日期时间(yyyyMMdd HH:mm:ss)
	public static String dateTime() {
		return getSimpleDateFormat(DATE_TIME_FORMAT).format(now());
	}

	// 获取日期时间(yyyy-MM-dd HH:mm:ss)
	public static String dateTimeWithL() {
		return getSimpleDateFormat(DATE_TIME_FORMAT_L).format(now());
	}

	// 获得当前时间的毫秒数
	public static long millis() {
		return System.currentTimeMillis();
	}

	// 获得当前Chinese月份
	public static int month() {
		return calendar().get(Calendar.MONTH) + 1;
	}

	// 获得月份中的第几天
	public static int dayOfMonth() {
		return calendar().get(Calendar.DAY_OF_MONTH);
	}

	// 今天是星期的第几天
	public static int dayOfWeek() {
		return calendar().get(Calendar.DAY_OF_WEEK);
	}

	// 今天是年中的第几天
	public static int dayOfYear() {
		return calendar().get(Calendar.DAY_OF_YEAR);
	}

	// 判断原日期是否在目标日期之前
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	// 判断原日期是否在目标日期之后
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	// 判断两日期是否相同
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	// 判断某个日期是否在某个日期范围
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	// 获得当前月的第一天 HH:mm:ss SS为零
	public static Date firstDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		return cal.getTime();
	}

	// 获得当前月的最后一天 HH:mm:ss为0，毫秒为999
	public static Date lastDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
		cal.set(Calendar.HOUR_OF_DAY, 0); // H置零
		cal.set(Calendar.MINUTE, 0); // m置零
		cal.set(Calendar.SECOND, 0); // s置零
		cal.set(Calendar.MILLISECOND, 0); // S置零
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); // 月份+1
		cal.set(Calendar.MILLISECOND, -1); // 毫秒-1
		return cal.getTime();
	}

	// 获得周五日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	public static Date friday() {
		return weekDay(Calendar.FRIDAY);
	}

	// 获得周六日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	public static Date saturday() {
		return weekDay(Calendar.SATURDAY);
	}

	// 获得周日日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	public static Date sunday() {
		return weekDay(Calendar.SUNDAY);
	}

	// 将字符串日期时间转换成java.util.Date类型 日期时间格式yyyyMMdd HH:mm:ss
	public static Date parseDatetime(String datetime) throws ParseException {
		return getSimpleDateFormat(DATE_TIME_FORMAT).parse(datetime);
	}

	// 将字符串日期转换成java.util.Date类型 日期时间格式yyyyMMdd
	public static Date parseDate(String date) throws ParseException {
		return getSimpleDateFormat(DATE_FORMAT).parse(date);
	}

	// 将字符串日期转换成java.util.Date类型 时间格式 HH:mm:ss
	public static Date parseTime(String time) throws ParseException {
		return getSimpleDateFormat(TIME_FORMAT).parse(time);
	}

	// 英文日期转换为中文日期
	public static String parseEnDateToCH(String date, String sourceFormat, String descFormat) {
		SimpleDateFormat sdf_ch = new SimpleDateFormat(descFormat);
		SimpleDateFormat sdf_en = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
		String dateCH = "";
		try {
			dateCH = sdf_ch.format(sdf_en.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateCH;
	}

	// 获取两个日期之间相差天数
	public static int getDasyBetween(String date1, String date2) throws ParseException {
		if (date1 == null || date1.equals("") || date2 == null || date2.equals("")) {
			return -10000;
		}
		long mill_start = parseDate(date1).getTime();
		long mill_end = parseDate(date2).getTime();
		long days_between = (mill_end - mill_start) / (1000 * 60 * 60 * 24);
		return Integer.parseInt(String.valueOf(days_between));
	}

}
