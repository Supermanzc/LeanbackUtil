package com.wt.leanbackutil.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间格式转换
 */
public class DateUtil {

    public static final String DATE_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YEAR_MONTH_DAY = "yyyy年MM月dd";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();

    /**
     * @param milliseconds 一段时间
     * @return the time format by "yyyy-MM-dd HH:mm:ss"
     */
    public static String toTime(long milliseconds) {
        Date date = new Date();
        date.setTime(milliseconds);
        return toTime(date, DATE_FORMAT_ALL);
    }

    /**
     * @param milliseconds 一段时间
     * @param pattern      format
     * @return the time format
     */
    public static String toTime(long milliseconds, String pattern) {
        Date date = new Date();
        date.setTime(milliseconds);
        return toTime(date, pattern);
    }

    /**
     * @param date    日期
     * @param pattern format
     * @return the time format
     */
    private static String toTime(Date date, String pattern) {
        if ("".equals(pattern)) {
            pattern = DATE_FORMAT_DEFAULT;
        }
        SIMPLE_DATE_FORMAT.applyPattern(pattern);
        if (date == null) {
            date = new Date();
        }
        try {
            return SIMPLE_DATE_FORMAT.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 把毫秒转换成00:00:00格式的字符串，如果不到1个小时，就显示00:00格式
     *
     * @param milliseconds 一段时间
     * @return String
     */
    public static String getTimeDisplay(int milliseconds) {
        if (milliseconds <= 0) {
            return "00:00";
        }
        StringBuilder sb = new StringBuilder();

        long hour = milliseconds / (3600 * 1000);
        long remainder = milliseconds % (3600 * 1000);
        if (hour > 0) {
            sb.append(hour < 10 ? "0" + hour : hour);
            sb.append(":");
        }

        long minute = remainder / (60 * 1000);
        remainder = remainder % (60 * 1000);
        sb.append(minute < 10 ? "0" + minute : minute);
        sb.append(":");

        long second = remainder / 1000;
        sb.append(second < 10 ? "0" + second : second);

        return sb.toString();
    }

    /**
     * 把毫秒转换成00:00:00格式的字符串
     *
     * @param milliseconds 一段时间
     * @return String
     */
    public static String getTimeDisplay2(int milliseconds) {
        if (milliseconds <= 0) {
            return "00:00:00";
        }
        StringBuilder sb = new StringBuilder();

        long hour = milliseconds / (3600 * 1000);
        long remainder = milliseconds % (3600 * 1000);
        sb.append(hour < 10 ? "0" + hour : hour);
        sb.append(":");

        long minute = remainder / (60 * 1000);
        remainder = remainder % (60 * 1000);
        sb.append(minute < 10 ? "0" + minute : minute);
        sb.append(":");

        long second = remainder / 1000;
        sb.append(second < 10 ? "0" + second : second);

        return sb.toString();
    }

    /**
     * 将格式00:00:00.000的时间转化成毫秒
     *
     * @param duration 00:00:00.000 或者00:00:00
     * @return int
     */
    public static int durationToMilliseconds(String duration) {
        if (null == duration || "".equals(duration)) {
            return 0;
        }
        String regx = "^\\d{1,2}:\\d{2}:\\d{2}(.\\d{3})?$";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(duration);
        int minSeconds = 0;
        if (null != m && m.find()) {
            String[] as = duration.split(":");
            minSeconds += Integer.parseInt(as[0]) * 3600 * 1000;
            minSeconds += Integer.parseInt(as[1]) * 60 * 1000;

            if (as[2].contains(".")) {
                String[] ms = as[2].split("\\.");
                minSeconds += Integer.parseInt(ms[0]) * 1000;
                minSeconds += Integer.parseInt(ms[1]);
            } else {
                minSeconds += Integer.parseInt(as[2]) * 1000;
            }
        }
        return minSeconds;
    }
}