package com.jiuxi.module.sys.domain.entity;

/**
 * 配置状态枚举
 * 定义配置的生命周期状态
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum ConfigStatus {
    
    /**
     * 激活状态
     */
    ACTIVE("ACTIVE", "激活"),
    
    /**
     * 禁用状态
     */
    INACTIVE("INACTIVE", "禁用"),
    
    /**
     * 草稿状态
     */
    DRAFT("DRAFT", "草稿"),
    
    /**
     * 待审核状态
     */
    PENDING("PENDING", "待审核");
    
    private final String code;
    private final String description;
    
    ConfigStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取配置状态
     * @param code 代码
     * @return 配置状态
     */
    public static ConfigStatus fromCode(String code) {
        for (ConfigStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的配置状态代码: " + code);
    }
    
    /**
     * 检查是否是激活状态
     * @return 是否是激活状态
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 检查是否可以编辑
     * @return 是否可以编辑
     */
    public boolean isEditable() {
        return this == DRAFT || this == INACTIVE;
    }
    
    /**
     * 检查是否需要审核
     * @return 是否需要审核
     */
    public boolean needsApproval() {
        return this == PENDING;
    }
}