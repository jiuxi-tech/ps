package com.jiuxi.common.util;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 时间工具类 1.8版本
 *
 * @author 杨攀
 * @date 2020/10/27 17:18
 */
public class DateUtils {

    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyyMMdd
     */
    public static final String DEFAULT_DATE_FORMAT_ORDER = "yyyyMMdd";

    /**
     * 缺省的文件目录： yyyyMM
     */
    public static final String DEFAULT_YYMM_FORMAT = "yyyyMM";
    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss:sss
     */
    public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss:sss";
    /**
     * 字符串时间
     */
    public static final String YMDHMS = "yyyyMMddHHmmss";
    /**
     * 当前时间
     */
    public static Date DATE_NOW = new Date();

    /**
     * 当前时间
     */
    public static LocalDate localDate = LocalDate.now();

    /**
     * @Title getDay
     * @Description 获取年月日（当天）
     * @Param []
     * @Return java.lang.String
     * @Date 2019/6/4 15:26
     * @Author 杨攀
     */
    public static String getDay() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return date.format(fmt);
    }

    /**
     * @Title getDayTime
     * @Description 年月日时分秒 （当天） 此方法不支持毫秒
     * @Param []
     * @Return java.lang.String
     * @Date 2019/6/4 15:28
     * @Author 杨攀
     */
    public static String getDayTime() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
        return date.format(fmt);
    }

    /**
     * @Title getDateToString
     * @Description date类型转换字符串
     * @Param [date]
     * @Return java.lang.String
     * @Date 2019/7/8 11:07
     * @Author 杨攀
     */
    public static String getDateToString(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_ORDER);
        return localDateTime.format(fmt);
    }

    /**
     * @Title getLocalDateToString
     * @Description LocalDate转换String
     * @Param [localDate]
     * @Return java.lang.String
     * @Date 2019/7/8 11:10
     * @Author 杨攀
     */
    public static String getLocalDateToString(LocalDate localDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_ORDER);
        return localDate.format(fmt);
    }

    /**
     * @Title getFixedTime
     * @Description 自定固定时间
     * @Param [year, month, day, hour, minute, second]
     * @Return java.time.LocalDateTime
     * @Date 2019/7/8 15:23
     * @Author 杨攀
     */
    public static LocalDateTime getFixedTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    /**
     * @Title settingsTime
     * @Description 设置固定时间
     * @Param [year, month, day, hour, minute, second]
     * @Return java.time.LocalDateTime
     * @Date 2019/7/8 15:29
     * @Author 杨攀
     */
    public static LocalDateTime settingsTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day).withHour(hour).withMinute(minute).withSecond(second);
    }

    /**
     * @Title firstToMonth
     * @Description 当月第一天
     * @Param []
     * @Return java.time.LocalDate
     * @Date 2019/7/8 15:31
     * @Author 杨攀
     */
    public static LocalDate firstToMonth() {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * @Title lastToMonth
     * @Description 本月最后一天
     * @Param []
     * @Return java.time.LocalDate
     * @Date 2019/7/8 15:33
     * @Author 杨攀
     */
    public static LocalDate lastToMonth() {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * @Title firstNextMonth
     * @Description 下个月第一天
     * @Param []
     * @Return java.time.LocalDate
     * @Date 2019/7/8 15:35
     * @Author 杨攀
     */
    public static LocalDate firstNextMonth() {
        return localDate.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * @Title firstToYear
     * @Description 本年第一天
     * @Param []
     * @Return java.time.LocalDate
     * @Date 2019/7/8 15:36
     * @Author 杨攀
     */
    public static LocalDate firstToYear() {
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * @Title lastToYear
     * @Description 本年最后一天
     * @Param []
     * @Return java.time.LocalDate
     * @Date 2019/7/8 15:37
     * @Author 杨攀
     */
    public static LocalDate lastToYear() {
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    /*———————————————————   时间相差 start    ———————————————————————*/

    /**
     * @Title difference
     * @Description 相差分钟
     * @Param [localTime, localTime1]
     * @Return long
     * @Date 2019/7/8 15:43
     * @Author 杨攀
     */
    public static long difference(LocalTime localTime, LocalTime localTime1) {
        return Duration.between(localTime, localTime1).getSeconds();
    }

    /**
     * @Title aFewYearsApart
     * @Description 相差几年
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:47
     * @Author 杨攀
     */
    public static long aFewYearsApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.YEARS.between(oldDate, newDate);
    }

    /**
     * @Title aFewMonthsApart
     * @Description 相差几个月
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:49
     * @Author 杨攀
     */
    public static long aFewMonthsApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.MONTHS.between(oldDate, newDate);
    }

    /**
     * @Title aFewWEEKSApart
     * @Description 相差多少周
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:50
     * @Author 杨攀
     */
    public static long aFewWEEKSApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.WEEKS.between(oldDate, newDate);
    }

    /**
     * @Title aFewDAYSApart
     * @Description 相差多少天
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:51
     * @Author 杨攀
     */
    public static long aFewDAYSApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.DAYS.between(oldDate, newDate);
    }

    /**
     * @Title aFewHOURSApart
     * @Description 相差多少小时
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:52
     * @Author 杨攀
     */
    public static long aFewHOURSApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.HOURS.between(oldDate, newDate);
    }

    /**
     * @Title aFewHOURSApart
     * @Description 相差多少分钟
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:52
     * @Author 杨攀
     */
    public static long aFewMINUTESApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.MINUTES.between(oldDate, newDate);
    }

    /**
     * @Title aFewSECONDSApart
     * @Description 相差多少秒
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:54
     * @Author 杨攀
     */
    public static long aFewSECONDSApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.SECONDS.between(oldDate, newDate);
    }

    /**
     * @Title aFewMILLISApart
     * @Description 相差多少毫秒
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:55
     * @Author 杨攀
     */
    public static long aFewMILLISApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.MILLIS.between(oldDate, newDate);
    }

    /**
     * @Title aFewMILLISApart
     * @Description 相差多少纳秒
     * @Param [oldDate, newDate]
     * @Return long
     * @Date 2019/7/8 15:55
     * @Author 杨攀
     */
    public static long aFewNANOSApart(LocalDateTime oldDate, LocalDateTime newDate) {
        return ChronoUnit.NANOS.between(oldDate, newDate);
    }

    /*———————————————————   时间相差 end    ———————————————————————*/

    /**
     * @Title getTimestamp
     * @Description 获取时间戳13位
     * @Param []
     * @Return long
     * @Date 2019/7/8 15:59
     * @Author 杨攀
     */
    public static long getTimestamp() {
        return Clock.systemDefaultZone().millis();
    }

    /*———————————————————————   时间转换 Start   ——————————————————————*/

    /**
     * @Title dateToLocalDateTime
     * @Description date转换LocalDateTime
     * @Param [date]
     * @Return java.time.LocalDateTime
     * @Date 2019/7/8 16:05
     * @Author 杨攀
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        // LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * @Title dateToLocalDate
     * @Description date转换LocalDate
     * @Param [date]
     * @Return java.time.LocalDate
     * @Date 2019/7/8 16:08
     * @Author 杨攀
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @Title dateToLocalTime
     * @Description date转换LocalTime
     * @Param [date]
     * @Return java.time.LocalTime
     * @Date 2019/7/8 16:08
     * @Author 杨攀
     */
    public static LocalTime dateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * @Title localDateTimeToDate
     * @Description LocalDateTime转为Date
     * @Param [localDateTime]
     * @Return java.util.Date
     * @Date 2019/7/8 16:10
     * @Author 杨攀
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @Title localDateToDate
     * @Description LocalDate -> Date，时间默认都是00
     * @Param [localDate]
     * @Return java.util.Date
     * @Date 2019/7/8 16:13
     * @Author 杨攀
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    /*———————————————————————   时间转换 End     ——————————————————————*/

    /*————————————————————————  日期与字符串、字符串与时间转换  Start ——————————————————————————*/

    /**
     * @Title stringConversionLocalDate
     * @Description 字符串转LocalDate
     * @Param [ymd]
     * @Return java.time.LocalDate
     * @Date 2019/7/8 16:19
     * @Author 杨攀
     */
    public static LocalDate stringConversionLocalDate(String ymd) {
        return LocalDate.parse(ymd, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * @Title stringConversionLocalDateTime
     * @Description 字符串转LocalDateTime
     * @Param [ymdhms]
     * @Return java.time.LocalDateTime
     * @Date 2019/7/8 16:22
     * @Author 杨攀
     */
    public static LocalDateTime stringConversionLocalDateTime(String ymdhms) {
        return LocalDateTime.parse(ymdhms, DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * @Title localDateConversionString
     * @Description localDate转字符串
     * @Param [localDate]
     * @Return java.lang.String
     * @Date 2019/7/8 16:24
     * @Author 杨攀
     */
    public static String localDateConversionString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * @Title localDateConversionString
     * @Description localDate转字符串
     * @Param [localDate]
     * @Return java.lang.String
     * @Date 2019/7/8 16:24
     * @Author 杨攀
     */
    public static String localDateConversionString(LocalDate localDate, String yyyyMM) {
        return localDate.format(DateTimeFormatter.ofPattern(yyyyMM));
    }

    /**
     * @Title localDateTimeConversionString
     * @Description localDateTime转字符串
     * @Param [localDateTime]
     * @Return java.lang.String
     * @Date 2019/7/8 16:25
     * @Author 杨攀
     */
    public static String localDateTimeConversionString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * @Title localDateTimeToString
     * @Description localDateTime转字符串
     * @Param [localDateTime]
     * @Return java.lang.String
     * @Date 2019/7/8 16:28
     * @Author 杨攀
     */
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT).format(localDateTime);
    }
    /*————————————————————————  日期与字符串、字符串与时间转换  end   ——————————————————————————*/

    /**
     * @Title timestampConversionLocalDateTime
     * @Description 时间戳转换Localdatetime
     * @Param [timeMillis]
     * @Return java.time.LocalDateTime
     * @Date 2019/7/8 16:32
     * @Author 杨攀
     */
    public static LocalDateTime timestampConversionLocalDateTime(long timeMillis) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * @Title localDateTimeConversiontimestamp
     * @Description localDateTime 转换时间戳
     * @Param [localDateTime]
     * @Return long
     * @Date 2019/7/8 16:34
     * @Author 杨攀
     */
    public static long localDateTimeConversiontimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @Title getWeek
     * @Description 获取周几
     * @Param [localDate]
     * @Return java.lang.String
     * @Date 2019/7/8 17:08
     * @Author 杨攀
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
     * @Title localDateTimeChangeTen
     * @Description LocalDateTime转换10位时间戳
     * @Param [localDateTime]
     * @Return long
     * @Date 2019/7/9 8:39
     * @Author 杨攀
     */
    public static long localDateTimeChangeTen(LocalDateTime localDateTime) {
        long lon = localDateTimeConversiontimestamp(localDateTime) / 1000;
        return Long.valueOf(String.format("%010d", lon));
    }


    /**
     * 日期格式字符串转换
     *
     * @param date   字符串格式的日期；如：20230220203000
     * @param input  输入的日期格式；如：yyyyMMddHHmmss
     * @param output 输出的日期格式；如：yyyy-MM-dd HH:mm:ss
     * @return java.lang.String
     * @author pand
     * @date
     */
    public static String formatStringDate(String date, String input, String output) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(input);
        LocalDateTime localDateTime = LocalDateTime.parse(date, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(output);
        return localDateTime.format(outputFormatter);
    }


    /**
     * 格式化时间戳成指定格式的字符串
     *
     * @param timeMillis 时间戳
     * @param output     输出的日期格式；如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatMillisToString(long timeMillis, String output) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(output);
        return dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }


    /**
     * 把 yyyy-MM-dd HH:mm:ss 格式化为 yyyyMMddHHmmss
     * @author 杨攀
     * @date 2023/6/8 18:15
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return java.lang.String
     */
    public static String formatDateTimeToyyyyMMddHHmmss(String dateTime){
        if(null == dateTime){
            return "";
        }
        return dateTime.replaceAll("[-/\\s:]", "");
    }

}
