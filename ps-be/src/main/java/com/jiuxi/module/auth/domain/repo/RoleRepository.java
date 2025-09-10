package com.jiuxi.module.auth.domain.repo;

import com.jiuxi.module.auth.domain.entity.Role;
import java.util.Optional;

/**
 * 角色仓储接口
 * 定义角色聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public interface RoleRepository {
    
    /**
     * 保存角色
     * @param role 角色聚合根
     * @return 保存后的角色
     */
    Role save(Role role);
    
    /**
     * 根据ID查找角色
     * @param roleId 角色ID
     * @return 角色Optional
     */
    Optional<Role> findById(String roleId);
    
    /**
     * 根据ID删除角色
     * @param roleId 角色ID
     */
    void deleteById(String roleId);
    
    /**
     * 根据角色编码查找角色
     * @param roleCode 角色编码
     * @param tenantId 租户ID
     * @return 角色Optional
     */
    Optional<Role> findByRoleCode(String roleCode, String tenantId);
}