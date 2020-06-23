package com.core.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.keith.common.utils.DateUtils.DATE_TIME_PATTERN;

/**
 * @author Lzy
 * @date 2020/6/17 20:48
 */
public class CountDownUtils {
    private static long day = 0;
    private static long hour = 0;
    private static long minute = 0;
    private static long second = 0;

    private static boolean dayNotAlready = false;
    private static boolean hourNotAlready = false;
    private static boolean minuteNotAlready = false;
    private static boolean secondNotAlready = false;

    /**
     * @Author chenqi
     * @Description 根据目标时间计算倒计时  天:小时:分钟:秒
     * @Date 17:41 2019/5/15
     * @Param [overTime(目标时间),currentDateTime(当前时间或指定时间)]
     * @return void
     **/
    public static String getCountDown(String overTime, String currentDateTime) {
        Long totalSeconds = getTimeDiffSecond(overTime,currentDateTime);
        resetData();

        if (totalSeconds > 0) {
            secondNotAlready = true;
            second = totalSeconds;
            if (second >= 60) {
                minuteNotAlready = true;
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hourNotAlready = true;
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        dayNotAlready = true;
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
        }

        return  day + ":" + hour + ":" + minute+ ":" + second;
    }

    private static void resetData() {
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;
        dayNotAlready = false;
        hourNotAlready = false;
        minuteNotAlready = false;
        secondNotAlready = false;
    }

    /**
     * @Author chenqi
     * @Description 获取两个时间的秒时差 date1 - date2
     * @Date 17:51 2019/5/15
     * @Param [date1, date2]
     * @return long
     **/
    public static long getTimeDiffSecond(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long day = (date.getTime() - mydate.getTime()) / 1000;
        return day;
    }
}
