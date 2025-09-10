package com.jiuxi.module.auth.domain.service;

import com.jiuxi.module.auth.domain.entity.Role;
import com.jiuxi.module.auth.domain.entity.RoleType;
import org.springframework.stereotype.Service;

/**
 * 角色领域服务
 * 处理角色相关的业务规则和复杂逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
public class RoleDomainService {
    
    /**
     * 获取默认角色类型
     * @return 默认角色类型
     */
    public RoleType getDefaultRoleType() {
        return RoleType.BUSINESS;
    }
    
    /**
     * 验证角色创建的业务规则
     * @param role 角色
     * @param tenantId 租户ID
     */
    public void validateForCreate(Role role, String tenantId) {
        // 验证角色编码唯一性
        // 这里应该调用仓储接口检查角色编码是否已存在
        // 为简化示例，暂时留空
        
        // 验证角色名称长度
        if (role.getRoleName() != null && role.getRoleName().length() > 50) {
            throw new IllegalArgumentException("角色名称长度不能超过50个字符");
        }
        
        // 验证角色描述长度
        if (role.getRoleDesc() != null && role.getRoleDesc().length() > 200) {
            throw new IllegalArgumentException("角色描述长度不能超过200个字符");
        }
    }
    
    /**
     * 验证角色更新的业务规则
     * @param role 角色
     */
    public void validateForUpdate(Role role) {
        // 验证角色名称长度
        if (role.getRoleName() != null && role.getRoleName().length() > 50) {
            throw new IllegalArgumentException("角色名称长度不能超过50个字符");
        }
        
        // 验证角色描述长度
        if (role.getRoleDesc() != null && role.getRoleDesc().length() > 200) {
            throw new IllegalArgumentException("角色描述长度不能超过200个字符");
        }
    }
    
    /**
     * 验证角色删除的业务规则
     * @param roleId 角色ID
     */
    public void validateForDelete(String roleId) {
        // 验证角色是否被用户引用
        // 这里应该调用用户服务检查是否有用户关联此角色
        // 为简化示例，暂时留空
    }
}