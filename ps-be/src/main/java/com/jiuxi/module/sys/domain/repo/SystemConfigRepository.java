package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import java.util.Optional;

/**
 * 系统配置仓储接口
 * 定义系统配置聚合根的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
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
}