package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.domain.entity.Role;
import com.jiuxi.module.auth.domain.entity.Permission;
import com.jiuxi.module.auth.domain.entity.Menu;
import com.jiuxi.module.auth.domain.repo.RoleRepository;
import com.jiuxi.module.auth.domain.service.RoleDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RoleDomainService roleDomainService;
    
    public RoleApplicationService(RoleRepository roleRepository, RoleDomainService roleDomainService) {
        this.roleRepository = roleRepository;
        this.roleDomainService = roleDomainService;
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
        role.setCreator(operator);
        role.setCreateTime(LocalDateTime.now());
        role.setTenantId(tenantId);
        
        // 业务规则验证
        roleDomainService.validateForCreate(role, tenantId);
        
        // 保存角色
        Role savedRole = roleRepository.save(role);
        
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
     */
    public void assignPermissionsToRole(String roleId, List<String> permissionIds) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        
        // 清空现有权限
        role.clearPermissions();
        
        // 添加新权限
        for (String permissionId : permissionIds) {
            // 这里应该从权限仓储中获取权限对象
            // 为简化示例，创建一个临时权限对象
            Permission permission = new Permission();
            permission.setPermissionId(permissionId);
            role.addPermission(permission);
        }
        
        roleRepository.save(role);
    }
    
    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    public void assignMenusToRole(String roleId, List<String> menuIds) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        
        // 清空现有菜单
        role.clearMenus();
        
        // 添加新菜单
        for (String menuId : menuIds) {
            // 这里应该从菜单仓储中获取菜单对象
            // 为简化示例，创建一个临时菜单对象
            Menu menu = new Menu();
            menu.setMenuId(menuId);
            role.addMenu(menu);
        }
        
        roleRepository.save(role);
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