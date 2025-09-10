package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpRoleQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 创建人
     */
    @JsonIgnore
    private String[] createRoles;

    /**
     * 创建人所在单位id
     */
    private String ascnId;

    /**
     * 人员类别
     */
    @JsonIgnore
    private Integer category;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
