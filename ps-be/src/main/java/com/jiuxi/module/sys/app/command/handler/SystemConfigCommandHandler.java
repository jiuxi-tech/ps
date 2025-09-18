package com.jiuxi.module.sys.app.command.handler;

import com.jiuxi.module.sys.app.command.dto.SystemConfigCreateCommand;
import com.jiuxi.module.sys.app.command.dto.SystemConfigUpdateCommand;
import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.vo.ConfigValue;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.domain.service.SystemConfigDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 系统配置命令处理器
 * 负责处理系统配置相关的命令操作（CUD）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class SystemConfigCommandHandler {
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
    private SystemConfigDomainService systemConfigDomainService;
    
    /**
     * 处理创建系统配置命令
     * @param command 创建命令
     * @param creator 创建人
     * @return 创建后的系统配置ID
     */
    @Transactional
    public String handleCreateCommand(SystemConfigCreateCommand command, String creator) {
        try {
            // 创建值对象
            ConfigKey configKey = new ConfigKey(command.getConfigKey());
            ConfigValue configValue = new ConfigValue(command.getConfigValue(), "STRING");
            ConfigType configType = ConfigType.fromCode(command.getConfigType());
            
            // 使用领域服务创建配置
            SystemConfig createdConfig = systemConfigDomainService.createConfig(
                configKey, 
                configValue, 
                command.getConfigName(), 
                configType, 
                command.getConfigGroup(), 
                creator, 
                command.getTenantId()
            );
            
            return createdConfig.getConfigId();
        } catch (Exception e) {
            throw new RuntimeException("创建系统配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理更新系统配置命令
     * @param command 更新命令
     * @param updator 更新人
     * @return 是否更新成功
     */
    @Transactional
    public boolean handleUpdateCommand(SystemConfigUpdateCommand command, String updator) {
        try {
            // 创建新的配置值对象
            ConfigValue newValue = new ConfigValue(command.getConfigValue(), "STRING");
            
            // 使用领域服务更新配置
            systemConfigDomainService.updateConfig(
                command.getConfigId(), 
                newValue, 
                updator
            );
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新系统配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理更新系统配置状态命令
     * @param configId 配置ID
     * @param statusCode 新状态代码
     * @param updator 更新人
     * @return 是否更新成功
     */
    @Transactional
    public boolean handleUpdateStatusCommand(String configId, String statusCode, String updator) {
        try {
            ConfigStatus newStatus = ConfigStatus.fromCode(statusCode);
            systemConfigDomainService.updateConfigStatus(configId, newStatus, updator);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新系统配置状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理删除系统配置命令
     * @param configId 配置ID
     * @param operator 操作人
     * @return 是否删除成功
     */
    @Transactional
    public boolean handleDeleteCommand(String configId, String operator) {
        try {
            systemConfigDomainService.deleteConfig(configId, operator);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除系统配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理批量配置值设置命令
     * @param configKey 配置键
     * @param configValue 配置值
     * @param operator 操作人
     * @return 是否设置成功
     */
    @Transactional
    public boolean handleSetConfigValueCommand(String configKey, String configValue, String operator) {
        try {
            // 这里实现具体的设置配置值逻辑
            // 可以通过查找现有配置并更新，或创建新配置
            // 暂时留作后续实现
            return true;
        } catch (Exception e) {
            throw new RuntimeException("设置配置值失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理热重载配置命令
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否重载成功
     */
    public boolean handleHotReloadCommand(String configKey, String tenantId) {
        try {
            return systemConfigDomainService.hotReloadConfig(configKey, tenantId);
        } catch (Exception e) {
            throw new RuntimeException("热重载配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理清理缓存命令
     * @param tenantId 租户ID
     * @return 是否清理成功
     */
    public boolean handleClearCacheCommand(String tenantId) {
        try {
            systemConfigDomainService.clearCache(tenantId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("清理缓存失败: " + e.getMessage(), e);
        }
    }
}