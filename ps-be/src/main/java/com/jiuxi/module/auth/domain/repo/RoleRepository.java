package com.jiuxi.module.auth.domain.repo;

import com.jiuxi.module.auth.domain.model.entity.Role;
import java.util.List;
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
    
    /**
     * 根据父角色ID查找子角色列表
     * @param parentRoleId 父角色ID
     * @param tenantId 租户ID
     * @return 子角色列表
     */
    List<Role> findByParentRoleId(String parentRoleId, String tenantId);
    
    /**
     * 根据角色路径查找角色层次结构
     * @param rolePathPrefix 角色路径前缀
     * @param tenantId 租户ID
     * @return 角色列表
     */
    List<Role> findByRolePathPrefix(String rolePathPrefix, String tenantId);
    
    /**
     * 查找所有根角色（没有父角色的角色）
     * @param tenantId 租户ID
     * @return 根角色列表
     */
    List<Role> findRootRoles(String tenantId);
    
    /**
     * 根据租户ID查找所有激活的角色
     * @param tenantId 租户ID
     * @return 角色列表
     */
    List<Role> findActiveRolesByTenant(String tenantId);
    
    /**
     * 检查角色编码是否存在
     * @param roleCode 角色编码
     * @param tenantId 租户ID
     * @param excludeRoleId 排除的角色ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByRoleCode(String roleCode, String tenantId, String excludeRoleId);
    
    /**
     * 检查角色是否有关联的用户
     * @param roleId 角色ID
     * @return 是否有关联用户
     */
    boolean hasAssociatedUsers(String roleId);
    
    /**
     * 检查角色是否有子角色
     * @param roleId 角色ID
     * @return 是否有子角色
     */
    boolean hasChildRoles(String roleId);
}