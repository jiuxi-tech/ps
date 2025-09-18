package com.jiuxi.module.sys.app.assembler;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
import com.jiuxi.module.sys.app.command.dto.SystemConfigCreateCommand;
import com.jiuxi.module.sys.app.command.dto.SystemConfigUpdateCommand;
import com.jiuxi.module.sys.app.query.dto.SystemConfigResponse;
import org.springframework.stereotype.Component;

/**
 * 系统配置装配器
 * 负责SystemConfig实体和DTO之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class SystemConfigAssembler {
    
    /**
     * 将SystemConfigCreateDTO转换为SystemConfig实体
     * @param dto 系统配置创建DTO
     * @return 系统配置实体
     */
    public SystemConfig toEntity(SystemConfigCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(dto.getConfigKey());
        systemConfig.setConfigValue(dto.getConfigValue());
        systemConfig.setConfigName(dto.getConfigName());
        systemConfig.setConfigDesc(dto.getConfigDesc());
        systemConfig.setConfigType(dto.getConfigType());
        systemConfig.setTenantId(dto.getTenantId());
        
        return systemConfig;
    }
    
    /**
     * 将SystemConfigUpdateDTO转换为SystemConfig实体
     * @param dto 系统配置更新DTO
     * @return 系统配置实体
     */
    public SystemConfig toEntity(SystemConfigUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigId(dto.getConfigId());
        systemConfig.setConfigKey(dto.getConfigKey());
        systemConfig.setConfigValue(dto.getConfigValue());
        systemConfig.setConfigName(dto.getConfigName());
        systemConfig.setConfigDesc(dto.getConfigDesc());
        systemConfig.setConfigType(dto.getConfigType());
        systemConfig.setStatus(dto.getStatus());
        systemConfig.setUpdator(dto.getUpdator());
        
        return systemConfig;
    }
    
    /**
     * 将SystemConfig实体转换为SystemConfigResponseDTO
     * @param systemConfig 系统配置实体
     * @return 系统配置响应DTO
     */
    public SystemConfigResponseDTO toResponseDTO(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return null;
        }
        
        SystemConfigResponseDTO dto = new SystemConfigResponseDTO();
        dto.setConfigId(systemConfig.getConfigId());
        dto.setConfigKey(systemConfig.getConfigKey());
        dto.setConfigValue(systemConfig.getConfigValue());
        dto.setConfigName(systemConfig.getConfigName());
        dto.setConfigDesc(systemConfig.getConfigDesc());
        dto.setConfigType(systemConfig.getConfigType());
        dto.setStatus(systemConfig.getStatus());
        dto.setCreator(systemConfig.getCreator());
        dto.setCreateTime(systemConfig.getCreateTime());
        dto.setUpdator(systemConfig.getUpdator());
        dto.setUpdateTime(systemConfig.getUpdateTime());
        dto.setTenantId(systemConfig.getTenantId());
        
        return dto;
    }
    
    // CQRS 兼容方法
    
    /**
     * 将SystemConfigCreateCommand转换为SystemConfig实体
     * @param command 系统配置创建命令
     * @return 系统配置实体
     */
    public SystemConfig toEntity(SystemConfigCreateCommand command) {
        if (command == null) {
            return null;
        }
        
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(command.getConfigKey());
        systemConfig.setConfigValue(command.getConfigValue());
        systemConfig.setConfigName(command.getConfigName());
        systemConfig.setConfigDesc(command.getConfigDesc());
        systemConfig.setConfigType(command.getConfigType());
        systemConfig.setTenantId(command.getTenantId());
        
        return systemConfig;
    }
    
    /**
     * 将SystemConfigUpdateCommand转换为SystemConfig实体
     * @param command 系统配置更新命令
     * @return 系统配置实体
     */
    public SystemConfig toEntity(SystemConfigUpdateCommand command) {
        if (command == null) {
            return null;
        }
        
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigId(command.getConfigId());
        systemConfig.setConfigKey(command.getConfigKey());
        systemConfig.setConfigValue(command.getConfigValue());
        systemConfig.setConfigName(command.getConfigName());
        systemConfig.setConfigDesc(command.getConfigDesc());
        systemConfig.setConfigType(command.getConfigType());
        systemConfig.setStatus(command.getStatus());
        systemConfig.setUpdator(command.getUpdator());
        
        return systemConfig;
    }
    
    /**
     * 将SystemConfig实体转换为SystemConfigResponse（新的CQRS响应）
     * @param systemConfig 系统配置实体
     * @return 系统配置响应
     */
    public SystemConfigResponse toResponse(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return null;
        }
        
        SystemConfigResponse response = new SystemConfigResponse();
        response.setConfigId(systemConfig.getConfigId());
        response.setConfigKey(systemConfig.getConfigKey());
        response.setConfigValue(systemConfig.getConfigValue());
        response.setConfigName(systemConfig.getConfigName());
        response.setConfigDesc(systemConfig.getConfigDesc());
        response.setConfigType(systemConfig.getConfigType());
        response.setStatus(systemConfig.getStatus());
        response.setCreator(systemConfig.getCreator());
        response.setCreateTime(systemConfig.getCreateTime());
        response.setUpdator(systemConfig.getUpdator());
        response.setUpdateTime(systemConfig.getUpdateTime());
        response.setTenantId(systemConfig.getTenantId());
        
        return response;
    }
}