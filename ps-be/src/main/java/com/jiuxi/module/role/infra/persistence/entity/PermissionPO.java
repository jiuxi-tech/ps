package com.jiuxi.module.role.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 权限持久化对象（PO）
 * 对应数据库表结构
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@TableName("tp_menu")
public class PermissionPO {
    
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private String menuId;
    
    @TableField("parent_id")
    private String parentId;
    
    @TableField("menu_name")
    private String menuName;
    
    @TableField("menu_type")
    private String menuType;
    
    @TableField("order_num")
    private Integer orderNum;
    
    @TableField("path")
    private String path;
    
    @TableField("component")
    private String component;
    
    @TableField("icon")
    private String icon;
    
    @TableField("is_enabled")
    private Integer isEnabled;
    
    @TableField("perms")
    private String perms;
    
    @TableField("remark")
    private String remark;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    
    public PermissionPO() {}
    
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    public String getMenuType() {
        return menuType;
    }
    
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    
    public Integer getOrderNum() {
        return orderNum;
    }
    
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public String getPerms() {
        return perms;
    }
    
    public void setPerms(String perms) {
        this.perms = perms;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @Override
    public String toString() {
        return "PermissionPO{" +
                "menuId='" + menuId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuType='" + menuType + '\'' +
                ", orderNum=" + orderNum +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", icon='" + icon + '\'' +
                ", isEnabled=" + isEnabled +
                ", perms='" + perms + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }
}