package com.jiuxi.common.server.bean;

import cn.hutool.core.date.DatePattern;
import com.jiuxi.common.util.CommonArithmeticUtil;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: JVM相关信息
 * @Author 杨占锐
 * @Date 2024/3/12 13:27
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class Jvm {

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    /**
     *  JDK启动时间
     */
    private String startTime;

    /**
     *  JDK运行时间
     */
    private String runTime;

    /**
     * JDK运行参数
     */
    private String inputArgs;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        return CommonArithmeticUtil.div(total - free, 1, 2);
    }

    public double getUsage() {
        return CommonArithmeticUtil.mul(CommonArithmeticUtil.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setInputArgs(String inputArgs) {
        this.inputArgs = inputArgs;
    }

    public String getStartTime() {
        return parseDateToStr(DatePattern.CHINESE_DATE_TIME_PATTERN, new Date());
    }

    public String getRunTime() {
        return timeDistance(new Date(), getServerStartDate());
    }

    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }
}
