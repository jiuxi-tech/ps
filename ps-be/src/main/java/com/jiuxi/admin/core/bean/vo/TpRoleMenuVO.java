package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 角色_菜单表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpRoleMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleId;
    /**
     * 菜单id
     */
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
