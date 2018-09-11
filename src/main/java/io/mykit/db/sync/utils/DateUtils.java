package io.mykit.db.sync.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 自定义时间日期工具
 * 
 * @author liuyazhuang
 * 
 */
public class DateUtils {
	
	/**
	 * 周一的日期
	 */
	public static final String MONDAY_DATE = "monday_date";
	/**
	 * 周日的日期
	 */
	public static final String SUNDAY_DATE = "sunday_date";
	
	public static final String MONTH_FIRST = "month_first";
	
	public static final String MONTH_LAST = "month_last";

	public static final String MONTH_SINGLE = "MM";
	public static final String DATE_SINGLE = "dd";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_NO_FORMAT = "yyyyMMddHHmmss";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final String MONTH_NO_FORMAT = "yyyyMM";
	public static final String YEAR_FORMAT = "yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String MINUTE_FORMAT = "HH:mm";
	public static final String BIRTHDAY_FORMAT = "yyyy年MM月dd日";
	public static final String DATE_DATE = "yyyyMMdd";
	public static final String LOCAL_ENGLISH_FORMAT = "dd/MMM/yyyy:hh:mm:ss Z";
	// MySQL数据库Date类型的字段必须赋值一个大于1970年1月1日的日期值。否则对此字段进行where条件查询或order
	// by等操作时就会报错。
	public static final int MYSQL_DATE_YEAR_MAXVALUE = 1970;

	public enum TimeUnit {
		Year, Month, Week, Day, Hour, Minute, Second
	}

	public enum CouponUnit {
		Year, Month, Week, Day, Hour, Minute, Second
	}

	// 计算两个日期相差年数
	public static int yearDateDiff(Date startDate, Date endDate) {
		Calendar calBegin = Calendar.getInstance(); // 获取日历实例
		Calendar calEnd = Calendar.getInstance();
		calBegin.setTime(startDate); // 字符串按照指定格式转化为日期
		calEnd.setTime(endDate);
		return calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);
	}
	
	
	/**
	 * 将某一格式的时间转化为另一种格式
	 * @param timeLocal
	 * @param localFormatStr
	 * @param formatStr
	 * @return
	 */
	public static String parse(String timeLocal, String localFormatStr, String formatStr){
		Date date = new Date();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(localFormatStr, Locale.ENGLISH);
			date = formatter.parse(timeLocal);
		} catch (Exception e) {
			date = new Date();
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String dateStr = format.format(date);
		return dateStr;
	}

	/**
	 * 获取与现在相隔param的时间戳，精确到毫秒
	 * 
	 * @param param
	 * @param couponUnit
	 * @return
	 */
	public static long getTimeStampFromNow(Integer param, CouponUnit couponUnit) {
		long timeStamp = 0;
		switch (couponUnit) {
		case Year:
			timeStamp = param * 365 * 24 * 60 * 60 * 1000;
			break;
		case Month:
			timeStamp = param * 30 * 24 * 60 * 60 * 1000;
			break;
		case Week:
			timeStamp = param * 7 * 24 * 60 * 60 * 1000;
			break;
		case Day:
			timeStamp = param * 24 * 60 * 60 * 1000;
			break;
		case Hour:
			timeStamp = param * 60 * 60 * 1000;
			break;
		case Minute:
			timeStamp = param * 60 * 1000;
			break;
		case Second:
			timeStamp = param * 1000;
			break;
		default:
			break;
		}
		return timeStamp;
	}

	public static Map<String, String>  convertWeekByDate(Date time) {
		 Map<String, String> map = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		//System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = parseDateToString(cal.getTime(), DATE_FORMAT);
		map.put(MONDAY_DATE, imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = parseDateToString(cal.getTime(), DATE_FORMAT);
		map.put(SUNDAY_DATE, imptimeEnd);
		return map;

	}
	
	public static String getLastDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);     
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
       return parseDateToString(cal.getTime(), DATE_FORMAT);  
    }   
	
    public static String getFirstDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);  
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
       return parseDateToString(cal.getTime(), DATE_FORMAT);  
    }
    
    
    /**
     * 获取指定年份的第一天和最后一天
     * @param year
     * @param month
     * @return
     */
    public static Map<String, String> getFirstAndLastDayFromMonth(int year, int month){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(MONTH_FIRST, getFirstDayOfMonth(year, month));
    	map.put(MONTH_LAST, getLastDayOfMonth(year, month));
    	return map;
    	
    }
    
	public static void main(String[] args) {
		System.out.println(getDelayTime(parseDateToString(new Date(), DATE_FORMAT), Calendar.DATE, -1, DATE_FORMAT));
	}

	/**
	 * 时间单位设置工具
	 * 
	 * @param date
	 * @param value
	 * @param couponUnit
	 * @return
	 */
	public static Date delayTime(Date date, Integer value, CouponUnit couponUnit) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (couponUnit) {
		case Year:
			calendar.add(Calendar.YEAR, value);
			break;
		case Month:
			calendar.add(Calendar.MONTH, value);
			break;
		case Week:
			calendar.add(Calendar.DAY_OF_WEEK, value);
			break;
		case Day:
			calendar.add(Calendar.DATE, value);
			break;
		case Hour:
			calendar.add(Calendar.HOUR, value);
			break;
		case Minute:
			calendar.add(Calendar.MINUTE, value);
			break;
		case Second:
			calendar.add(Calendar.SECOND, value);
			break;
		default:
			break;
		}
		return calendar.getTime();
	}


	/**
	 * 取得当月天数
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 得到指定月的天数
	 */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 时间单位设置工具
	 * 
	 * @param date
	 * @param value
	 * @param couponUnit
	 * @return
	 */
	public static Date delayTime(Date date, BigDecimal value, CouponUnit couponUnit) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (couponUnit) {
		case Year:
			calendar.add(Calendar.YEAR, value.intValue());
			break;
		case Month:
			calendar.add(Calendar.MONTH, value.intValue());
			break;
		case Week:
			calendar.add(Calendar.DAY_OF_WEEK, value.intValue());
			break;
		case Day:
			calendar.add(Calendar.DATE, value.intValue());
			break;
		case Hour:
			calendar.add(Calendar.HOUR, value.intValue());
			break;
		case Minute:
			calendar.add(Calendar.MINUTE, value.intValue());
			break;
		case Second:
			calendar.add(Calendar.SECOND, value.intValue());
			break;
		default:
			break;
		}
		return calendar.getTime();
	}

	/**
	 * 方向
	 * 
	 * @author liuyazhuang
	 *
	 */
	public enum Direction {
		Previous, After
	}

	/**
	 * 将指定格式的时间提前或延迟指定的时间端
	 * 
	 * @param sourceTime：原时间
	 * @param format：原时间格式
	 * @param timeLen：提前或延迟的时间段
	 * @param lenTimeUnit：提前或延迟的时间格式
	 * @param direction：标志是提前还是延后
	 * @return 返回format所代表的时间格式字符串
	 */
	public static String changeTime(String sourceTime, String format, Double timeLen, TimeUnit lenTimeUnit,
			Direction direction) {
		long source = parseStringDateToDate(sourceTime, format).getTime();
		long targetLen = getTimeLen(timeLen, lenTimeUnit);
		switch (direction) {
		case Previous:
			source -= targetLen;
			break;
		case After:
			source += targetLen;
			break;
		default:
			break;
		}
		return parseDateToString(new Date(source), format);
	}

	/**
	 * 获取距离当前时间days天的时间
	 * 
	 * @param direction：方向
	 * @param days：距离的天数
	 * @return
	 */
	public static Date getDate(Direction direction, int days) {
		Calendar calendar = Calendar.getInstance();
		switch (direction) {
		case After:
			calendar.add(Calendar.DAY_OF_MONTH, days);
			break;
		case Previous:
			calendar.add(Calendar.DAY_OF_MONTH, -days);
			break;

		default:
			break;
		}
		return calendar.getTime();
	}

	/**
	 * 获取时间段的毫秒数
	 * 
	 * @param timeLen
	 * @param lenTimeUnit
	 * @return
	 */
	private static long getTimeLen(Double timeLen, TimeUnit lenTimeUnit) {
		long time = 0;
		switch (lenTimeUnit) {
		case Week:
			time = (long) (timeLen * 7 * 24 * 3600 * 1000);
			break;
		case Day:
			time = (long) (timeLen * 24 * 3600 * 1000);
			break;
		case Hour:
			time = (long) (timeLen * 3600 * 1000);
			break;
		case Minute:
			time = (long) (timeLen * 60 * 1000);
			break;
		case Second:
			time = (long) (timeLen * 1000);
			break;
		default:
			break;
		}
		return time;
	}

	/**
	 * 比较前面的日期比后面的日期大
	 * 
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static boolean compareDate(Date firstDate, Date secondDate) {
		return firstDate.getTime() >= secondDate.getTime();
	}

	public static String parseDateToString(Date date, String formatString) {
		return getSimpleDateFormat(formatString).format(date);
	}

	public static SimpleDateFormat getSimpleDateFormat(String formatString) {
		return new SimpleDateFormat(formatString);
	}

	/**
	 * 时间单位设置工具
	 * 
	 * @param date
	 * @param value
	 * @param couponUnit
	 * @return
	 */
	public static Date delayTime(Date date, BigDecimal value, TimeUnit couponUnit) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (couponUnit) {
		case Year:
			calendar.add(Calendar.YEAR, value.intValue());
			break;
		case Month:
			calendar.add(Calendar.MONTH, value.intValue());
			break;
		case Week:
			calendar.add(Calendar.DAY_OF_WEEK, value.intValue());
			break;
		case Day:
			calendar.add(Calendar.DATE, value.intValue());
			break;
		case Hour:
			calendar.add(Calendar.HOUR, value.intValue());
			break;
		case Minute:
			calendar.add(Calendar.MINUTE, value.intValue());
			break;
		case Second:
			calendar.add(Calendar.SECOND, value.intValue());
			break;
		default:
			break;
		}
		return calendar.getTime();
	}

	/**
	 * 获取星期在某一段时间内的日期
	 * 
	 * @param startDate:格式为yyyy-MM-dd
	 * @param endDate格式为yyyy-MM-dd
	 * @param week:1到7代表星期一到星期日
	 * @return
	 * @throws Exception
	 */
	public static List<Date> getDaysBetween(Date startDate, Date endDate, int week) throws Exception {
		List<Date> dates = new ArrayList<Date>();
		// 获取开始日期的星期
		int weekDay = getWeekByDate(startDate);
		// 获取输入的星期与开始时间的星期相隔的天数
		int days = (week - weekDay) < 0 ? (week - weekDay) + 7 : week - weekDay;
		// 获取输入的星期所在日期在时间段内的以一个日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, days);
		while (calendar.getTime().getTime() <= endDate.getTime()) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
		}
		return dates;
	}

	/**
	 * 获取星期在某一段时间内的日期
	 * 
	 * @param start：格式为yyyy-MM-dd
	 * @param end:格式为yyyy-MM-dd
	 * @param week:数字1到7代表星期一到星期日
	 * @return
	 * @throws Exception
	 */
	public static List<Date> getDaysBetween(String start, String end, int week) throws Exception {
		Date startDate = parseStringDateToDate(start, DATE_FORMAT);
		Date endDate = parseStringDateToDate(end, DATE_FORMAT);
		List<Date> dates = new ArrayList<Date>();
		// 获取开始日期的星期
		int weekDay = getWeekByDate(startDate);
		// 获取输入的星期与开始时间的星期相隔的天数
		int days = (week - weekDay) < 0 ? (week - weekDay) + 7 : week - weekDay;
		// 获取输入的星期所在日期在时间段内的以一个日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, days);
		while (calendar.getTime().getTime() <= endDate.getTime()) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
		}
		return dates;
	}

	/**
	 * 获取延迟的时间
	 * @param time 传入的日期
	 * @param field 
	 * @param amount
	 * @param format 
	 * @return
	 */
	public static String getDelayTime(String time, int field, int amount, String format) {
		Date date = DateUtils.parseStringDateToDate(time, format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		date = calendar.getTime();
		return DateUtils.parseDateToString(date, format);
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = getDateFormat(DATE_FORMAT);
		Date smdate = sdf.parse(startDate);
		Date bdate = sdf.parse(endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 将Date转换为Calendar
	 * 
	 * @param date
	 *            Date date
	 * @return {@link Calendar}
	 * @author jsZhu
	 */
	public static Calendar parseDateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * 将Date 格式为yyyy-MM-dd 形式转换为 Integer
	 * 
	 * @param date
	 *            Convert Date Object
	 * @return Covert Date {@link Integer}
	 * @author jsZhu
	 */
	public static Integer parseDateToInteger(Date date) {
		Calendar calendar = parseDateToCalendar(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(((calendar.get(Calendar.MONTH)) + 1));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(calendar.get(Calendar.HOUR));
		String minute = String.valueOf((calendar.get(Calendar.MINUTE)));
		return Integer.parseInt(year + month + day + hour + minute);
	}

	/**
	 * 两个Date日期比较是否相当
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 * @author jsZhu
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * 两个装有日期 Calenday比较是否相等
	 * 
	 * @param cal1
	 * @param cal2
	 * @return boolean
	 * @author jsZhu
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * 将Date字符串转换为Calendar
	 * 
	 * @param date
	 *            DateString
	 * @return Calendar
	 * @author jsZhu
	 */
	public static Calendar parseStringDateToCalendar(String date) {
		Calendar calendar = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
		try {
			Date daystart = df.parse(date);
			calendar.setTime(daystart);
			return calendar;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取在线北京时间
	 * 
	 * @return Date
	 * @author jsZhu
	 */
	public static Date getDateOnline() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); // 时区设置
		java.net.URLConnection uc;
		try {
			URL url = new URL("http://www.bjtime.cn");
			uc = url.openConnection();
			uc.connect();
			long ld = uc.getDate();
			Date date = new Date(ld);
			return date;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 将日期类型转化为字符串
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String parseLongDateToStringDate(Date date, String formatString) {
		SimpleDateFormat format = getDateFormat(formatString);
		return format.format(date);
	}

	/**
	 * 获取SimpleDateFormat
	 * 
	 * @param formatString
	 * @return
	 */
	private static SimpleDateFormat getDateFormat(String formatString) {
		return new SimpleDateFormat(formatString);
	}

	/**
	 * 将字符串转化为日期类型
	 * 
	 * @param formatString
	 * @return
	 */
	public static Date parseStringDateToDate(String date, String formatString) {
		try {
			return getDateFormat(formatString).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取任意月份第一天或者最后一天
	 * 
	 * @param date
	 *            ：当前月的Date对象
	 * @param currentDate
	 *            ：枚举
	 * @param formatString
	 *            ：格式化字符串
	 * @return
	 */
	public static String getStringDay(Date date, CurrentDate currentDate, String formatString) {
		SimpleDateFormat df = getDateFormat(formatString);
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(date);
		switch (currentDate) {
		case FIRST:
			gcLast.add(Calendar.MONTH, 0);
			gcLast.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			break;
		case LAST:
			gcLast.set(Calendar.DAY_OF_MONTH, gcLast.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		default:
			break;
		}
		return df.format(gcLast.getTime());
	}

	/**
	 * 获取任意月份第一天或者最后一天
	 * 
	 * @param date
	 *            ：当前月的Date对象
	 * @param currentDate
	 *            ：枚举
	 * @param formatString
	 *            ：格式化字符串
	 * @return
	 */
	public static Date getDateDay(Date date, CurrentDate currentDate, String formatString) {
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(date);
		switch (currentDate) {
		case FIRST:
			gcLast.add(Calendar.MONTH, 0);
			gcLast.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			break;
		case LAST:
			gcLast.set(Calendar.DAY_OF_MONTH, gcLast.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		default:
			break;
		}
		return gcLast.getTime();
	}

	/**
	 * 日期枚举
	 * 
	 * @author liuyazhuang
	 * 
	 */
	public static enum CurrentDate {
		FIRST, LAST
	}

	/**
	 * 格式化类别枚举
	 * 
	 * @author liuyazhuang
	 * 
	 */
	public static enum FormatType {
		DATE_FORMAT, DATE_TIME_FORMAT, DATE_MINUTE_FORMAT, MONTH_FORMAT, TIME_FORMAT
	}

	/**
	 * 根据日期取得星期
	 * 
	 * @param dateStr
	 * @param formatType
	 * @return
	 */
	public static String parseStringDateToStringWeek(String dateStr, FormatType formatType) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat sdfInput = null;
		switch (formatType) {
		case DATE_FORMAT:
			sdfInput = getDateFormat(DATE_FORMAT);
			break;
		case DATE_TIME_FORMAT:
			sdfInput = getDateFormat(DATE_TIME_FORMAT);
			break;
		case DATE_MINUTE_FORMAT:
			sdfInput = getDateFormat(DATE_MINUTE_FORMAT);
			break;
		case MONTH_FORMAT:
			sdfInput = getDateFormat(MONTH_FORMAT);
			break;
		case TIME_FORMAT:
			sdfInput = getDateFormat(TIME_FORMAT);
			break;
		default:
			break;
		}
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	/**
	 * 更加当前日期的星期（数字）
	 * 
	 * @param dateStr
	 * @param formatType
	 * @return
	 */
	public static int getWeekByDate(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayOfWeek;
	}

	/**
	 * 取得当前时间
	 * 
	 * @return
	 */
	public static String getTodayTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		return today;
	}

	// 获取现在的时间
	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		String today = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
		return today;
	}

	public static String getTodayTimes() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dFormat.format(new Date()).toString();
	}

	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return today;
	}

	public static Long getAppointTime(String times) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return df.parse(getToday() + " " + times).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static long getLongTypetime(String times) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return df.parse(times).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	// 获取指定时间的时间戳(精确到秒)
	public static long getMillis(String times) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(times).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public static String getDateTimeStr() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			return df.format(new Date());
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDate() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
		return dFormat.format(new Date()).toString();
	}

	public static long getCacheEffectMillisTime() {
		return getTomorrowZeroPointMillis() - System.currentTimeMillis();
	}

	public static long getTomorrowZeroPointMillis() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		long millis = 0;
		try {
			millis = dFormat.parse(getTomorrow() + " 00:00:00").getTime();
		} catch (ParseException e) {
			// 如果出现异常，则默认保存24小时
			return System.currentTimeMillis() + 1000 * 60 * 60 * 23;
		}
		return millis;
	}

	public static String getTomorrow() {
		long myTime = System.currentTimeMillis() + 60 * 60 * 24 * 1000;
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dFormat.format(new Date(myTime)).toString();
	}

	public static String getSpecifiedDayBefore(int agoday) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(getToday());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - agoday);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	public static Date getSpecifiedDayBeforeFormatDate(int agoday, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - agoday);
		return c.getTime();
	}

	public static String getSpecifiedDayAfter(int agoday) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(getToday());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + agoday);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	public static Date getSpecifiedDayAfterFormatDate(int agoday, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + agoday);
		return c.getTime();
	}

	public static Long getAge(Date date) {
		Date endDate = new Date();
		long day = (endDate.getTime() - date.getTime()) / 1000;
		return day / (60 * 60 * 24 * 365);
	}

	/**
	 * 获得当前月的最后一天的最大天数
	 */
	public static int getMaxDayByYearMonth(int year, int month) {
		int maxDay = 0;
		int day = 1;
		/**
		 * Calendar 的 getInstance 方法返回一 个 Calendar 对象，其日历字段已由当前日期和时间初始化：
		 */
		Calendar calendar = Calendar.getInstance();
		/**
		 * 实例化日历各个字段,这里的day为实例化使用
		 */
		calendar.set(year, month - 1, day);
		/**
		 * Calendar.Date:表示一个月中的某天 calendar.getActualMaximum(int
		 * field):返回指定日历字段可能拥有的最大值
		 */
		maxDay = calendar.getActualMaximum(Calendar.DATE);
		return maxDay;
	}

	/**
	 * 得到得到日期的前一天
	 */
	public static Date getbeforDay(Date dateSign) {
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dateSign);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime();
		return dBefore;
	}

	/**
	 * 3天后失效
	 * 
	 * @param createTime
	 * @return
	 */
	public static Date getLoseEffectiveness(Date createTime) {
		Date threeLoseTime = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(createTime);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, +3); // 设置为3天后的时间
		threeLoseTime = calendar.getTime();
		return threeLoseTime;

	}

	/**
	 * 0:上午 1:下午
	 * 
	 * @return
	 */
	public static int isAmOrPm() {
		GregorianCalendar ca = new GregorianCalendar();
		return ca.get(GregorianCalendar.AM_PM);
	}
}
