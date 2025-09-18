package com.jiuxi.module.sys.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.vo.ConfigValue;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.domain.service.SystemConfigDomainService;
import com.jiuxi.module.sys.domain.service.SystemConfigCacheService;
import com.jiuxi.module.sys.domain.service.SystemConfigHotReloadService;
import com.jiuxi.module.sys.app.assembler.SystemConfigAssembler;
import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统配置应用服务
 * 负责系统配置相关的应用逻辑和事务协调
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Service
public class SystemConfigApplicationService {
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
    private SystemConfigDomainService systemConfigDomainService;
    
    @Autowired
    private SystemConfigCacheService systemConfigCacheService;
    
    @Autowired
    private SystemConfigHotReloadService systemConfigHotReloadService;
    
    @Autowired
    private SystemConfigAssembler systemConfigAssembler;
    
    @Autowired
    private TpSystemConfigService tpSystemConfigService;
    
    /**
     * 创建系统配置
     * @param createDTO 系统配置创建DTO
     * @param creator 创建人
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO createSystemConfig(SystemConfigCreateDTO createDTO, String creator) {
        // 转换DTO为实体
        SystemConfig systemConfig = systemConfigAssembler.toEntity(createDTO);
        
        // 设置创建信息
        systemConfig.setCreator(creator);
        systemConfig.setCreateTime(LocalDateTime.now());
        
        // 保存系统配置
        SystemConfig savedSystemConfig = systemConfigRepository.save(systemConfig);
        
        // 转换为响应DTO
        return systemConfigAssembler.toResponseDTO(savedSystemConfig);
    }
    
    /**
     * 更新系统配置
     * @param updateDTO 系统配置更新DTO
     * @param updator 更新人
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO updateSystemConfig(SystemConfigUpdateDTO updateDTO, String updator) {
        // 查找现有系统配置
        Optional<SystemConfig> existingSystemConfigOpt = systemConfigRepository.findById(updateDTO.getConfigId());
        if (!existingSystemConfigOpt.isPresent()) {
            throw new RuntimeException("系统配置不存在");
        }
        
        // 更新系统配置信息
        SystemConfig existingSystemConfig = existingSystemConfigOpt.get();
        existingSystemConfig.setConfigKey(updateDTO.getConfigKey());
        existingSystemConfig.setConfigValue(updateDTO.getConfigValue());
        existingSystemConfig.setConfigName(updateDTO.getConfigName());
        existingSystemConfig.setConfigDesc(updateDTO.getConfigDesc());
        existingSystemConfig.setConfigType(updateDTO.getConfigType());
        existingSystemConfig.setStatus(updateDTO.getStatus());
        existingSystemConfig.setUpdator(updator);
        existingSystemConfig.setUpdateTime(LocalDateTime.now());
        
        // 保存更新后的系统配置
        SystemConfig updatedSystemConfig = systemConfigRepository.save(existingSystemConfig);
        
        // 转换为响应DTO
        return systemConfigAssembler.toResponseDTO(updatedSystemConfig);
    }
    
    /**
     * 根据ID删除系统配置
     * @param configId 配置ID
     */
    public void deleteSystemConfig(String configId) {
        systemConfigRepository.deleteById(configId);
    }
    
    /**
     * 根据ID获取系统配置
     * @param configId 配置ID
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO getSystemConfigById(String configId) {
        Optional<SystemConfig> systemConfigOpt = systemConfigRepository.findById(configId);
        if (!systemConfigOpt.isPresent()) {
            return null;
        }
        
        return systemConfigAssembler.toResponseDTO(systemConfigOpt.get());
    }
    
    /**
     * 根据配置键获取系统配置（增强版，支持缓存）
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO getSystemConfigByKey(String configKey, String tenantId) {
        // 优先使用新的领域服务（带缓存）
        Optional<SystemConfig> systemConfigOpt = systemConfigDomainService.getConfig(configKey, tenantId);
        if (!systemConfigOpt.isPresent()) {
            return null;
        }
        
        return systemConfigAssembler.toResponseDTO(systemConfigOpt.get());
    }
    
    // 新增的DDD领域功能方法
    
    /**
     * 获取类型安全的配置值
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @param valueType 值类型
     * @return 类型化的配置值
     */
    public <T> T getTypedConfigValue(String configKey, String tenantId, Class<T> valueType) {
        return systemConfigDomainService.getTypedConfigValue(configKey, tenantId, valueType).orElse(null);
    }
    
    /**
     * 根据配置类型获取配置列表
     * @param configTypeCode 配置类型代码
     * @param tenantId 租户ID
     * @return 配置响应DTO列表
     */
    public List<SystemConfigResponseDTO> getSystemConfigsByType(String configTypeCode, String tenantId) {
        try {
            ConfigType configType = ConfigType.fromCode(configTypeCode);
            List<SystemConfig> configs = systemConfigDomainService.getConfigsByType(configType, tenantId);
            return configs.stream()
                    .map(systemConfigAssembler::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据配置组获取配置列表
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 配置响应DTO列表
     */
    public List<SystemConfigResponseDTO> getSystemConfigsByGroup(String configGroup, String tenantId) {
        List<SystemConfig> configs = systemConfigDomainService.getConfigsByGroup(configGroup, tenantId);
        return configs.stream()
                .map(systemConfigAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取系统级配置
     * @param tenantId 租户ID
     * @return 系统级配置列表
     */
    public List<SystemConfigResponseDTO> getSystemLevelConfigs(String tenantId) {
        List<SystemConfig> configs = systemConfigDomainService.getSystemLevelConfigs(tenantId);
        return configs.stream()
                .map(systemConfigAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取激活状态的配置
     * @param tenantId 租户ID
     * @return 激活配置列表
     */
    public List<SystemConfigResponseDTO> getActiveConfigs(String tenantId) {
        List<SystemConfig> configs = systemConfigDomainService.getActiveConfigs(tenantId);
        return configs.stream()
                .map(systemConfigAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 手动热更新配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否更新成功
     */
    public boolean hotReloadConfig(String configKey, String tenantId) {
        return systemConfigHotReloadService.manualHotReload(configKey, tenantId);
    }
    
    /**
     * 批量热更新配置
     * @param configKeys 配置键列表
     * @param tenantId 租户ID
     * @return 更新成功的配置数量
     */
    public int batchHotReloadConfigs(List<String> configKeys, String tenantId) {
        return systemConfigHotReloadService.batchHotReload(configKeys, tenantId);
    }
    
    /**
     * 获取缓存统计信息
     * @param tenantId 租户ID
     * @return 缓存统计信息
     */
    public SystemConfigCacheService.CacheStats getCacheStats(String tenantId) {
        return systemConfigCacheService.getCacheStats(tenantId);
    }
    
    /**
     * 清理缓存
     * @param tenantId 租户ID
     */
    public void clearCache(String tenantId) {
        systemConfigCacheService.evictAllConfigsForTenant(tenantId);
    }
    
    /**
       * 获取配置值
       * @param configKey 配置键
       * @return 配置值
       */
      public String getConfigValue(String configKey) {
          TpSystemConfig config = getByConfigKey(configKey);
          return config != null ? config.getConfigValue() : null;
      }

     /**
      * 获取配置值，如果不存在返回默认值
      * @param configKey 配置键
      * @param defaultValue 默认值
      * @return 配置值
      */
     public String getConfigValue(String configKey, String defaultValue) {
          String value = getConfigValue(configKey);
          return value != null ? value : defaultValue;
      }
 
     /**
      * 根据配置键获取配置对象
      * @param configKey 配置键
      * @return 配置对象
      */
     public TpSystemConfig getByConfigKey(String configKey) {
          return tpSystemConfigService.getByConfigKey(configKey);
      }
 
     /**
      * 分页查询系统配置
      * @param query 查询条件
      * @return 分页结果
      */
     public IPage<TpSystemConfigVO> queryPage(TpSystemConfigQuery query) {
         return tpSystemConfigService.queryPage(query);
      }
 
     /**
      * 设置配置值
      * @param configKey 配置键
      * @param configValue 配置值
      * @param description 描述
      */
     public void setConfigValue(String configKey, String configValue, String description) {
          // 这里需要实现具体的设置配置值逻辑
          // 暂时为空实现，需要根据实际业务逻辑实现
      }
 
     /**
      * 设置配置值
      * @param configKey 配置键
      * @param configValue 配置值
      */
     public void setConfigValue(String configKey, String configValue) {
          setConfigValue(configKey, configValue, null);
      }
 
     /**
      * 获取所有配置
      * @return 配置列表
      */
     public List<TpSystemConfig> getAllConfigs() {
         // 这里需要实现具体的获取所有配置逻辑
         // 暂时返回null，需要根据实际业务逻辑实现
         return null;
     }
 
     /**
      * 获取所有配置作为Map
      * @return 配置Map
      */
     public Map<String, String> getAllConfigsAsMap() {
          // 这里需要实现具体的获取所有配置作为Map的逻辑
          // 暂时返回null，需要根据实际业务逻辑实现
          return null;
      }
 
     /**
      * 批量更新配置
      * @param configs 配置Map
      */
     public void updateConfigs(Map<String, String> configs) {
          // 这里需要实现具体的批量更新配置逻辑
          // 暂时为空实现，需要根据实际业务逻辑实现
      }
 
     /**
      * 删除配置
      * @param configKey 配置键
      */
     public void deleteConfig(String configKey) {
         // 这里需要实现具体的删除配置逻辑
         // 暂时为空实现，需要根据实际业务逻辑实现
     }
}