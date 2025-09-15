package com.jiuxi.module.role.domain.service;

import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import java.util.List;

/**
 * 角色授权领域服务接口
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public interface RoleAuthorizationService {
    
    /**
     * 为角色分配权限
     * @param role 角色聚合根
     * @param permissions 权限列表
     * @throws IllegalArgumentException 如果权限分配违反业务规则
     */
    void assignPermissions(Role role, List<Permission> permissions);
    
    /**
     * 验证角色权限分配是否合法
     * @param role 角色聚合根
     * @param permissions 待分配的权限列表
     * @return 验证结果，包含错误信息
     */
    PermissionValidationResult validatePermissionAssignment(Role role, List<Permission> permissions);
    
    /**
     * 检查用户是否有权限访问指定角色
     * @param personId 人员ID
     * @param deptId 部门ID
     * @param roleIds 用户拥有的角色ID列表
     * @param targetRoleId 目标角色ID
     * @return 是否有权限访问
     */
    boolean hasAccessToRole(String personId, String deptId, List<String> roleIds, RoleId targetRoleId);
    
    /**
     * 获取用户有权限访问的角色列表
     * @param personId 人员ID
     * @param deptId 部门ID
     * @param roleIds 用户拥有的角色ID列表
     * @return 可访问的角色列表
     */
    List<Role> getAccessibleRoles(String personId, String deptId, List<String> roleIds);
    
    /**
     * 检查角色是否可以被指定用户创建
     * @param creatorId 创建者ID
     * @param creatorRoleIds 创建者拥有的角色ID列表
     * @param targetRole 目标角色
     * @return 是否可以创建
     */
    boolean canCreateRole(String creatorId, List<String> creatorRoleIds, Role targetRole);
    
    /**
     * 检查角色是否可以被指定用户删除
     * @param deleterId 删除者ID
     * @param deleterRoleIds 删除者拥有的角色ID列表
     * @param targetRole 目标角色
     * @return 是否可以删除
     */
    boolean canDeleteRole(String deleterId, List<String> deleterRoleIds, Role targetRole);
    
    /**
     * 计算角色的有效权限（包括继承的权限）
     * @param roleId 角色ID
     * @return 有效权限列表
     */
    List<Permission> calculateEffectivePermissions(RoleId roleId);
    
    /**
     * 检查权限冲突
     * @param permissions 权限列表
     * @return 冲突检查结果
     */
    PermissionConflictResult checkPermissionConflicts(List<Permission> permissions);
}