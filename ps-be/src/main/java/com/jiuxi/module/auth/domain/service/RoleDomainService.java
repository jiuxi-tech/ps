package com.jiuxi.module.auth.domain.service;

import com.jiuxi.module.auth.domain.entity.Menu;
import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.entity.Role;
import com.jiuxi.module.auth.domain.entity.RoleType;
import com.jiuxi.module.auth.domain.event.PermissionAssignedEvent;
import com.jiuxi.module.auth.domain.event.RoleCreatedEvent;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.domain.repo.PermissionRepository;
import com.jiuxi.module.auth.domain.repo.RoleRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色领域服务
 * 处理角色相关的业务规则和复杂逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
@Transactional
public class RoleDomainService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final MenuRepository menuRepository;
    private final PermissionCacheService permissionCacheService;
    private final ApplicationEventPublisher eventPublisher;
    
    public RoleDomainService(RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           MenuRepository menuRepository,
                           PermissionCacheService permissionCacheService,
                           ApplicationEventPublisher eventPublisher) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.menuRepository = menuRepository;
        this.permissionCacheService = permissionCacheService;
        this.eventPublisher = eventPublisher;
    }
    
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
        if (roleRepository.existsByRoleCode(role.getRoleCode(), tenantId, null)) {
            throw new IllegalArgumentException("角色编码已存在: " + role.getRoleCode());
        }
        
        // 验证角色名称长度
        if (role.getRoleName() != null && role.getRoleName().length() > 50) {
            throw new IllegalArgumentException("角色名称长度不能超过50个字符");
        }
        
        // 验证角色描述长度
        if (role.getRoleDesc() != null && role.getRoleDesc().length() > 200) {
            throw new IllegalArgumentException("角色描述长度不能超过200个字符");
        }
        
        // 验证父角色层级关系
        if (StringUtils.hasText(role.getParentRoleId())) {
            if (!validateRoleHierarchy(role, role.getParentRoleId())) {
                throw new IllegalArgumentException("无效的角色层级关系");
            }
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
    
    /**
     * 创建角色
     * @param role 角色实体
     * @param operator 操作者
     * @return 创建后的角色
     */
    public Role createRole(Role role, String operator) {
        // 1. 验证角色创建的业务规则
        validateForCreate(role, role.getTenantId());
        
        // 2. 设置创建信息
        role.setCreator(operator);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdator(operator);
        role.setUpdateTime(LocalDateTime.now());
        
        // 3. 处理父角色关系
        if (StringUtils.hasText(role.getParentRoleId())) {
            Optional<Role> parentRole = roleRepository.findById(role.getParentRoleId());
            if (parentRole.isPresent()) {
                Role parent = parentRole.get();
                role.setParentRole(parent.getRoleId(), parent.getRoleLevel(), parent.getRolePath());
            } else {
                throw new IllegalArgumentException("父角色不存在: " + role.getParentRoleId());
            }
        } else {
            // 根角色
            role.setRoleLevel(1);
            role.setRolePath(role.getRoleId());
        }
        
        // 4. 保存角色
        Role savedRole = roleRepository.save(role);
        
        // 5. 发布角色创建事件
        RoleCreatedEvent event = new RoleCreatedEvent(
            savedRole.getRoleId(),
            savedRole.getRoleCode(),
            savedRole.getRoleName(),
            savedRole.getRoleType() != null ? savedRole.getRoleType().name() : null,
            savedRole.getDeptId(),
            savedRole.getTenantId(),
            operator
        );
        eventPublisher.publishEvent(event);
        
        return savedRole;
    }
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @param menuIds 菜单ID列表
     * @param operator 操作者
     */
    public void assignPermissions(String roleId, List<String> permissionIds, List<String> menuIds, String operator) {
        // 1. 查找角色
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        
        // 2. 清空现有权限和菜单
        role.clearPermissions();
        role.clearMenus();
        
        // 3. 分配新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<Permission> permissions = permissionRepository.findByIds(permissionIds, role.getTenantId());
            for (Permission permission : permissions) {
                role.addPermission(permission);
            }
        }
        
        // 4. 分配新菜单
        if (menuIds != null && !menuIds.isEmpty()) {
            List<Menu> menus = menuRepository.findByIds(menuIds, role.getTenantId());
            for (Menu menu : menus) {
                role.addMenu(menu);
            }
        }
        
        // 5. 更新角色
        role.setUpdator(operator);
        role.setUpdateTime(LocalDateTime.now());
        roleRepository.save(role);
        
        // 6. 清除相关缓存
        permissionCacheService.evictRolePermissionsCache(roleId, role.getTenantId());
        permissionCacheService.evictAllUserPermissionsCache(); // 清除所有用户权限缓存
        
        // 7. 发布权限分配事件
        PermissionAssignedEvent event = new PermissionAssignedEvent(
            roleId,
            role.getRoleName(),
            permissionIds != null ? permissionIds : Collections.emptyList(),
            menuIds != null ? menuIds : Collections.emptyList(),
            role.getDataScope() != null ? role.getDataScope().name() : null,
            role.getTenantId(),
            operator
        );
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 获取角色的所有有效权限（包括继承权限）
     * @param role 角色
     * @return 权限列表
     */
    public List<Permission> getRoleEffectivePermissions(Role role) {
        // 1. 先尝试从缓存获取
        List<Permission> cachedPermissions = permissionCacheService.getCachedRolePermissions(
            role.getRoleId(), role.getTenantId());
        if (cachedPermissions != null) {
            return cachedPermissions;
        }
        
        // 2. 获取直接权限
        Set<Permission> allPermissions = new HashSet<>(role.getPermissions());
        
        // 3. 如果启用权限继承，递归获取父角色权限
        if (role.shouldInheritParentPermissions() && StringUtils.hasText(role.getParentRoleId())) {
            List<Permission> inheritedPermissions = getInheritedPermissions(role);
            allPermissions.addAll(inheritedPermissions);
        }
        
        // 4. 转换为列表并排序
        List<Permission> effectivePermissions = allPermissions.stream()
            .sorted(Comparator.comparing(Permission::getOrderIndex, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Permission::getPermissionCode))
            .collect(Collectors.toList());
        
        // 5. 缓存结果
        permissionCacheService.cacheRolePermissions(role.getRoleId(), role.getTenantId(), effectivePermissions);
        
        return effectivePermissions;
    }
    
    /**
     * 获取角色的所有有效菜单（包括继承菜单）
     * @param role 角色
     * @return 菜单列表
     */
    public List<Menu> getRoleEffectiveMenus(Role role) {
        // 1. 获取直接菜单
        Set<Menu> allMenus = new HashSet<>(role.getMenus());
        
        // 2. 如果启用权限继承，递归获取父角色菜单
        if (role.shouldInheritParentPermissions() && StringUtils.hasText(role.getParentRoleId())) {
            List<Menu> inheritedMenus = getInheritedMenus(role);
            allMenus.addAll(inheritedMenus);
        }
        
        // 3. 转换为列表并排序
        return allMenus.stream()
            .sorted(Comparator.comparing(Menu::getOrderIndex, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Menu::getMenuCode))
            .collect(Collectors.toList());
    }
    
    /**
     * 验证角色层级关系的合法性
     * @param role 角色
     * @param parentRoleId 新的父角色ID
     * @return 是否合法
     */
    public boolean validateRoleHierarchy(Role role, String parentRoleId) {
        if (!StringUtils.hasText(parentRoleId)) {
            return true; // 设置为根角色是合法的
        }
        
        // 1. 父角色不能是自己
        if (role.getRoleId().equals(parentRoleId)) {
            return false;
        }
        
        // 2. 父角色必须存在
        Optional<Role> parentRole = roleRepository.findById(parentRoleId);
        if (!parentRole.isPresent()) {
            return false;
        }
        
        // 3. 不能形成循环引用（父角色的路径中不能包含当前角色）
        Role parent = parentRole.get();
        if (StringUtils.hasText(parent.getRolePath()) && 
            parent.getRolePath().contains(role.getRoleId())) {
            return false;
        }
        
        // 4. 检查层级深度限制（最多10层）
        if (parent.getRoleLevel() != null && parent.getRoleLevel() >= 10) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 更新角色层级关系
     * @param role 角色
     * @param newParentRoleId 新的父角色ID
     * @param operator 操作者
     */
    public void updateRoleHierarchy(Role role, String newParentRoleId, String operator) {
        // 1. 验证层级关系
        if (!validateRoleHierarchy(role, newParentRoleId)) {
            throw new IllegalArgumentException("无效的角色层级关系");
        }
        
        // 2. 更新父角色关系
        if (StringUtils.hasText(newParentRoleId)) {
            Optional<Role> parentRole = roleRepository.findById(newParentRoleId);
            if (parentRole.isPresent()) {
                Role parent = parentRole.get();
                role.setParentRole(parent.getRoleId(), parent.getRoleLevel(), parent.getRolePath());
            }
        } else {
            // 设置为根角色
            role.setParentRoleId(null);
            role.setRoleLevel(1);
            role.setRolePath(role.getRoleId());
        }
        
        // 3. 递归更新子角色的路径和层级
        updateChildRolesPaths(role);
        
        // 4. 保存角色
        role.setUpdator(operator);
        role.setUpdateTime(LocalDateTime.now());
        roleRepository.save(role);
        
        // 5. 清除相关缓存
        permissionCacheService.evictRolePermissionsCache(role.getRoleId(), role.getTenantId());
        permissionCacheService.evictAllUserPermissionsCache();
    }
    
    /**
     * 递归获取继承的权限
     */
    private List<Permission> getInheritedPermissions(Role role) {
        Set<Permission> inheritedPermissions = new HashSet<>();
        
        if (StringUtils.hasText(role.getParentRoleId())) {
            Optional<Role> parentRole = roleRepository.findById(role.getParentRoleId());
            if (parentRole.isPresent()) {
                Role parent = parentRole.get();
                // 获取父角色的直接权限
                inheritedPermissions.addAll(parent.getPermissions());
                
                // 递归获取父角色的继承权限
                if (parent.shouldInheritParentPermissions()) {
                    inheritedPermissions.addAll(getInheritedPermissions(parent));
                }
            }
        }
        
        return new ArrayList<>(inheritedPermissions);
    }
    
    /**
     * 递归获取继承的菜单
     */
    private List<Menu> getInheritedMenus(Role role) {
        Set<Menu> inheritedMenus = new HashSet<>();
        
        if (StringUtils.hasText(role.getParentRoleId())) {
            Optional<Role> parentRole = roleRepository.findById(role.getParentRoleId());
            if (parentRole.isPresent()) {
                Role parent = parentRole.get();
                // 获取父角色的直接菜单
                inheritedMenus.addAll(parent.getMenus());
                
                // 递归获取父角色的继承菜单
                if (parent.shouldInheritParentPermissions()) {
                    inheritedMenus.addAll(getInheritedMenus(parent));
                }
            }
        }
        
        return new ArrayList<>(inheritedMenus);
    }
    
    /**
     * 递归更新子角色的路径和层级
     */
    private void updateChildRolesPaths(Role parentRole) {
        List<Role> childRoles = roleRepository.findByParentRoleId(parentRole.getRoleId(), parentRole.getTenantId());
        
        for (Role child : childRoles) {
            // 更新子角色的层级和路径
            child.setRoleLevel(parentRole.getRoleLevel() + 1);
            child.setRolePath(parentRole.getRolePath() + "/" + child.getRoleId());
            
            // 保存子角色
            roleRepository.save(child);
            
            // 递归更新子角色的子角色
            updateChildRolesPaths(child);
            
            // 清除子角色的权限缓存
            permissionCacheService.evictRolePermissionsCache(child.getRoleId(), child.getTenantId());
        }
    }
}