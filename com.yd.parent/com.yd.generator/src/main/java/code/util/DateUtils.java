package code.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	
	public static final String	ymdhms1	= "yyyy-MM-dd HH:mm:ss";
	public static final String	ymd		= "yyyy-MM-dd";
	public static final String	mydhms2	= "yyyyMMddHHmmss";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "2012-07-10 10:00:00";
		System.out.println(getStringHourDate(str));
	}
	
	/**
	 * 基本样例  没实际意义
	 * @param source
	 * @return
	 */
	public static long getTimeInMillis(String source) {
		DateFormat format1 = new SimpleDateFormat(ymd);
		DateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		Date date = null;
		String str = null;
		
		// String转Date
		str = "2007-1-18";
		try {
			date = format1.parse(str);
			// data = format2.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Date转String
		date = new Date();
		str = format1.format(date);
		str = format2.format(date);
		return 0;
		
	}
	
	/**
	 * 返回年月日的日期字符串  例：2001-11-03
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat(ymd, Locale.SIMPLIFIED_CHINESE);
		return sd.format(date);
	}
	
	/**
	 * 截取日期   2001-12-22 11:11:33  ==>> 2001-12-22
	 * @param dataStr
	 * @return
	 */
	public static String getDateForString(String dataStr) {
		Date date = stringToDate(dataStr);
		SimpleDateFormat sd = new SimpleDateFormat(ymd, Locale.SIMPLIFIED_CHINESE);
		return sd.format(date);
	}
	
	/**
	 * 获取时间字符串中的时分秒     2001-12-22 11:11:33
	 * @param str
	 * @return 11:11:33
	 */
	public static String getStringHourDate(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		}
		String[] args = str.split(" ");
		return args[1];
	}
	
	/**
	 * 转换日期格式:返回只值为如下形式 2002-10-30 20:24:39
	 * 
	 * @return String
	 */
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(ymdhms1);
		String ctime = formatter.format(time);
		return ctime;
	}
	
	/**
	 * 转换日期格式:返回只值为如下形式 20021030202439
	 * @param time
	 * @return
	 */
	public static String dateToTime(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(mydhms2);
		String ctime = formatter.format(time);
		return ctime;
	}
	
	/**
	 * 时间戳转换成日期格式字符串  yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String chageTime(long date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ymdhms1);
		String str_date = dateFormat.format(new Date(date * 1000));
		return str_date;
	}
	
	public static long chageTime(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(ymdhms1);
		try {
			return dateFormat.parse(dateStr).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 将字符串yyyy-MM-dd HH:mm:ss 转换日期
	 * @param time
	 * @return
	 */
	public static Date stringToDate(String time) {
		return stringToDate(time, ymdhms1);
	}
	
	/**
	 * 将字符串yyyy-MM-dd HH:mm:ss 转换日期
	 * @param time
	 * @return
	 */
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
	
}
