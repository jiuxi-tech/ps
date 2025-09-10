package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 角色表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpRoleAuthQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 创建人所在单位id
     */
    private String ascnId;

    /**
     * 当前登陆人id
     */
    private String personId;

    /**
     * 当前登陆人所在部门id
     */
    private String deptId;

    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 创建人
     */
    @JsonIgnore
    private String[] createRoles;

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String[] getCreateRoles() {
        return createRoles;
    }

    public void setCreateRoles(String[] createRoles) {
        this.createRoles = createRoles;
    }
}
