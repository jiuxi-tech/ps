package com.jiuxi.module.sys.app.assembler;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
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
}