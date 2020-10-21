package com.yd.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author
 */
@SuppressWarnings({"JavadocReference", "unused", "JavaDoc", "UnnecessaryUnboxing",
        "StringConcatenationInLoop", "WeakerAccess", "UnusedReturnValue", "MagicConstant",
        "UnusedAssignment", "RedundantStringOperation", "Convert2Diamond"})
public final class DateUtils {

    public static final String TODAY_START_DATETIME_FORMAT = "yyyy-MM-dd 00:00:00";

    public static final String TODAY_END_DATETIME_FORMAT = "yyyy-MM-dd 59:59:59";

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_DATEFULLDATE_FORMAT = "yyyyMMdd";

    public static final String DEFAULT_YEAR_FORMAT = "yyyy";

    public static final String DEFAULT_MONTH_FORMAT = "MM";

    public static final String DEFAULT_DAY_FORMAT = "dd";

    public static final long MILLISECONDS_A_DAY = 24 * 3600 * 1000;

    public static final long MILLISECONDS_A_HOUR = 3600 * 1000;

    public static final long MILLISECONDS_A_SECOND = 1000;

    public static final long MILLISECONDS_A_MIN = MILLISECONDS_A_SECOND * 60;

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DEFAULT_DATEFULLTIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * 验证日期字符串，有效日期范围1900-1-1到2099-12-31.
     */
    private static final Pattern PATTERN = Pattern
            .compile("(?:(?:19|20)\\d{2})-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|[12][0-9]|3[01])");

    private static final int FOUR = 4;

    private static final int SIX = 6;

    private static final int TEN = 10;

    /**
     * 根据时间戳获取支付单
     *
     * @return
     */
    public static String getPayTransactionByTimestamp() {
        Long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        String str1 = formatter.format(date);
        int len = String.valueOf(timeMillis).length();
        String str2 = String.valueOf(timeMillis).substring(len - 3, len);
        return str1 + str2;
    }

    /**
     * 当前时间加上days天.
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public static Date addSeconds(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Date getMaxDayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 当前时间加上days月.
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 获取当前月的最大日期.
     *
     * @return
     */
    public static Date getMaxDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    /**
     * 获取当前年份.
     *
     * @return
     */
    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月.
     *
     * @return
     */
    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日.
     *
     * @return
     */
    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月的最小日期.
     *
     * @return
     */
    public static Date getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
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
        return cal.getTime();
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
     * 取得某月的的最后一天.
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfLastMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 当前时间加上years年.
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * 获得指定格式的日期时间字符串.
     *
     * @param date
     * @param format
     * @return
     */
    public static String datetime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date());
    }

    /**
     * 获得指定格式的日期时间字符串.
     *
     * @param date
     * @param format
     * @return
     */
    public static String datetime(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 获得指定格式的日期时间字符串.
     *
     * @param 日期字符串
     * @param format
     * @return
     */
    public static String datetime(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
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

    /**
     * 获得指定格式的当前日期字符串.
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String date(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(dateStr);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获得"yyyy-MM-dd"格式的当前日期字符串.
     *
     * @param date
     * @return
     */
    public static String getNowDateStr() {
        return getNowDatetimeStr(DEFAULT_DATE_FORMAT);
    }

    /**
     * 获得"yyyy-MM-dd HH:mm:ss"格式的当前日期时间字符串.
     *
     * @param date
     * @return
     */
    public static String getNowDatetimeStr() {
        return getNowDatetimeStr(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 获得"yyyyMMddHHmmss"格式的当前日期时间字符串.
     *
     * @param date
     * @return
     */
    public static String getNowDateminStr() {
        return getNowDatetimeStr(DEFAULT_DATEFULLTIME_FORMAT);
    }

    /**
     * 获得当前日期时间字符串.
     *
     * @param format 日期格式
     * @return 日期时间字符串
     */
    public static String getNowDatetimeStr(String format) {
        Calendar cal = Calendar.getInstance();
        return datetime(cal.getTime(), format);
    }

    /**
     * ֻ只取当前时间的日期部分，小时、分、秒等字段归零.
     */
    @Deprecated
    public static Date dateOnly(Date date) {
        return new Date(date.getTime() / MILLISECONDS_A_DAY);
    }

    /**
     * ֻ只取当前时间的日期部分，小时、分、秒等字段归零.
     */
    public static Date dateOnlyExt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date dateMaxExt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * ֻ只取当前时间的日期部分，小时、分、秒等字段归零.
     */
    public static Date dateMinTime(Date date) {
        return dateOnlyExt(date);
    }

    /**
     * 把类似2007-2-2 7:1:8的时间串变为标准的2007-02-02 07:01:08.
     *
     * @param dateTimeStr 未校正日期值
     * @return 日期对象
     */
    public static String getStandDateTimeStr(String dateTimeStr) {
        if (dateTimeStr == null || "".equals(dateTimeStr)) {
            return "";
        }

        String str = dateTimeStr.replaceAll("\\s+", "|");
        String[] a = str.split("\\|");
        List<String> list = Arrays.asList(a);
        String datetime = "";
        int count = 1;
        for (int i = 0; i < list.size(); i++) {
            String temp = (String) list.get(i);
            StringTokenizer st;
            if (i == 0) {
                st = new StringTokenizer(temp, "-");
            } else {
                st = new StringTokenizer(temp, ":");
            }
            int k = st.countTokens();
            for (int j = 0; j < k; j++) {
                String sttemp = (String) st.nextElement();
                if (count == 1) {
                    datetime = sttemp;
                } else {
                    if (("0".equals(sttemp)) || ("00".equals(sttemp))) {
                        sttemp = "0";
                    } else if ((Integer.valueOf(sttemp).intValue()) < TEN) {
                        sttemp = sttemp.replaceAll("0", "");
                    }
                    if (count < FOUR) {
                        if ((Integer.valueOf(sttemp).intValue()) < TEN) {
                            datetime = datetime + "-0" + sttemp;
                        } else {
                            datetime = datetime + "-" + sttemp;
                        }
                    }
                    if (count == FOUR) {
                        if ((Integer.valueOf(sttemp).intValue()) < TEN) {
                            datetime = datetime + " 0" + sttemp;
                        } else {
                            datetime = datetime + " " + sttemp;
                        }
                    }
                    if (count > FOUR) {
                        if ((Integer.valueOf(sttemp).intValue()) < TEN) {
                            datetime = datetime + ":0" + sttemp;
                        } else {
                            datetime = datetime + ":" + sttemp;
                        }
                    }
                }
                count++;
            }
        }

        try {
            getDateFromStr(datetime);
            return datetime;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 把标准的2007-02-02 07:01:08格式转换成日期对象.
     *
     * @param datetime 日期,标准的2007-02-02 07:01:08格式
     * @return 日期对象
     */
    @SuppressWarnings("deprecation")
    public static Date getDateFromStr(String datetime) {
        if (datetime == null || "".equals(datetime)) {
            return new Date();
        }

        String nyr = datetime.trim();

        if (nyr.indexOf(' ') > 0) {
            nyr = nyr.substring(0, nyr.indexOf(' '));
        } else {
            nyr = nyr.substring(0, nyr.length());
        }

        StringTokenizer st = new StringTokenizer(nyr, "-");
        Date date = new Date();
        String temp = "";
        int count = st.countTokens();
        for (int i = 0; i < count; i++) {
            temp = (String) st.nextElement();
            if (i == 0) {
                date.setYear(Integer.parseInt(temp) - 1900);
            }
            if (i == 1) {
                date.setMonth(Integer.parseInt(temp) - 1);
            }
            if (i == 2) {
                date.setDate(Integer.parseInt(temp));
            }
        }

        if (datetime.length() > TEN) {
            String sfm = datetime.substring(11, 19);
            StringTokenizer st2 = new StringTokenizer(sfm, ":");
            count = st2.countTokens();
            for (int i = 0; i < count; i++) {
                temp = (String) st2.nextElement();
                if (i == 0) {
                    date.setHours(Integer.parseInt(temp));
                }
                if (i == 1) {
                    date.setMinutes(Integer.parseInt(temp));
                }
                if (i == 2) {
                    date.setSeconds(Integer.parseInt(temp));
                }
            }
        }
        return date;
    }

    /**
     * 返回两个日期相差天数.
     *
     * @param startDate 起始日期对象
     * @param endDate   截止日期对象
     * @return
     */
    public static int getQuot(Date startDate, Date endDate) {
        long quot = 0;
        quot = endDate.getTime() - startDate.getTime();
        quot = quot / MILLISECONDS_A_DAY;
        return (int) quot;
    }

    /**
     * 返回两个日期相差天数.
     *
     * @param startDateStr 起始日期字符串
     * @param endDateStr   截止期字符串
     * @param format       时间格式
     * @return
     */
    public static int getQuot(String startDateStr, String endDateStr,
                              String format) {
        long quot = 0;
        String str = (format != null && format.length() > 0) ? format
                : DEFAULT_DATE_FORMAT;
        SimpleDateFormat ft = new SimpleDateFormat(str);
        try {
            Date date1 = ft.parse(endDateStr);
            Date date2 = ft.parse(startDateStr);
            quot = date1.getTime() - date2.getTime();
            quot = quot / MILLISECONDS_A_DAY;
        } catch (ParseException e) {
            logger.error("获取两个日期相差天数异常: ", e);
        }
        return (int) quot;
    }

    /**
     * 返回日期字符串："yyyy-MM-dd HH:mm" 格式.
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return ymdhmsFormat.format(date);
    }

    /**
     * Date转换成"yyyy-MM-dd HH:mm:ss"格式的字符串
     *
     * @param date
     * @return
     */
    public static String getDetailDateTime(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        return ymdhmsFormat.format(date);
    }

    /**
     * 按给定格式返回时间的字符串.
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateTime(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
        return ymdhmsFormat.format(date);
    }

    /**
     * 返回两个日期相差的小时.
     *
     * @param startDateStr
     * @param endDateStr
     * @param format
     * @return
     */
    public static int getQuotHours(Date startDate, Date endDate) {
        long quot = 0;
        quot = endDate.getTime() - startDate.getTime();
        quot = quot / MILLISECONDS_A_HOUR;
        return (int) quot;
    }

    /**
     * 返回两个日期相差的秒.
     *
     * @param startDateStr
     * @param endDateStr
     * @param format
     * @return
     */
    public static int getQuotSeconds(Date startDate, Date endDate) {
        long quot = 0;
        quot = endDate.getTime() - startDate.getTime();
        quot = quot / MILLISECONDS_A_SECOND;
        return (int) quot;
    }

    /**
     * 将字符串转换为日期型 格式为: yyyy-MM-dd.
     *
     * @param dateTime
     * @return
     */
    public static Date getDateTime(String dateTime) {
        return getDateTime(dateTime, "yyyy-MM-dd");
    }

    /**
     * 将字符串转换为日期型 格式为: HH:mm
     *
     * @param dateTime
     * @return
     */
    public static Date getTime(String dateTime) {
        return getDateTime(dateTime, "HH:mm");
    }

    public static Date getDateTime(String dateTime, String formatPattern) {
        try {
            if (StringUtils.isNotBlank(dateTime)
                    && StringUtils.isNotBlank(formatPattern)) {
                SimpleDateFormat format = new SimpleDateFormat(formatPattern);
                return format.parse(dateTime);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * 将字符串转换为日期型 格式为: yyyy-MM-dd HH:mm:ss.
     *
     * @param dateTime
     * @return
     */
    public static Date getDateDetailTime(String dateTime) {
        try {
            if (StringUtils.isNotBlank(dateTime)) {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");

                return format.parse(dateTime);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * 取当前的时间戳，在页面上保证URL唯一，防止缓存.
     *
     * @return
     */
    public static long getDtSeq() {
        return System.currentTimeMillis();
    }

    /**
     * 判断是否在参数日期的最大值和最小值之间.
     *
     * @param date
     * @return
     */
    public static boolean isBetween(Date min, Date compare) {
        Boolean ret = false;
        Date minDate = DateUtils.dateOnlyExt(min);
        Date maxDate = DateUtils.dateOnlyExt(DateUtils.addDays(min, 1));
        if (compare.after(minDate) && compare.before(maxDate)) {
            ret = true;
        }
        return ret;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }

    /**
     * 获取本月/上月/本季度的月初和月末.
     *
     * @param monthRange 取值范围{cm:本月，pm:上月，sm:本季度}
     * @return Map{firstDay:yyyy-MM-dd, lastDay:yyyy-MM-dd}
     */
    public static Map<String, String> getFLDayMap(String monthRange) {
        return getFLDayMap(monthRange, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取本月/上月/本季度的月初和月末.
     *
     * @param monthRange 取值范围{cm:本月，pm:上月，sm:本季度}
     * @param pattern
     * @return Map{firstDay:yyyy-MM-dd, lastDay:yyyy-MM-dd}
     */
    public static Map<String, String> getFLDayMap(String monthRange,
                                                  String pattern) {
        Map<String, String> rs = new LinkedHashMap<String, String>();

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        String range = monthRange;

        if (StringUtils.isBlank(range)) {
            range = "cm";
        }

        if (!"sm".equals(range)) {
            if ("pm".equals(range)) {
                calendar.add(Calendar.MONTH, -1);
            }

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            rs.put("firstDay", df.format(calendar.getTime()));

            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            rs.put("lastDay", df.format(calendar.getTime()));

            return rs;
        }

        /*
         * 本季度的月初和月末
         */
        int[][] seasons = {{2, 4}, {5, 7}, {8, 10}, {11, 1}};
        int cm = calendar.get(Calendar.MONTH) + 1;

        for (int[] im : seasons) {
            if (cm >= im[0] && cm <= im[1]) {
                calendar.set(Calendar.MONTH, im[0] - 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                rs.put("firstDay", df.format(calendar.getTime()));

                calendar.set(Calendar.MONTH, im[1] - 1);
                calendar.set(Calendar.DAY_OF_MONTH,
                        calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                rs.put("lastDay", df.format(calendar.getTime()));

                break;
            }
        }

        return rs;
    }

    /**
     * 获取某日期的年份字符串.
     *
     * @param date
     * @return 字符串类型的年份
     */
    public static String getYearString(Date date) {
        return DateUtil.date(date, DEFAULT_YEAR_FORMAT);
    }

    /**
     * 获取某日期的年份数字.
     *
     * @param date
     * @return 数字类型的年份
     */
    public static int getYearInteger(Date date) {
        return Integer.parseInt(DateUtil.date(date, DEFAULT_YEAR_FORMAT));
    }

    /**
     * 获取某日期的月份字符串.
     *
     * @param date
     * @return
     */
    public static String getMonthString(Date date) {
        return DateUtil.date(date, DEFAULT_MONTH_FORMAT);
    }

    /**
     * 获取某日期的月份数字.
     *
     * @param date
     * @return 数字类型的月份
     */
    public static int getMonthInteger(Date date) {
        return Integer.parseInt(DateUtil.date(date, DEFAULT_MONTH_FORMAT));
    }

    /**
     * 获取某日期的几号数字
     *
     * @param date the date
     * @return 数字类型的几号
     */
    public static int getDayInteger(Date date) {
        return Integer.parseInt(DateUtil.date(date, DEFAULT_DAY_FORMAT));
    }

    /**
     * 取得当前月的的最后一天.
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfCurMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 取得当前月的的第一天.
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfCurMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }

    /**
     * 取得某天所在周的第一天.
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 取得某天所在周的最后一天.
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + SIX);
        return c.getTime();
    }

    /**
     * 验证日期是否有效，有效日期范围1900-1-1到2099-12-31.
     *
     * @param ds
     * @return
     */
    public static boolean isValidDate(String ds) {
        if (StringUtils.isBlank(ds)) {
            return false;
        }
        return PATTERN.matcher(ds).matches();
    }

    /**
     * 验证日期是否有效，有效日期范围1900-1-1到2099-12-31.
     *
     * @param d
     * @return
     */
    public static boolean isValidDate(Date d) {
        if (d == null) {
            return false;
        }
        return PATTERN.matcher(date(d, DEFAULT_DATE_FORMAT)).matches();
    }

    @SuppressWarnings("all")
    public static long getNowMilSeconds() {
        return new Date().getTime();
    }

    @SuppressWarnings("unchecked")
    public static Map milSecondsToHms(long mss) {
        // 秒 分钟 小时 天
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        Map res = new HashMap<String, Long>();
        res.put("days", days);
        res.put("hours", hours);
        res.put("minutes", minutes);
        res.put("seconds", seconds);
        return res;
    }

    public static Date stringToDate(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateForYyyyMMddHHmmss(String str) {
        return stringToDate(str, DEFAULT_DATEFULLTIME_FORMAT);
    }

    public static Date getPaHfActiveTime() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int date = ca.get(Calendar.DATE);
        ca.set(year, month, date, 20, 0, 0);
        if (ca.getTime().before(new Date())) {
            ca.set(year, month, date + 1, 20, 0, 0);
        }
        int weekDay = ca.get(Calendar.DAY_OF_WEEK);
        if (1 == weekDay) {
            ca.set(year, month, date + 1, 20, 0, 0);
        }
        if (7 == weekDay) {
            ca.set(year, month, date + 2, 20, 0, 0);
        }
        return ca.getTime();
    }

    public static Date getPaMobileActiveTime() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int date = ca.get(Calendar.DATE);
        ca.set(year, month, date, 20, 0, 0);
        ca.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        if (ca.getTime().before(new Date())) {
            ca.set(year, month, date + 6, 20, 0, 0);
        }
        return ca.getTime();
    }

    public static Date getPaBigActiveTime() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int date = ca.get(Calendar.DATE);
        ca.set(year, month, date, 20, 0, 0);
        if (ca.getTime().before(new Date())) {
            ca.set(year, month, date + 1, 20, 0, 0);
        }
        return ca.getTime();
    }

    /**
     * 获取今日凌晨数据
     * @return
     */
    public static String getDayStartDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TODAY_START_DATETIME_FORMAT);
        Calendar tt = Calendar.getInstance();
        return simpleDateFormat.format(tt.getTime());
    }

    /**
     * 获取凌晨日期
     * @param date
     * @return
     */
    public static Date getDayStartDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TODAY_START_DATETIME_FORMAT);
        String dateStr = simpleDateFormat.format(date.getTime());
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return null;
    }

    public static String getThisMonthFirstDayStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 0);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextMonthFirstDayStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getThisMonthFirstDayStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 0);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextMonthFirstDayStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 1);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getDateStrBetweenList(String startTimeStr, String endTimeStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        List<String> list = new ArrayList<String>(); //保存日期集合
        try {
            Date startTime = sdf.parse(startTimeStr);
            Date endTime = sdf.parse(endTimeStr);
            Date date = startTime;
            Calendar cd = Calendar.getInstance();
            while (date.getTime() <= endTime.getTime()){
                list.add(sdf.format(date));
                cd.setTime(date);
                cd.add(Calendar.DATE, 1);//增加一天 放入集合
                date = cd.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void main(String[] args) {
//        System.out.println(getQuot("2018-09-04 12:32:32", "2018-09-05 08:32:32", "yyyy-MM-dd"));
//
//        System.out.println(getDayStartDateStr(new Date()));
//        Date todayStart = getDayStartDate(new Date());
//        System.out.println(todayStart);
//        System.out.println(getDayStartDate(addDays(todayStart, 1)));

        // getPayTransactionByTimestamp();

        // System.out.println(getNowDateminStr());
//
//        System.out.println(getThisMonthFirstDayStr(new Date(), "yyyy-MM-dd"));
//        System.out.println(getNextMonthFirstDayStr(new Date(), "yyyy-MM-dd"));
//        System.out.println(getThisMonthFirstDayStr(new Date(), DateUtils.TODAY_START_DATETIME_FORMAT));
//        System.out.println(getThisMonthFirstDayStr("2019-12", "yyyy-MM"));
//        System.out.println(getNextMonthFirstDayStr("2019-12-13", "yyyy-MM-dd"));


        String endTime = "2013-02-01";
        Date date = getDateTime(endTime);
        System.out.println(getDetailDateTime(date));

        String startTime = "2020-02";
        startTime = startTime + "-01";
        endTime = DateUtils.getDateTime(DateUtils.getMaxDateByMonth(DateUtils.getDateTime(startTime)), DEFAULT_DATE_FORMAT);
        System.out.println("====endTime=" + endTime);
        // System.out.println(JSON.toJSONString(getDateStrBetweenList("2019-12-13", "2020-01-13", DateUtils.DEFAULT_DATE_FORMAT)));

    }

    /**
     * 获得指定日期的中文星期几
     *
     * @param date
     * @return
     */
    public static String getDayChs(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            default:
                return "周日";
        }
    }

    public static int getMonthDiff(Date startDate, Date endDate) {
        //开始时间
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        //结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        if (!start.after(end)) {
            int subMonthCount =
                    (end.get(Calendar.YEAR) - start.get(Calendar.YEAR) == 0)
                            ? end.get(Calendar.MONTH) - start.get(Calendar.MONTH)  //同一年
                            : ((end.get(Calendar.YEAR) - start.get(Calendar.YEAR) >= 2) //年数差超过2年
                            ? (end.get(Calendar.YEAR) - start.get(Calendar.YEAR) - 1)
                            * 12 + start.getActualMaximum(Calendar.MONTH) - start.get(Calendar.MONTH)
                            + end.get(Calendar.MONTH) + 1
                            : start.getActualMaximum(Calendar.MONTH) - start.get(Calendar.MONTH)
                            + end.get(Calendar.MONTH) + 1);  //年数差为1，Calendar.get(MONTH) 第一月是0，所以+1
            return subMonthCount;
        }
        return 0;
    }

    /**
     * 判断给定日期是否为月末的一天
     *
     * @param date the date
     * @return true:是|false:不是
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 判断给定日期是否为月初的一天
     *
     * @param date the date
     * @return true:是|false:不是
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 获取给定日期是星期几
     *
     * @param date the date
     * @return {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"}
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
