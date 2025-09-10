package com.jiuxi.module.auth.app.assembler;

import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.app.dto.PermissionResponseDTO;
import org.springframework.stereotype.Component;

/**
 * 权限装配器
 * 负责Permission实体和PermissionResponseDTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Component
public class PermissionAssembler {
    
    /**
     * 将Permission实体转换为PermissionResponseDTO
     * @param permission 权限实体
     * @return 权限响应DTO
     */
    public PermissionResponseDTO toResponseDTO(Permission permission) {
        if (permission == null) {
            return null;
        }
        
        PermissionResponseDTO dto = new PermissionResponseDTO();
        dto.setPermissionId(permission.getPermissionId());
        dto.setPermissionCode(permission.getPermissionCode());
        dto.setPermissionName(permission.getPermissionName());
        dto.setPermissionDesc(permission.getPermissionDesc());
        dto.setPermissionType(permission.getPermissionType());
        dto.setStatus(permission.getStatus());
        dto.setPermissionUri(permission.getResourcePath());
        dto.setPermissionMethod(permission.getHttpMethod());
        // dto.setComponentId(permission.getComponentId());
        // dto.setDataScope(permission.getDataScope());
        dto.setOrderIndex(permission.getOrderIndex());
        dto.setCreator(permission.getCreator());
        dto.setCreateTime(permission.getCreateTime());
        dto.setUpdator(permission.getUpdator());
        dto.setUpdateTime(permission.getUpdateTime());
        dto.setTenantId(permission.getTenantId());
        
        return dto;
    }
}