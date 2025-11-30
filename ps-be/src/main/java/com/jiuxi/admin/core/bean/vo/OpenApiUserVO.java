package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 开放API - 用户信息VO（脱敏后）
 * 
 * @author system
 * @date 2025-01-30
 */
public class OpenApiUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 人员姓名（脱敏）
     */
    private String personName;

    /**
     * 人员编号（脱敏）
     */
    private String personNo;

    /**
     * 性别：1-男，2-女
     */
    private Integer sex;

    /**
     * 性别名称
     */
    private String sexName;

    /**
     * 手机号码（脱敏）
     */
    private String phone;

    /**
     * 电子邮箱（脱敏）
     */
    private String email;

    /**
     * 职位
     */
    private String office;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 状态：1-有效，0-无效
     */
    private Integer actived;

    /**
     * 创建时间
     */
    private String createTime;

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

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
