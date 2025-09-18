package com.jiuxi.module.auth.app.dto;

import com.jiuxi.module.auth.domain.model.entity.MenuType;
import com.jiuxi.module.auth.domain.model.entity.MenuStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单响应DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class MenuResponseDTO {
    
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
     * 是否可见
     */
    private Boolean visible;
    
    /**
     * 是否需要缓存
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
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 子菜单列表
     */
    private List<MenuResponseDTO> children;
    
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
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public List<MenuResponseDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<MenuResponseDTO> children) {
        this.children = children;
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
}