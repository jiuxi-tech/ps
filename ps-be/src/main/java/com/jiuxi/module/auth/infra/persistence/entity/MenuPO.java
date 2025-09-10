package com.jiuxi.module.auth.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 菜单持久化对象
 * 对应数据库中的菜单表
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("sys_menu")
public class MenuPO {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 菜单编码
     */
    @TableField("menu_code")
    private String menuCode;
    
    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;
    
    /**
     * 菜单标题
     */
    @TableField("menu_title")
    private String menuTitle;
    
    /**
     * 父菜单ID
     */
    @TableField("parent_menu_id")
    private String parentMenuId;
    
    /**
     * 菜单路径
     */
    @TableField("menu_path")
    private String menuPath;
    
    /**
     * 菜单层级
     */
    @TableField("menu_level")
    private Integer menuLevel;
    
    /**
     * 菜单类型
     */
    @TableField("menu_type")
    private String menuType;
    
    /**
     * 菜单URI
     */
    @TableField("menu_uri")
    private String menuUri;
    
    /**
     * 菜单图标
     */
    @TableField("menu_icon")
    private String menuIcon;
    
    /**
     * 组件路径
     */
    @TableField("component")
    private String component;
    
    /**
     * 菜单状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 是否可见
     */
    @TableField("visible")
    private Boolean visible;
    
    /**
     * 是否需要缓存
     */
    @TableField("keep_alive")
    private Boolean keepAlive;
    
    /**
     * 是否外链
     */
    @TableField("external")
    private Boolean external;
    
    /**
     * 是否叶子节点
     */
    @TableField("leaf")
    private Boolean leaf;
    
    /**
     * 排序序号
     */
    @TableField("order_index")
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    @TableField("creator")
    private String creator;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    @TableField("updator")
    private String updator;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getMenuType() {
        return menuType;
    }
    
    public void setMenuType(String menuType) {
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
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
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}