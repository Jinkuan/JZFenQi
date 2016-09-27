package com.juzifenqi.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Desc:   时间日期格式化及转换工具类
 * Time:   2016-09-23 12:54
 * Author: chende
 */
public class TimeUtils {
    public static String getDateStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getTimeStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getTimeStrShort(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getStepKey(String preffix) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return preffix + ":" + formater.format(Calendar.getInstance().getTime());
    }


    public static String getDateStrSeconds(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getDateStrMinutes(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String[] getDateStrArrays(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy", Locale.CHINA);
        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd", Locale.CHINA);
        return new String[]{formatYear.format(calendar.getTime()), formatDate.format(calendar.getTime())};
    }

    public static String getDateStr(long start_time, long end_time) {
        String startTimeStr = null;
        String endTimeStr = null;
        SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start_time);
        startTimeStr = formatDate.format(calendar.getTime());
        calendar.setTimeInMillis(end_time);
        endTimeStr = formatDate.format(calendar.getTime());
        return startTimeStr + " --- " + endTimeStr;
    }

    public static long timeFromStr(String timeStr) {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(timeStr);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String getDateCnStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getDateMonthDayCnStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("M月dd日", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static String getDateMonthDayEnStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }
}
