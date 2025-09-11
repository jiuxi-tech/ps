package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import java.util.List;
import java.util.Optional;

/**
 * 系统配置仓储接口
 * 定义系统配置聚合根的持久化操作，增强支持DDD模式
 * 
 * @author System Management (Enhanced by DDD Refactor)
 * @date 2025-09-06
 * @version 2.0 - Enhanced with DDD query methods
 */
public interface SystemConfigRepository {
    
    /**
     * 保存系统配置
     * @param systemConfig 系统配置聚合根
     * @return 保存后的系统配置
     */
    SystemConfig save(SystemConfig systemConfig);
    
    /**
     * 根据ID查找系统配置
     * @param configId 配置ID
     * @return 系统配置Optional
     */
    Optional<SystemConfig> findById(String configId);
    
    /**
     * 根据ID删除系统配置
     * @param configId 配置ID
     */
    void deleteById(String configId);
    
    /**
     * 根据配置键查找系统配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置Optional
     */
    Optional<SystemConfig> findByConfigKey(String configKey, String tenantId);
    
    // 新增的DDD领域查询方法
    
    /**
     * 根据配置键值对象查找系统配置
     * @param configKey 配置键值对象
     * @param tenantId 租户ID
     * @return 系统配置 Optional
     */
    Optional<SystemConfig> findByConfigKey(ConfigKey configKey, String tenantId);
    
    /**
     * 根据配置类型查找系统配置列表
     * @param configType 配置类型
     * @param tenantId 租户ID
     * @return 系统配置列表
     */
    List<SystemConfig> findByConfigType(ConfigType configType, String tenantId);
    
    /**
     * 根据配置状态查找系统配置列表
     * @param configStatus 配置状态
     * @param tenantId 租户ID
     * @return 系统配置列表
     */
    List<SystemConfig> findByConfigStatus(ConfigStatus configStatus, String tenantId);
    
    /**
     * 根据配置键前缀查找系统配置列表
     * @param keyPrefix 配置键前缀
     * @param tenantId 租户ID
     * @return 系统配置列表
     */
    List<SystemConfig> findByKeyPrefix(String keyPrefix, String tenantId);
    
    /**
     * 根据配置组查找系统配置列表
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 系统配置列表
     */
    List<SystemConfig> findByConfigGroup(String configGroup, String tenantId);
    
    /**
     * 查找系统级配置
     * @param tenantId 租户ID
     * @return 系统级配置列表
     */
    List<SystemConfig> findSystemLevelConfigs(String tenantId);
    
    /**
     * 查找激活状态的配置
     * @param tenantId 租户ID
     * @return 激活配置列表
     */
    List<SystemConfig> findActiveConfigs(String tenantId);
    
    /**
     * 批量保存系统配置
     * @param systemConfigs 系统配置列表
     * @return 保存后的系统配置列表
     */
    List<SystemConfig> batchSave(List<SystemConfig> systemConfigs);
    
    /**
     * 按配置类型和状态统计数量
     * @param configType 配置类型
     * @param configStatus 配置状态
     * @param tenantId 租户ID
     * @return 配置数量
     */
    long countByTypeAndStatus(ConfigType configType, ConfigStatus configStatus, String tenantId);
    
    /**
     * 检查配置键是否存在
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否存在
     */
    boolean existsByConfigKey(String configKey, String tenantId);
}