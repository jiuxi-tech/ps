package com.jiuxi.module.role.domain.service;

import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.model.vo.RoleName;
import java.util.List;

/**
 * 权限验证领域服务接口
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public interface PermissionValidationService {
    
    /**
     * 验证角色名称是否符合业务规范
     * @param role 角色聚合根
     * @return 验证结果
     */
    RoleValidationResult validateRoleName(Role role);
    
    /**
     * 验证角色是否可以删除
     * @param role 角色聚合根
     * @return 验证结果，包含不能删除的原因
     */
    RoleValidationResult validateRoleDeletion(Role role);
    
    /**
     * 验证角色权限分配是否合理
     * @param role 角色聚合根
     * @param permissions 待分配的权限列表
     * @return 验证结果
     */
    PermissionValidationResult validatePermissionAssignment(Role role, List<Permission> permissions);
    
    /**
     * 验证权限树的完整性
     * @param permissions 权限列表
     * @return 验证结果
     */
    PermissionValidationResult validatePermissionTree(List<Permission> permissions);
    
    /**
     * 检查角色名称是否重复
     * @param roleName 角色名称
     * @param excludeRoleId 排除的角色ID（用于更新时的检查）
     * @return 是否重复
     */
    boolean isDuplicateRoleName(RoleName roleName, RoleId excludeRoleId);
    
    /**
     * 验证政府角色的特殊规则
     * @param role 政府角色
     * @return 验证结果
     */
    RoleValidationResult validateGovernmentRole(Role role);
    
    /**
     * 检查角色的权限数量是否合理
     * @param permissions 权限列表
     * @return 是否合理
     */
    boolean isReasonablePermissionCount(List<Permission> permissions);
    
    /**
     * 验证权限的层级关系
     * @param permissions 权限列表
     * @return 验证结果
     */
    PermissionValidationResult validatePermissionHierarchy(List<Permission> permissions);
    
    /**
     * 检查角色是否有循环依赖的权限
     * @param roleId 角色ID
     * @param permissions 权限列表
     * @return 是否存在循环依赖
     */
    boolean hasCircularPermissionDependency(RoleId roleId, List<Permission> permissions);
}