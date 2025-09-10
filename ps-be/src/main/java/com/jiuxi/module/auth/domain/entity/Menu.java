package com.jiuxi.module.auth.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 菜单实体
 * 表示系统中的菜单项
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class Menu {
    
    /**
     * 菜单ID
     */
    private String menuId;
    
    /**
     * 菜单编码
     */
    private String menuCode;
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 菜单标题
     */
    private String menuTitle;
    
    /**
     * 父菜单ID
     */
    private String parentMenuId;
    
    /**
     * 菜单路径
     */
    private String menuPath;
    
    /**
     * 菜单层级
     */
    private Integer menuLevel;
    
    /**
     * 菜单类型
     */
    private MenuType menuType;
    
    /**
     * 菜单URI
     */
    private String menuUri;
    
    /**
     * 菜单图标
     */
    private String menuIcon;
    
    /**
     * 组件路径
     */
    private String component;
    
    /**
     * 菜单状态
     */
    private MenuStatus status;
    
    /**
     * 是否显示
     */
    private Boolean visible;
    
    /**
     * 是否缓存
     */
    private Boolean keepAlive;
    
    /**
     * 是否外链
     */
    private Boolean external;
    
    /**
     * 是否叶子节点
     */
    private Boolean leaf;
    
    /**
     * 子菜单列表
     */
    private List<Menu> children;
    
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
    public Menu() {
        this.children = new ArrayList<>();
        this.status = MenuStatus.ACTIVE;
        this.visible = true;
        this.keepAlive = false;
        this.external = false;
        this.leaf = false;
    }
    
    public Menu(String menuCode, String menuName, MenuType menuType) {
        this();
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuType = menuType;
    }
    
    // 业务方法
    
    /**
     * 添加子菜单
     */
    public void addChild(Menu child) {
        if (child != null && !this.children.contains(child)) {
            child.setParentMenuId(this.menuId);
            child.setMenuLevel(this.menuLevel + 1);
            this.children.add(child);
            this.leaf = false;
        }
    }
    
    /**
     * 移除子菜单
     */
    public void removeChild(Menu child) {
        this.children.remove(child);
        if (this.children.isEmpty()) {
            this.leaf = true;
        }
    }
    
    /**
     * 清空所有子菜单
     */
    public void clearChildren() {
        this.children.clear();
        this.leaf = true;
    }
    
    /**
     * 启用菜单
     */
    public void enable() {
        this.status = MenuStatus.ACTIVE;
    }
    
    /**
     * 停用菜单
     */
    public void disable() {
        this.status = MenuStatus.INACTIVE;
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return MenuStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 显示菜单
     */
    public void show() {
        this.visible = true;
    }
    
    /**
     * 隐藏菜单
     */
    public void hide() {
        this.visible = false;
    }
    
    /**
     * 检查是否可见
     */
    public boolean isVisible() {
        return Boolean.TRUE.equals(this.visible);
    }
    
    /**
     * 检查是否是目录
     */
    public boolean isDirectory() {
        return MenuType.DIRECTORY.equals(this.menuType);
    }
    
    /**
     * 检查是否是菜单
     */
    public boolean isMenu() {
        return MenuType.MENU.equals(this.menuType);
    }
    
    /**
     * 检查是否是按钮
     */
    public boolean isButton() {
        return MenuType.BUTTON.equals(this.menuType);
    }
    
    /**
     * 检查是否是叶子节点
     */
    public boolean isLeaf() {
        return Boolean.TRUE.equals(this.leaf) || this.children.isEmpty();
    }
    
    /**
     * 检查是否是外链
     */
    public boolean isExternal() {
        return Boolean.TRUE.equals(this.external);
    }
    
    /**
     * 设置为外链
     */
    public void setAsExternal() {
        this.external = true;
    }
    
    /**
     * 检查是否需要缓存
     */
    public boolean needKeepAlive() {
        return Boolean.TRUE.equals(this.keepAlive);
    }
    
    /**
     * 启用缓存
     */
    public void enableKeepAlive() {
        this.keepAlive = true;
    }
    
    /**
     * 禁用缓存
     */
    public void disableKeepAlive() {
        this.keepAlive = false;
    }
    
    // Getters and Setters
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    
    public String getMenuCode() {
        return menuCode;
    }
    
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    public String getMenuTitle() {
        return menuTitle;
    }
    
    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
    
    public String getParentMenuId() {
        return parentMenuId;
    }
    
    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }
    
    public String getMenuPath() {
        return menuPath;
    }
    
    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }
    
    public Integer getMenuLevel() {
        return menuLevel;
    }
    
    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }
    
    public MenuType getMenuType() {
        return menuType;
    }
    
    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }
    
    public String getMenuUri() {
        return menuUri;
    }
    
    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri;
    }
    
    public String getMenuIcon() {
        return menuIcon;
    }
    
    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public MenuStatus getStatus() {
        return status;
    }
    
    public void setStatus(MenuStatus status) {
        this.status = status;
    }
    
    public Boolean getVisible() {
        return visible;
    }
    
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public Boolean getKeepAlive() {
        return keepAlive;
    }
    
    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    public Boolean getExternal() {
        return external;
    }
    
    public void setExternal(Boolean external) {
        this.external = external;
    }
    
    public Boolean getLeaf() {
        return leaf;
    }
    
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }
    
    public List<Menu> getChildren() {
        return children;
    }
    
    public void setChildren(List<Menu> children) {
        this.children = children != null ? children : new ArrayList<>();
        this.leaf = this.children.isEmpty();
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(menuId, menu.menuId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}