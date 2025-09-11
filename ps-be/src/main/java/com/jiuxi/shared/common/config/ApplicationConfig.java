package com.jiuxi.shared.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用程序配置属性
 * 统一管理应用基础配置信息
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@Component("enhancedApplicationConfig")
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

    /**
     * 应用基础信息
     */
    private Info info = new Info();

    /**
     * 文件上传配置
     */
    private Upload upload = new Upload();

    /**
     * 国际化配置
     */
    private I18n i18n = new I18n();

    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();

    @Data
    public static class Info {
        /**
         * 应用名称
         */
        private String name = "ps-be";

        /**
         * 应用版本
         */
        private String version = "2.2.2-SNAPSHOT";

        /**
         * 应用描述
         */
        private String description = "PS-BMP Backend System";

        /**
         * 应用编码
         */
        private String encoding = "UTF-8";

        /**
         * 作者信息
         */
        private String author = "System";

        /**
         * 联系方式
         */
        private String contact = "admin@jiuxi.com";
    }

    @Data
    public static class Upload {
        /**
         * 单个文件最大大小
         */
        private String maxFileSize = "50MB";

        /**
         * 总上传数据最大大小
         */
        private String maxRequestSize = "100MB";

        /**
         * 允许的文件类型
         */
        private String[] allowedTypes = {
            "image/jpeg", "image/png", "image/gif", "image/bmp",
            "application/pdf", "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        };

        /**
         * 上传目录
         */
        private String uploadDir = "/upload";
    }

    @Data
    public static class I18n {
        /**
         * 国际化资源文件基础名
         */
        private String[] basename = {"i18n/messages", "i18n/validation"};

        /**
         * 编码格式
         */
        private String encoding = "UTF-8";

        /**
         * 缓存时长（秒）
         */
        private int cacheDuration = 3600;

        /**
         * 是否回退到系统本地化
         */
        private boolean fallbackToSystemLocale = true;

        /**
         * 默认语言
         */
        private String defaultLocale = "zh_CN";
    }

    @Data
    public static class Monitor {
        /**
         * 监控服务器URL
         */
        private String serverUrl = "http://localhost:8082/ps-be";

        /**
         * 系统描述
         */
        private String systemDesc = "PS-BMP后端系统";

        /**
         * 客户端ID
         */
        private String clientId = "ps-bmp-backend";

        /**
         * 连接超时时间（毫秒）
         */
        private int connectionTimeout = 3000;

        /**
         * 读取超时时间（毫秒）
         */
        private int readTimeout = 5000;

        /**
         * 是否启用监控
         */
        private boolean enabled = true;
    }
}