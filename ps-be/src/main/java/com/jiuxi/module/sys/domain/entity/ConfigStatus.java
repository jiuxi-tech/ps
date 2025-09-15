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
    PENDING("PENDING", "待审核"),
    
    /**
     * 启用状态 (兼容system模块)
     */
    ENABLED("ENABLED", "启用"),
    
    /**
     * 禁用状态 (兼容system模块)
     */
    DISABLED("DISABLED", "禁用");
    
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
    
    /**
     * 检查是否启用 (兼容system模块)
     */
    public boolean isEnabled() {
        return this == ENABLED || this == ACTIVE;
    }
    
    /**
     * 检查是否禁用 (兼容system模块)
     */
    public boolean isDisabled() {
        return this == DISABLED || this == INACTIVE;
    }
    
    /**
     * 获取Integer类型的代码 (兼容system模块)
     */
    public Integer getIntegerCode() {
        switch (this) {
            case ENABLED:
            case ACTIVE:
                return 1;
            case DISABLED:
            case INACTIVE:
                return 0;
            case DRAFT:
                return 2;
            case PENDING:
                return 3;
            default:
                return 0;
        }
    }
    
    /**
     * 根据Integer代码获取枚举 (兼容system模块)
     */
    public static ConfigStatus fromIntegerCode(Integer code) {
        if (code == null) {
            return INACTIVE;
        }
        switch (code) {
            case 1:
                return ENABLED;
            case 0:
                return DISABLED;
            case 2:
                return DRAFT;
            case 3:
                return PENDING;
            default:
                return DISABLED;
        }
    }
}