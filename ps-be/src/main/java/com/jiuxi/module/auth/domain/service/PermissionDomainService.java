package com.jiuxi.module.auth.domain.service;

import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.entity.PermissionType;
import org.springframework.stereotype.Service;

/**
 * 权限领域服务
 * 处理权限相关的业务规则和复杂逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
public class PermissionDomainService {
    
    /**
     * 获取默认权限类型
     * @return 默认权限类型
     */
    public PermissionType getDefaultPermissionType() {
        return PermissionType.API;
    }
    
    /**
     * 验证权限创建的业务规则
     * @param permission 权限
     * @param tenantId 租户ID
     */
    public void validateForCreate(Permission permission, String tenantId) {
        // 验证权限编码唯一性
        // 这里应该调用仓储接口检查权限编码是否已存在
        // 为简化示例，暂时留空
        
        // 验证权限名称长度
        if (permission.getPermissionName() != null && permission.getPermissionName().length() > 50) {
            throw new IllegalArgumentException("权限名称长度不能超过50个字符");
        }
        
        // 验证权限描述长度
        if (permission.getPermissionDesc() != null && permission.getPermissionDesc().length() > 200) {
            throw new IllegalArgumentException("权限描述长度不能超过200个字符");
        }
    }
    
    /**
     * 验证权限更新的业务规则
     * @param permission 权限
     */
    public void validateForUpdate(Permission permission) {
        // 验证权限名称长度
        if (permission.getPermissionName() != null && permission.getPermissionName().length() > 50) {
            throw new IllegalArgumentException("权限名称长度不能超过50个字符");
        }
        
        // 验证权限描述长度
        if (permission.getPermissionDesc() != null && permission.getPermissionDesc().length() > 200) {
            throw new IllegalArgumentException("权限描述长度不能超过200个字符");
        }
    }
    
    /**
     * 验证权限删除的业务规则
     * @param permissionId 权限ID
     */
    public void validateForDelete(String permissionId) {
        // 验证权限是否被角色引用
        // 这里应该调用角色服务检查是否有角色关联此权限
        // 为简化示例，暂时留空
    }
}