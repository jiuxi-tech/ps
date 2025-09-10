package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 人员部门表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpPersonDeptVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    private String personId;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 默认所属部门 （1是所属部门，0是兼职部门）
     */
    private Integer defaultDept;

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

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(Integer defaultDept) {
        this.defaultDept = defaultDept;
    }
}
