package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: DictionaryType
 * @Description: 字典类型枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum DictionaryType {
    
    /**
     * 系统字典 - 系统内置，不可删除
     */
    SYSTEM("SYSTEM", "系统字典"),
    
    /**
     * 业务字典 - 业务自定义，可维护
     */
    BUSINESS("BUSINESS", "业务字典");
    
    private final String code;
    private final String description;
    
    DictionaryType(String code, String description) {
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
     * 根据代码获取枚举
     */
    public static DictionaryType fromCode(String code) {
        for (DictionaryType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown dictionary type code: " + code);
    }
    
    /**
     * 检查是否为系统字典
     */
    public boolean isSystem() {
        return this == SYSTEM;
    }
    
    /**
     * 检查是否为业务字典
     */
    public boolean isBusiness() {
        return this == BUSINESS;
    }
}