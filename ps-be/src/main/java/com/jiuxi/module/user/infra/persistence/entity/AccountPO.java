package com.jiuxi.module.user.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 用户账户持久化对象
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("tp_account")
public class AccountPO {
    
    @TableId
    private String accountId;
    private String username;
    private String userpwd;
    private String phone;
    private LocalDateTime expiredTime;
    private Boolean locked;
    private Boolean enabled;
    private String personId;
    private String tenantId;
    private String weixin;
    private String dingding;
    private String threeId;
    private String keycloakId;
    private Integer actived;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String extend01;
    private String extend02;
    private String extend03;
    
    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
    
    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }
    
    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public Boolean getLocked() {
        return locked;
    }
    
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getWeixin() {
        return weixin;
    }
    
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    
    public String getDingding() {
        return dingding;
    }
    
    public void setDingding(String dingding) {
        this.dingding = dingding;
    }
    
    public String getThreeId() {
        return threeId;
    }
    
    public void setThreeId(String threeId) {
        this.threeId = threeId;
    }
    
    public String getKeycloakId() {
        return keycloakId;
    }
    
    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }
    
    public Integer getActived() {
        return actived;
    }
    
    public void setActived(Integer actived) {
        this.actived = actived;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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