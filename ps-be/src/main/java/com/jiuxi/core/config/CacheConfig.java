package com.jiuxi.core.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存配置类
 * 配置RedisCacheManager以支持Spring Cache注解
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofHours(1)); // 默认1小时过期

        // 特定缓存名称的配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 菜单服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpMenuService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 参数配置服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpParameterConfigService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 字典服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpDictionaryService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 人员基础信息服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpPersonBasicinfoService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 企业基础信息服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpEntBasicinfoService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 部门基础信息服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpDeptBasicinfoService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 城市服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpCityService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 数据权限服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpDataPermissionsService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));
        
        // 菜单服务缓存 - 1天过期
        cacheConfigurations.put("platform.{TpMenuService}$[86400]", 
                defaultConfig.entryTtl(Duration.ofDays(1)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}