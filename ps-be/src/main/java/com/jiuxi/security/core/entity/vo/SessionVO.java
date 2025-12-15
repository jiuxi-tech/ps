package com.jiuxi.security.core.entity.vo;


/**
 * @ClassName: SessionVO
 * @Description: 会话 VO
 * @Author: Ypp
 * @Date: 2023/10/17 19:34
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 *
 * 过期，请使用 com.jiuxi.common.bean.SessionVO
 *
 */
@Deprecated
public class SessionVO {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 单位id
     */
    private String ascnId;

    /**
     * 账号id
     */
    private String accountId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 人员Name
     */
    private String personName;

    /**
     * 角色ids
     */
    private String roleIds;

    /**
     * 登录的部门id
     */
    private String deptId;

    /**
     * 部门层级编码 单位部门层级code以三位数字递增，自动生成。可以根据此字段统计本级及下级
     */
    private String deptLevelcode;

    /**
     * 登录的部门全称
     */
    private String deptFullName;

    /**
     * 登录的部门简称
     */
    private String deptSimpleName;

    /**
     * 当前登陆人所在部门的行政区划code
     */
    private String cityCode;


    /**
     * 部门类别 政府or企业 0政府 1企业 2其他
     */
    private Integer category;

    /**
     * 是否是主部门，1是所属部门，0是兼职部门
     */
    private Integer defaultDept;


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptLevelcode() {
        return deptLevelcode;
    }

    public void setDeptLevelcode(String deptLevelcode) {
        this.deptLevelcode = deptLevelcode;
    }

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

    public String getDeptSimpleName() {
        return deptSimpleName;
    }

    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(Integer defaultDept) {
        this.defaultDept = defaultDept;
    }
}
