package com.jiuxi.shared.common.config;

import com.jiuxi.shared.common.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * 配置管理器
 * 统一管理和验证所有配置属性
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@Configuration
public class ConfigurationManager {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("enhancedApplicationConfig")
    private ApplicationConfig applicationConfig;

    @Autowired
    @Qualifier("enhancedDatabaseConfig")
    private DatabaseConfig databaseConfig;

    @Autowired
    @Qualifier("enhancedCacheConfig")
    private CacheConfig cacheConfig;

    @Autowired
    @Qualifier("enhancedSecurityConfig")
    private SecurityConfig securityConfig;

    /**
     * 初始化后验证配置
     */
    @PostConstruct
    public void validateConfigurations() {
        log.info("开始验证系统配置...");
        
        validateBasicConfiguration();
        validateDatabaseConfiguration();
        validateCacheConfiguration();
        validateSecurityConfiguration();
        
        log.info("系统配置验证完成");
        printConfigurationSummary();
    }

    /**
     * 验证基础配置
     */
    private void validateBasicConfiguration() {
        ApplicationConfig.Info info = applicationConfig.getInfo();
        if (info.getName() == null || info.getName().trim().isEmpty()) {
            throw new IllegalStateException("应用名称不能为空");
        }
        
        if (info.getVersion() == null || info.getVersion().trim().isEmpty()) {
            throw new IllegalStateException("应用版本不能为空");
        }

        log.debug("基础配置验证通过: {} v{}", info.getName(), info.getVersion());
    }

    /**
     * 验证数据库配置
     */
    private void validateDatabaseConfiguration() {
        DatabaseConfig.Datasource datasource = databaseConfig.getDatasource();
        
        if (datasource.getUrl() == null || datasource.getUrl().trim().isEmpty()) {
            log.warn("数据库URL未配置，请确保通过Spring配置文件正确设置");
        }
        
        if (datasource.getUsername() == null || datasource.getUsername().trim().isEmpty()) {
            log.warn("数据库用户名未配置，请确保通过Spring配置文件正确设置");
        }

        DatabaseConfig.Pool pool = databaseConfig.getPool();
        if (pool.getMaxActive() <= 0) {
            throw new IllegalStateException("数据库连接池最大连接数必须大于0");
        }
        
        if (pool.getMinIdle() < 0) {
            throw new IllegalStateException("数据库连接池最小空闲连接数不能小于0");
        }
        
        if (pool.getMinIdle() > pool.getMaxActive()) {
            throw new IllegalStateException("数据库连接池最小空闲连接数不能大于最大连接数");
        }

        log.debug("数据库配置验证通过: maxActive={}, minIdle={}", 
                 pool.getMaxActive(), pool.getMinIdle());
    }

    /**
     * 验证缓存配置
     */
    private void validateCacheConfiguration() {
        CacheConfig.Redis redis = cacheConfig.getRedis();
        
        if (redis.getHost() == null || redis.getHost().trim().isEmpty()) {
            throw new IllegalStateException("Redis服务器地址不能为空");
        }
        
        if (redis.getPort() <= 0 || redis.getPort() > 65535) {
            throw new IllegalStateException("Redis端口号必须在1-65535范围内");
        }

        CacheConfig.Redis.Pool pool = redis.getPool();
        if (pool.getMaxActive() <= 0) {
            throw new IllegalStateException("Redis连接池最大连接数必须大于0");
        }

        log.debug("缓存配置验证通过: Redis {}:{}", redis.getHost(), redis.getPort());
    }

    /**
     * 验证安全配置
     */
    private void validateSecurityConfiguration() {
        SecurityConfig.Keycloak keycloak = securityConfig.getKeycloak();
        
        if (keycloak.getServerUrl() == null || keycloak.getServerUrl().trim().isEmpty()) {
            throw new IllegalStateException("Keycloak服务器地址不能为空");
        }
        
        if (keycloak.getRealm() == null || keycloak.getRealm().trim().isEmpty()) {
            throw new IllegalStateException("Keycloak Realm名称不能为空");
        }

        SecurityConfig.Keycloak.Sso sso = keycloak.getSso();
        if (sso.isEnabled()) {
            if (sso.getClientId() == null || sso.getClientId().trim().isEmpty()) {
                throw new IllegalStateException("Keycloak客户端ID不能为空");
            }
        }

        SecurityConfig.Jwt jwt = securityConfig.getJwt();
        if (jwt.getSecret() == null || jwt.getSecret().length() < 32) {
            throw new IllegalStateException("JWT密钥长度至少需要32位");
        }

        log.debug("安全配置验证通过: Keycloak {}, SSO启用: {}", 
                 keycloak.getServerUrl(), sso.isEnabled());
    }

    /**
     * 打印配置摘要
     */
    private void printConfigurationSummary() {
        log.info("================ 系统配置摘要 ================");
        log.info("应用名称: {}", applicationConfig.getInfo().getName());
        log.info("应用版本: {}", applicationConfig.getInfo().getVersion());
        log.info("运行环境: {}", Arrays.toString(environment.getActiveProfiles()));
        log.info("服务器端口: {}", environment.getProperty("server.port", "8080"));
        log.info("数据库最大连接数: {}", databaseConfig.getPool().getMaxActive());
        log.info("Redis服务器: {}:{}", cacheConfig.getRedis().getHost(), cacheConfig.getRedis().getPort());
        log.info("Keycloak服务器: {}", securityConfig.getKeycloak().getServerUrl());
        log.info("SSO功能状态: {}", securityConfig.getKeycloak().getSso().isEnabled() ? "启用" : "禁用");
        log.info("监控功能状态: {}", applicationConfig.getMonitor().isEnabled() ? "启用" : "禁用");
        log.info("============================================");
    }

    /**
     * 获取当前环境名称
     */
    public String getCurrentEnvironment() {
        String[] profiles = environment.getActiveProfiles();
        return profiles.length > 0 ? profiles[0] : SystemConstants.Environment.DEV;
    }

    /**
     * 检查是否为开发环境
     */
    public boolean isDevelopmentEnvironment() {
        return SystemConstants.Environment.DEV.equals(getCurrentEnvironment());
    }

    /**
     * 检查是否为测试环境
     */
    public boolean isTestEnvironment() {
        return SystemConstants.Environment.TEST.equals(getCurrentEnvironment());
    }

    /**
     * 检查是否为生产环境
     */
    public boolean isProductionEnvironment() {
        return SystemConstants.Environment.PROD.equals(getCurrentEnvironment());
    }

    /**
     * 获取配置属性值
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置属性值，如果不存在则返回默认值
     */
    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 获取指定类型的配置属性值
     */
    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    /**
     * 获取指定类型的配置属性值，如果不存在则返回默认值
     */
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    // Getter methods for accessing configuration objects
    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public SecurityConfig getSecurityConfig() {
        return securityConfig;
    }
}