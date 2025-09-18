package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 更新菜单命令
 * 封装更新菜单操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class UpdateMenuCommand {
    
    @NotBlank(message = "菜单ID不能为空")
    private String menuId;
    
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;
    
    @Size(max = 100, message = "菜单标题长度不能超过100个字符")
    private String menuTitle;
    
    @Size(max = 200, message = "菜单路径长度不能超过200个字符")
    private String menuPath;
    
    @Size(max = 200, message = "菜单URI长度不能超过200个字符")
    private String menuUri;
    
    @Size(max = 50, message = "菜单图标长度不能超过50个字符")
    private String menuIcon;
    
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    private String component;
    
    private Boolean visible;
    
    private Boolean keepAlive;
    
    private Boolean external;
    
    private Integer orderIndex;
    
    @NotBlank(message = "操作者不能为空")
    private String operator;
    
    // 构造器
    public UpdateMenuCommand() {
    }
    
    public UpdateMenuCommand(String menuId, String menuName, String menuTitle, String operator) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuTitle = menuTitle;
        this.operator = operator;
    }
    
    // Getters and Setters
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
    
    public String getMenuPath() {
        return menuPath;
    }
    
    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
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
}