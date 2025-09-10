package com.jiuxi.module.user.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户聚合根
 * 领域驱动设计中的用户实体，包含用户基本信息和账户信息
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class User {
    
    /**
     * 用户ID (聚合根标识)
     */
    private String personId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 用户基本信息
     */
    private UserProfile profile;
    
    /**
     * 用户账户信息
     */
    private UserAccount account;
    
    /**
     * 联系信息
     */
    private ContactInfo contactInfo;
    
    /**
     * 用户状态
     */
    private UserStatus status;
    
    /**
     * 用户类别 (个人/组织)
     */
    private UserCategory category;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造器
    public User() {
    }
    
    public User(String personId, UserProfile profile) {
        this.personId = personId;
        this.profile = profile;
        this.status = UserStatus.ACTIVE;
        this.createTime = LocalDateTime.now();
    }
    
    /**
     * 创建用户账户
     */
    public void createAccount(String username, String password) {
        if (this.account != null) {
            throw new IllegalStateException("用户已存在账户");
        }
        this.account = new UserAccount(username, password, this.personId);
    }
    
    /**
     * 更新用户资料
     */
    public void updateProfile(UserProfile newProfile) {
        this.profile = newProfile;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 激活用户
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用用户
     */
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查用户是否激活
     */
    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 检查用户是否有账户
     */
    public boolean hasAccount() {
        return this.account != null;
    }
    
    // Getters and Setters
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public UserProfile getProfile() {
        return profile;
    }
    
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
    
    public UserAccount getAccount() {
        return account;
    }
    
    public void setAccount(UserAccount account) {
        this.account = account;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public UserCategory getCategory() {
        return category;
    }
    
    public void setCategory(UserCategory category) {
        this.category = category;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public ContactInfo getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(personId, user.personId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "personId='" + personId + '\'' +
                ", profile=" + profile +
                ", status=" + status +
                ", category=" + category +
                '}';
    }
}