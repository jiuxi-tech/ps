package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.domain.model.entity.Role;
import com.jiuxi.module.auth.domain.model.entity.Permission;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.repo.RoleRepository;
import com.jiuxi.module.auth.domain.repo.PermissionRepository;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.domain.service.RoleDomainService;
import com.jiuxi.module.auth.domain.service.PermissionCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 角色应用服务
 * 负责角色相关的应用逻辑和事务协调
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
@Transactional
public class RoleApplicationService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final MenuRepository menuRepository;
    private final RoleDomainService roleDomainService;
    private final PermissionCacheService permissionCacheService;
    
    public RoleApplicationService(RoleRepository roleRepository,
                                PermissionRepository permissionRepository,
                                MenuRepository menuRepository,
                                RoleDomainService roleDomainService,
                                PermissionCacheService permissionCacheService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.menuRepository = menuRepository;
        this.roleDomainService = roleDomainService;
        this.permissionCacheService = permissionCacheService;
    }
    
    /**
     * 创建角色
     * @param roleCode 角色编码
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 角色ID
     */
    public String createRole(String roleCode, String roleName, String roleDesc, 
                            String operator, String tenantId) {
        // 创建角色聚合根
        Role role = new Role(roleCode, roleName, roleDomainService.getDefaultRoleType());
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleDesc(roleDesc);
        role.setTenantId(tenantId);
        
        // 使用领域服务创建角色（包含业务规则验证）
        Role savedRole = roleDomainService.createRole(role, operator);
        
        return savedRole.getRoleId();
    }
    
    /**
     * 创建带父角色的角色
     * @param roleCode 角色编码
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @param parentRoleId 父角色ID
     * @param inheritParentPermissions 是否继承父角色权限
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 角色ID
     */
    public String createRoleWithParent(String roleCode, String roleName, String roleDesc,
                                     String parentRoleId, Boolean inheritParentPermissions,
                                     String operator, String tenantId) {
        // 创建角色聚合根
        Role role = new Role(roleCode, roleName, roleDomainService.getDefaultRoleType());
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleDesc(roleDesc);
        role.setParentRoleId(parentRoleId);
        role.setInheritParentPermissions(inheritParentPermissions != null ? inheritParentPermissions : true);
        role.setTenantId(tenantId);
        
        // 使用领域服务创建角色（包含业务规则验证和父角色关系处理）
        Role savedRole = roleDomainService.createRole(role, operator);
        
        return savedRole.getRoleId();
    }
    
    /**
     * 更新角色
     * @param roleId 角色ID
     * @param roleName 角色名称
     * @param roleDesc 角色描述
     * @param operator 操作者
     */
    public void updateRole(String roleId, String roleName, String roleDesc, String operator) {
        // 查找现有角色
        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);
        if (existingRoleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role existingRole = existingRoleOpt.get();
        
        // 更新角色信息
        existingRole.setRoleName(roleName);
        existingRole.setRoleDesc(roleDesc);
        existingRole.setUpdator(operator);
        existingRole.setUpdateTime(LocalDateTime.now());
        
        // 业务规则验证
        roleDomainService.validateForUpdate(existingRole);
        
        // 保存角色
        roleRepository.save(existingRole);
    }
    
    /**
     * 删除角色
     * @param roleId 角色ID
     */
    public void deleteRole(String roleId) {
        // 查找现有角色
        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);
        if (existingRoleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role existingRole = existingRoleOpt.get();
        
        // 业务规则验证
        roleDomainService.validateForDelete(roleId);
        
        // 删除角色
        roleRepository.deleteById(roleId);
    }
    
    /**
     * 启用角色
     * @param roleId 角色ID
     * @param operator 操作者
     */
    public void enableRole(String roleId, String operator) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        role.enable();
        role.setUpdator(operator);
        role.setUpdateTime(LocalDateTime.now());
        
        roleRepository.save(role);
    }
    
    /**
     * 停用角色
     * @param roleId 角色ID
     * @param operator 操作者
     */
    public void disableRole(String roleId, String operator) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        role.disable();
        role.setUpdator(operator);
        role.setUpdateTime(LocalDateTime.now());
        
        roleRepository.save(role);
    }
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @param operator 操作者
     */
    public void assignPermissionsToRole(String roleId, List<String> permissionIds, String operator) {
        // 使用领域服务分配权限（包含业务规则验证和缓存更新）
        roleDomainService.assignPermissions(roleId, permissionIds, null, operator);
    }
    
    /**
     * 为角色分配权限（保持向后兼容）
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    public void assignPermissionsToRole(String roleId, List<String> permissionIds) {
        // 使用默认操作者调用增强版本
        assignPermissionsToRole(roleId, permissionIds, "system");
    }
    
    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @param operator 操作者
     */
    public void assignMenusToRole(String roleId, List<String> menuIds, String operator) {
        // 使用领域服务分配菜单（包含业务规则验证和缓存更新）
        roleDomainService.assignPermissions(roleId, null, menuIds, operator);
    }
    
    /**
     * 为角色分配菜单（保持向后兼容）
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    public void assignMenusToRole(String roleId, List<String> menuIds) {
        // 使用默认操作者调用增强版本
        assignMenusToRole(roleId, menuIds, "system");
    }
    
    /**
     * 为角色分配权限和菜单
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @param menuIds 菜单ID列表
     * @param operator 操作者
     */
    public void assignPermissionsAndMenusToRole(String roleId, List<String> permissionIds, 
                                               List<String> menuIds, String operator) {
        // 使用领域服务分配权限和菜单（包含业务规则验证和缓存更新）
        roleDomainService.assignPermissions(roleId, permissionIds, menuIds, operator);
    }
    
    /**
     * 更新角色层级关系
     * @param roleId 角色ID
     * @param parentRoleId 新的父角色ID
     * @param inheritParentPermissions 是否继承父角色权限
     * @param operator 操作者
     */
    public void updateRoleHierarchy(String roleId, String parentRoleId, 
                                   Boolean inheritParentPermissions, String operator) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        
        // 更新权限继承设置
        if (inheritParentPermissions != null) {
            if (inheritParentPermissions) {
                role.enablePermissionInheritance();
            } else {
                role.disablePermissionInheritance();
            }
        }
        
        // 使用领域服务更新层级关系
        roleDomainService.updateRoleHierarchy(role, parentRoleId, operator);
    }
    
    /**
     * 获取角色的所有有效权限（包括继承权限）
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Transactional(readOnly = true)
    public List<Permission> getRoleEffectivePermissions(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        return roleDomainService.getRoleEffectivePermissions(role);
    }
    
    /**
     * 获取角色的所有有效菜单（包括继承菜单）
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @Transactional(readOnly = true)
    public List<Menu> getRoleEffectiveMenus(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        return roleDomainService.getRoleEffectiveMenus(role);
    }
    
    /**
     * 获取角色的子角色列表
     * @param roleId 角色ID
     * @return 子角色列表
     */
    @Transactional(readOnly = true)
    public List<Role> getChildRoles(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        return roleRepository.findByParentRoleId(roleId, role.getTenantId());
    }
    
    /**
     * 获取租户的所有根角色
     * @param tenantId 租户ID
     * @return 根角色列表
     */
    @Transactional(readOnly = true)
    public List<Role> getRootRoles(String tenantId) {
        return roleRepository.findRootRoles(tenantId);
    }
    
    /**
     * 根据ID获取角色
     * @param roleId 角色ID
     * @return 角色对象
     */
    @Transactional(readOnly = true)
    public Role getRoleById(String roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
    }
    
    /**
     * 根据角色编码获取角色
     * @param roleCode 角色编码
     * @param tenantId 租户ID
     * @return 角色对象
     */
    @Transactional(readOnly = true)
    public Role getRoleByCode(String roleCode, String tenantId) {
        return roleRepository.findByRoleCode(roleCode, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleCode));
    }
}