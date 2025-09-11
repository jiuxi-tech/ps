package com.jiuxi.module.sys.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 配置键值对象
 * 封装配置键的业务规则和验证逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ConfigKey {
    
    /**
     * 配置键格式正则表达式
     * 支持分级配置键，如：app.server.port, system.cache.redis.host
     */
    private static final Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._-]*[a-zA-Z0-9]$");
    
    /**
     * 配置键最大长度
     */
    private static final int MAX_LENGTH = 100;
    
    /**
     * 配置键最小长度
     */
    private static final int MIN_LENGTH = 3;
    
    private final String value;
    
    /**
     * 构造函数
     * @param value 配置键值
     */
    public ConfigKey(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("配置键不能为空");
        }
        
        String trimmedValue = value.trim();
        
        if (trimmedValue.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("配置键长度不能少于" + MIN_LENGTH + "个字符");
        }
        
        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("配置键长度不能超过" + MAX_LENGTH + "个字符");
        }
        
        if (!KEY_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("配置键格式不正确，支持字母、数字、点、下划线和连字符，必须以字母开头");
        }
        
        this.value = trimmedValue.toLowerCase();
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 获取配置键的层级
     * @return 层级数组
     */
    public String[] getHierarchy() {
        return value.split("\\.");
    }
    
    /**
     * 获取配置键的根级别
     * @return 根级别
     */
    public String getRootLevel() {
        String[] hierarchy = getHierarchy();
        return hierarchy.length > 0 ? hierarchy[0] : value;
    }
    
    /**
     * 检查是否是指定前缀的配置
     * @param prefix 前缀
     * @return 是否匹配前缀
     */
    public boolean hasPrefix(String prefix) {
        return value.startsWith(prefix.toLowerCase() + ".");
    }
    
    /**
     * 检查是否是系统级配置键
     * @return 是否是系统级配置键
     */
    public boolean isSystemLevel() {
        return hasPrefix("system") || hasPrefix("app") || hasPrefix("server");
    }
    
    /**
     * 检查是否是业务级配置键
     * @return 是否是业务级配置键
     */
    public boolean isBusinessLevel() {
        return hasPrefix("business") || hasPrefix("feature") || hasPrefix("module");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigKey configKey = (ConfigKey) o;
        return Objects.equals(value, configKey.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}