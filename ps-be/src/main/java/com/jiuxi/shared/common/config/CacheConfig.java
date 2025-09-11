package com.jiuxi.shared.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存配置属性
 * 统一管理Redis和本地缓存配置
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@Component("enhancedCacheConfig")
@ConfigurationProperties(prefix = "app.cache")
public class CacheConfig {

    /**
     * Redis配置
     */
    private Redis redis = new Redis();

    /**
     * 本地缓存配置
     */
    private Local local = new Local();

    /**
     * 分布式锁配置
     */
    private DistributedLock distributedLock = new DistributedLock();

    /**
     * 缓存策略配置
     */
    private Strategy strategy = new Strategy();

    @Data
    public static class Redis {
        /**
         * Redis服务器地址
         */
        private String host = "localhost";

        /**
         * Redis服务器端口
         */
        private int port = 6379;

        /**
         * Redis密码
         */
        private String password;

        /**
         * 数据库索引
         */
        private int database = 0;

        /**
         * 连接超时时间（毫秒）
         */
        private long timeout = 3000;

        /**
         * 连接池配置
         */
        private Pool pool = new Pool();

        /**
         * 集群配置
         */
        private Cluster cluster = new Cluster();

        /**
         * 哨兵配置
         */
        private Sentinel sentinel = new Sentinel();

        @Data
        public static class Pool {
            /**
             * 最大活跃连接数
             */
            private int maxActive = 8;

            /**
             * 最大等待时间（毫秒）
             */
            private long maxWait = -1;

            /**
             * 最大空闲连接数
             */
            private int maxIdle = 8;

            /**
             * 最小空闲连接数
             */
            private int minIdle = 0;
        }

        @Data
        public static class Cluster {
            /**
             * 是否启用集群模式
             */
            private boolean enabled = false;

            /**
             * 集群节点
             */
            private String[] nodes = {};

            /**
             * 最大重定向次数
             */
            private int maxRedirects = 3;
        }

        @Data
        public static class Sentinel {
            /**
             * 是否启用哨兵模式
             */
            private boolean enabled = false;

            /**
             * 主节点名称
             */
            private String master;

            /**
             * 哨兵节点
             */
            private String[] nodes = {};
        }
    }

    @Data
    public static class Local {
        /**
         * 缓存类型：ehcache, caffeine
         */
        private String type = "ehcache";

        /**
         * EhCache配置文件路径
         */
        private String ehcacheConfig = "classpath:config/cache/ehcache.xml";

        /**
         * Caffeine配置
         */
        private Caffeine caffeine = new Caffeine();

        @Data
        public static class Caffeine {
            /**
             * 最大缓存数量
             */
            private long maximumSize = 1000;

            /**
             * 写入后过期时间（秒）
             */
            private long expireAfterWrite = 3600;

            /**
             * 访问后过期时间（秒）
             */
            private long expireAfterAccess = 1800;

            /**
             * 初始容量
             */
            private int initialCapacity = 100;
        }
    }

    @Data
    public static class DistributedLock {
        /**
         * 是否启用分布式锁
         */
        private boolean enabled = true;

        /**
         * 锁等待时间（毫秒）
         */
        private long waitTime = 3000;

        /**
         * 锁租期时间（毫秒）
         */
        private long leaseTime = 30000;

        /**
         * 锁前缀
         */
        private String keyPrefix = "lock:";

        /**
         * 是否启用看门狗机制
         */
        private boolean watchdog = true;
    }

    @Data
    public static class Strategy {
        /**
         * 默认缓存TTL（秒）
         */
        private long defaultTtl = 3600;

        /**
         * 短期缓存TTL（秒）
         */
        private long shortTtl = 300;

        /**
         * 长期缓存TTL（秒）
         */
        private long longTtl = 86400;

        /**
         * 是否启用缓存统计
         */
        private boolean enableStatistics = true;

        /**
         * 缓存刷新策略：async, sync
         */
        private String refreshStrategy = "async";

        /**
         * 缓存预热配置
         */
        private Warmup warmup = new Warmup();

        @Data
        public static class Warmup {
            /**
             * 是否启用缓存预热
             */
            private boolean enabled = false;

            /**
             * 预热延迟时间（秒）
             */
            private int delay = 30;

            /**
             * 预热数据源
             */
            private String[] dataSources = {};
        }
    }
}