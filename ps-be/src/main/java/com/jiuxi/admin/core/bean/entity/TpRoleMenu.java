package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 角色_菜单表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
@TableName("tp_role_menu")
public class TpRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId
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
