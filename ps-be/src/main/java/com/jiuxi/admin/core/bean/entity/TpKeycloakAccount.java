package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Keycloak账号关联表实体类
 * 
 * @author System
 * @since 2025-01-21
 */
@TableName("tp_keycloak_account")
public class TpKeycloakAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 关联账号ID（tp_account.ACCOUNT_ID）
     */
    private String accountId;

    /**
     * Keycloak客户端ID
     */
    private String kcClientId;

    /**
     * Keycloak客户端密钥（建议加密存储）
     */
    private String kcClientSecret;

    /**
     * 授权类型（password/client_credentials等）
     */
    private String kcGrantType;

    /**
     * Keycloak用户名
     */
    private String kcUsername;

    /**
     * Keycloak用户ID
     */
    private String kcUserId;

    /**
     * Keycloak密码（建议加密存储）
     */
    private String kcPassword;

    /**
     * 刷新令牌
     */
    private String kcRefreshToken;

    /**
     * 访问令牌
     */
    private String kcAccessToken;

    /**
     * 访问令牌过期时间
     */
    private LocalDateTime kcTokenExpiresAt;

    /**
     * 刷新令牌过期时间
     */
    private LocalDateTime kcRefreshExpiresAt;

    /**
     * 登出后重定向URI
     */
    private String kcPostLogoutRedirectUri;

    /**
     * Keycloak Realm名称
     */
    private String kcRealm;

    /**
     * Keycloak服务器地址
     */
    private String kcServerUrl;

    /**
     * 是否启用（0:禁用, 1:启用）
     */
    private Integer enabled;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后令牌刷新时间
     */
    private LocalDateTime lastTokenRefreshTime;

    /**
     * 创建时间（YYYYMMDDHHMMSS）
     */
    private String createTime;

    /**
     * 更新时间（YYYYMMDDHHMMSS）
     */
    private String updateTime;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 更新人ID
     */
    private String updater;

    /**
     * 租户ID（多租户支持）
     */
    private String tenantId;

    /**
     * 扩展字段1
     */
    private String extend01;

    /**
     * 扩展字段2
     */
    private String extend02;

    /**
     * 扩展字段3
     */
    private String extend03;

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getKcClientId() {
        return kcClientId;
    }

    public void setKcClientId(String kcClientId) {
        this.kcClientId = kcClientId;
    }

    public String getKcClientSecret() {
        return kcClientSecret;
    }

    public void setKcClientSecret(String kcClientSecret) {
        this.kcClientSecret = kcClientSecret;
    }

    public String getKcGrantType() {
        return kcGrantType;
    }

    public void setKcGrantType(String kcGrantType) {
        this.kcGrantType = kcGrantType;
    }

    public String getKcUsername() {
        return kcUsername;
    }

    public void setKcUsername(String kcUsername) {
        this.kcUsername = kcUsername;
    }

    public String getKcUserId() {
        return kcUserId;
    }

    public void setKcUserId(String kcUserId) {
        this.kcUserId = kcUserId;
    }

    public String getKcPassword() {
        return kcPassword;
    }

    public void setKcPassword(String kcPassword) {
        this.kcPassword = kcPassword;
    }

    public String getKcRefreshToken() {
        return kcRefreshToken;
    }

    public void setKcRefreshToken(String kcRefreshToken) {
        this.kcRefreshToken = kcRefreshToken;
    }

    public String getKcAccessToken() {
        return kcAccessToken;
    }

    public void setKcAccessToken(String kcAccessToken) {
        this.kcAccessToken = kcAccessToken;
    }

    public LocalDateTime getKcTokenExpiresAt() {
        return kcTokenExpiresAt;
    }

    public void setKcTokenExpiresAt(LocalDateTime kcTokenExpiresAt) {
        this.kcTokenExpiresAt = kcTokenExpiresAt;
    }

    public LocalDateTime getKcRefreshExpiresAt() {
        return kcRefreshExpiresAt;
    }

    public void setKcRefreshExpiresAt(LocalDateTime kcRefreshExpiresAt) {
        this.kcRefreshExpiresAt = kcRefreshExpiresAt;
    }

    public String getKcPostLogoutRedirectUri() {
        return kcPostLogoutRedirectUri;
    }

    public void setKcPostLogoutRedirectUri(String kcPostLogoutRedirectUri) {
        this.kcPostLogoutRedirectUri = kcPostLogoutRedirectUri;
    }

    public String getKcRealm() {
        return kcRealm;
    }

    public void setKcRealm(String kcRealm) {
        this.kcRealm = kcRealm;
    }

    public String getKcServerUrl() {
        return kcServerUrl;
    }

    public void setKcServerUrl(String kcServerUrl) {
        this.kcServerUrl = kcServerUrl;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public LocalDateTime getLastTokenRefreshTime() {
        return lastTokenRefreshTime;
    }

    public void setLastTokenRefreshTime(LocalDateTime lastTokenRefreshTime) {
        this.lastTokenRefreshTime = lastTokenRefreshTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    @Override
    public String toString() {
        return "TpKeycloakAccount{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", kcClientId='" + kcClientId + '\'' +
                ", kcClientSecret='" + kcClientSecret + '\'' +
                ", kcGrantType='" + kcGrantType + '\'' +
                ", kcUsername='" + kcUsername + '\'' +
                ", kcUserId='" + kcUserId + '\'' +
                ", kcPassword='" + kcPassword + '\'' +
                ", kcRefreshToken='" + kcRefreshToken + '\'' +
                ", kcAccessToken='" + kcAccessToken + '\'' +
                ", kcTokenExpiresAt=" + kcTokenExpiresAt +
                ", kcRefreshExpiresAt=" + kcRefreshExpiresAt +
                ", kcPostLogoutRedirectUri='" + kcPostLogoutRedirectUri + '\'' +
                ", kcRealm='" + kcRealm + '\'' +
                ", kcServerUrl='" + kcServerUrl + '\'' +
                ", enabled=" + enabled +
                ", lastLoginTime=" + lastLoginTime +
                ", lastTokenRefreshTime=" + lastTokenRefreshTime +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", creator='" + creator + '\'' +
                ", updater='" + updater + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", extend01='" + extend01 + '\'' +
                ", extend02='" + extend02 + '\'' +
                ", extend03='" + extend03 + '\'' +
                '}';
    }
}