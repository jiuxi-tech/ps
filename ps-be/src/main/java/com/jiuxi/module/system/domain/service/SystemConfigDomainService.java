package com.jiuxi.module.system.domain.service;

import com.jiuxi.module.system.domain.entity.SystemConfig;
import com.jiuxi.module.system.domain.entity.ConfigType;
import com.jiuxi.module.system.domain.entity.ConfigDataType;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName: SystemConfigDomainService
 * @Description: 系统配置领域服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface SystemConfigDomainService {
    
    /**
     * 创建系统配置
     */
    SystemConfig createSystemConfig(String configKey, String configName, String configValue, 
                                  ConfigType configType, ConfigDataType dataType, String description);
    
    /**
     * 验证配置键唯一性
     */
    void validateConfigKeyUniqueness(String configKey, String excludeId);
    
    /**
     * 验证配置值格式
     */
    void validateConfigValue(String configValue, ConfigDataType dataType);
    
    /**
     * 验证配置是否可以删除
     */
    void validateConfigDeletable(String configId);
    
    /**
     * 批量启用配置
     */
    void enableConfigs(List<String> configIds);
    
    /**
     * 批量禁用配置
     */
    void disableConfigs(List<String> configIds);
    
    /**
     * 更新配置值
     */
    void updateConfigValue(String configId, String newValue);
    
    /**
     * 批量更新配置值
     */
    void updateConfigValues(Map<String, String> configValues);
    
    /**
     * 加密配置值
     */
    String encryptConfigValue(String configValue);
    
    /**
     * 解密配置值
     */
    String decryptConfigValue(String encryptedValue);
    
    /**
     * 设置配置为加密存储
     */
    void setConfigEncrypted(String configId, boolean encrypted);
    
    /**
     * 获取配置的实际值（处理加密）
     */
    String getActualConfigValue(String configKey);
    
    /**
     * 获取配置的类型化值
     */
    <T> T getTypedConfigValue(String configKey, Class<T> targetType);
    
    /**
     * 获取配置的默认值
     */
    String getConfigDefaultValue(String configKey);
    
    /**
     * 重置配置为默认值
     */
    void resetConfigToDefault(String configId);
    
    /**
     * 批量重置配置为默认值
     */
    void resetConfigsToDefault(List<String> configIds);
    
    /**
     * 导出配置
     */
    Map<String, Object> exportConfigs(List<String> configIds);
    
    /**
     * 导入配置
     */
    void importConfigs(Map<String, Object> configData, boolean overwrite);
    
    /**
     * 备份配置
     */
    String backupConfigs(List<String> configIds);
    
    /**
     * 恢复配置
     */
    void restoreConfigs(String backupData);
    
    /**
     * 验证配置依赖关系
     */
    void validateConfigDependencies(String configId, String newValue);
    
    /**
     * 获取配置变更历史
     */
    List<Object> getConfigChangeHistory(String configId);
    
    /**
     * 同步配置缓存
     */
    void syncConfigCache(String configKey);
    
    /**
     * 清理配置缓存
     */
    void clearConfigCache();
    
    /**
     * 刷新所有配置缓存
     */
    void refreshAllConfigCache();
    
    /**
     * 获取配置统计信息
     */
    Map<String, Object> getConfigStatistics();
}