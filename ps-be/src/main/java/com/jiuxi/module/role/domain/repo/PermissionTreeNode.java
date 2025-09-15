package com.jiuxi.module.role.domain.repo;

import com.jiuxi.module.role.domain.model.vo.PermissionId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 权限树节点
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class PermissionTreeNode {
    
    private final PermissionId permissionId;
    private final String menuId;
    private final String menuName;
    private final String parentId;
    private boolean assigned; // 是否已分配给角色
    private final List<PermissionTreeNode> children;
    
    public PermissionTreeNode(PermissionId permissionId, String menuId, String menuName, String parentId) {
        this.permissionId = Objects.requireNonNull(permissionId, "权限ID不能为空");
        this.menuId = menuId;
        this.menuName = menuName;
        this.parentId = parentId;
        this.assigned = false;
        this.children = new ArrayList<>();
    }
    
    public static PermissionTreeNode create(String permissionId, String menuId, String menuName, String parentId) {
        return new PermissionTreeNode(PermissionId.of(permissionId), menuId, menuName, parentId);
    }
    
    /**
     * 添加子节点
     */
    public void addChild(PermissionTreeNode child) {
        Objects.requireNonNull(child, "子节点不能为空");
        this.children.add(child);
    }
    
    /**
     * 移除子节点
     */
    public void removeChild(PermissionTreeNode child) {
        this.children.remove(child);
    }
    
    /**
     * 标记为已分配
     */
    public void markAsAssigned() {
        this.assigned = true;
    }
    
    /**
     * 标记为未分配
     */
    public void markAsUnassigned() {
        this.assigned = false;
    }
    
    /**
     * 检查是否为根节点
     */
    public boolean isRoot() {
        return parentId == null || parentId.trim().isEmpty();
    }
    
    /**
     * 检查是否为叶子节点
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /**
     * 检查是否有子节点
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    /**
     * 递归查找子节点
     */
    public PermissionTreeNode findChild(PermissionId childId) {
        for (PermissionTreeNode child : children) {
            if (child.getPermissionId().equals(childId)) {
                return child;
            }
            PermissionTreeNode found = child.findChild(childId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
    
    /**
     * 获取所有后代节点（包含自身）
     */
    public List<PermissionTreeNode> getAllDescendants() {
        List<PermissionTreeNode> descendants = new ArrayList<>();
        descendants.add(this);
        for (PermissionTreeNode child : children) {
            descendants.addAll(child.getAllDescendants());
        }
        return descendants;
    }
    
    /**
     * 统计子孙节点数量
     */
    public int getDescendantCount() {
        int count = children.size();
        for (PermissionTreeNode child : children) {
            count += child.getDescendantCount();
        }
        return count;
    }
    
    // Getters
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
    
    public boolean isAssigned() {
        return assigned;
    }
    
    public List<PermissionTreeNode> getChildren() {
        return new ArrayList<>(children);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionTreeNode that = (PermissionTreeNode) o;
        return Objects.equals(permissionId, that.permissionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }
    
    @Override
    public String toString() {
        return "PermissionTreeNode{" +
                "permissionId=" + permissionId +
                ", menuName='" + menuName + '\'' +
                ", assigned=" + assigned +
                ", childrenCount=" + children.size() +
                '}';
    }
}