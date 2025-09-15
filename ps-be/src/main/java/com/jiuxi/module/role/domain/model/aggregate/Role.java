package com.jiuxi.module.role.domain.model.aggregate;

import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色聚合根
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class Role {
    
    private final RoleId roleId;
    private RoleName roleName;
    private String roleDesc;
    private RoleStatus status;
    private final RoleCategory category;
    private final String creator;
    private String remark;
    private final LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private final Set<Permission> permissions;
    
    /**
     * 构造函数（用于从持久层重建聚合根）
     */
    public Role(RoleId roleId, RoleName roleName, String roleDesc, RoleStatus status, 
                RoleCategory category, String creator, String remark, 
                LocalDateTime createTime, LocalDateTime updateTime) {
        this.roleId = Objects.requireNonNull(roleId, "角色ID不能为空");
        this.roleName = Objects.requireNonNull(roleName, "角色名称不能为空");
        this.roleDesc = roleDesc;
        this.status = Objects.requireNonNull(status, "角色状态不能为空");
        this.category = Objects.requireNonNull(category, "角色类别不能为空");
        this.creator = Objects.requireNonNull(creator, "创建者不能为空");
        this.remark = remark;
        this.createTime = Objects.requireNonNull(createTime, "创建时间不能为空");
        this.updateTime = updateTime;
        this.permissions = new HashSet<>();
    }
    
    /**
     * 创建新角色（领域工厂方法）
     */
    public static Role create(String roleId, String roleName, String roleDesc, 
                             Integer category, String creator, String remark) {
        return new Role(
            RoleId.of(roleId),
            RoleName.of(roleName),
            roleDesc,
            RoleStatus.ENABLED, // 新创建的角色默认启用
            RoleCategory.of(category),
            creator,
            remark,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
    
    /**
     * 重建角色聚合根（用于数据持久化重建）
     */
    public static Role rebuild(RoleId roleId, RoleName roleName, String roleDesc,
                              RoleStatus status, RoleCategory category, String creator, 
                              String remark, LocalDateTime createTime, LocalDateTime updateTime) {
        Objects.requireNonNull(roleId, "角色ID不能为空");
        Objects.requireNonNull(roleName, "角色名称不能为空");
        Objects.requireNonNull(status, "角色状态不能为空");
        Objects.requireNonNull(category, "角色类别不能为空");
        
        return new Role(roleId, roleName, roleDesc, status, category, creator, remark, createTime, updateTime);
    }
    
    /**
     * 更新角色信息
     */
    public void updateInfo(String roleName, String roleDesc, String remark) {
        if (roleName != null && !roleName.trim().isEmpty()) {
            this.roleName = RoleName.of(roleName);
        }
        this.roleDesc = roleDesc;
        this.remark = remark;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用角色
     */
    public void enable() {
        this.status = RoleStatus.ENABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用角色
     */
    public void disable() {
        this.status = RoleStatus.DISABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 分配权限
     */
    public void assignPermissions(List<Permission> newPermissions) {
        if (newPermissions == null) {
            return;
        }
        
        this.permissions.clear();
        this.permissions.addAll(newPermissions);
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 添加单个权限
     */
    public void addPermission(Permission permission) {
        Objects.requireNonNull(permission, "权限不能为空");
        this.permissions.add(permission);
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 移除权限
     */
    public void removePermission(PermissionId permissionId) {
        Objects.requireNonNull(permissionId, "权限ID不能为空");
        this.permissions.removeIf(p -> p.getPermissionId().equals(permissionId));
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查是否拥有指定权限
     */
    public boolean hasPermission(PermissionId permissionId) {
        return this.permissions.stream()
                .anyMatch(p -> p.getPermissionId().equals(permissionId));
    }
    
    /**
     * 获取权限ID列表
     */
    public List<String> getPermissionIds() {
        return this.permissions.stream()
                .map(p -> p.getPermissionId().getValue())
                .collect(Collectors.toList());
    }
    
    /**
     * 检查角色是否可以删除
     */
    public boolean canDelete() {
        // 业务规则：只有禁用状态的角色才能删除
        return this.status.isDisabled();
    }
    
    /**
     * 验证角色名称是否符合政府角色命名规范
     */
    public boolean isValidGovernmentRoleName() {
        if (!this.category.isGovernment()) {
            return true; // 非政府角色不需要验证
        }
        // 政府角色命名规范：可以根据具体业务规则定义
        String name = this.roleName.getValue().toLowerCase();
        return name.contains("政府") || name.contains("gov") || name.contains("管理");
    }
    
    // Getters
    public RoleId getRoleId() {
        return roleId;
    }
    
    public RoleName getRoleName() {
        return roleName;
    }
    
    public String getRoleDesc() {
        return roleDesc;
    }
    
    public RoleStatus getStatus() {
        return status;
    }
    
    /**
     * 检查角色是否启用
     */
    public boolean isEnabled() {
        return status.isEnabled();
    }
    
    public RoleCategory getCategory() {
        return category;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
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
    
    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName=" + roleName +
                ", status=" + status +
                ", category=" + category +
                ", creator='" + creator + '\'' +
                '}';
    }
}