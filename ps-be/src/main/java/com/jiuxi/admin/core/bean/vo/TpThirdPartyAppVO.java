package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 第三方应用VO
 *
 * @author system
 * @date 2024-01-18 11:05:17
 */
public class TpThirdPartyAppVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    @NotBlank(message = "应用ID不能为空", groups = UpdateGroup.class)
    private String appId;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String appName;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * API密钥（仅用于创建时返回明文，不存储）
     */
    private String apiSecret;

    /**
     * 状态（1:启用 0:禁用）
     */
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer status;

    /**
     * 过期时间
     */
    private String expireTime;

    /**
     * IP白名单（多个IP用逗号分隔）
     */
    private String ipWhitelist;

    /**
     * 访问频率限制（每秒请求数）
     */
    private Integer rateLimit;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 最后调用时间
     */
    private String lastCallTime;

    /**
     * 创建人
     */
    @NotBlank(message = "创建人不能为空", groups = UpdateGroup.class)
    private String creator;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人姓名
     */
    private String createPersonName;

    /**
     * 修改人
     */
    private String updator;

    /**
     * 修改人姓名
     */
    private String updatePersonName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 已授权的API ID列表
     */
    private List<String> apiIds;

    /**
     * 数据密钥
     */
    private String passKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpWhitelist() {
        return ipWhitelist;
    }

    public void setIpWhitelist(String ipWhitelist) {
        this.ipWhitelist = ipWhitelist;
    }

    public Integer getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLastCallTime() {
        return lastCallTime;
    }

    public void setLastCallTime(String lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdatePersonName() {
        return updatePersonName;
    }

    public void setUpdatePersonName(String updatePersonName) {
        this.updatePersonName = updatePersonName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getApiIds() {
        return apiIds;
    }

    public void setApiIds(List<String> apiIds) {
        this.apiIds = apiIds;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpThirdPartyAppVO that = (TpThirdPartyAppVO) o;
        return appId.equals(that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId);
    }
}
