package com.jiuxi.module.user.domain.model.aggregate;

import com.jiuxi.module.user.domain.event.UserCreatedEvent;
import com.jiuxi.module.user.domain.event.UserAccountCreatedEvent;
import com.jiuxi.module.user.domain.event.UserProfileUpdatedEvent;
import com.jiuxi.module.user.domain.event.UserEvent;
import com.jiuxi.module.user.domain.model.entity.UserAccount;
import com.jiuxi.module.user.domain.model.vo.ContactInfo;
import com.jiuxi.module.user.domain.model.vo.UserProfile;
import com.jiuxi.module.user.domain.model.vo.UserStatus;
import com.jiuxi.module.user.domain.model.vo.UserCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    
    /**
     * 未发布的领域事件
     */
    private final List<UserEvent> domainEvents = new ArrayList<>();
    
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
     * 创建新用户（工厂方法）
     */
    public static User create(String personId, UserProfile profile, UserCategory category, String creator, String tenantId) {
        User user = new User();
        user.personId = personId;
        user.profile = profile;
        user.category = category;
        user.status = UserStatus.ACTIVE;
        user.creator = creator;
        user.createTime = LocalDateTime.now();
        user.tenantId = tenantId;
        
        // 发布用户创建事件
        user.addDomainEvent(new UserCreatedEvent(
            personId, 
            profile != null ? profile.getPersonName() : null, 
            category, 
            creator, 
            tenantId
        ));
        
        return user;
    }
    
    /**
     * 创建用户账户
     */
    public void createAccount(String username, String password, String creator) {
        if (this.account != null) {
            throw new IllegalStateException("用户已存在账户");
        }
        this.account = new UserAccount(username, password, this.personId);
        this.updateTime = LocalDateTime.now();
        this.updator = creator;
        
        // 发布账户创建事件
        addDomainEvent(new UserAccountCreatedEvent(
            this.personId, 
            this.account.getAccountId(), 
            username, 
            creator
        ));
    }
    
    /**
     * 更新用户资料
     */
    public void updateProfile(UserProfile newProfile, String updator, String[] updatedFields, String reason) {
        this.profile = newProfile;
        this.updateTime = LocalDateTime.now();
        this.updator = updator;
        
        // 发布资料更新事件
        addDomainEvent(new UserProfileUpdatedEvent(
            this.personId, 
            updator, 
            updatedFields, 
            reason
        ));
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
    
    /**
     * 添加领域事件
     */
    private void addDomainEvent(UserEvent event) {
        this.domainEvents.add(event);
    }
    
    /**
     * 获取未发布的领域事件
     */
    public List<UserEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }
    
    /**
     * 清除已发布的领域事件
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
    
    /**
     * 检查是否有未发布的事件
     */
    public boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
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