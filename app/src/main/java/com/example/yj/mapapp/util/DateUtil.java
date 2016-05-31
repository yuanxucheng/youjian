package com.example.yj.mapapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE0 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_DATE2 = new SimpleDateFormat(
			"HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_DATE3 = new SimpleDateFormat(
			"yyyy-MM");
	public static final SimpleDateFormat DATE_FORMAT_DATE4 = new SimpleDateFormat(
			"MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_DATE5 = new SimpleDateFormat(
			"MM月dd日");
	public static final SimpleDateFormat DATE_FORMAT_DATE6 = new SimpleDateFormat(
			"yyMMdd");
	public static final SimpleDateFormat DATE_FORMAT_DATE7 = new SimpleDateFormat(
			"MM-dd-HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_DATE8 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String FULL_DATE_PATTERN_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_DATE_PATTERN_1 = "yyyy年MM月dd日";
	public static final String DEFAULT_DATE_PATTERN_2 = "HH:mm";
	public static final String DEFAULT_DATE_PATTERN_3 = "MM月dd日";

	public static final String DEFAULT_DATE_PATTERN_4 = "MM-dd";

	public static final String DEFAULT_DATE_PATTERN_5 = "MM-dd HH:mm";

	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			DEFAULT_DATE_PATTERN_1);

	public static final int TIME_1 = 60;// 1分钟的秒数
	public static final int TIME_2 = 60 * 60;// 1小时的秒数
	public static final int TIME_3 = 24 * 60 * 60;// 1天的秒数

	public static final long ONE_DAY = 24 * 60 * 60 * 1000;//

	public static String getString(Date date) {
		return getString(date, DEFAULT_DATE_PATTERN);
	}

	public static String getString(Date date, String pattern) {
		String str = "";
		try {
			SimpleDateFormat sf = new SimpleDateFormat(pattern);
			str = sf.format(date);
		} catch (Exception e) {
			LogUtil.w(e);
		}
		return str;
	}

	public static Date getDate(String dateStr) {
		return getDate(dateStr, DEFAULT_DATE_PATTERN);
	}

	public static Date getDate(String dateStr, String pattern) {
		Date date = null;
		try {
			SimpleDateFormat sf = new SimpleDateFormat(pattern);
			date = sf.parse(dateStr);
		} catch (Exception e) {
			// LogUtil.w(e);
			// LogUtil.d("date trans error..");
		}
		return date;
	}

	public static String getCurrentTime(String pattern) {
		return getString(new Date(), pattern);
	}

	public static String getCurrentTime() {
		return getCurrentTime(DEFAULT_DATE_PATTERN);
	}

	public static Date getAfterDate(Date date, int dayCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime() + dayCount * 1000 * 60 * 60
				* 24);
		// * 24 * dayCount);
		return calendar.getTime();
	}

	/**
	 * 获取两日期相差的秒数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiff(Date startTime, Date endTime) {
		long diff = 0;
		diff = endTime.getTime() / 1000 - startTime.getTime() / 1000;
		LogUtil.w("startTime.getTime()===" + startTime.getTime()
				+ ",endTime.getTime()===" + endTime.getTime());
		// LogUtil.w("diff==="+diff);
		// diff = diff / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(diff));
	}

	/**
	 * 判断日期相差几个月
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static double getMounthDiff(String startTime, String endTime) {
		String[] beginDates = startTime.split("-");
		String[] endDates = endTime.split("-");

		double len = (Integer.valueOf(endDates[0]) - Integer
				.valueOf(beginDates[0]))
				* 12
				+ (Integer.valueOf(endDates[1]) - Integer
						.valueOf(beginDates[1]));
		int day = Integer.valueOf(endDates[2]) - Integer.valueOf(beginDates[2]);
		if (day > 0) {
			len += 0.5;
		} else if (day < 0) {
			len -= 0.5;
		}
		return len;
	}

	/**
	 * 把日期转为字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String ConverToString2(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);

		return df.format(date);
	}

	// 把日期转为字符串
	public static String ConverToString1(Date date) {
		DateFormat df = new SimpleDateFormat(DEFAULT_DATE_PATTERN_2);

		return df.format(date);
	}

	// 把日期转为字符串
	public static String ConverToString(Date date) {
		DateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);

		return df.format(date);
	}

	/**
	 * 把字符串转为日期
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date ConverToDate(String strDate) {
		DateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String stringConverToString(String strDate) {
		String str = "";
		Date d = ConverToDate(strDate);

		return ConverToString1(d);
	}

	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());// 获取当前时间 ;
	}

	public static Date getTomorrowDate() {
		return new Date(System.currentTimeMillis());// 获取明天时间 ;
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}
	/**
	 * 将秒数转化为分钟数 s 秒数
	 */
	public static int sToM(int s) {
		long l = s / 60;
		return Integer.parseInt(String.valueOf(l));// 获取当前时间 ;
	}

	/**
	 * 将分钟数转化为小时数 m 分钟数
	 */
	public static int MToH(int m) {
		long l = m / 60;
		return Integer.parseInt(String.valueOf(l));// 获取当前时间 ;
	}

	/**
	 * 将小时数转化为天数 h 分钟数
	 */
	public static int HToD(int h) {
		long l = h / 24;
		return Integer.parseInt(String.valueOf(l));// 获取当前时间 ;
	}

	/**
	 * 将秒数转化为小时数 s 分钟数
	 */
	public static int sToH(int s) {
		int m = sToM(s);
		return MToH(m);
	}

	/**
	 * 获取明天的日期字符串
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getAfterTime(String pattern) {
		return new SimpleDateFormat(pattern, Locale.CHINA).format(getAfterDate(
				new Date(), 1));
	}

	public static String getTime(String pattern) {
		return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date());
	}

	public static String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	/**
	 * 判断两个日期的前后
	 * 
	 * @param time1
	 * @param time2
	 * @param type
	 * @return time1在time2后面返回true 否则返回false
	 */
	public static boolean compareData(String time1, String time2,
			SimpleDateFormat type) {

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = type.parse(time1);
			date2 = type.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeStemp1 = date1.getTime();

		long timeStemp2 = date2.getTime();
		if (timeStemp1 > timeStemp2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验生日是否大于当前时间
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isAfterToday(Date date) {
		if (date.getTime() > new Date().getTime())
			return false;
		else
			return true;
	}

	/**
	 * 校验生日是否小于等于当前时间 判断精度为yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeToday(Date date) {
		if (DateUtil.getDate(DateUtil.getString(date)).getTime() <= DateUtil
				.getDate(DateUtil.getString(new Date())).getTime())
			return true;
		else
			return false;
	}

	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            时间戳
	 * @return 毫秒数
	 */
	public static long getStandardTime(String timeStr) {

		StringBuffer sb = new StringBuffer();

		long t = Long.parseLong(timeStr);
		return t;
	}

	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            时间戳
	 * @return 分钟数
	 */
	public static long getStandardMinute(String timeStr) {

		StringBuffer sb = new StringBuffer();

		long t = Long.parseLong(timeStr);
		long time = System.currentTimeMillis() - (t * 1000);
		long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
		return minute;
	}

	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            时间戳
	 * @return 秒数
	 */
	public static long getStandardMill(String timeStr) {

		StringBuffer sb = new StringBuffer();

		long t = Long.parseLong(timeStr);
		long time = System.currentTimeMillis() - (t * 1000);
		long mill = (long) Math.ceil(time / 1000);// 秒前
		return mill;
	}

	/**
	 * 根据long型的数据获取时间值
	 * 
	 * @param value
	 * @return
	 */
	public static String getNormalTime(long value) {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		// ;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(new Date(value * 1000));
		return time;
	}

	/**
	 * 根据long型的数据获取时间值
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public static String getNormalTime(long value, SimpleDateFormat type) {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		// ;
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;
		String time = type.format(new Date(value * 1000));
		return time;
	}

	/**
	 * 根据string型的数据获取时间值
	 * 
	 * @param time
	 * @return
	 */
	public static String getNormalTime(String time) {
		return getNormalTime(Long.parseLong(time));
	}

	/**
	 * 根据string型的数据获取时间值
	 * 
	 * @param time
	 * @param type
	 * @return
	 */
	public static String getNormalTime(String time, SimpleDateFormat type) {
		return getNormalTime(Long.parseLong(time), type);
	}
	
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转为日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}
}
