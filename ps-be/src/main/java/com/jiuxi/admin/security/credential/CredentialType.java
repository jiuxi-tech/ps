package com.jiuxi.admin.security.credential;

/**
 * 登录凭据类型枚举
 * 用于标识用户输入的登录凭据是账号、手机号还是身份证号
 * 
 * @author Qoder AI
 * @since 2024-12-15
 */
public enum CredentialType {
    
    /**
     * 手机号登录
     */
    PHONE("手机号"),
    
    /**
     * 身份证号登录
     */
    IDCARD("身份证号"),
    
    /**
     * 用户名登录
     */
    USERNAME("用户名");
    
    private final String description;
    
    CredentialType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
