

package com.keith.common.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author JohnSon
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     * @param week  周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return  返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     *对日期进行yyyy-MM-dd转化并比较大小
     */
//    public Date formatEqual(Date start,Date end){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        simpleDateFormat.format(start);
//
//    }

    /**
     * 从年月日时分秒的Date获取年月日Date
     * @param date
     * @return
     */
    public static Date getYmd(Date date) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        sdf.format(date);
        Date result = sdf.parse(sdf.format(date));
        return result;
    }

    /**
     * 从年月日时分秒的Date获取年月日Date对应的时间戳(秒)
     * @param date
     * @return
     */
    public static Long getYmdSTimestamps(Date date) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        sdf.format(date);
        Date result = sdf.parse(sdf.format(date));
        return result.getTime()/1000;
    }


    /**
     * 获取当前月第一天（yyyy-MM-dd）
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String thisFirstDay = sdf.format(calendar.getTime())+" 00:00:00";
        return thisFirstDay;
    }

    /**
     * 获取上个月的最后一天（yyyy-MM-dd）
     * @param
     * @return
     */
    public static Date getBeforeMonthLastDay(){
        DateTime nowTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, nowTime.getMonthOfYear()-1);
        // 获取本月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime aa = new DateTime(calendar.getTime());

        Date result = fmt.parseLocalDateTime(sdf.format(aa.plusDays(-1).toDate().getTime())).toDate();
        return  result;
    }

    /**
     * 获取这个月最后一天
     * @return
     */
    public static Date getThisMonthLastDay(){
        DateTime nowTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, nowTime.getMonthOfYear()-1);
        // 获取本月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime aa = new DateTime(calendar.getTime());

        DateTime result = fmt.parseLocalDateTime(sdf.format(aa.plusDays(-1).toDate().getTime())).toDateTime();
        //上个月最后一天再加一个月即本月最后一天
        Date thisMonthLastDay = result.plusMonths(1).toDate();
        return thisMonthLastDay;
    }
    public static void main(String[] args) throws Exception{
        DateTime nowTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, nowTime.getMonthOfYear()-1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String thisFirstDay = sdf.format(calendar.getTime());
        System.out.println("thisFirstDay:"+thisFirstDay);
        System.out.println("______:"+calendar.getTime());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime a = fmt.parseLocalDateTime(thisFirstDay).toDateTime();// .toDate();
         System.out.println("StringToDate:"+a);


        //减一天
        DateTime aa = new DateTime(calendar.getTime());
        System.out.println("上月最后一天："+aa.plusDays(-1).toDateTime());
        System.out.println("上月最后一天："+sdf.format(aa.plusDays(-1).toDate().getTime()));

        System.out.println("转化格式："+fmt.parseLocalDate(sdf.format(aa.plusDays(-1).toDate().getTime())).toDate());

        System.out.println("上个月最后一天加一个月："+aa.plusDays(-1).toDateTime().plusMonths(1));
        //本月最早一天减去一天即上月最后一天
        DateTime v = aa.plusDays(-1).toDateTime();
        System.out.println("v:"+v);
        DateTime bb = v.plusMonths(1);
        System.out.println("bb:"+bb);

        System.out.println("bb:"+bb.toDate());
        System.out.println("上个月最后一天加一个月："+aa.plusDays(-1).toDateTime().plusMonths(1).toDate());
        System.out.println("上个月最后一天加一个月："+sdf.format(aa.plusDays(-1).toDateTime().plusMonths(1).toDate().getTime()));


    }
}
