package com.jiuxi.module.sys.domain.repository;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.entity.ConfigDataType;

import java.util.List;
import java.util.Optional;

/**
 * @InterfaceName: SystemConfigRepository
 * @Description: 系统配置仓储接口
 * 整合了原system模块的SystemConfigRepository功能到sys模块
 * @Author: DDD重构 (Merged from system module)
 * @Date: 2025-09-12
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface SystemConfigRepository {
    
    /**
     * 保存系统配置
     */
    SystemConfig save(SystemConfig systemConfig);
    
    /**
     * 根据ID查找系统配置
     */
    Optional<SystemConfig> findById(String configId);
    
    /**
     * 根据配置键查找系统配置
     */
    Optional<SystemConfig> findByConfigKey(String configKey);
    
    /**
     * 根据配置类型查找系统配置列表
     */
    List<SystemConfig> findByConfigType(ConfigType configType);
    
    /**
     * 根据状态查找系统配置列表
     */
    List<SystemConfig> findByStatus(ConfigStatus status);
    
    /**
     * 查找所有启用的系统配置
     */
    List<SystemConfig> findAllEnabled();
    
    /**
     * 查找所有系统配置
     */
    List<SystemConfig> findAll();
    
    /**
     * 根据数据类型查找系统配置列表
     */
    List<SystemConfig> findByDataType(ConfigDataType dataType);
    
    /**
     * 根据配置名称模糊查询
     */
    List<SystemConfig> findByConfigNameContaining(String configName);
    
    /**
     * 根据配置类型和状态查找配置
     */
    List<SystemConfig> findByConfigTypeAndStatus(ConfigType configType, ConfigStatus status);
    
    /**
     * 查找需要加密存储的配置
     */
    List<SystemConfig> findByEncryptedStorage(boolean encryptedStorage);
    
    /**
     * 检查配置键是否存在
     */
    boolean existsByConfigKey(String configKey);
    
    /**
     * 检查配置键是否存在（排除指定ID）
     */
    boolean existsByConfigKeyAndIdNot(String configKey, String configId);
    
    /**
     * 根据ID删除系统配置
     */
    void deleteById(String configId);
    
    /**
     * 根据配置键删除系统配置
     */
    void deleteByConfigKey(String configKey);
    
    /**
     * 批量删除系统配置
     */
    void deleteByIds(List<String> configIds);
    
    /**
     * 统计系统配置总数
     */
    long count();
    
    /**
     * 根据配置类型统计配置数量
     */
    long countByConfigType(ConfigType configType);
    
    /**
     * 根据状态统计配置数量
     */
    long countByStatus(ConfigStatus status);
    
    /**
     * 分页查询系统配置
     */
    List<SystemConfig> findWithPagination(int page, int size);
    
    /**
     * 根据条件分页查询系统配置
     */
    List<SystemConfig> findByConditionsWithPagination(ConfigType configType, ConfigStatus status, String keyword, int page, int size);
    
    /**
     * 批量更新配置状态
     */
    void updateStatusByIds(List<String> configIds, ConfigStatus status);
    
    /**
     * 根据配置类型批量更新状态
     */
    void updateStatusByConfigType(ConfigType configType, ConfigStatus status);
}