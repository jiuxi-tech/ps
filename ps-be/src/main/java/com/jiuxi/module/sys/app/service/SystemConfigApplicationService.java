package com.jiuxi.module.sys.app.service;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.app.assembler.SystemConfigAssembler;
import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private SystemConfigAssembler systemConfigAssembler;
    
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
     * 根据配置键获取系统配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO getSystemConfigByKey(String configKey, String tenantId) {
        Optional<SystemConfig> systemConfigOpt = systemConfigRepository.findByConfigKey(configKey, tenantId);
        if (!systemConfigOpt.isPresent()) {
            return null;
        }
        
        return systemConfigAssembler.toResponseDTO(systemConfigOpt.get());
    }
}