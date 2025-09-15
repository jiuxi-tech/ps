package com.jiuxi.module.user.app.command.dto;

import com.jiuxi.module.user.domain.model.vo.UserCategory;

/**
 * 创建用户命令
 */
public class UserCreateCommand {
    
    private String username;
    private String password;
    private String phone;
    private String email;
    private UserCategory category;
    private String tenantId;
    private String operator;
    
    // Getters and Setters
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
    
    public UserCategory getCategory() {
        return category;
    }
    
    public void setCategory(UserCategory category) {
        this.category = category;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
}