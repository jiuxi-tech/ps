package com.jiuxi.admin.core.bean.query;

/**
 * @ClassName: TpDataPermissionsQuery
 * @Description: TODO
 * @Author: 杨攀
 * @Date: 2023/11/1 15:12
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public class TpDataPermissionsQuery {

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

    public String getPerId() {
        return perId;
    }

    public void setPerId(String perId) {
        this.perId = perId;
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

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getExtend01() {
        return extend01;
    }

    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }

    public String getExtend02() {
        return extend02;
    }

    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }

    public String getExtend03() {
        return extend03;
    }

    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }
}
