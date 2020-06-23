package com.keith.common.utils;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUtil {
	 private static String timezone;

	  public static String getCurrentTimeZone()
	  {
	    if (timezone == null) {
	      System.out.println("Init local timezone once");
	      TimeZone tz = TimeZone.getDefault();
	      int offset = tz.getRawOffset();
	      timezone = "\\" + String.format("%s%02d%02d", new Object[] { offset >= 0 ? "+" : "-", Integer.valueOf(offset / 3600000), Integer.valueOf(offset / 60000 % 60) });
	    }
	    return timezone;
	  }

	  public static int dateSubtract(String date1, String date2)
	  {
	    if ((date1 == null) || ("".equals(date1)))
	      date1 = Date2Str(new Date());
	    if ((date2 == null) || ("".equals(date2)))
	      date2 = Date2Str(new Date());
	    long daterange = Str2Date(date1, "yyyy-MM-dd").getTime() - 
	      Str2Date(date2, "yyyy-MM-dd").getTime();
	    long time = 86400000L;
	    return (int)(daterange / time);
	  }

	  public static Date getLastDayOfMonth(Date sDate1)
	  {
	    Calendar cDay1 = Calendar.getInstance();
	    cDay1.setTime(sDate1);
	    int lastDay = cDay1.getActualMaximum(5);
	    Date lastDate = cDay1.getTime();
	    lastDate.setDate(lastDay);
	    return lastDate;
	  }

	  public static Date getFirstDayOfMonth(Date sDate1)
	  {
	    Calendar cDay1 = Calendar.getInstance();
	    cDay1.setTime(sDate1);
	    int firstDay = cDay1.getActualMinimum(5);
	    Date firstDate = cDay1.getTime();
	    firstDate.setDate(firstDay);
	    return firstDate;
	  }

	  public static String tansFormat(String date)
	  {
	    String rev = null;
	    SimpleDateFormat from = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", 
	      Locale.ENGLISH);
	    SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date dt = null;
	    try {
	      dt = from.parse(date);
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    rev = to.format(dt);
	    return rev;
	  }

	  public static Date getCurrentTime()
	  {
	    return Calendar.getInstance().getTime();
	  }

	  public static String getCurrentDate()
	  {
	    return Date2Str(Calendar.getInstance().getTime(), "yyyyMMdd");
	  }

	  public static Date getTimeInterval(int unit, int interval, Date oriTime)
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.setTime(oriTime);
	    ca.add(unit, interval);
	    return ca.getTime();
	  }

	  public static Date getYesterdayDate()
	  {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(5, -1);
	    return calendar.getTime();
	  }

	  public static Date Str2Date(String date)
	  {
	    try
	    {
	      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	      if (date.length() > 10) {
	        ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      }
	      Date d = ft.parse(date);

	      return new Date(d.getTime()); } catch (Exception ex) {
	    }
	    return new Date(Calendar.getInstance().getTime().getTime());
	  }

	  public static Date Str2Date_SO(String date)
	  {
	    try {
	      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	      if (date.length() > 10) {
	        ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      }
	      return ft.parse(date);
	    }
	    catch (Exception ex)
	    {
	    }
	    return new Date(Calendar.getInstance().getTime().getTime());
	  }

	  public static Date Str2Date(String date, String pattern)
	  {
	    try
	    {
	      SimpleDateFormat ft = new SimpleDateFormat(pattern);
	      return ft.parse(date);
	    } catch (Exception ex) {
	    }
	    return null;
	  }

	  public static Date Str2utilDate(String date, boolean all)
	  {
	    try
	    {
	      SimpleDateFormat ft = null;
	      if (all)
	        ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      else
	        ft = new SimpleDateFormat("yyyy-MM-dd");
	      return ft.parse(date);
	    } catch (Exception ex) {
	    }
	    return null;
	  }

	  public static Date StrutilDate(String date, String pattern)
	  {
	    try {
	      SimpleDateFormat ft = new SimpleDateFormat(pattern);
	      return ft.parse(date);
	    } catch (Exception ex) {
	    }
	    return null;
	  }

	  public static String Date2Str(Date date, String pattern)
	  {
	    if (date == null)
	      return "";
	    SimpleDateFormat ft = new SimpleDateFormat(pattern);
	    return ft.format(date);
	  }

	  public static String Date2Str(Date date) {
	    if (date == null)
	      date = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return ft.format(date);
	  }

	  public static String getYear(Date date) {
	    SimpleDateFormat ft = new SimpleDateFormat("yyyy");
	    return ft.format(date);
	  }

	  public static String getMonth(Date date) {
	    SimpleDateFormat ft = new SimpleDateFormat("MM");
	    return ft.format(date);
	  }

	  public static String getDay(Date date) {
	    SimpleDateFormat ft = new SimpleDateFormat("dd");
	    return ft.format(date);
	  }

	  public static String getHour(Date date) {
	    SimpleDateFormat ft = new SimpleDateFormat("HH");
	    return ft.format(date);
	  }

	  public static String getMinute(Date date) {
	    SimpleDateFormat ft = new SimpleDateFormat("mm");
	    return ft.format(date);
	  }

	  public static String getGoDay(int i, String date)
	  {
	    GregorianCalendar gc = new GregorianCalendar();
	    Date daytime = Str2Date(date, "yyyy-MM-dd");
	    if (daytime == null)
	      daytime = new Date();
	    gc.setTime(daytime);
	    gc.set(6, gc.get(6) + i);
	    return Date2Str(gc.getTime(), "yyyy-MM-dd");
	  }

	  public static Date getRangeHour(int i, Date date)
	  {
	    Calendar calendar = Calendar.getInstance();
	    if (date != null)
	      calendar.setTime(date);
	    calendar.add(11, i);
	    return calendar.getTime();
	  }

	  public static long getQuot(String time1, String time2)
	  {
	    long quot = 0L;
	    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	      Date date1 = ft.parse(time1);
	      Date date2 = ft.parse(time2);
	      quot = date1.getTime() - date2.getTime();
	      quot = quot / 1000L / 60L / 60L / 24L;
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return quot;
	  }

	  public static boolean getShiJianDuan(String date1)
	  {
	    boolean flag = false;
	    GregorianCalendar gc1 = new GregorianCalendar();
	    gc1.setTime(Str2Date(date1, "yyyy-MM-dd"));
	    GregorianCalendar gc2 = new GregorianCalendar();
	    gc2.setTime(new Date());
	    if (gc1.compareTo(gc2) <= 0) {
	      flag = true;
	    }
	    return flag;
	  }

	  public static String getJxsChuandanTime(String date)
	  {
	    if (Integer.parseInt(getHour(Str2Date(date))) >= 10) {
	      date = getGoDay(1, date);
	    }
	    return date.substring(0, 10);
	  }

	  public static String secToTime(int time)
	  {
	    String timeStr = null;
	    int hour = 0;
	    int minute = 0;
	    int second = 0;
	    if (time <= 0) {
	      return "00:00:00";
	    }
	    minute = time / 60;
	    if (minute < 60) {
	      second = time % 60;
	      timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
	    } else {
	      hour = minute / 60;
	      if (hour > 99)
	        return "99:59:59";
	      minute %= 60;
	      second = time - hour * 3600 - minute * 60;
	      timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
	    }

	    return timeStr;
	  }
	  public static String unitFormat(int i) {
	    String retStr = null;
	    if ((i >= 0) && (i < 10))
	      retStr = "0" + Integer.toString(i);
	    else
	      retStr = i+"";
	    return retStr;
	  }

	  public String myPercent(int y, int z)
	  {
	    String baifenbi = "";
	    double baiy = y * 1.0D;
	    double baiz = z * 1.0D;
	    double fen = baiy / baiz;
	    DecimalFormat df = new DecimalFormat("##.00%");
	    baifenbi = df.format(fen);
	    return baifenbi;
	  }

	  public static Date getTodayZero()
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.set(ca.get(1), ca.get(2), ca.get(5), 0, 0, 0);
	    return ca.getTime();
	  }

	  public static Date getYesterdayZero()
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.add(5, -1);
	    ca.set(ca.get(1), ca.get(2), ca.get(5), 0, 0, 0);
	    return ca.getTime();
	  }

	  public static long getYesterdayStamp()
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.add(5, -1);
	    ca.set(ca.get(1), ca.get(2), ca.get(5), 0, 0, 0);
	    return ca.getTimeInMillis() / 1000L;
	  }

	  public static long getTodayStamp()
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.set(ca.get(1), ca.get(2), ca.get(5), 0, 0, 0);
	    return ca.getTimeInMillis() / 1000L;
	  }

	  public static long getTomorrowStamp()
	  {
	    Calendar ca = Calendar.getInstance();
	    ca.add(5, 1);
	    ca.set(ca.get(1), ca.get(2), ca.get(5), 0, 0, 0);
	    return ca.getTimeInMillis() / 1000L;
	  }

	  public static String strToDateStrTimeZone(String date)
	  {
	    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
	    Date time = Str2utilDate(date, true);
	    return (ft.format(time) + getCurrentTimeZone()).replace("\\", "");
	  }

	  public static String formatDate(Date date, Object[] pattern)
	  {
	    String formatDate = null;
	    if ((pattern != null) && (pattern.length > 0))
	      formatDate = DateFormatUtils.format(date, pattern[0].toString());
	    else {
	      formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
	    }
	    return formatDate;
	  }

	  public static Date getDateStart(Date date) {
	    if (date == null) {
	      return null;
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	      date = sdf.parse(formatDate(date, new Object[] { "yyyy-MM-dd" }) + " 00:00:00");
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return date;
	  }

	  public static Date getDateEnd(Date date) {
	    if (date == null) {
	      return null;
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	      date = sdf.parse(formatDate(date, new Object[] { "yyyy-MM-dd" }) + " 23:59:59");
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return date;
	  }

	  public static long getBeforeNowDate(int step)
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	    String time = sdf.format(getCurrentTime());
	    long nowTime = Long.parseLong(time);
	    long offset = nowTime % step;
	    return nowTime - offset;
	  }

	  public static long getNextNowDate(int step)
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	    Calendar cal = Calendar.getInstance();
	    cal.add(12, 10);
	    String time = sdf.format(cal.getTime());
	    long nowTime = Long.parseLong(time);
	    long offset = nowTime % step;
	    return nowTime - offset;
	  }

	  public static Date getNextRateTime(int step)
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	    Calendar cal = Calendar.getInstance();
	    cal.add(12, 10);
	    String time = sdf.format(cal.getTime());
	    long nowTime = Long.parseLong(time);
	    long offset = nowTime % step;
	    cal.add(12, -(int)offset);
	    cal.set(13, 0);
	    cal.set(14, 0);
	    return cal.getTime();
	  }
	  public static void main(String[] args) {
	    Calendar c = Calendar.getInstance();
	    c.set(2, 4);
	    c.set(5, 19);
	    System.out.println(Date2Str(c.getTime()));
	    Calendar cc = Calendar.getInstance();
	    System.out.println((c.getTimeInMillis() - cc.getTimeInMillis()) / 1000L / 60L / 60L / 24L);
	  }

	  public static long getTimeLong()
	  {
	    return System.currentTimeMillis();
	  }
	  
	  /**    
	     * 得到几天前的时间    
	      *     
	      * @param d    
	      * @param day    
	      * @return    
	      */     
	     public static Date getDateBefore(int day) {      
	         Calendar now = Calendar.getInstance();      
	         now.setTime(new Date());      
	         now.set(Calendar.DATE, now.get(Calendar.DATE) - day);      
	         return now.getTime();      
	     }     
	  /**    
	      * 得到几天后的时间    
	      *     
	      * @param d    
	      * @param day    
	      * @return    
	      */     
	     public static Date getDateAfter(int day) {      
	        Calendar now = Calendar.getInstance();      
	         now.setTime(new Date());      
	        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);      
	         return now.getTime();      
	     }    
	  
	  

	  public static Date getNextDays(int days)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.add(5, days);
	    cal.set(11, 0);
	    cal.set(12, 0);
	    cal.set(13, 0);
	    cal.set(14, 0);
	    return cal.getTime();
	  }

	  public static Date getNextDays(int days, int houer)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.add(5, days);
	    cal.set(11, houer);
	    cal.set(12, 0);
	    cal.set(13, 0);
	    cal.set(14, 0);
	    return cal.getTime();
	  }
	  public static String getYyyyMMddHHmmss() {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    return sdf.format(getCurrentTime());
	  }

	/**
	 * 获取一天的结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getEndTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 取当前时间
	 *
	 * @return
	 */
	public static Date getDateTime() {
		return Calendar.getInstance().getTime();
	}
}
