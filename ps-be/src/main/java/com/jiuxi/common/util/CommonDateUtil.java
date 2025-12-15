package com.jiuxi.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @ClassName: CommonDateUtil
 * @Description: 时间格式化工具类
 * @Author: Ypp
 * @Date: 2023/10/12 10:06
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonDateUtil {

    private static final long ONE_MINUTE_SECONDS = 60;

    private static final int BEFORE_DAWN_HOUR = 6;

    private static final int MORNING_END_HOUR = 12;

    private static final int NOON_END_HOUR = 13;

    private static final int AFTERNOON_END_HOUR = 18;

    private static final int NIGHT_END_HOUR = 24;

    private static final String ZONEID = "Asia/Shanghai";

    /**
     * 日期格式，标准日期格式：yyyyMMddHHmmss
     * @author Ypp
     * @date 2023/10/18 17:02
     * @param dateTime
     * @return void
     */
    public static LocalDateTime parse(String dateTime){
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
    }

    /**
     * 日期格式，格式推荐使用 hutool 的 DatePattern， 如：DatePattern.PURE_DATETIME_PATTERN
     * @author Ypp
     * @date 2023/10/18 17:03
     * @param dateTime
     * @param format  格式推荐使用 hutool 的 DatePattern， 如：DatePattern.PURE_DATETIME_PATTERN
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime parse(String dateTime, String format){
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取当前东八时区的 时间戳
     * @author Ypp
     * @date 2023/11/23 9:19
     * @param
     * @return long
     */
    public static long currentTimeMillis(){
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.of(ZONEID));
        return  zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取传入时间的 时间戳
     * @author Ypp
     * @date 2023/10/18 17:02
     * @param dateTime 日期格式，标准日期格式：yyyyMMddHHmmss
     * @return void
     */
    public static long currentTimeMillis(String dateTime){
        LocalDateTime localDateTime = parse(dateTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(ZONEID));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取传入时间的 时间戳
     * @author Ypp
     * @date 2023/10/18 17:02
     * @param localDateTime 日期时间
     * @return void
     */
    public static long currentTimeMillis(LocalDateTime localDateTime){
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(ZONEID));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间，返回：yyyyMMddHHmmss
     * @author Ypp
     * @date 2023/10/12 10:25
     * @param
     * @return java.lang.String
     */
    public static String now() {
        String now = LocalDateTime.now(ZoneId.of(ZONEID)).format(DatePattern.PURE_DATETIME_FORMATTER);
        return now;
    }

    /**
     * 获取当前日期，返回：yyyyMMdd
     * @author Ypp
     * @date 2023/10/12 10:25
     * @param
     * @return java.lang.String
     */
    public static String date() {
        String date = LocalDateTime.now(ZoneId.of(ZONEID)).format(DatePattern.PURE_DATE_FORMATTER);
        return date;
    }

    /**
     * 根据日期获取是周几
     * @author Ypp
     * @date 2023/10/12 10:27
     * @param localDate
     * @return java.lang.String
     */
    public static String getWeek(LocalDate localDate) {
        String week = String.valueOf(localDate.getDayOfWeek());
        switch (week) {
            case "MONDAY":
                week = "周一";
                break;
            case "TUESDAY":
                week = "周二";
                break;
            case "WEDNESDAY":
                week = "周三";
                break;
            case "THURSDAY":
                week = "周四";
                break;
            case "FRIDAY":
                week = "周五";
                break;
            case "SATURDAY":
                week = "周六";
                break;
            case "SUNDAY":
                week = "周日";
                break;
        }
        return week;
    }

    /**
     * 把 yyyy-MM-dd HH:mm:ss 格式化为 yyyyMMddHHmmss
     * @author Ypp
     * @date 2023/6/8 18:15
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return java.lang.String
     */
    public static String formatToyyyyMMddHHmmss(String dateTime){
        if(null == dateTime){
            return "";
        }
        // 移除 -、/、:、空格
        return StrUtil.removeAll(dateTime, '-', '/', ':', ' ');
    }


    /**
     * Date 转 LocalDateTime
     * @author Ypp
     * @date 2023/10/12 15:37
     * @param date
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date){
        // 获取系统默认的时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 将Date对象转换为Instant对象
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * LocalDateTime 转 Date
     * @author Ypp
     * @date 2023/10/12 15:34
     * @param dateTime
     * @return java.util.Date
     */
    public static Date toDate(LocalDateTime dateTime){
        // 获取系统默认的时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 将LocalDateTime对象转换为Instant对象
        Instant instant = dateTime.atZone(zoneId).toInstant();
        // 将Instant对象转换为Date对象
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 将日期格式化为仿微信的日期
     * @author Ypp
     * @date 2023/10/12 10:15
     * @param dateTime
     * @return java.lang.String
     */
    public static String formatWeiXinDate(LocalDateTime dateTime) {
        Date date = toDate(dateTime);
        return formatWeiXinDate(date);
    }
    /**
     * 将日期格式化为仿微信的日期
     * @author Ypp
     * @date 2023/10/12 10:07
     * @param date
     * @return java.lang.String
     */
    public static String formatWeiXinDate(Date date) {
        //
        if (DateUtil.between(date, DateUtil.date(), DateUnit.SECOND, false) < 0) {
            // 今天之后的时间显示年月日时分
            return DateUtil.format(date, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        } else {
            // 如果是今年
            if (DateUtil.thisYear() == DateUtil.year(date)) {
                // 如果是今天
                if (DateUtil.isSameDay(date, DateUtil.date())) {
                    // 相差分钟数
                    long betweenMinute = DateUtil.between(date, DateUtil.date(), DateUnit.MINUTE);
                    // 如果在1小时之内
                    if (betweenMinute < ONE_MINUTE_SECONDS) {
                        //一分钟之内，显示刚刚
                        if (betweenMinute < 1) {
                            return "刚刚";
                        } else {
                            //一分钟之外，显示xx分钟前
                            return betweenMinute + "分钟前";
                        }
                    } else {
                        // 一小时之外，显示时分
                        return getTodayHour(date) + " " + DateUtil.format(date, "HH:mm");
                    }
                } else if (DateUtil.isSameDay(date, DateUtil.yesterday())) {
                    // 如果是昨天，显示昨天时分
                    return "昨天 " + DateUtil.format(date, "HH:mm");
                } else if (isThisWeek(date)) {
                    // 如果是本周
                    String weekday;
                    // 获取是本周的第几天
                    int dayOfWeek = DateUtil.dayOfWeek(date) - 1;
                    switch (dayOfWeek) {
                        case 1:
                            weekday = "周一";
                            break;
                        case 2:
                            weekday = "周二";
                            break;
                        case 3:
                            weekday = "周三";
                            break;
                        case 4:
                            weekday = "周四";
                            break;
                        case 5:
                            weekday = "周五";
                            break;
                        case 6:
                            weekday = "周六";
                            break;
                        default:
                            weekday = "周日";
                            break;
                    }
                    // 显示本周时分
                    return weekday + " " + DateUtil.format(date, "HH:mm");
                } else {
                    // 否则显示月日时分
                    return DateUtil.format(date, "MM-dd HH:mm");
                }
            } else {
                // 本年之外显示年月日时分
                return DateUtil.format(date, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            }
        }
    }

    /**
     * 判断日期是否是本周
     * @author Ypp
     * @date 2023/10/12 10:12
     * @param date
     * @return boolean
     */
    private static boolean isThisWeek(Date date) {
        // 获取本周开始时间
        DateTime beginOfWeek = DateUtil.beginOfWeek(DateUtil.date());
        // 获取与本周开始时间相差的天数
        long betweenBegin = DateUtil.between(date, beginOfWeek, DateUnit.DAY, false) + 1;
        // 如果是同一天，或相差天数小于0，则是本周
        return DateUtil.isSameDay(date, beginOfWeek) || betweenBegin < 0;
    }

    /**
     * 根据今天时间获取 早中晚
     * @author Ypp
     * @date 2023/10/12 10:11
     * @param date
     * @return java.lang.String
     */
    private static String getTodayHour(Date date) {
        String result = "";
        int hour = DateUtil.hour(date, true);
        if (hour >= 0 && hour <= BEFORE_DAWN_HOUR) {
            result = "凌晨";
        }
        if (hour > BEFORE_DAWN_HOUR && hour < MORNING_END_HOUR) {
            result = "上午";
        }
        if (hour == MORNING_END_HOUR) {
            result = "中午";
        }
        if (hour >= NOON_END_HOUR && hour <= AFTERNOON_END_HOUR) {
            result = "下午";
        }
        if (hour > AFTERNOON_END_HOUR && hour <= NIGHT_END_HOUR) {
            result = "晚上";
        }
        return result;
    }



}
