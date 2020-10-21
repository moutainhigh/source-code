package com.yd.core.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * @Filename JodaTime.java
 * @Description
 * @Version 1.0
 * @Author 杨碧涛
 * @Email ybtccc@163.com
 * <p>
 * <li>Author: 杨碧涛</li>
 * <li>Date: 2016/11/17</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class JodaTime {
	
	public static final String	YYYY_MM_DD			= "yyyy-MM-dd";
	public static final String	YYYYMMDD			= "yyyyMMdd";
	public static final String	YYYY_MM_DD2			= "yyyy/MM/dd";
	public static final String	YYYY_MM_DD_HH_MM_SS	= "yyyy-MM-dd HH:mm:ss";
	public static final String	YYYY_MM_DD_HH_MM	= "yyyy-MM-dd HH:mm";
	public static final String	YYYYMMDDHHMMSS		= "yyyyMMddHHmmss";
	
	public static String format(Date date, String format) {
		return new DateTime(date).toString(format);
	}
	
	public static Date parse(String str) {
		return new DateTime(str).toDate();
	}

	public static Date parse(String str,String pattern) {
		return DateTime.parse(str,DateTimeFormat.forPattern(pattern)).toDate();
	}
	
	public static Date beginOfDay(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMinimumValue().minuteOfHour()
			.withMinimumValue().secondOfMinute().withMinimumValue();
		return dateTime.toDate();
	}
	public static String beginOfMonth(Date day) {
		DateTime dateTime = new DateTime(day).dayOfMonth().withMinimumValue().hourOfDay().withMinimumValue().minuteOfHour()
				.withMinimumValue().secondOfMinute().withMinimumValue();
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	public static String yeBeginOfDay(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMinimumValue().minuteOfHour()
				.withMinimumValue().secondOfMinute().withMinimumValue();
		dateTime = dateTime.minusDays(1);
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static Date beginNOfDay(Date day, int n) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMinimumValue().minuteOfHour()
			.withMinimumValue().secondOfMinute().withMinimumValue();
		dateTime = dateTime.plusDays(n);
		return dateTime.toDate();
	}
	
	public static String beginOfDayStr(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMinimumValue().minuteOfHour()
			.withMinimumValue().secondOfMinute().withMinimumValue();
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String beginNOfDayStr(Date day, int n) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMinimumValue().minuteOfHour()
			.withMinimumValue().secondOfMinute().withMinimumValue();
		dateTime = dateTime.plusDays(n);
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static Date endOfDay(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMaximumValue().minuteOfHour()
			.withMaximumValue().secondOfMinute().withMaximumValue();
		return dateTime.toDate();
	}
	public static String yeEndOfDay(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMaximumValue().minuteOfHour()
				.withMaximumValue().secondOfMinute().withMaximumValue();
		dateTime = dateTime.minusDays(1);
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static Date endNOfDay(Date day, int n) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMaximumValue().minuteOfHour()
			.withMaximumValue().secondOfMinute().withMaximumValue();
		dateTime = dateTime.plusDays(n);
		return dateTime.toDate();
	}
	
	public static String endOfDayStr(Date day) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMaximumValue().minuteOfHour()
			.withMaximumValue().secondOfMinute().withMaximumValue();
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String endNOfDayStr(Date day, int n) {
		DateTime dateTime = new DateTime(day).hourOfDay().withMaximumValue().minuteOfHour()
			.withMaximumValue().secondOfMinute().withMaximumValue();
		
		dateTime = dateTime.plusDays(n);
		return dateTime.toString(YYYY_MM_DD_HH_MM_SS);
	}
	
	public static int daysBetween(Date start, Date end) {
		return Days.daysBetween(new DateTime(start), new DateTime(end)).getDays();
	}
	
	public static Date newTime(int year, int month, int day) {
		DateTime dateTime = new DateTime(year, month, day, 0, 0);
		return dateTime.toDate();
	}
	
	public static Date newTime(String str) {
		DateTime dateTime = new DateTime(str);
		return dateTime.toDate();
	}
	
	public static void main(String[] args) {
		Date cc = parse("2017-07-01");
		System.out.println(cc);
		String ss = JodaTime.format(new Date(), JodaTime.YYYY_MM_DD_HH_MM);
		System.out.println(ss);
	}
}
