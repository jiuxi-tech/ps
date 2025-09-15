package com.jiuxi.module.role.domain.impl;

import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.model.vo.RoleName;
import com.jiuxi.module.role.domain.model.vo.PermissionId;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.domain.repo.PermissionRepository;
import com.jiuxi.module.role.domain.service.PermissionValidationService;
import com.jiuxi.module.role.domain.service.RoleValidationResult;
import com.jiuxi.module.role.domain.service.PermissionValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限验证领域服务实现
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service("roleModulePermissionValidationService")
public class PermissionValidationServiceImpl implements PermissionValidationService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public RoleValidationResult validateRoleName(Role role) {
        Objects.requireNonNull(role, "角色不能为空");
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        RoleName roleName = role.getRoleName();
        String nameValue = roleName.getValue();
        
        // 检查角色名称长度
        if (nameValue.length() < 2) {
            errors.add("角色名称长度不能少于2个字符");
        } else if (nameValue.length() > 50) {
            errors.add("角色名称长度不能超过50个字符");
        }
        
        // 检查角色名称格式
        if (!nameValue.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5_-]+$")) {
            errors.add("角色名称只能包含中文、英文、数字、下划线和连接符");
        }
        
        // 检查敏感词汇
        List<String> sensitiveWords = Arrays.asList("系统管理员", "超级管理员", "root", "admin");
        for (String sensitiveWord : sensitiveWords) {
            if (nameValue.toLowerCase().contains(sensitiveWord.toLowerCase())) {
                if (!role.getCategory().isGovernment()) {
                    errors.add("普通角色不能包含管理员相关名称: " + sensitiveWord);
                } else {
                    warnings.add("政府角色使用管理员名称，请确保权限配置合理");
                }
            }
        }
        
        // 检查角色名称重复
        if (isDuplicateRoleName(roleName, role.getRoleId())) {
            errors.add("角色名称已存在: " + nameValue);
        }
        
        if (!errors.isEmpty()) {
            return RoleValidationResult.withErrorsAndWarnings(errors, warnings);
        }
        
        if (!warnings.isEmpty()) {
            return RoleValidationResult.withWarnings(warnings);
        }
        
        return RoleValidationResult.success();
    }
    
    @Override
    public RoleValidationResult validateRoleDeletion(Role role) {
        Objects.requireNonNull(role, "角色不能为空");
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 检查角色状态
        if (!role.canDelete()) {
            errors.add("角色当前状态不允许删除");
        }
        
        // 检查是否有用户正在使用该角色
        long userCount = roleRepository.countUsersWithRole(role.getRoleId());
        if (userCount > 0) {
            errors.add("角色正在被 " + userCount + " 个用户使用，无法删除");
        }
        
        // 检查是否为系统预置角色
        if (role.getRoleName().getValue().toLowerCase().contains("系统")) {
            warnings.add("删除系统相关角色可能影响系统功能");
        }
        
        // 检查角色权限数量
        List<Permission> permissions = permissionRepository.findByRoleId(role.getRoleId());
        if (permissions.size() > 20) {
            warnings.add("该角色拥有大量权限（" + permissions.size() + "个），删除后将无法恢复");
        }
        
        // 检查是否为政府角色
        if (role.getCategory().isGovernment()) {
            warnings.add("删除政府角色可能影响政府用户的权限体系");
        }
        
        if (!errors.isEmpty()) {
            return RoleValidationResult.withErrorsAndWarnings(errors, warnings);
        }
        
        if (!warnings.isEmpty()) {
            return RoleValidationResult.withWarnings(warnings);
        }
        
        return RoleValidationResult.success();
    }
    
    @Override
    public PermissionValidationResult validatePermissionAssignment(Role role, List<Permission> permissions) {
        Objects.requireNonNull(role, "角色不能为空");
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<Permission> invalidPermissions = new ArrayList<>();
        
        // 检查权限数量
        if (!isReasonablePermissionCount(permissions)) {
            if (permissions.size() > 100) {
                errors.add("权限数量过多，不能超过100个");
            } else if (permissions.size() == 0) {
                warnings.add("角色没有分配任何权限");
            }
        }
        
        // 检查权限树完整性
        PermissionValidationResult treeResult = validatePermissionTree(permissions);
        if (treeResult.hasErrors()) {
            errors.addAll(treeResult.getErrorMessages());
        }
        if (treeResult.hasWarnings()) {
            warnings.addAll(treeResult.getWarningMessages());
        }
        
        // 检查权限层级关系
        PermissionValidationResult hierarchyResult = validatePermissionHierarchy(permissions);
        if (hierarchyResult.hasErrors()) {
            errors.addAll(hierarchyResult.getErrorMessages());
        }
        
        // 检查权限存在性
        for (Permission permission : permissions) {
            if (!permissionRepository.existsById(permission.getPermissionId())) {
                invalidPermissions.add(permission);
                errors.add("权限不存在: " + permission.getMenuName());
            }
        }
        
        // 检查循环依赖
        if (hasCircularPermissionDependency(role.getRoleId(), permissions)) {
            errors.add("检测到权限循环依赖");
        }
        
        // 政府角色特殊检查
        if (role.getCategory().isGovernment()) {
            boolean hasSystemPermission = permissions.stream()
                    .anyMatch(p -> p.getMenuName().contains("系统管理"));
            if (!hasSystemPermission) {
                warnings.add("政府角色建议包含系统管理权限");
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
    public PermissionValidationResult validatePermissionTree(List<Permission> permissions) {
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (permissions.isEmpty()) {
            return PermissionValidationResult.success();
        }
        
        // 构建权限映射
        Map<String, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(p -> p.getPermissionId().getValue(), p -> p));
        
        // 检查父子关系完整性
        for (Permission permission : permissions) {
            if (permission.getParentId() != null) {
                Permission parent = permissionMap.get(permission.getParentId());
                if (parent == null) {
                    // 检查父权限是否在数据库中存在
                    if (!permissionRepository.existsById(PermissionId.of(permission.getParentId()))) {
                        errors.add("权限 '" + permission.getMenuName() + "' 的父权限不存在");
                    } else {
                        warnings.add("权限 '" + permission.getMenuName() + "' 的父权限未包含在当前分配列表中");
                    }
                }
            }
        }
        
        // 检查孤立节点
        Set<String> parentIds = permissions.stream()
                .map(Permission::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        long rootCount = permissions.stream()
                .filter(p -> p.getParentId() == null)
                .count();
        
        if (rootCount == 0 && !parentIds.isEmpty()) {
            warnings.add("权限列表中没有根权限，可能存在权限树不完整的情况");
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
    public boolean isDuplicateRoleName(RoleName roleName, RoleId excludeRoleId) {
        Objects.requireNonNull(roleName, "角色名称不能为空");
        
        Optional<Role> existingRole = roleRepository.findByRoleName(roleName);
        if (!existingRole.isPresent()) {
            return false;
        }
        
        // 如果指定了排除的角色ID，检查是否是同一个角色
        if (excludeRoleId != null && existingRole.get().getRoleId().equals(excludeRoleId)) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public RoleValidationResult validateGovernmentRole(Role role) {
        Objects.requireNonNull(role, "角色不能为空");
        
        if (!role.getCategory().isGovernment()) {
            return RoleValidationResult.failure("该角色不是政府角色");
        }
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 政府角色名称规范检查
        String roleName = role.getRoleName().getValue();
        if (!roleName.contains("政府") && !roleName.contains("管理")) {
            warnings.add("政府角色建议在名称中包含'政府'或'管理'字样");
        }
        
        // 检查政府角色的权限配置
        List<Permission> permissions = permissionRepository.findByRoleId(role.getRoleId());
        long systemPermissionCount = permissions.stream()
                .filter(p -> p.getMenuName().contains("系统管理"))
                .count();
        
        if (systemPermissionCount == 0) {
            warnings.add("政府角色未配置系统管理权限，可能影响管理功能");
        }
        
        // 检查敏感权限
        boolean hasSensitivePermissions = permissions.stream()
                .anyMatch(p -> p.getMenuName().contains("用户管理") || 
                             p.getMenuName().contains("角色管理"));
        
        if (!hasSensitivePermissions) {
            warnings.add("政府角色未配置用户或角色管理权限");
        }
        
        if (!errors.isEmpty()) {
            return RoleValidationResult.withErrorsAndWarnings(errors, warnings);
        }
        
        if (!warnings.isEmpty()) {
            return RoleValidationResult.withWarnings(warnings);
        }
        
        return RoleValidationResult.success();
    }
    
    @Override
    public boolean isReasonablePermissionCount(List<Permission> permissions) {
        if (permissions == null) {
            return false;
        }
        
        int count = permissions.size();
        return count >= 1 && count <= 100;
    }
    
    @Override
    public PermissionValidationResult validatePermissionHierarchy(List<Permission> permissions) {
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        List<String> errors = new ArrayList<>();
        
        // 构建权限映射和层级检查
        Map<String, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(p -> p.getPermissionId().getValue(), p -> p));
        
        // 检查每个权限的层级深度，防止层级过深
        for (Permission permission : permissions) {
            int depth = calculatePermissionDepth(permission, permissionMap, new HashSet<>());
            if (depth > 5) {
                errors.add("权限 '" + permission.getMenuName() + "' 层级过深（深度: " + depth + "）");
            }
        }
        
        // 检查是否存在循环引用
        for (Permission permission : permissions) {
            if (hasCircularReference(permission, permissionMap, new HashSet<>())) {
                errors.add("权限 '" + permission.getMenuName() + "' 存在循环引用");
            }
        }
        
        if (!errors.isEmpty()) {
            return PermissionValidationResult.failure(errors);
        }
        
        return PermissionValidationResult.success();
    }
    
    @Override
    public boolean hasCircularPermissionDependency(RoleId roleId, List<Permission> permissions) {
        Objects.requireNonNull(roleId, "角色ID不能为空");
        Objects.requireNonNull(permissions, "权限列表不能为空");
        
        Map<String, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(p -> p.getPermissionId().getValue(), p -> p));
        
        for (Permission permission : permissions) {
            if (hasCircularReference(permission, permissionMap, new HashSet<>())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 计算权限层级深度
     */
    private int calculatePermissionDepth(Permission permission, Map<String, Permission> permissionMap, Set<String> visited) {
        String permissionId = permission.getPermissionId().getValue();
        
        if (visited.contains(permissionId)) {
            return 0; // 避免循环计算
        }
        
        visited.add(permissionId);
        
        if (permission.getParentId() == null) {
            return 1; // 根权限深度为1
        }
        
        Permission parent = permissionMap.get(permission.getParentId());
        if (parent == null) {
            return 1; // 父权限不在当前列表中，视为根权限
        }
        
        return 1 + calculatePermissionDepth(parent, permissionMap, visited);
    }
    
    /**
     * 检查权限是否存在循环引用
     */
    private boolean hasCircularReference(Permission permission, Map<String, Permission> permissionMap, Set<String> visited) {
        String permissionId = permission.getPermissionId().getValue();
        
        if (visited.contains(permissionId)) {
            return true; // 检测到循环
        }
        
        if (permission.getParentId() == null) {
            return false; // 根权限，无循环
        }
        
        visited.add(permissionId);
        
        Permission parent = permissionMap.get(permission.getParentId());
        if (parent == null) {
            return false; // 父权限不在当前列表中
        }
        
        return hasCircularReference(parent, permissionMap, visited);
    }
}