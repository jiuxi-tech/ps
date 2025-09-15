package com.jiuxi.module.role.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 角色菜单关联PO
 * 
 * @author DDD重构
 * @date 2025-09-16
 */
@TableName("tp_role_menu")
public class RoleMenuPO {
    
    private String roleId;
    private String menuId;
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}