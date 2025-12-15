package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * @ClassName: TpDataPermissions
 * @Description: 人员数据权限表
 * @Author: Ypp
 * @Date: 2023/11/1 14:54
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpDataPermissions implements Serializable {


    private static final long serialVersionUID = -7049581571943893759L;

    /**
     * 主键
     */
    private String perId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 单位ID
     */
    private String ascnId;

    /**
     * 备用
     */
    private String extend01;

    /**
     * 备用
     */
    private String extend02;

    /**
     * 备用
     */
    private String extend03;

    /**
     * 主键
     */
    public String getPerId() {
        return this.perId;
    }

    /**
     * 主键
     */
    public void setPerId(String perId) {
        this.perId = perId;
    }

    /**
     * 人员ID
     */
    public String getPersonId() {
        return this.personId;
    }

    /**
     * 人员ID
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * 部门ID
     */
    public String getDeptId() {
        return this.deptId;
    }

    /**
     * 部门ID
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 单位ID
     */
    public String getAscnId() {
        return this.ascnId;
    }

    /**
     * 单位ID
     */
    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    /**
     * 备用
     */
    public String getExtend01() {
        return this.extend01;
    }

    /**
     * 备用
     */
    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }

    /**
     * 备用
     */
    public String getExtend02() {
        return this.extend02;
    }

    /**
     * 备用
     */
    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }

    /**
     * 备用
     */
    public String getExtend03() {
        return this.extend03;
    }

    /**
     * 备用
     */
    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }
}
