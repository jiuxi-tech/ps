package com.jiuxi.shared.common.config.validation;

import com.jiuxi.shared.common.config.*;
import com.jiuxi.shared.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 配置验证器
 * 提供各种配置属性的验证功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@Component
public class ConfigurationValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^https?://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$"
    );

    /**
     * 验证应用配置
     */
    public void validateApplicationConfig(ApplicationConfig config) {
        if (config == null) {
            throw new SystemException("应用配置不能为空");
        }

        validateApplicationInfo(config.getInfo());
        validateUploadConfig(config.getUpload());
        validateI18nConfig(config.getI18n());
        validateMonitorConfig(config.getMonitor());
    }

    /**
     * 验证数据库配置
     */
    public void validateDatabaseConfig(DatabaseConfig config) {
        if (config == null) {
            throw new SystemException("数据库配置不能为空");
        }

        validateDatasourceConfig(config.getDatasource());
        validatePoolConfig(config.getPool());
        validateMybatisConfig(config.getMybatis());
    }

    /**
     * 验证缓存配置
     */
    public void validateCacheConfig(CacheConfig config) {
        if (config == null) {
            throw new SystemException("缓存配置不能为空");
        }

        validateRedisConfig(config.getRedis());
        validateLocalCacheConfig(config.getLocal());
        validateCacheStrategyConfig(config.getStrategy());
    }

    /**
     * 验证安全配置
     */
    public void validateSecurityConfig(SecurityConfig config) {
        if (config == null) {
            throw new SystemException("安全配置不能为空");
        }

        validateBasicSecurityConfig(config.getBasic());
        validateKeycloakConfig(config.getKeycloak());
        validateJwtConfig(config.getJwt());
        validateFileServiceConfig(config.getFileService());
    }

    private void validateApplicationInfo(ApplicationConfig.Info info) {
        if (!StringUtils.hasText(info.getName())) {
            throw new SystemException("应用名称不能为空");
        }
        
        if (!StringUtils.hasText(info.getVersion())) {
            throw new SystemException("应用版本不能为空");
        }
        
        if (StringUtils.hasText(info.getContact()) && !isValidEmail(info.getContact())) {
            throw new SystemException("联系邮箱格式无效: " + info.getContact());
        }
    }

    private void validateUploadConfig(ApplicationConfig.Upload upload) {
        if (!StringUtils.hasText(upload.getMaxFileSize())) {
            throw new SystemException("最大文件大小配置不能为空");
        }
        
        if (!StringUtils.hasText(upload.getMaxRequestSize())) {
            throw new SystemException("最大请求大小配置不能为空");
        }
        
        if (upload.getAllowedTypes() == null || upload.getAllowedTypes().length == 0) {
            throw new SystemException("允许的文件类型不能为空");
        }
    }

    private void validateI18nConfig(ApplicationConfig.I18n i18n) {
        if (i18n.getBasename() == null || i18n.getBasename().length == 0) {
            throw new SystemException("国际化资源文件基础名不能为空");
        }
        
        if (i18n.getCacheDuration() < 0) {
            throw new SystemException("国际化缓存时长不能小于0");
        }
    }

    private void validateMonitorConfig(ApplicationConfig.Monitor monitor) {
        if (monitor.isEnabled()) {
            if (!StringUtils.hasText(monitor.getServerUrl())) {
                throw new SystemException("监控服务器URL不能为空");
            }
            
            if (!isValidUrl(monitor.getServerUrl())) {
                throw new SystemException("监控服务器URL格式无效: " + monitor.getServerUrl());
            }
            
            if (monitor.getConnectionTimeout() <= 0) {
                throw new SystemException("连接超时时间必须大于0");
            }
            
            if (monitor.getReadTimeout() <= 0) {
                throw new SystemException("读取超时时间必须大于0");
            }
        }
    }

    private void validateDatasourceConfig(DatabaseConfig.Datasource datasource) {
        if (!StringUtils.hasText(datasource.getDriverClassName())) {
            throw new SystemException("数据库驱动类名不能为空");
        }
        
        if (datasource.getConnectionTimeout() <= 0) {
            throw new SystemException("数据库连接超时时间必须大于0");
        }
        
        if (datasource.getMaxLifetime() <= 0) {
            throw new SystemException("数据库连接最大生存时间必须大于0");
        }
    }

    private void validatePoolConfig(DatabaseConfig.Pool pool) {
        if (pool.getInitialSize() < 0) {
            throw new SystemException("数据库连接池初始大小不能小于0");
        }
        
        if (pool.getMinIdle() < 0) {
            throw new SystemException("数据库连接池最小空闲连接数不能小于0");
        }
        
        if (pool.getMaxActive() <= 0) {
            throw new SystemException("数据库连接池最大活跃连接数必须大于0");
        }
        
        if (pool.getMinIdle() > pool.getMaxActive()) {
            throw new SystemException("数据库连接池最小空闲连接数不能大于最大活跃连接数");
        }
        
        if (pool.getMaxWait() < 0) {
            throw new SystemException("数据库连接池最大等待时间不能小于0");
        }
    }

    private void validateMybatisConfig(DatabaseConfig.Mybatis mybatis) {
        if (mybatis.getDatacenterId() < 0) {
            throw new SystemException("MyBatis数据中心ID不能小于0");
        }
        
        if (mybatis.getMachineId() < 0) {
            throw new SystemException("MyBatis机器ID不能小于0");
        }
        
        if (mybatis.getPageLimit() <= 0) {
            throw new SystemException("MyBatis分页大小限制必须大于0");
        }
        
        if (mybatis.getSlowSqlThreshold() <= 0) {
            throw new SystemException("MyBatis慢SQL阈值必须大于0");
        }
    }

    private void validateRedisConfig(CacheConfig.Redis redis) {
        if (!StringUtils.hasText(redis.getHost())) {
            throw new SystemException("Redis服务器地址不能为空");
        }
        
        if (redis.getPort() <= 0 || redis.getPort() > 65535) {
            throw new SystemException("Redis端口号必须在1-65535范围内");
        }
        
        if (redis.getDatabase() < 0) {
            throw new SystemException("Redis数据库索引不能小于0");
        }
        
        if (redis.getTimeout() <= 0) {
            throw new SystemException("Redis连接超时时间必须大于0");
        }

        validateRedisPoolConfig(redis.getPool());
    }

    private void validateRedisPoolConfig(CacheConfig.Redis.Pool pool) {
        if (pool.getMaxActive() <= 0) {
            throw new SystemException("Redis连接池最大活跃连接数必须大于0");
        }
        
        if (pool.getMaxIdle() < 0) {
            throw new SystemException("Redis连接池最大空闲连接数不能小于0");
        }
        
        if (pool.getMinIdle() < 0) {
            throw new SystemException("Redis连接池最小空闲连接数不能小于0");
        }
        
        if (pool.getMinIdle() > pool.getMaxIdle()) {
            throw new SystemException("Redis连接池最小空闲连接数不能大于最大空闲连接数");
        }
    }

    private void validateLocalCacheConfig(CacheConfig.Local local) {
        if (!StringUtils.hasText(local.getType())) {
            throw new SystemException("本地缓存类型不能为空");
        }
        
        if ("caffeine".equals(local.getType())) {
            CacheConfig.Local.Caffeine caffeine = local.getCaffeine();
            if (caffeine.getMaximumSize() <= 0) {
                throw new SystemException("Caffeine缓存最大数量必须大于0");
            }
            
            if (caffeine.getExpireAfterWrite() <= 0) {
                throw new SystemException("Caffeine缓存写入后过期时间必须大于0");
            }
            
            if (caffeine.getInitialCapacity() <= 0) {
                throw new SystemException("Caffeine缓存初始容量必须大于0");
            }
        }
    }

    private void validateCacheStrategyConfig(CacheConfig.Strategy strategy) {
        if (strategy.getDefaultTtl() <= 0) {
            throw new SystemException("默认缓存TTL必须大于0");
        }
        
        if (strategy.getShortTtl() <= 0) {
            throw new SystemException("短期缓存TTL必须大于0");
        }
        
        if (strategy.getLongTtl() <= 0) {
            throw new SystemException("长期缓存TTL必须大于0");
        }
        
        if (!StringUtils.hasText(strategy.getRefreshStrategy())) {
            throw new SystemException("缓存刷新策略不能为空");
        }
    }

    private void validateBasicSecurityConfig(SecurityConfig.Basic basic) {
        if (basic.getSessionTimeout() <= 0) {
            throw new SystemException("会话超时时间必须大于0");
        }
    }

    private void validateKeycloakConfig(SecurityConfig.Keycloak keycloak) {
        if (!StringUtils.hasText(keycloak.getServerUrl())) {
            throw new SystemException("Keycloak服务器地址不能为空");
        }
        
        if (!isValidUrl(keycloak.getServerUrl())) {
            throw new SystemException("Keycloak服务器地址格式无效: " + keycloak.getServerUrl());
        }
        
        if (!StringUtils.hasText(keycloak.getRealm())) {
            throw new SystemException("Keycloak Realm名称不能为空");
        }

        SecurityConfig.Keycloak.Sso sso = keycloak.getSso();
        if (sso.isEnabled()) {
            if (!StringUtils.hasText(sso.getClientId())) {
                throw new SystemException("Keycloak客户端ID不能为空");
            }
            
            validateRedirectUrls(sso.getRedirect());
        }

        validateKeycloakAdminConfig(keycloak.getAdmin());
    }

    private void validateRedirectUrls(SecurityConfig.Keycloak.Sso.Redirect redirect) {
        if (!StringUtils.hasText(redirect.getSuccessUrl()) || !isValidUrl(redirect.getSuccessUrl())) {
            throw new SystemException("Keycloak登录成功重定向地址无效: " + redirect.getSuccessUrl());
        }
        
        if (!StringUtils.hasText(redirect.getErrorUrl()) || !isValidUrl(redirect.getErrorUrl())) {
            throw new SystemException("Keycloak登录失败重定向地址无效: " + redirect.getErrorUrl());
        }
    }

    private void validateKeycloakAdminConfig(SecurityConfig.Keycloak.Admin admin) {
        if (!StringUtils.hasText(admin.getClientId())) {
            throw new SystemException("Keycloak管理员客户端ID不能为空");
        }
        
        if (!StringUtils.hasText(admin.getUsername())) {
            throw new SystemException("Keycloak管理员用户名不能为空");
        }
        
        if (!StringUtils.hasText(admin.getPassword())) {
            throw new SystemException("Keycloak管理员密码不能为空");
        }
        
        if (admin.getConnectionTimeout() <= 0) {
            throw new SystemException("Keycloak管理员连接超时时间必须大于0");
        }
        
        if (admin.getReadTimeout() <= 0) {
            throw new SystemException("Keycloak管理员读取超时时间必须大于0");
        }
    }

    private void validateJwtConfig(SecurityConfig.Jwt jwt) {
        if (!StringUtils.hasText(jwt.getSecret())) {
            throw new SystemException("JWT密钥不能为空");
        }
        
        if (jwt.getSecret().length() < 32) {
            throw new SystemException("JWT密钥长度必须至少32位");
        }
        
        if (jwt.getExpiration() <= 0) {
            throw new SystemException("JWT过期时间必须大于0");
        }
        
        if (jwt.getRefreshExpiration() <= 0) {
            throw new SystemException("JWT刷新过期时间必须大于0");
        }
        
        if (jwt.getCacheTtl() <= 0) {
            throw new SystemException("JWT缓存TTL必须大于0");
        }
    }

    private void validateFileServiceConfig(SecurityConfig.FileService fileService) {
        if (!StringUtils.hasText(fileService.getMode())) {
            throw new SystemException("文件服务模式不能为空");
        }
        
        if ("local".equals(fileService.getMode())) {
            validateLocalFileConfig(fileService.getLocal());
        } else if ("remote".equals(fileService.getMode())) {
            validateRemoteFileConfig(fileService.getRemote());
        } else {
            throw new SystemException("不支持的文件服务模式: " + fileService.getMode());
        }
    }

    private void validateLocalFileConfig(SecurityConfig.FileService.Local local) {
        if (!StringUtils.hasText(local.getBaseDir())) {
            throw new SystemException("本地文件服务基础目录不能为空");
        }
        
        if (!StringUtils.hasText(local.getUrlPrefix())) {
            throw new SystemException("本地文件服务URL前缀不能为空");
        }
        
        List<String> allowedExtensions = local.getAllowedExtensions();
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            throw new SystemException("本地文件服务允许的文件扩展名不能为空");
        }
    }

    private void validateRemoteFileConfig(SecurityConfig.FileService.Remote remote) {
        if (!StringUtils.hasText(remote.getServerUrl())) {
            throw new SystemException("远程文件服务器地址不能为空");
        }
        
        if (!isValidUrl(remote.getServerUrl())) {
            throw new SystemException("远程文件服务器地址格式无效: " + remote.getServerUrl());
        }
        
        if (!StringUtils.hasText(remote.getBucketName())) {
            throw new SystemException("远程文件服务存储桶名称不能为空");
        }
    }

    private boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return false;
        }
        
        try {
            new URL(url);
            return URL_PATTERN.matcher(url).matches();
        } catch (MalformedURLException e) {
            return false;
        }
    }
}