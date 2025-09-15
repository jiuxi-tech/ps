package com.jiuxi.module.role.domain.impl;

import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.repo.PermissionRepository;
import com.jiuxi.module.role.domain.service.RoleAuthorizationService;
import com.jiuxi.module.role.domain.service.PermissionValidationResult;
import com.jiuxi.module.role.domain.service.PermissionConflictResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色授权领域服务实现
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModuleAuthorizationService")
public class RoleAuthorizationServiceImpl implements RoleAuthorizationService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public void assignPermissions(Role role, List<Permission> permissions) {
        Objects.requireNonNull(role, "角色不能为空");
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        // 验证权限分配是否合法
        PermissionValidationResult validationResult = validatePermissionAssignment(role, permissions);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("权限分配不合法: " + validationResult.getFirstErrorMessage());
        }
        
        // 检查权限冲突
        PermissionConflictResult conflictResult = checkPermissionConflicts(permissions);
        if (conflictResult.hasConflicts()) {
            throw new IllegalArgumentException("权限存在冲突: " + conflictResult.getConflictMessages().get(0));
        }
        
        // 分配权限
        role.assignPermissions(permissions);
    }
    
    @Override
    public PermissionValidationResult validatePermissionAssignment(Role role, List<Permission> permissions) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 检查权限数量限制
        if (permissions.size() > 100) {
            errors.add("权限数量不能超过100个");
        }
        
        // 检查政府角色的特殊权限限制
        if (role.getCategory().isGovernment()) {
            long systemPermissionCount = permissions.stream()
                    .filter(p -> p.getMenuName().contains("系统管理"))
                    .count();
            if (systemPermissionCount == 0) {
                warnings.add("政府角色建议包含系统管理权限");
            }
        }
        
        // 检查权限的有效性
        List<Permission> invalidPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            if (!permissionRepository.existsById(permission.getPermissionId())) {
                invalidPermissions.add(permission);
                errors.add("权限不存在: " + permission.getMenuName());
            }
        }
        
        if (!errors.isEmpty()) {
            return PermissionValidationResult.failure(errors);
        }
        
        if (!warnings.isEmpty()) {
            return PermissionValidationResult.withWarnings(warnings);
        }
        
        return PermissionValidationResult.success();
    }
    
    @Override
    public boolean hasAccessToRole(String personId, String deptId, List<String> roleIds, RoleId targetRoleId) {
        Objects.requireNonNull(personId, "人员ID不能为空");
        Objects.requireNonNull(targetRoleId, "目标角色ID不能为空");
        
        // 查找目标角色
        Optional<Role> targetRole = roleRepository.findById(targetRoleId);
        if (!targetRole.isPresent()) {
            return false;
        }
        
        Role role = targetRole.get();
        
        // 检查是否是角色创建者
        if (personId.equals(role.getCreator())) {
            return true;
        }
        
        // 检查是否通过角色继承有权限访问
        return hasInheritedAccess(personId, deptId, roleIds, role);
    }
    
    @Override
    public List<Role> getAccessibleRoles(String personId, String deptId, List<String> roleIds) {
        Objects.requireNonNull(personId, "人员ID不能为空");
        
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        return roleRepository.findAuthorizedRoles(personId, deptId, roleIds);
    }
    
    @Override
    public boolean canCreateRole(String creatorId, List<String> creatorRoleIds, Role targetRole) {
        Objects.requireNonNull(creatorId, "创建者ID不能为空");
        Objects.requireNonNull(targetRole, "目标角色不能为空");
        
        if (creatorRoleIds == null || creatorRoleIds.isEmpty()) {
            return false; // 没有角色的用户不能创建角色
        }
        
        // 政府角色只能由具有管理权限的用户创建
        if (targetRole.getCategory().isGovernment()) {
            return hasAdminPermission(creatorRoleIds);
        }
        
        return true; // 普通角色可以被有角色的用户创建
    }
    
    @Override
    public boolean canDeleteRole(String deleterId, List<String> deleterRoleIds, Role targetRole) {
        Objects.requireNonNull(deleterId, "删除者ID不能为空");
        Objects.requireNonNull(targetRole, "目标角色不能为空");
        
        // 只有角色创建者或管理员可以删除角色
        if (deleterId.equals(targetRole.getCreator())) {
            return targetRole.canDelete(); // 检查角色本身是否允许删除
        }
        
        return hasAdminPermission(deleterRoleIds);
    }
    
    @Override
    public List<Permission> calculateEffectivePermissions(RoleId roleId) {
        Objects.requireNonNull(roleId, "角色ID不能为空");
        
        List<Permission> directPermissions = permissionRepository.findByRoleId(roleId);
        Set<Permission> effectivePermissions = new HashSet<>(directPermissions);
        
        // 添加继承的权限（如果有权限继承逻辑）
        for (Permission permission : directPermissions) {
            if (!permission.isRoot()) {
                // 如果有父权限，可以考虑加入父权限的逻辑
                // 这里简化处理，实际业务可能更复杂
            }
        }
        
        return new ArrayList<>(effectivePermissions);
    }
    
    @Override
    public PermissionConflictResult checkPermissionConflicts(List<Permission> permissions) {
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        List<PermissionConflictResult.ConflictPair> conflicts = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        
        // 检查重复权限
        Set<String> menuIds = new HashSet<>();
        for (Permission permission : permissions) {
            if (!menuIds.add(permission.getMenuId())) {
                // 找到重复权限
                Permission duplicate = permissions.stream()
                        .filter(p -> p.getMenuId().equals(permission.getMenuId()))
                        .findFirst()
                        .orElse(null);
                if (duplicate != null && !duplicate.equals(permission)) {
                    conflicts.add(new PermissionConflictResult.ConflictPair(
                            permission, duplicate,
                            PermissionConflictResult.ConflictType.DUPLICATE,
                            "菜单权限重复: " + permission.getMenuName()
                    ));
                    messages.add("检测到重复的菜单权限: " + permission.getMenuName());
                }
            }
        }
        
        // 检查层级冲突
        checkHierarchyConflicts(permissions, conflicts, messages);
        
        if (conflicts.isEmpty()) {
            return PermissionConflictResult.noConflict();
        }
        
        return PermissionConflictResult.withConflicts(conflicts, messages);
    }
    
    /**
     * 检查是否有继承访问权限
     */
    private boolean hasInheritedAccess(String personId, String deptId, List<String> roleIds, Role targetRole) {
        // 简化实现：检查用户的角色是否包含目标角色的创建者角色
        // 实际业务可能有更复杂的继承逻辑
        for (String roleId : roleIds) {
            Optional<Role> userRole = roleRepository.findById(RoleId.of(roleId));
            if (userRole.isPresent() && userRole.get().getCategory().isGovernment()) {
                return true; // 政府角色用户可以访问其他角色
            }
        }
        return false;
    }
    
    /**
     * 检查是否有管理权限
     */
    private boolean hasAdminPermission(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return false;
        }
        
        for (String roleId : roleIds) {
            Optional<Role> role = roleRepository.findById(RoleId.of(roleId));
            if (role.isPresent() && role.get().getRoleName().getValue().toLowerCase().contains("管理")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查权限层级冲突
     */
    private void checkHierarchyConflicts(List<Permission> permissions, 
                                       List<PermissionConflictResult.ConflictPair> conflicts, 
                                       List<String> messages) {
        for (int i = 0; i < permissions.size(); i++) {
            for (int j = i + 1; j < permissions.size(); j++) {
                Permission p1 = permissions.get(i);
                Permission p2 = permissions.get(j);
                
                // 检查是否存在父子关系冲突
                if (p1.getParentId() != null && p1.getParentId().equals(p2.getPermissionId().getValue())) {
                    // p1是p2的子权限，这通常不是冲突，而是正常的层级关系
                } else if (p2.getParentId() != null && p2.getParentId().equals(p1.getPermissionId().getValue())) {
                    // p2是p1的子权限，这通常不是冲突，而是正常的层级关系
                }
                
                // 这里可以添加更多的层级冲突检查逻辑
            }
        }
    }
}