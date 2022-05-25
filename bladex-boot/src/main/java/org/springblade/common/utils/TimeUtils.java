package org.springblade.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangqing
 */
public class TimeUtils {

	/**
	 * 开始时间：前一个小时
	 * 结束时间：当前时间
	 * @return map
	 */
	public static Map<String, Long> getTimestamp() {
		Map<String, Long> map = new HashMap<>(16);
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int monthValue = now.getMonthValue();
		int dayOfMonth = now.getDayOfMonth();
		int hour = now.getHour();
		String startTime = year + "-" + monthValue + "-" + dayOfMonth + " " + (hour - 1) + ":00:00";
		String endTime = year + "-" + monthValue + "-" + dayOfMonth + " " + hour + ":00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = df.parse(startTime);
			Date date2 = df2.parse(endTime);
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.setTime(date);
			cal2.setTime(date2);
			long start = cal.getTimeInMillis();
			long end = cal2.getTimeInMillis();
			map.put("startTime", start / 1000);
			map.put("endTime", end / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	/**
	 * 开始时间：前一天00：00：00
	 * 结束时间：前一天23：00：00
	 * @return map
	 */
	public static  Map<String, Long> getYesterdayTimestamp(){
		Map<String, Long> map = new HashMap<>(16);
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int monthValue = now.getMonthValue();
		int dayOfMonth = now.getDayOfMonth();
		String startTime = year + "-" + monthValue + "-" + (dayOfMonth - 1) + " " +   "00:00:00";
		String endTime = year + "-" + monthValue + "-" + (dayOfMonth - 1) + " " +  "23:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = df.parse(startTime);
			Date date2 = df2.parse(endTime);
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.setTime(date);
			cal2.setTime(date2);
			long start = cal.getTimeInMillis();
			long end = cal2.getTimeInMillis();
			map.put("startTime", start / 1000);
			map.put("endTime", end / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	/**
	 * 获取昨天日期和前天日期  yyyy-MM-dd
	 * @return map
	 */
	public static Map<String,String> getDate(){
		Map<String,String> map = new HashMap<>(16);
		LocalDate theDayBeforeYesterday = LocalDate.now().plusDays(-2);
		LocalDate yesterday = LocalDate.now().plusDays(-1);
		map.put("theDayBeforeYesterday",theDayBeforeYesterday.toString());
		map.put("yesterday",yesterday.toString());
		return map;
	}

	/**
	 * 开始时间：当前整点
	 * 结束时间：当前整点减一个小时
	 * @return map
	 */
	public static Map<String,Long> getLocalDate(){
		Map<String, Long> map = new HashMap<>(16);
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int monthValue = now.getMonthValue();
		int dayOfMonth = now.getDayOfMonth();
		int hour = now.getHour();
		String startTime = year + "-" + monthValue + "-" + dayOfMonth + " " + hour + ":00:00";
		String endTime = year + "-" + monthValue + "-" + dayOfMonth + " " + (hour + 1) + ":00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = df.parse(startTime);
			Date date2 = df2.parse(endTime);
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.setTime(date);
			cal2.setTime(date2);
			long start = cal.getTimeInMillis();
			long end = cal2.getTimeInMillis();
			map.put("startTime", start / 1000);
			map.put("endTime", end / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}


}
