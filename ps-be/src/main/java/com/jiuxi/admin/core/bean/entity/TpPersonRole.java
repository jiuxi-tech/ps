package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 人员角色表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
@TableName("tp_person_role")
public class TpPersonRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @TableId
    private String personId;
    /**
     * 角色id
     */
    private String roleId;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

}
