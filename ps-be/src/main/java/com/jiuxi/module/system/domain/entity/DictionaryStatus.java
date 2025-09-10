package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: DictionaryStatus
 * @Description: 字典状态枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum DictionaryStatus {
    
    /**
     * 启用状态
     */
    ENABLED(1, "启用"),
    
    /**
     * 禁用状态
     */
    DISABLED(0, "禁用");
    
    private final Integer code;
    private final String description;
    
    DictionaryStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static DictionaryStatus fromCode(Integer code) {
        for (DictionaryStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown dictionary status code: " + code);
    }
    
    /**
     * 检查是否启用
     */
    public boolean isEnabled() {
        return this == ENABLED;
    }
    
    /**
     * 检查是否禁用
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }
}