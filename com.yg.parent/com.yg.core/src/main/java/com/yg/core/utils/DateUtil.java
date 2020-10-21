package com.yg.core.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATEFULLTIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DEFAULT_DATE = "yyyy-MM-dd HH:mm:ss";

    public static DateTime stringToDateForYYYY_MM_DD(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return new DateTime(str);
    }

    public static DateTime stringToDateForYYYY_MM_DD_HH_MM_SS(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return new DateTime(str.replace(" ", "T"));
    }

    /**
     * 获得指定格式的当前日期字符串.
     *
     * @param date
     * @param format
     * @return
     */
    public static String date(Date date, String format) {
        if (date == null) {
            return "";
        }

        return (new SimpleDateFormat(format)).format(date);
    }

    public static Date getDateForYyyyMMddHHmmss(String timeEnd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = dateFormat.parse(timeEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateForSdf(String timeEnd, String sdf) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(sdf);
        Date date = null;
        try {
            date = dateFormat.parse(timeEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getMinDayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前月的最小日期.
     *
     * @return
     */
    public static Date getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return getMinDayDate(cal.getTime());
    }

    /**
     * 获取下一个月的最小日期.
     *
     * @return
     */
    public static Date getPerFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return getMinDayDate(cal.getTime());
    }

    /**
     * 获取指定月的最小时间.
     *
     * @param date
     * @return
     */
    public static Date getMinDateByMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return getMinDayDate(cal.getTime());
    }

    /**
     * 获取指定月的最大时间.
     *
     * @param date
     * @return
     */
    public static Date getMaxDateByMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    /**
     * 时间戳转换成日期格式字符串  yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String chageTime(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE);
        String str_date = dateFormat.format(new Date(date));
        return str_date;
    }

    public static void main(String[] args) {
        System.out.println(chageTime(1561324980000l));
    }
}
