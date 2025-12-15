package com.jiuxi.security.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuxi.shared.common.base.vo.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 账号基本信息
 * @ClassName: PersonBasicVO
 * @Author: pdd
 * @Date: 2020-08-27 09:37
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AccountVO extends BaseVO  implements Serializable {

    private static final long serialVersionUID = -5731609031276947720L;

    /**
     * 账号id
     */
    @JsonIgnore
    private String accountId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 租户id
     */
    private String ascnId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 角色ids
     */
    private String roleIds;

    /**
     * 部门ids
     */
    private String deptIds;

    /**
     * 当前登陆人所在部门的行政区划code
     */
    private String cityCode;

    /**
     * 是否锁住
     */
    private String locked;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    @JsonIgnore
    private String userpwd;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 短信验证码
     */
    @JsonIgnore
    private String code;

    /**
     * 验证码 票据
     */
    @JsonIgnore
    private String ticket;


    /**
     * 统一从返回数据获取token，uni-app无法从header中获取token
     */
    private String token;

    /**
     * 是否重置密码
     */
    private String restPwd;

    /**
     * 部门类别 政府or企业 0政府 1企业 2其他
     */
    private Integer category;

    /**
     * 账号过期时间
     */
    private String expiredTime;

    /**
     * 上次密码修改时间
     */
    private String lastPasswordChangeTime;

    /**
     * 兼职部门的时候，部门列表
     */
    private List<DeptVO> deptVOList;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public List<DeptVO> getDeptVOList() {
        return deptVOList;
    }

    public void setDeptVOList(List<DeptVO> deptVOList) {
        this.deptVOList = deptVOList;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getRestPwd() {
        return restPwd;
    }

    public void setRestPwd(String restPwd) {
        this.restPwd = restPwd;
    }

    public String getLastPasswordChangeTime() {
        return lastPasswordChangeTime;
    }

    public void setLastPasswordChangeTime(String lastPasswordChangeTime) {
        this.lastPasswordChangeTime = lastPasswordChangeTime;
    }
}
