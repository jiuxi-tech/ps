package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 人员角色表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpPersonRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    private String personId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 部门id
     */
    private String deptId;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
