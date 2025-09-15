package com.jiuxi.module.role.domain.model.entity;

import com.jiuxi.module.role.domain.model.vo.PermissionId;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 权限实体
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class Permission {
    
    private final PermissionId permissionId;
    private final String menuId;
    private final String menuName;
    private final String parentId;
    private final String menuType;
    private final Integer orderNum;
    private final String path;
    private final String component;
    private final String icon;
    private final boolean enabled;
    private final String permCode;
    private final String remark;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    
    public Permission(PermissionId permissionId, String parentId, String menuName, String menuId, 
                     String menuType, Integer orderNum, String path, String component, String icon,
                     boolean enabled, String permCode, String remark, LocalDateTime createTime, LocalDateTime updateTime) {
        this.permissionId = Objects.requireNonNull(permissionId, "权限ID不能为空");
        this.menuId = menuId;
        this.menuName = menuName;
        this.parentId = parentId;
        this.menuType = menuType;
        this.orderNum = orderNum;
        this.path = path;
        this.component = component;
        this.icon = icon;
        this.enabled = enabled;
        this.permCode = permCode;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
    public static Permission create(String permissionId, String menuId, String menuName, String parentId) {
        LocalDateTime now = LocalDateTime.now();
        return new Permission(PermissionId.of(permissionId), parentId, menuName, menuId, 
                            "M", 0, null, null, null, true, null, null, now, now);
    }
    
    public PermissionId getPermissionId() {
        return permissionId;
    }
    
    public String getMenuId() {
        return menuId;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public String getMenuType() {
        return menuType;
    }
    
    public Integer getOrderNum() {
        return orderNum;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getComponent() {
        return component;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public String getPermCode() {
        return permCode;
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
    
    /**
     * 检查是否为根权限（无父权限）
     */
    public boolean isRoot() {
        return parentId == null || parentId.trim().isEmpty();
    }
    
    /**
     * 检查是否为指定父权限的子权限
     */
    public boolean isChildOf(String parentPermissionId) {
        return Objects.equals(this.parentId, parentPermissionId);
    }
    
    /**
     * 重建权限实体（用于数据持久化重建）
     */
    public static Permission rebuild(PermissionId permissionId, String parentId, String menuName, 
                                   String menuId, String menuType, Integer orderNum, String path,
                                   String component, String icon, boolean enabled, String permCode,
                                   String remark, java.time.LocalDateTime createTime, java.time.LocalDateTime updateTime) {
        return new Permission(permissionId, parentId, menuName, menuId, menuType, orderNum, 
                            path, component, icon, enabled, permCode, remark, createTime, updateTime);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(permissionId, that.permissionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}