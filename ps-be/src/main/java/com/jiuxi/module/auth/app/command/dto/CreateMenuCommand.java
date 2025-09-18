package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 创建菜单命令
 * 封装创建菜单操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class CreateMenuCommand {
    
    @NotBlank(message = "菜单编码不能为空")
    @Size(max = 50, message = "菜单编码长度不能超过50个字符")
    private String menuCode;
    
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;
    
    @Size(max = 100, message = "菜单标题长度不能超过100个字符")
    private String menuTitle;
    
    private String parentMenuId;
    
    @Size(max = 200, message = "菜单路径长度不能超过200个字符")
    private String menuPath;
    
    private String menuType;
    
    @Size(max = 200, message = "菜单URI长度不能超过200个字符")
    private String menuUri;
    
    @Size(max = 50, message = "菜单图标长度不能超过50个字符")
    private String menuIcon;
    
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    private String component;
    
    private Boolean visible = true;
    
    private Boolean keepAlive = false;
    
    private Boolean external = false;
    
    private Integer orderIndex;
    
    @NotBlank(message = "操作者不能为空")
    private String operator;
    
    @NotBlank(message = "租户ID不能为空")
    private String tenantId;
    
    // 构造器
    public CreateMenuCommand() {
    }
    
    public CreateMenuCommand(String menuCode, String menuName, String menuTitle, 
                           String operator, String tenantId) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuTitle = menuTitle;
        this.operator = operator;
        this.tenantId = tenantId;
    }
    
    // Getters and Setters
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
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}