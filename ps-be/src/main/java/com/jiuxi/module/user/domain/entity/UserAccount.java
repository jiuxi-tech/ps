package com.jiuxi.module.user.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户账户实体
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class UserAccount {
    
    /**
     * 账户ID
     */
    private String accountId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码 (加密后)
     */
    private String password;
    
    /**
     * 关联的用户ID
     */
    private String personId;
    
    /**
     * 账户状态
     */
    private AccountStatus status;
    
    /**
     * 是否锁定
     */
    private Boolean locked;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;
    
    /**
     * Keycloak ID (外部系统ID)
     */
    private String keycloakId;
    
    /**
     * 第三方登录ID
     */
    private String thirdId;
    
    /**
     * 微信OpenID
     */
    private String weixinOpenId;
    
    /**
     * 钉钉ID
     */
    private String dingdingId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 构造器
    public UserAccount() {
    }
    
    public UserAccount(String username, String password, String personId) {
        this.username = username;
        this.password = password;
        this.personId = personId;
        this.status = AccountStatus.ACTIVE;
        this.locked = false;
        this.enabled = true;
        this.createTime = LocalDateTime.now();
    }
    
    /**
     * 锁定账户
     */
    public void lock() {
        this.locked = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 解锁账户
     */
    public void unlock() {
        this.locked = false;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用账户
     */
    public void enable() {
        this.enabled = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用账户
     */
    public void disable() {
        this.enabled = false;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新密码
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置过期时间
     */
    public void setExpirationTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查账户是否可用
     */
    public boolean isAvailable() {
        return enabled && !locked && !isExpired();
    }
    
    /**
     * 检查账户是否过期
     */
    public boolean isExpired() {
        return expiredTime != null && LocalDateTime.now().isAfter(expiredTime);
    }
    
    /**
     * 绑定Keycloak
     */
    public void bindKeycloak(String keycloakId) {
        this.keycloakId = keycloakId;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 绑定第三方登录
     */
    public void bindThirdParty(String thirdId, String weixinOpenId, String dingdingId) {
        this.thirdId = thirdId;
        this.weixinOpenId = weixinOpenId;
        this.dingdingId = dingdingId;
        this.updateTime = LocalDateTime.now();
    }
    
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
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
    
    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }
    
    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public String getKeycloakId() {
        return keycloakId;
    }
    
    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }
    
    public String getThirdId() {
        return thirdId;
    }
    
    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }
    
    public String getWeixinOpenId() {
        return weixinOpenId;
    }
    
    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }
    
    public String getDingdingId() {
        return dingdingId;
    }
    
    public void setDingdingId(String dingdingId) {
        this.dingdingId = dingdingId;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(accountId, that.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
    
    @Override
    public String toString() {
        return "UserAccount{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", personId='" + personId + '\'' +
                ", status=" + status +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}