package com.jiuxi.module.sys.app.query.handler;

import com.jiuxi.module.sys.app.query.dto.SystemConfigResponse;
import com.jiuxi.module.sys.app.assembler.SystemConfigAssembler;
import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.domain.service.SystemConfigDomainService;
import com.jiuxi.module.sys.domain.service.SystemConfigCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统配置查询处理器
 * 负责处理系统配置相关的查询操作（R）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class SystemConfigQueryHandler {
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
    private SystemConfigDomainService systemConfigDomainService;
    
    @Autowired
    private SystemConfigCacheService systemConfigCacheService;
    
    @Autowired
    private SystemConfigAssembler systemConfigAssembler;
    
    /**
     * 根据ID查询系统配置
     * @param configId 配置ID
     * @return 系统配置响应DTO
     */
    public SystemConfigResponse handleGetByIdQuery(String configId) {
        try {
            Optional<SystemConfig> systemConfigOpt = systemConfigRepository.findById(configId);
            if (!systemConfigOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(systemConfigOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("查询系统配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据配置键查询系统配置（带缓存）
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置响应DTO
     */
    public SystemConfigResponse handleGetByKeyQuery(String configKey, String tenantId) {
        try {
            Optional<SystemConfig> systemConfigOpt = systemConfigDomainService.getConfig(configKey, tenantId);
            if (!systemConfigOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(systemConfigOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("根据配置键查询失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 查询类型安全的配置值
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @param valueType 值类型
     * @return 类型化的配置值
     */
    public <T> T handleGetTypedValueQuery(String configKey, String tenantId, Class<T> valueType) {
        try {
            return systemConfigDomainService.getTypedConfigValue(configKey, tenantId, valueType).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("查询类型化配置值失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据配置类型查询配置列表
     * @param configTypeCode 配置类型代码
     * @param tenantId 租户ID
     * @return 配置响应DTO列表
     */
    public List<SystemConfigResponse> handleGetByTypeQuery(String configTypeCode, String tenantId) {
        try {
            ConfigType configType = ConfigType.fromCode(configTypeCode);
            List<SystemConfig> configs = systemConfigDomainService.getConfigsByType(configType, tenantId);
            return configs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据配置组查询配置列表
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 配置响应DTO列表
     */
    public List<SystemConfigResponse> handleGetByGroupQuery(String configGroup, String tenantId) {
        try {
            List<SystemConfig> configs = systemConfigDomainService.getConfigsByGroup(configGroup, tenantId);
            return configs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询系统级配置
     * @param tenantId 租户ID
     * @return 系统级配置列表
     */
    public List<SystemConfigResponse> handleGetSystemLevelQuery(String tenantId) {
        try {
            List<SystemConfig> configs = systemConfigDomainService.getSystemLevelConfigs(tenantId);
            return configs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询激活状态的配置
     * @param tenantId 租户ID
     * @return 激活配置列表
     */
    public List<SystemConfigResponse> handleGetActiveConfigsQuery(String tenantId) {
        try {
            List<SystemConfig> configs = systemConfigDomainService.getActiveConfigs(tenantId);
            return configs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询缓存统计信息
     * @param tenantId 租户ID
     * @return 缓存统计信息
     */
    public SystemConfigCacheService.CacheStats handleGetCacheStatsQuery(String tenantId) {
        try {
            return systemConfigCacheService.getCacheStats(tenantId);
        } catch (Exception e) {
            throw new RuntimeException("查询缓存统计失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 简单配置值查询（兼容性接口）
     * @param configKey 配置键
     * @return 配置值字符串
     */
    public String handleGetValueQuery(String configKey) {
        try {
            // 这里可以添加默认租户ID逻辑
            String defaultTenantId = "default"; // 根据实际需求调整
            Optional<SystemConfig> configOpt = systemConfigDomainService.getConfig(configKey, defaultTenantId);
            return configOpt.map(SystemConfig::getConfigValue).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 带默认值的配置值查询（兼容性接口）
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值字符串
     */
    public String handleGetValueWithDefaultQuery(String configKey, String defaultValue) {
        try {
            String value = handleGetValueQuery(configKey);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * 检查配置键是否存在
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否存在
     */
    public boolean handleExistsByKeyQuery(String configKey, String tenantId) {
        try {
            return systemConfigRepository.existsByConfigKey(configKey, tenantId);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 转换SystemConfig实体为响应DTO
     * @param systemConfig 系统配置实体
     * @return 系统配置响应DTO
     */
    private SystemConfigResponse convertToResponse(SystemConfig systemConfig) {
        return systemConfigAssembler.toResponse(systemConfig);
    }
}