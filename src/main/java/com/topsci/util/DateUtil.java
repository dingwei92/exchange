package com.topsci.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * 
 * @author CM-TOPSCI
 * 
 */
public class DateUtil {

	public static Date getDate(String date,String format) throws Exception
	{
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		return format1.parse(date);
	}
	
	/**
	   * 格式化日期，将日期从yyyy-MM-dd HH:mm:ss转换为yyyyMMddHHmmss
	   * @param datetime String
	   * @return String
	   */
	  public static String formatDate6(String datetime) {
	    try {
	      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Date date1 = df.parse(datetime);
	      df = new SimpleDateFormat("yyyyMMddHHmmss");
	      datetime = df.format(date1);
	    }
	    catch (Exception ex) {
	      datetime = "";
	    }
	    return datetime;
	  }

	/**
	 * 计算从当前时间currentDate开始，满足条件hourOfDay,
	 * minuteOfHour, secondOfMinite的最近时间
	 * @return
	 */
	public static long getDelay(int hourOfDay, int minuteOfHour, int secondOfMinite) {
		//计算当前时间的WEEK_OF_YEAR,DAY_OF_WEEK, HOUR_OF_DAY, MINUTE,SECOND等各个字段值
		Calendar currentDate = Calendar.getInstance();
		long currentDateLong = currentDate.getTime().getTime();
		//计算满足条件的最近一次执行时间
//        Calendar earliestDate = test.getEarliestDate(currentDate, 3, 16, 38, 10);
		Calendar earliestDate = Calendar.getInstance();
		earliestDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
		earliestDate.set(Calendar.MINUTE,minuteOfHour);
		earliestDate.set(Calendar.SECOND,secondOfMinite);
		if(earliestDate.getTime().getTime()-currentDate.getTime().getTime() < 0)
		{
			earliestDate.add(Calendar.DAY_OF_MONTH,1);
		}
		long earliestDateLong = earliestDate.getTime().getTime();
		//计算从当前时间到最近一次执行时间的时间间隔
		long delay = earliestDateLong - currentDateLong;
		return delay;
	}
	  
	  /**
	   * 格式化日期，将日期从yyyy-MM-dd HH:mm转换为yyyyMMddHHmmss
	   * @param datetime String
	   * @return String
	   */
	  public static String formatDate7(String datetime) {
		    try {
		      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		      Date date1 = df.parse(datetime);
		      df = new SimpleDateFormat("yyyyMMddHHmmss");
		      datetime = df.format(date1);
		    }
		    catch (Exception ex) {
		      datetime = "";
		    }
		    return datetime;
		  }
	
	/**
	 * 获取给定时间相差value的前后时间
	 * @param date 给定时间 YyyyMMddhhmmss
	 * @param call call 相差时间单位 y-年，M-月，d-日 ,h-时，m-分，s-秒
	 * @param value 相差值
	 * @return 给定时间相差value的前后时间
	 */
	public  static String getCompareDate(String date,char call,int value)
	{
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(date.substring(8, 10));
		int minute = Integer.parseInt(date.substring(10, 12));
		int second = Integer.parseInt(date.substring(12, 14));
		Calendar cal = Calendar.getInstance();
	    cal.set(year,month-1,day,hour,minute,second);
	    switch (call) {
		case 'y':
			cal.add(Calendar.YEAR, value);
			break;
        case 'M':
        	cal.add(Calendar.MONTH, value);
			break;
        case 'd':
        	cal.add(Calendar.DAY_OF_MONTH, value);
			break;
        case 'h':
        	cal.add(Calendar.HOUR_OF_DAY, value);
			break;
        case 'm':
        	cal.add(Calendar.MINUTE, value);
			break;
        case 's':
        	cal.add(Calendar.SECOND, value);
			break;
		default:
			break;
		}
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = dateFormat.format(cal.getTime());
		return strDate;
	}
	
	  /**
	   * 获得当天时间，格式为yyyy-MM-dd HH:mm:ss
	   * @return String
	   */
	  public static String getNow() {
	    Date d = new Date();
	    String str = "";
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    str = formatter.format(d);
	    return str;
	  }
	  
	  /**
	   * 获得当天时间，格式为yyyyMMddHHmmss
	   * @return String
	   */
	  public static String getNow3() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    String strDate = dateFormat.format(new Date());
	    return strDate;
	  }
	
	/**
	 * 比较两个时间字符串，是否相差时间内
	 * 用dst时间+value和src对比
	 * @param src 源时间 YyyyMMddhhmmss
	 * @param dst 目的时间 YyyyMMddhhmmss
	 * @param call 相差时间单位 y-年，M-月，d-日 ,h-时，m-分，s-秒
	 * @param value 相差时间值
	 * @return  0-相差value，小于0-小于相差值，大于0-大于相差值
	 */
	public static int compareTo_Date(String src,String dst,char call,int value)
	{
		int year = Integer.parseInt(dst.substring(0, 4));
		int month = Integer.parseInt(dst.substring(4, 6));
		int day = Integer.parseInt(dst.substring(6, 8));
		int hour = Integer.parseInt(dst.substring(8, 10));
		int minute = Integer.parseInt(dst.substring(10, 12));
		int second = Integer.parseInt(dst.substring(12, 14));
		Calendar cal = Calendar.getInstance();
	    cal.set(year,month-1,day,hour,minute,second);
		switch (call) {
		case 'y':
			cal.add(Calendar.YEAR, value);
			break;
        case 'M':
        	cal.add(Calendar.MONTH, value);
			break;
        case 'd':
        	cal.add(Calendar.DAY_OF_MONTH, value);
			break;
        case 'h':
        	cal.add(Calendar.HOUR_OF_DAY, value);
			break;
        case 'm':
        	cal.add(Calendar.MINUTE, value);
			break;
        case 's':
        	cal.add(Calendar.SECOND, value);
			break;
		default:
			break;
		}
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		 String strDate = dateFormat.format(cal.getTime());
		 return src.compareTo(strDate);
	}

	/**
	 * 判断系统当前时间(小时)是否在指定时间(小时)范围内
	 * 
	 * @param beginHour
	 *            开始小时
	 * @param endHour
	 *            结束小时
	 * @return 
	 *         如beginHour=01，系统当前时间为14点整,endHour=16;则beginHour<=14<=endHour,返回true
	 */
	public static boolean isHourTime(int beginHour, int endHour) {
		boolean flag = false;

		int begin = beginHour;
		int end = endHour;
		if (begin < 0 || begin > 23) {
			begin = 0;
		}
		if (end < 0 || end > 23) {
			end = 23;
		}
		if (begin > end) {
			end = begin;
		}
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		// 判断当前时间是否在配置的范围内
		if (begin <= end && begin <= hour && hour <= end) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 静态方法,获得某一日期的前n天的日期
	 * 
	 * @param date
	 *            既定日期,以此日期为基准进行查询.格式为"yyyy-MM-dd"
	 * @param n
	 *            在给定日期上加或减的天数.当n为正值时,将获得将来第n天的日期, 当n为负值时,将获得过去n天前的日期
	 * @return String字符串,格式为"yyyyMMdd"
	 */
	public static String getPreviousDate(String date, int n) {
		String str = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter.parse(date);
			Calendar tempCal = formatter.getCalendar();
			tempCal.add(Calendar.DATE, n);
			str = formatter.format(tempCal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	  /**
	   * 格式化日期，将日期从yyyy-MM-dd转换为yyyyMMdd
	   * @param datetime String
	   * @return String
	   */
	  public static String formatDate4(String datetime) {
	    try {
	      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	      Date date1 = df.parse(datetime);
	      df = new SimpleDateFormat("yyyyMMdd");
	      datetime = df.format(date1);
	    }
	    catch (Exception ex) {
	      datetime = "";
	    }
	    return datetime;
	  }
	
	/**
	 * 获得当前日期的前几天或者后几天
	 * 
	 * @param n
	 *            当n为正数的时候则是当前时间加n天，反之是当前时间减n天
	 * @return 返回前一天日期的字符串变量，如"2003-07-11"
	 */
	public static String getlastDate(int n) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, n);
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		str = formatter.format(cal.getTime());
		return str;
	}

	/**
	 * 格式化日期，将日期从yyyyMMdd转换为yyyy-MM-dd
	 * 
	 * @param datetime
	 *            String
	 * @return String
	 */
	public static String formatDate5(String datetime) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date date1 = df.parse(datetime);
			df = new SimpleDateFormat("yyyy-MM-dd");
			datetime = df.format(date1);
		} catch (Exception ex) {
			datetime = "";
		}
		return datetime;
	}
	
	  /**
	   * 格式化日期，将日期从yyyy-MM-dd HH:mm转换为yyyyMMddHHmm
	   * @param datetime String
	   * @return String
	   */
	  public static String formatDate_6(String datetime) {
	    try {
	      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      Date date1 = df.parse(datetime);
	      df = new SimpleDateFormat("yyyyMMddHHmm");
	      datetime = df.format(date1);
	    }
	    catch (Exception ex) {
	      datetime = "";
	    }
	    return datetime;
	  }
	  
	  /**
	   * 获得当天日期，格式为yyyyMMdd
	   * @return String
	   */
	  public static String getNow4() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    String strDate = dateFormat.format(new Date());
	    return strDate;
	  }
	  
	  /**
	   * 格式化日期，将日期从yyyyMMddHHmm转换为yyyy-MM-dd HH:mm
	   * @param datetime String
	   * @return String
	   */
	  public static String formatDate_3(String datetime) {
	    try {
	      SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	      Date date1 = df.parse(datetime);
	      df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      datetime = df.format(date1);
	    }
	    catch (Exception ex) {
	      datetime = "";
	    }
	    return datetime;
	  }

	/**
	 * 格式化日期，将日期从yyyyMMddHHmmss转换为yyyy-MM-dd HH:mm:ss 如果datetime为空 则返回null
	 * 
	 * @param datetime
	 *            String
	 * @return String
	 */
	public static String formatDate3(String datetime) {
		try {
			if (datetime == null || datetime.length() <= 0) {
				return null;
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date1 = df.parse(datetime);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			datetime = df.format(date1);
		} catch (Exception ex) {
			datetime = "";
		}
		return datetime;
	}

	/**
	 * 获得当天日期，格式为yyyy-MM-dd
	 * 
	 * @return String
	 */
	public static String getNow2() {
		Date d = new Date();
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		str = formatter.format(d);
		return str;
	}
}
