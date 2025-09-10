package com.jiuxi.module.sys.infra.persistence.assembler;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.infra.persistence.entity.SystemConfigPO;
import org.springframework.stereotype.Component;

/**
 * 系统配置PO装配器
 * 负责SystemConfig实体和SystemConfigPO持久化对象之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class SystemConfigPOAssembler {
    
    /**
     * 将SystemConfig实体转换为SystemConfigPO持久化对象
     * @param systemConfig 系统配置实体
     * @return 系统配置持久化对象
     */
    public SystemConfigPO toPO(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return null;
        }
        
        SystemConfigPO systemConfigPO = new SystemConfigPO();
        systemConfigPO.setConfigId(systemConfig.getConfigId());
        systemConfigPO.setConfigKey(systemConfig.getConfigKey());
        systemConfigPO.setConfigValue(systemConfig.getConfigValue());
        systemConfigPO.setConfigName(systemConfig.getConfigName());
        systemConfigPO.setConfigDesc(systemConfig.getConfigDesc());
        systemConfigPO.setConfigType(systemConfig.getConfigType());
        systemConfigPO.setStatus(systemConfig.getStatus());
        systemConfigPO.setCreator(systemConfig.getCreator());
        systemConfigPO.setCreateTime(systemConfig.getCreateTime());
        systemConfigPO.setUpdator(systemConfig.getUpdator());
        systemConfigPO.setUpdateTime(systemConfig.getUpdateTime());
        systemConfigPO.setTenantId(systemConfig.getTenantId());
        systemConfigPO.setDeleted(0); // 默认未删除
        
        return systemConfigPO;
    }
    
    /**
     * 将SystemConfigPO持久化对象转换为SystemConfig实体
     * @param systemConfigPO 系统配置持久化对象
     * @return 系统配置实体
     */
    public SystemConfig toEntity(SystemConfigPO systemConfigPO) {
        if (systemConfigPO == null) {
            return null;
        }
        
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigId(systemConfigPO.getConfigId());
        systemConfig.setConfigKey(systemConfigPO.getConfigKey());
        systemConfig.setConfigValue(systemConfigPO.getConfigValue());
        systemConfig.setConfigName(systemConfigPO.getConfigName());
        systemConfig.setConfigDesc(systemConfigPO.getConfigDesc());
        systemConfig.setConfigType(systemConfigPO.getConfigType());
        systemConfig.setStatus(systemConfigPO.getStatus());
        systemConfig.setCreator(systemConfigPO.getCreator());
        systemConfig.setCreateTime(systemConfigPO.getCreateTime());
        systemConfig.setUpdator(systemConfigPO.getUpdator());
        systemConfig.setUpdateTime(systemConfigPO.getUpdateTime());
        systemConfig.setTenantId(systemConfigPO.getTenantId());
        
        return systemConfig;
    }
}