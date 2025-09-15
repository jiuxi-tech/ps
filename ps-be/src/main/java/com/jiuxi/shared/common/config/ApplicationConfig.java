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

    // Getter methods
    public Info getInfo() {
        return info;
    }

    public Upload getUpload() {
        return upload;
    }

    public I18n getI18n() {
        return i18n;
    }

    public Monitor getMonitor() {
        return monitor;
    }

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

        // Getter methods
        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getDescription() {
            return description;
        }

        public String getEncoding() {
            return encoding;
        }

        public String getAuthor() {
            return author;
        }

        public String getContact() {
            return contact;
        }

        // Setter methods
        public void setName(String name) {
            this.name = name;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
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

        // Getter methods
        public String getServerUrl() {
            return serverUrl;
        }

        public String getSystemDesc() {
            return systemDesc;
        }

        public String getClientId() {
            return clientId;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public boolean isEnabled() {
            return enabled;
        }

        // Setter methods
        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public void setSystemDesc(String systemDesc) {
            this.systemDesc = systemDesc;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}