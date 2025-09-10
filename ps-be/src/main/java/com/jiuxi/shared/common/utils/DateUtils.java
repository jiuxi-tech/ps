package com.jiuxi.shared.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 * 提供日期时间格式化、转换、计算等功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final long ONE_MINUTE_SECONDS = 60;
    private static final int BEFORE_DAWN_HOUR = 6;
    private static final int MORNING_END_HOUR = 12;
    private static final int NOON_END_HOUR = 13;
    private static final int AFTERNOON_END_HOUR = 18;
    private static final int NIGHT_END_HOUR = 24;
    private static final String ZONE_ID = "Asia/Shanghai";

    /**
     * 解析标准日期格式字符串：yyyyMMddHHmmss
     * 
     * @param dateTime 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
    }

    /**
     * 按指定格式解析日期时间字符串
     * 
     * @param dateTime 日期时间字符串
     * @param format   格式字符串，推荐使用hutool的DatePattern
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTime, String format) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取当前东八时区的时间戳（毫秒）
     * 
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.of(ZONE_ID));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期时间字符串的时间戳
     * 
     * @param dateTime 日期时间字符串，格式：yyyyMMddHHmmss
     * @return 时间戳
     */
    public static long currentTimeMillis(String dateTime) {
        LocalDateTime localDateTime = parse(dateTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(ZONE_ID));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取指定LocalDateTime的时间戳
     * 
     * @param localDateTime 日期时间对象
     * @return 时间戳
     */
    public static long currentTimeMillis(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(ZONE_ID));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间字符串
     * 
     * @return 当前时间，格式：yyyyMMddHHmmss
     */
    public static String now() {
        return LocalDateTime.now(ZoneId.of(ZONE_ID)).format(DatePattern.PURE_DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期字符串
     * 
     * @return 当前日期，格式：yyyyMMdd
     */
    public static String today() {
        return LocalDateTime.now(ZoneId.of(ZONE_ID)).format(DatePattern.PURE_DATE_FORMATTER);
    }

    /**
     * 根据日期获取星期几的中文描述
     * 
     * @param localDate 本地日期
     * @return 星期描述
     */
    public static String getWeekText(LocalDate localDate) {
        String week = String.valueOf(localDate.getDayOfWeek());
        switch (week) {
            case "MONDAY":
                return "周一";
            case "TUESDAY":
                return "周二";
            case "WEDNESDAY":
                return "周三";
            case "THURSDAY":
                return "周四";
            case "FRIDAY":
                return "周五";
            case "SATURDAY":
                return "周六";
            case "SUNDAY":
                return "周日";
            default:
                return "";
        }
    }

    /**
     * 格式化日期时间字符串
     * 将 yyyy-MM-dd HH:mm:ss 格式转换为 yyyyMMddHHmmss
     * 
     * @param dateTime 带分隔符的日期时间字符串
     * @return 纯数字格式的日期时间字符串
     */
    public static String formatToyyyyMMddHHmmss(String dateTime) {
        if (dateTime == null) {
            return "";
        }
        // 移除分隔符
        return StrUtil.removeAll(dateTime, '-', '/', ':', ' ');
    }

    /**
     * Date转LocalDateTime
     * 
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转Date
     * 
     * @param dateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = dateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * 格式化为微信风格的时间显示
     * 
     * @param dateTime LocalDateTime对象
     * @return 格式化后的时间字符串
     */
    public static String formatWeiXinStyle(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        Date date = toDate(dateTime);
        return formatWeiXinStyle(date);
    }

    /**
     * 格式化为微信风格的时间显示
     * 
     * @param date Date对象
     * @return 格式化后的时间字符串
     */
    public static String formatWeiXinStyle(Date date) {
        if (date == null) {
            return "";
        }

        if (DateUtil.between(date, DateUtil.date(), DateUnit.SECOND, false) < 0) {
            // 未来时间显示年月日时分
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
                        // 一小时之外，显示时段+时分
                        return getTimeOfDayText(date) + " " + DateUtil.format(date, "HH:mm");
                    }
                } else if (DateUtil.isSameDay(date, DateUtil.yesterday())) {
                    // 昨天
                    return "昨天 " + DateUtil.format(date, "HH:mm");
                } else if (isThisWeek(date)) {
                    // 本周
                    String weekday = getWeekdayText(date);
                    return weekday + " " + DateUtil.format(date, "HH:mm");
                } else {
                    // 本年内其他时间
                    return DateUtil.format(date, "MM-dd HH:mm");
                }
            } else {
                // 非本年
                return DateUtil.format(date, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            }
        }
    }

    /**
     * 判断日期是否在本周内
     * 
     * @param date 要判断的日期
     * @return 是否在本周
     */
    private static boolean isThisWeek(Date date) {
        DateTime beginOfWeek = DateUtil.beginOfWeek(DateUtil.date());
        long betweenBegin = DateUtil.between(date, beginOfWeek, DateUnit.DAY, false) + 1;
        return DateUtil.isSameDay(date, beginOfWeek) || betweenBegin < 0;
    }

    /**
     * 根据时间获取时段描述
     * 
     * @param date 日期时间
     * @return 时段描述
     */
    private static String getTimeOfDayText(Date date) {
        int hour = DateUtil.hour(date, true);
        if (hour >= 0 && hour <= BEFORE_DAWN_HOUR) {
            return "凌晨";
        }
        if (hour > BEFORE_DAWN_HOUR && hour < MORNING_END_HOUR) {
            return "上午";
        }
        if (hour == MORNING_END_HOUR) {
            return "中午";
        }
        if (hour >= NOON_END_HOUR && hour <= AFTERNOON_END_HOUR) {
            return "下午";
        }
        if (hour > AFTERNOON_END_HOUR && hour <= NIGHT_END_HOUR) {
            return "晚上";
        }
        return "";
    }

    /**
     * 获取星期几的文字描述
     * 
     * @param date 日期
     * @return 星期描述
     */
    private static String getWeekdayText(Date date) {
        int dayOfWeek = DateUtil.dayOfWeek(date) - 1;
        switch (dayOfWeek) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            default:
                return "周日";
        }
    }

    // ================ 向后兼容的方法 ================

    /**
     * @deprecated 使用 {@link #today()} 替代
     */
    @Deprecated
    public static String date() {
        return today();
    }

    /**
     * @deprecated 使用 {@link #getWeekText(LocalDate)} 替代
     */
    @Deprecated
    public static String getWeek(LocalDate localDate) {
        return getWeekText(localDate);
    }

    /**
     * @deprecated 使用 {@link #formatWeiXinStyle(LocalDateTime)} 替代
     */
    @Deprecated
    public static String formatWeiXinDate(LocalDateTime dateTime) {
        return formatWeiXinStyle(dateTime);
    }

    /**
     * @deprecated 使用 {@link #formatWeiXinStyle(Date)} 替代
     */
    @Deprecated
    public static String formatWeiXinDate(Date date) {
        return formatWeiXinStyle(date);
    }
}