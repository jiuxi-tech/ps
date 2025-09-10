package com.jiuxi.security.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuxi.core.bean.BaseVO;

import java.io.Serializable;

/**
 * 人员基本信息表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class PersonVO extends BaseVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    private String personId;
    /**
     * 人员姓名
     */
    private String personName;
    private String userName;

    /**
     * 密码
     */
    @JsonIgnore
    private String userpwd;

    /**
     * 头像地址
     */
    private String profilePhoto;
    /**
     * 人员编号
     */
    private String personNo;
    /**
     * 性别
     */
    private String sexName;
    /**
     * 身份证号
     */
    private String idcard;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 职位
     */
    private String office;

    /**
     * 所属机构（单位）id 政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
     */
    private String ascnId;
    private String ascnName;
    /**
     * 部门类别 政府or企业 0政府 1企业 2其他
     */
    private Integer category;

    /**
     * 部门信息
     */
    private String deptId;
    private String deptFullName;

    /**
     * 角色ids
     */
    private String roleIds;

    /**
     * 当前登陆人所在部门的行政区划code
     */
    private String cityCode;
    /**
     * 当前登陆人所在部门的行政区划名称
     */
    private String cityName;

    /**
     * 是否需要修改默认密码，1：修改修改   0：否
     */
    private Integer updatePwdFlag;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getAscnName() {
        return ascnName;
    }

    public void setAscnName(String ascnName) {
        this.ascnName = ascnName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getUpdatePwdFlag() {
        return updatePwdFlag;
    }

    public void setUpdatePwdFlag(Integer updatePwdFlag) {
        this.updatePwdFlag = updatePwdFlag;
    }
}
