package com.jiuxi.monitor.server.constant;

import com.jiuxi.monitor.common.constant.MonitorCommonConstant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MonitorClientConstant
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/15 13:37
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MonitorServerConstant extends MonitorCommonConstant {

    /**
     * 基本信息缓存key
     */
    public static final String KEY_PREFIX = "jiuxi:platform:plugin:monitor:";

    /**
     * 基本信息缓存key
     */
    public static final String BASEINFO_KEY = KEY_PREFIX + "client:baseinfo:";

    /**
     * 配置信息缓存key
     */
    public static final String ALARM_CONFIG_KEY = KEY_PREFIX + "alarm:config";

    /**
     * 发送邮件缓存key
     */
    public static final String SEND_MAIL_KEY = KEY_PREFIX + "client:send_mail:";

    /**
     * 心跳信息缓存key
     */
    public static final String HEARTBEAT_KEY = KEY_PREFIX + "heartbeat:";

    /**
     * 心跳报警信息缓存key
     */
    public static final String HEARTBEAT_ALARM_KEY = KEY_PREFIX + "heartbeat_alarm:";

    /**
     * 缓存天数
     */
    public static final int CACHE_DAYS = 30;

    /**
     * 缓存小时数
     */
    public static final int CACHE_HOURS = 12;

    /**
     * 离线分钟数
     */
    public static final int OFFLINE_MINUTE = 5;

    /**
     * 持续分钟数
     */
    public static final int CONTINUE_MINUTE = 5;

    /**
     * cpu
     */
    public static final String CPU = "cpu";

    /**
     * 内存
     */
    public static final String MEMORY = "memory";

    /**
     * 磁盘
     */
    public static final String DISK = "disk";

    /**
     * 离线
     */
    public static final String OFF_LINE = "off_line";

    /**
     * 数据源映射
     */
    public static final Map<String, String> SOURCE_MAP = new HashMap<String, String>(){{
        put(CPU, "CPU");
        put(MEMORY, "内存");
        put(DISK, "磁盘");
        put(SINGLE_DATA_SOURCE, "单数据源");
        put(DYNAMIC_DATA_SOURCE, "动态数据源");
        put(REDIS, "Redis");
        put(MQ, "MQ");
        put(MQTT, "MQTT");
        put(OFF_LINE, "离线");
    }};

    /**
     * 数据源列表
     */
    public static final List<String> SOURCE_LIST = Arrays.asList(CPU, MEMORY, DISK, SINGLE_DATA_SOURCE, DYNAMIC_DATA_SOURCE, REDIS, MQ, MQTT);
}