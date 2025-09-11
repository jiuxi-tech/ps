package com.jiuxi.module.org.domain.entity;

/**
 * 组织类型枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum OrganizationType {
    
    /**
     * 政府机构
     */
    GOVERNMENT("GOVERNMENT", "政府机构"),
    
    /**
     * 企业集团
     */
    ENTERPRISE_GROUP("ENTERPRISE_GROUP", "企业集团"),
    
    /**
     * 事业单位
     */
    INSTITUTION("INSTITUTION", "事业单位"),
    
    /**
     * 社会团体
     */
    SOCIAL_ORGANIZATION("SOCIAL_ORGANIZATION", "社会团体"),
    
    /**
     * 其他组织
     */
    OTHER("OTHER", "其他组织");
    
    private final String code;
    private final String description;
    
    OrganizationType(String code, String description) {
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
    public static OrganizationType fromCode(String code) {
        for (OrganizationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的组织类型代码: " + code);
    }
}