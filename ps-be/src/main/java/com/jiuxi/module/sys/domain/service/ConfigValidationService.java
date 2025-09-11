package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import com.jiuxi.module.sys.domain.valueobject.ConfigValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 配置验证领域服务
 * 提供配置数据的验证逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ConfigValidationService {
    
    /**
     * 系统级敏感配置键模式
     */
    private static final Set<String> SENSITIVE_KEY_PATTERNS = new HashSet<>(Arrays.asList(
        "password", "secret", "key", "token", "credential", "auth"
    ));
    
    /**
     * 系统级只读配置键
     */
    private static final Set<String> READONLY_SYSTEM_KEYS = new HashSet<>(Arrays.asList(
        "system.version", "system.build", "system.startup.time"
    ));
    
    /**
     * URL格式验证正则
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$"
    );
    
    /**
     * 邮箱格式验证正则
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    /**
     * 验证配置完整性
     * @param configKey 配置键
     * @param configValue 配置值
     * @param configType 配置类型
     * @param status 配置状态
     * @return 验证结果
     */
    public ValidationResult validateConfiguration(ConfigKey configKey, ConfigValue configValue, 
            ConfigType configType, ConfigStatus status) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 验证配置键
        if (configKey == null) {
            errors.add("配置键不能为空");
        } else {
            validateConfigKey(configKey, configType, errors, warnings);
        }
        
        // 验证配置值
        if (configValue == null) {
            errors.add("配置值不能为空");
        } else {
            validateConfigValue(configKey, configValue, configType, errors, warnings);
        }
        
        // 验证配置类型和状态的组合
        if (configType != null && status != null) {
            validateTypeStatusCombination(configType, status, errors, warnings);
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * 验证配置键
     */
    private void validateConfigKey(ConfigKey configKey, ConfigType configType, 
            List<String> errors, List<String> warnings) {
        String keyValue = configKey.getValue();
        
        // 检查系统只读配置
        if (READONLY_SYSTEM_KEYS.contains(keyValue)) {
            warnings.add("配置键 [" + keyValue + "] 是系统只读配置，修改后可能需要重启");
        }
        
        // 检查敏感配置
        if (isSensitiveKey(keyValue)) {
            warnings.add("配置键 [" + keyValue + "] 包含敏感信息，请确保数据安全");
        }
        
        // 检查类型匹配
        if (configType != null) {
            if (configType.isSystemLevel() && !configKey.isSystemLevel()) {
                warnings.add("配置类型为系统级但配置键不是系统级格式");
            }
            if (!configType.isSystemLevel() && configKey.isSystemLevel()) {
                warnings.add("配置类型为非系统级但配置键是系统级格式");
            }
        }
    }
    
    /**
     * 验证配置值
     */
    private void validateConfigValue(ConfigKey configKey, ConfigValue configValue, 
            ConfigType configType, List<String> errors, List<String> warnings) {
        String keyValue = configKey != null ? configKey.getValue() : "";
        String value = configValue.getValue();
        String dataType = configValue.getDataType();
        
        // 数值型配置的范围检查
        if ("NUMBER".equals(dataType)) {
            validateNumberValue(keyValue, configValue, errors, warnings);
        }
        
        // URL配置的格式检查
        if (keyValue.contains("url") || keyValue.contains("endpoint")) {
            if (!URL_PATTERN.matcher(value).matches()) {
                warnings.add("配置值可能不是有效的URL格式: " + value);
            }
        }
        
        // 邮箱配置的格式检查
        if (keyValue.contains("email") || keyValue.contains("mail")) {
            if (!EMAIL_PATTERN.matcher(value).matches()) {
                warnings.add("配置值可能不是有效的邮箱格式: " + value);
            }
        }
        
        // 端口号配置的范围检查
        if (keyValue.contains("port") && "NUMBER".equals(dataType)) {
            Integer port = configValue.asInteger();
            if (port < 1 || port > 65535) {
                errors.add("端口号必须在1-65535范围内");
            }
        }
        
        // 缓存配置的检查
        if (configType == ConfigType.CACHE) {
            validateCacheConfig(keyValue, configValue, errors, warnings);
        }
        
        // 数据库配置的检查
        if (configType == ConfigType.DATABASE) {
            validateDatabaseConfig(keyValue, configValue, errors, warnings);
        }
    }
    
    /**
     * 验证数值型配置
     */
    private void validateNumberValue(String key, ConfigValue configValue, 
            List<String> errors, List<String> warnings) {
        try {
            Double value = configValue.asDouble();
            
            // 超时配置检查
            if (key.contains("timeout")) {
                if (value < 0) {
                    errors.add("超时时间不能为负数");
                }
                if (value > 300000) { // 5分钟
                    warnings.add("超时时间设置过长，可能影响用户体验");
                }
            }
            
            // 大小配置检查
            if (key.contains("size") || key.contains("limit")) {
                if (value < 0) {
                    errors.add("大小限制不能为负数");
                }
                if (value > 1000000) {
                    warnings.add("大小限制设置过大，可能影响系统性能");
                }
            }
            
        } catch (Exception e) {
            errors.add("数值型配置解析失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证缓存配置
     */
    private void validateCacheConfig(String key, ConfigValue configValue, 
            List<String> errors, List<String> warnings) {
        String value = configValue.getValue();
        
        if (key.contains("redis.host") && (value == null || value.trim().isEmpty())) {
            warnings.add("Redis主机地址为空，可能导致缓存功能不可用");
        }
        
        if (key.contains("expire") && "NUMBER".equals(configValue.getDataType())) {
            Integer expireTime = configValue.asInteger();
            if (expireTime <= 0) {
                errors.add("缓存过期时间必须大于0");
            }
            if (expireTime < 60) {
                warnings.add("缓存过期时间过短，可能增加数据库访问压力");
            }
        }
    }
    
    /**
     * 验证数据库配置
     */
    private void validateDatabaseConfig(String key, ConfigValue configValue, 
            List<String> errors, List<String> warnings) {
        String value = configValue.getValue();
        
        if (key.contains("url") && !value.startsWith("jdbc:")) {
            errors.add("数据库连接URL格式错误，必须以'jdbc:'开头");
        }
        
        if (key.contains("pool.size") && "NUMBER".equals(configValue.getDataType())) {
            Integer poolSize = configValue.asInteger();
            if (poolSize <= 0) {
                errors.add("连接池大小必须大于0");
            }
            if (poolSize > 200) {
                warnings.add("连接池大小过大，可能消耗过多资源");
            }
        }
    }
    
    /**
     * 验证类型状态组合
     */
    private void validateTypeStatusCombination(ConfigType configType, ConfigStatus status,
            List<String> errors, List<String> warnings) {
        // 系统级配置通常不应该是草稿状态
        if (configType.isSystemLevel() && status == ConfigStatus.DRAFT) {
            warnings.add("系统级配置通常不应处于草稿状态");
        }
        
        // 安全配置必须激活才能生效
        if (configType == ConfigType.SECURITY && status != ConfigStatus.ACTIVE) {
            warnings.add("安全配置只有在激活状态下才能生效");
        }
    }
    
    /**
     * 检查是否是敏感配置键
     */
    private boolean isSensitiveKey(String key) {
        String lowerKey = key.toLowerCase();
        return SENSITIVE_KEY_PATTERNS.stream().anyMatch(lowerKey::contains);
    }
    
    /**
     * 验证批量配置的一致性
     * @param configs 配置列表
     * @return 验证结果
     */
    public ValidationResult validateBatchConfigurations(List<ConfigValidationItem> configs) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        Set<String> duplicateKeys = new HashSet<>();
        Set<String> seenKeys = new HashSet<>();
        
        for (ConfigValidationItem config : configs) {
            // 检查重复键
            String keyValue = config.getConfigKey().getValue();
            if (seenKeys.contains(keyValue)) {
                duplicateKeys.add(keyValue);
            } else {
                seenKeys.add(keyValue);
            }
            
            // 验证单个配置
            ValidationResult singleResult = validateConfiguration(
                config.getConfigKey(), 
                config.getConfigValue(),
                config.getConfigType(), 
                config.getStatus()
            );
            
            errors.addAll(singleResult.getErrors());
            warnings.addAll(singleResult.getWarnings());
        }
        
        // 添加重复键错误
        for (String duplicateKey : duplicateKeys) {
            errors.add("配置键重复: " + duplicateKey);
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * 配置验证项
     */
    public static class ConfigValidationItem {
        private final ConfigKey configKey;
        private final ConfigValue configValue;
        private final ConfigType configType;
        private final ConfigStatus status;
        
        public ConfigValidationItem(ConfigKey configKey, ConfigValue configValue, 
                ConfigType configType, ConfigStatus status) {
            this.configKey = configKey;
            this.configValue = configValue;
            this.configType = configType;
            this.status = status;
        }
        
        public ConfigKey getConfigKey() { return configKey; }
        public ConfigValue getConfigValue() { return configValue; }
        public ConfigType getConfigType() { return configType; }
        public ConfigStatus getStatus() { return status; }
    }
    
    /**
     * 验证结果
     */
    public static class ValidationResult {
        private final List<String> errors;
        private final List<String> warnings;
        
        public ValidationResult(List<String> errors, List<String> warnings) {
            this.errors = errors;
            this.warnings = warnings;
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public List<String> getWarnings() {
            return warnings;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!errors.isEmpty()) {
                sb.append("错误: ").append(String.join(", ", errors));
            }
            if (!warnings.isEmpty()) {
                if (sb.length() > 0) sb.append("; ");
                sb.append("警告: ").append(String.join(", ", warnings));
            }
            return sb.toString();
        }
    }
}