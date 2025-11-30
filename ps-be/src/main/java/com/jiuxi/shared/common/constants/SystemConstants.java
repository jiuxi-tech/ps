package com.jiuxi.shared.common.constants;

import java.time.format.DateTimeFormatter;

/**
 * 系统常量类
 * 定义系统级别的配置常量
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class SystemConstants {

    private SystemConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /**
     * 系统信息
     */
    public static final class System {
        /** 系统名称 */
        public static final String NAME = "业务中台统一认证平台";
        /** 系统版本 */
        public static final String VERSION = "2.2.2-SNAPSHOT";
        /** 系统编码 */
        public static final String CODE = "JIUXI-SSO";
        /** 系统描述 */
        public static final String DESCRIPTION = "基于Keycloak的企业级单点登录平台";
    }

    /**
     * 日期时间格式
     */
    public static final class DateFormat {
        /** 日期格式 */
        public static final String DATE = "yyyy-MM-dd";
        /** 时间格式 */
        public static final String TIME = "HH:mm:ss";
        /** 日期时间格式 */
        public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
        /** 时间戳格式 */
        public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
        /** ISO日期时间格式 */
        public static final String ISO_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        /** 紧凑日期格式 */
        public static final String COMPACT_DATE = "yyyyMMdd";
        /** 紧凑日期时间格式 */
        public static final String COMPACT_DATETIME = "yyyyMMddHHmmss";

        /** 日期格式化器 */
        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE);
        public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME);
        public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME);
        public static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATE);
        public static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATETIME);
    }

    /**
     * 字符编码
     */
    public static final class Charset {
        /** UTF-8编码 */
        public static final String UTF8 = "UTF-8";
        /** GBK编码 */
        public static final String GBK = "GBK";
        /** ISO编码 */
        public static final String ISO = "ISO-8859-1";
    }

    /**
     * 文件相关
     */
    public static final class File {
        /** 文件分隔符 */
        public static final String SEPARATOR = "/";
        /** 扩展名分隔符 */
        public static final String EXTENSION_SEPARATOR = ".";
        /** 最大文件大小（10MB） */
        public static final long MAX_SIZE = 10 * 1024 * 1024;
        /** 临时文件前缀 */
        public static final String TEMP_PREFIX = "temp_";
        /** 上传文件前缀 */
        public static final String UPLOAD_PREFIX = "upload_";

        /** 允许的图片类型 */
        public static final String[] ALLOWED_IMAGE_TYPES = {
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"
        };

        /** 允许的文档类型 */
        public static final String[] ALLOWED_DOCUMENT_TYPES = {
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv"
        };

        /** 允许的压缩文件类型 */
        public static final String[] ALLOWED_ARCHIVE_TYPES = {
            "zip", "rar", "7z", "tar", "gz", "bz2"
        };
    }

    /**
     * 缓存相关
     */
    public static final class Cache {
        /** 缓存前缀 */
        public static final String PREFIX = "jiuxi:sso:";
        /** 用户缓存前缀 */
        public static final String USER_PREFIX = PREFIX + "user:";
        /** 角色缓存前缀 */
        public static final String ROLE_PREFIX = PREFIX + "role:";
        /** 权限缓存前缀 */
        public static final String PERMISSION_PREFIX = PREFIX + "permission:";
        /** 配置缓存前缀 */
        public static final String CONFIG_PREFIX = PREFIX + "config:";
        /** 字典缓存前缀 */
        public static final String DICT_PREFIX = PREFIX + "dict:";
        /** 验证码缓存前缀 */
        public static final String CAPTCHA_PREFIX = PREFIX + "captcha:";
        /** 令牌缓存前缀 */
        public static final String TOKEN_PREFIX = PREFIX + "token:";

        /** 缓存过期时间（秒） */
        public static final long EXPIRE_SHORT = 5 * 60;        // 5分钟
        public static final long EXPIRE_MEDIUM = 30 * 60;      // 30分钟
        public static final long EXPIRE_LONG = 2 * 60 * 60;    // 2小时
        public static final long EXPIRE_DAY = 24 * 60 * 60;    // 1天
        public static final long EXPIRE_WEEK = 7 * 24 * 60 * 60; // 1周
    }

    /**
     * 数据库相关
     */
    public static final class Database {
        /** 动态表前缀 */
        public static final String DYNAMIC_TABLE_PREFIX = "C_JSON_";
        /** 表名前缀 */
        public static final String TABLE_PREFIX = "tp_";
        /** 主键字段名 */
        public static final String PRIMARY_KEY = "id";
        /** 创建时间字段名 */
        public static final String CREATE_TIME = "create_time";
        /** 更新时间字段名 */
        public static final String UPDATE_TIME = "update_time";
        /** 逻辑删除字段名 */
        public static final String DELETED = "deleted";
        /** 版本号字段名 */
        public static final String VERSION = "version";
    }

    /**
     * 线程池配置
     */
    public static final class ThreadPool {
        /** 核心线程数 */
        public static final int CORE_POOL_SIZE = 5;
        /** 最大线程数 */
        public static final int MAX_POOL_SIZE = 20;
        /** 队列容量 */
        public static final int QUEUE_CAPACITY = 100;
        /** 线程名称前缀 */
        public static final String THREAD_NAME_PREFIX = "jiuxi-async-";
        /** 线程保活时间（秒） */
        public static final int KEEP_ALIVE_SECONDS = 60;
    }

    /**
     * 监控相关
     */
    public static final class Monitor {
        /** 单数据源 */
        public static final String SINGLE_DATA_SOURCE = "single_data_source";
        /** 动态数据源 */
        public static final String DYNAMIC_DATA_SOURCE = "dynamic_data_source";
        /** Redis */
        public static final String REDIS = "redis";
        /** 消息队列 */
        public static final String MQ = "mq";
        /** MQTT */
        public static final String MQTT = "mqtt";
        /** 健康检查端点 */
        public static final String HEALTH_CHECK = "/actuator/health";
        /** 监控指标端点 */
        public static final String METRICS = "/actuator/metrics";
    }

    /**
     * 环境配置
     */
    public static final class Environment {
        /** 开发环境 */
        public static final String DEV = "dev";
        /** 测试环境 */
        public static final String TEST = "test";
        /** 生产环境 */
        public static final String PROD = "prod";
        /** 本地环境 */
        public static final String LOCAL = "local";
    }

    /**
     * 配置键
     */
    public static final class ConfigKey {
        /** 系统名称 */
        public static final String SYSTEM_NAME = "system.name";
        /** 系统版本 */
        public static final String SYSTEM_VERSION = "system.version";
        /** 系统LOGO */
        public static final String SYSTEM_LOGO = "system.logo";
        /** 版权信息 */
        public static final String SYSTEM_COPYRIGHT = "system.copyright";
        /** 上传路径 */
        public static final String UPLOAD_PATH = "upload.path";
        /** 上传最大大小 */
        public static final String UPLOAD_MAX_SIZE = "upload.maxSize";
        /** 密码策略 */
        public static final String PASSWORD_POLICY = "password.policy";
        /** 会话超时时间 */
        public static final String SESSION_TIMEOUT = "session.timeout";
        /** 验证码开关 */
        public static final String CAPTCHA_ENABLED = "captcha.enabled";
        /** 注册开关 */
        public static final String REGISTER_ENABLED = "register.enabled";
    }
}