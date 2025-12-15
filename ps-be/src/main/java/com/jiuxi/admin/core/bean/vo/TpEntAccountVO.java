package com.jiuxi.admin.core.bean.vo;

/**
 * @Description: 企业账户返回对象
 * @ClassName: TpEntAccountVO
 * @Author: pdd
 * @Date: 2021-09-13 10:16
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpEntAccountVO {

    /**
     * 账号id
     */
    private String accountId;
    /**
     * 人员名称
     */
    private String personId;
    private String personName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户名/账号
     */
    private String userName;
    /**
     * 账号过期时间
     */
    private String expiredTime;
    /**
     * 是否冻结
     */
    private Integer locked;
    /**
     * 是否启用
     */
    private Integer enabled;
    /**
     * 钉钉id
     */
    private String dingding;
    /**
     * 微信id
     */
    private String weixin;
    /**
     * 第三方id
     */
    private String threeId;
    /**
     * 数据密钥
     */
    private String passKey;

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDingding() {
        return dingding;
    }

    public void setDingding(String dingding) {
        this.dingding = dingding;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getThreeId() {
        return threeId;
    }

    public void setThreeId(String threeId) {
        this.threeId = threeId;
    }
}
