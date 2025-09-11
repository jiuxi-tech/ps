package com.jiuxi.shared.common.enums;

/**
 * 数据权限范围枚举
 * 定义用户可访问的数据范围
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public enum DataScopeEnum {

    /** 全部数据权限 */
    ALL("1", "全部数据权限", "可以访问系统中的所有数据"),
    
    /** 自定义数据权限 */
    CUSTOM("2", "自定义数据权限", "根据自定义规则设置的数据权限"),
    
    /** 本部门数据权限 */
    DEPT("3", "本部门数据权限", "只能访问本部门的数据"),
    
    /** 本部门及以下数据权限 */
    DEPT_AND_CHILD("4", "本部门及以下数据权限", "可以访问本部门及其下级部门的数据"),
    
    /** 仅本人数据权限 */
    SELF("5", "仅本人数据权限", "只能访问自己创建或相关的数据"),
    
    /** 本组织数据权限 */
    ORG("6", "本组织数据权限", "只能访问本组织的数据"),
    
    /** 本组织及以下数据权限 */
    ORG_AND_CHILD("7", "本组织及以下数据权限", "可以访问本组织及其下级组织的数据"),
    
    /** 同级部门数据权限 */
    DEPT_SAME_LEVEL("8", "同级部门数据权限", "可以访问同级部门的数据"),
    
    /** 分管部门数据权限 */
    DEPT_ASSIGNED("9", "分管部门数据权限", "可以访问分管部门的数据");

    private final String code;
    private final String name;
    private final String description;

    DataScopeEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static DataScopeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (DataScopeEnum scope : values()) {
            if (scope.getCode().equals(code)) {
                return scope;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     */
    public static DataScopeEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        for (DataScopeEnum scope : values()) {
            if (scope.getName().equals(name)) {
                return scope;
            }
        }
        return null;
    }

    /**
     * 判断是否为全部数据权限
     */
    public boolean isAll() {
        return this == ALL;
    }

    /**
     * 判断是否为自定义权限
     */
    public boolean isCustom() {
        return this == CUSTOM;
    }

    /**
     * 判断是否为仅本人权限
     */
    public boolean isSelf() {
        return this == SELF;
    }

    /**
     * 判断是否包含下级数据
     */
    public boolean includesChild() {
        return this == DEPT_AND_CHILD || this == ORG_AND_CHILD;
    }

    /**
     * 判断是否为部门级权限
     */
    public boolean isDeptLevel() {
        return this == DEPT || this == DEPT_AND_CHILD || this == DEPT_SAME_LEVEL || this == DEPT_ASSIGNED;
    }

    /**
     * 判断是否为组织级权限
     */
    public boolean isOrgLevel() {
        return this == ORG || this == ORG_AND_CHILD;
    }

    /**
     * 获取权限级别（数字越小权限越大）
     */
    public int getLevel() {
        switch (this) {
            case ALL:
                return 1;
            case ORG_AND_CHILD:
                return 2;
            case ORG:
                return 3;
            case DEPT_AND_CHILD:
                return 4;
            case DEPT:
            case DEPT_SAME_LEVEL:
            case DEPT_ASSIGNED:
                return 5;
            case SELF:
                return 6;
            case CUSTOM:
            default:
                return 7;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", code, name, description);
    }
}