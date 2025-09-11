package com.jiuxi.shared.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据库配置属性
 * 统一管理数据库相关配置
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@Component("enhancedDatabaseConfig")
@ConfigurationProperties(prefix = "app.database")
public class DatabaseConfig {

    /**
     * 数据源配置
     */
    private Datasource datasource = new Datasource();

    /**
     * MyBatis配置
     */
    private Mybatis mybatis = new Mybatis();

    /**
     * 连接池配置
     */
    private Pool pool = new Pool();

    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();

    @Data
    public static class Datasource {
        /**
         * 数据库驱动类名
         */
        private String driverClassName = "org.mariadb.jdbc.Driver";

        /**
         * 数据库连接URL
         */
        private String url;

        /**
         * 数据库用户名
         */
        private String username;

        /**
         * 数据库密码
         */
        private String password;

        /**
         * 连接验证查询语句
         */
        private String validationQuery = "SELECT 1";

        /**
         * 连接超时时间（毫秒）
         */
        private long connectionTimeout = 30000;

        /**
         * 连接最大生存时间（毫秒）
         */
        private long maxLifetime = 1800000;
    }

    @Data
    public static class Pool {
        /**
         * 初始连接数
         */
        private int initialSize = 5;

        /**
         * 最小空闲连接数
         */
        private int minIdle = 5;

        /**
         * 最大活跃连接数
         */
        private int maxActive = 500;

        /**
         * 获取连接时最大等待时间（毫秒）
         */
        private long maxWait = 60000;

        /**
         * 空闲时是否验证连接
         */
        private boolean testWhileIdle = true;

        /**
         * 获取连接时是否验证
         */
        private boolean testOnBorrow = false;

        /**
         * 归还连接时是否验证
         */
        private boolean testOnReturn = false;

        /**
         * 空闲连接回收时间间隔（毫秒）
         */
        private long timeBetweenEvictionRunsMillis = 60000;

        /**
         * 连接在池中最小生存时间（毫秒）
         */
        private long minEvictableIdleTimeMillis = 300000;
    }

    @Data
    public static class Mybatis {
        /**
         * 数据中心ID（用于雪花算法）
         */
        private long datacenterId = 0;

        /**
         * 机器ID（用于雪花算法）
         */
        private long machineId = 0;

        /**
         * 是否启用租户模式
         */
        private boolean tenant = false;

        /**
         * 忽略租户条件的表名
         */
        private String[] ignoreTenantTables = {"test"};

        /**
         * 分页大小限制
         */
        private long pageLimit = 1000;

        /**
         * 是否启用分页插件
         */
        private boolean pageHelperEnabled = true;

        /**
         * 是否启用性能分析插件
         */
        private boolean performanceInterceptorEnabled = false;

        /**
         * SQL执行时间阈值（毫秒），超过则记录警告日志
         */
        private long slowSqlThreshold = 3000;
    }

    @Data
    public static class Monitor {
        /**
         * 是否启用SQL监控
         */
        private boolean enabled = true;

        /**
         * 监控访问路径
         */
        private String urlPattern = "/druid/*";

        /**
         * 是否允许重置统计数据
         */
        private boolean resetEnable = false;

        /**
         * Web统计过滤器配置
         */
        private WebStatFilter webStatFilter = new WebStatFilter();

        @Data
        public static class WebStatFilter {
            /**
             * 是否启用Web统计过滤器
             */
            private boolean enabled = true;

            /**
             * 过滤URL模式
             */
            private String urlPattern = "/*";

            /**
             * 排除的路径模式
             */
            private String exclusions = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*";
        }
    }
}