package com.jiuxi.module.auth.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色聚合根
 * 权限管理领域的核心实体
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class Role {
    
    /**
     * 角色ID
     */
    private String roleId;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色描述
     */
    private String roleDesc;
    
    /**
     * 角色类型
     */
    private RoleType roleType;
    
    /**
     * 角色状态
     */
    private RoleStatus status;
    
    /**
     * 是否内置角色
     */
    private Boolean builtIn;
    
    /**
     * 部门ID（如果是部门角色）
     */
    private String deptId;
    
    /**
     * 权限列表
     */
    private List<Permission> permissions;
    
    /**
     * 菜单列表
     */
    private List<Menu> menus;
    
    /**
     * 数据权限范围
     */
    private DataScope dataScope;
    
    /**
     * 数据权限实体
     */
    private DataPermission dataPermission;
    
    /**
     * 父角色ID（用于权限继承）
     */
    private String parentRoleId;
    
    /**
     * 角色层级
     */
    private Integer roleLevel;
    
    /**
     * 角色路径（用于权限继承计算）
     */
    private String rolePath;
    
    /**
     * 是否继承父角色权限
     */
    private Boolean inheritParentPermissions;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造器
    public Role() {
        this.permissions = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.status = RoleStatus.ACTIVE;
        this.builtIn = false;
        this.dataScope = DataScope.DEPT;
        this.inheritParentPermissions = true;
        this.roleLevel = 1;
    }
    
    public Role(String roleCode, String roleName, RoleType roleType) {
        this();
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
    }
    
    // 业务方法
    
    /**
     * 添加权限
     */
    public void addPermission(Permission permission) {
        if (permission != null && !this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }
    
    /**
     * 移除权限
     */
    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }
    
    /**
     * 清空所有权限
     */
    public void clearPermissions() {
        this.permissions.clear();
    }
    
    /**
     * 添加菜单
     */
    public void addMenu(Menu menu) {
        if (menu != null && !this.menus.contains(menu)) {
            this.menus.add(menu);
        }
    }
    
    /**
     * 移除菜单
     */
    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
    }
    
    /**
     * 清空所有菜单
     */
    public void clearMenus() {
        this.menus.clear();
    }
    
    /**
     * 检查是否有特定权限
     */
    public boolean hasPermission(String permissionCode) {
        return permissions.stream()
                .anyMatch(p -> Objects.equals(p.getPermissionCode(), permissionCode));
    }
    
    /**
     * 检查是否有特定菜单
     */
    public boolean hasMenu(String menuCode) {
        return menus.stream()
                .anyMatch(m -> Objects.equals(m.getMenuCode(), menuCode));
    }
    
    /**
     * 启用角色
     */
    public void enable() {
        this.status = RoleStatus.ACTIVE;
    }
    
    /**
     * 停用角色
     */
    public void disable() {
        this.status = RoleStatus.INACTIVE;
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return RoleStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 检查是否是内置角色
     */
    public boolean isBuiltIn() {
        return Boolean.TRUE.equals(this.builtIn);
    }
    
    /**
     * 设置为内置角色
     */
    public void setAsBuiltIn() {
        this.builtIn = true;
    }
    
    /**
     * 设置父角色
     */
    public void setParentRole(String parentRoleId, Integer parentRoleLevel, String parentRolePath) {
        this.parentRoleId = parentRoleId;
        this.roleLevel = parentRoleLevel != null ? parentRoleLevel + 1 : 1;
        this.rolePath = buildRolePath(parentRolePath, this.roleId);
    }
    
    /**
     * 构建角色路径
     */
    private String buildRolePath(String parentRolePath, String currentRoleId) {
        if (parentRolePath == null || parentRolePath.trim().isEmpty()) {
            return currentRoleId;
        }
        return parentRolePath + "/" + currentRoleId;
    }
    
    /**
     * 检查是否继承父角色权限
     */
    public boolean shouldInheritParentPermissions() {
        return Boolean.TRUE.equals(this.inheritParentPermissions) && this.parentRoleId != null;
    }
    
    /**
     * 启用权限继承
     */
    public void enablePermissionInheritance() {
        this.inheritParentPermissions = true;
    }
    
    /**
     * 禁用权限继承
     */
    public void disablePermissionInheritance() {
        this.inheritParentPermissions = false;
    }
    
    /**
     * 检查是否是子角色
     */
    public boolean isChildRole() {
        return this.parentRoleId != null && !this.parentRoleId.trim().isEmpty();
    }
    
    /**
     * 检查是否是根角色
     */
    public boolean isRootRole() {
        return this.parentRoleId == null || this.parentRoleId.trim().isEmpty();
    }
    
    /**
     * 获取所有有效权限（包括继承的权限）
     * 注意：此方法需要在应用服务层实现完整的权限继承逻辑
     */
    public List<Permission> getAllEffectivePermissions() {
        // 返回直接权限，继承权限的合并需要在领域服务中实现
        return new ArrayList<>(this.permissions);
    }
    
    /**
     * 获取所有有效菜单（包括继承的菜单）
     * 注意：此方法需要在应用服务层实现完整的菜单继承逻辑
     */
    public List<Menu> getAllEffectiveMenus() {
        // 返回直接菜单，继承菜单的合并需要在领域服务中实现
        return new ArrayList<>(this.menus);
    }
    
    // Getters and Setters
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleDesc() {
        return roleDesc;
    }
    
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    
    public RoleType getRoleType() {
        return roleType;
    }
    
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
    
    public RoleStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoleStatus status) {
        this.status = status;
    }
    
    public Boolean getBuiltIn() {
        return builtIn;
    }
    
    public void setBuiltIn(Boolean builtIn) {
        this.builtIn = builtIn;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public List<Permission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions != null ? permissions : new ArrayList<>();
    }
    
    public List<Menu> getMenus() {
        return menus;
    }
    
    public void setMenus(List<Menu> menus) {
        this.menus = menus != null ? menus : new ArrayList<>();
    }
    
    public DataScope getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public DataPermission getDataPermission() {
        return dataPermission;
    }
    
    public void setDataPermission(DataPermission dataPermission) {
        this.dataPermission = dataPermission;
    }
    
    public String getParentRoleId() {
        return parentRoleId;
    }
    
    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }
    
    public Integer getRoleLevel() {
        return roleLevel;
    }
    
    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }
    
    public String getRolePath() {
        return rolePath;
    }
    
    public void setRolePath(String rolePath) {
        this.rolePath = rolePath;
    }
    
    public Boolean getInheritParentPermissions() {
        return inheritParentPermissions;
    }
    
    public void setInheritParentPermissions(Boolean inheritParentPermissions) {
        this.inheritParentPermissions = inheritParentPermissions;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }
}