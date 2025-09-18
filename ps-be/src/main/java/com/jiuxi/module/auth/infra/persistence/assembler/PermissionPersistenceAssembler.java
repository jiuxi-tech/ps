package com.jiuxi.module.auth.infra.persistence.assembler;

import com.jiuxi.module.auth.domain.model.entity.Permission;
import com.jiuxi.module.auth.domain.model.entity.PermissionStatus;
import com.jiuxi.module.auth.domain.model.entity.PermissionType;
import com.jiuxi.module.auth.infra.persistence.entity.PermissionPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限持久化装配器
 * 负责领域对象与持久化对象之间的转换
 * 实现统一的转换规范和异常处理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component("authPermissionPersistenceAssembler")
public class PermissionPersistenceAssembler {
    
    /**
     * 将Permission领域对象转换为PermissionPO持久化对象
     * @param permission 权限领域对象
     * @return 权限持久化对象
     */
    public PermissionPO toPO(Permission permission) {
        if (permission == null) {
            return null;
        }
        
        try {
            PermissionPO permissionPO = new PermissionPO();
            permissionPO.setId(permission.getPermissionId());
            permissionPO.setPermissionCode(permission.getPermissionCode());
            permissionPO.setPermissionName(permission.getPermissionName());
            permissionPO.setPermissionDesc(permission.getPermissionDesc());
            permissionPO.setPermissionType(permission.getPermissionType() != null ? permission.getPermissionType().getCode() : null);
            permissionPO.setPermissionUri(permission.getResourcePath());
            permissionPO.setPermissionMethod(permission.getHttpMethod());
            permissionPO.setStatus(permission.getStatus() != null ? permission.getStatus().getCode() : null);
            // PermissionPO没有builtIn字段，跳过
            // PermissionPO没有permissionGroup字段，可以使用componentId代替
            permissionPO.setComponentId(permission.getPermissionGroup());
            permissionPO.setOrderIndex(permission.getOrderIndex());
            permissionPO.setCreator(permission.getCreator());
            permissionPO.setCreateTime(permission.getCreateTime());
            permissionPO.setUpdator(permission.getUpdator());
            permissionPO.setUpdateTime(permission.getUpdateTime());
            permissionPO.setTenantId(permission.getTenantId());
            
            return permissionPO;
        } catch (Exception e) {
            throw new RuntimeException("Permission领域对象转换为持久化对象失败", e);
        }
    }
    
    /**
     * 将PermissionPO持久化对象转换为Permission领域对象
     * @param permissionPO 权限持久化对象
     * @return 权限领域对象
     */
    public Permission toEntity(PermissionPO permissionPO) {
        if (permissionPO == null) {
            return null;
        }
        
        try {
            Permission permission = new Permission();
            permission.setPermissionId(permissionPO.getId());
            permission.setPermissionCode(permissionPO.getPermissionCode());
            permission.setPermissionName(permissionPO.getPermissionName());
            permission.setPermissionDesc(permissionPO.getPermissionDesc());
            
            // 安全转换权限类型枚举
            if (permissionPO.getPermissionType() != null) {
                try {
                    permission.setPermissionType(PermissionType.fromCode(permissionPO.getPermissionType()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    permission.setPermissionType(PermissionType.API);
                }
            }
            
            permission.setResourcePath(permissionPO.getPermissionUri());
            permission.setHttpMethod(permissionPO.getPermissionMethod());
            
            // 安全转换状态枚举
            if (permissionPO.getStatus() != null) {
                try {
                    permission.setStatus(PermissionStatus.fromCode(permissionPO.getStatus()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    permission.setStatus(PermissionStatus.ACTIVE);
                }
            }
            
            // PermissionPO没有builtIn字段，设置默认值
            permission.setBuiltIn(false);
            permission.setPermissionGroup(permissionPO.getComponentId());
            permission.setOrderIndex(permissionPO.getOrderIndex());
            permission.setCreator(permissionPO.getCreator());
            permission.setCreateTime(permissionPO.getCreateTime());
            permission.setUpdator(permissionPO.getUpdator());
            permission.setUpdateTime(permissionPO.getUpdateTime());
            permission.setTenantId(permissionPO.getTenantId());
            
            return permission;
        } catch (Exception e) {
            throw new RuntimeException("PermissionPO持久化对象转换为领域对象失败: " + permissionPO.getId(), e);
        }
    }
    
    /**
     * 批量转换PO列表为实体列表
     * @param permissionPOs 持久化对象列表
     * @return 领域对象列表
     */
    public List<Permission> toEntityList(List<PermissionPO> permissionPOs) {
        if (permissionPOs == null || permissionPOs.isEmpty()) {
            return List.of();
        }
        
        return permissionPOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换实体列表为PO列表
     * @param permissions 领域对象列表
     * @return 持久化对象列表
     */
    public List<PermissionPO> toPOList(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return List.of();
        }
        
        return permissions.stream()
                .map(this::toPO)
                .collect(Collectors.toList());
    }
}