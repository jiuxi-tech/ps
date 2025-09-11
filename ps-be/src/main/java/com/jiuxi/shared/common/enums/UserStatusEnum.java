package com.jiuxi.shared.common.enums;

/**
 * 用户状态枚举
 * 定义用户的各种状态
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public enum UserStatusEnum {

    /** 正常状态 */
    NORMAL(1, "正常", "用户状态正常，可以正常使用系统"),
    
    /** 禁用状态 */
    DISABLED(0, "禁用", "用户被禁用，无法登录系统"),
    
    /** 锁定状态 */
    LOCKED(2, "锁定", "用户被锁定，通常因为多次登录失败"),
    
    /** 过期状态 */
    EXPIRED(3, "过期", "用户账户已过期，需要续期"),
    
    /** 待激活状态 */
    PENDING_ACTIVATION(4, "待激活", "新注册用户，等待激活"),
    
    /** 待审核状态 */
    PENDING_APPROVAL(5, "待审核", "用户注册后等待管理员审核"),
    
    /** 审核拒绝状态 */
    REJECTED(6, "审核拒绝", "用户注册被管理员拒绝"),
    
    /** 注销状态 */
    CANCELLED(7, "注销", "用户主动注销账户"),
    
    /** 冻结状态 */
    FROZEN(8, "冻结", "用户因违规被冻结"),
    
    /** 临时状态 */
    TEMPORARY(9, "临时", "临时用户，有使用期限限制");

    private final Integer code;
    private final String name;
    private final String description;

    UserStatusEnum(Integer code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getCode() {
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
    public static UserStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     */
    public static UserStatusEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        for (UserStatusEnum status : values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断用户是否可以登录
     */
    public boolean canLogin() {
        return this == NORMAL;
    }

    /**
     * 判断用户是否被禁用
     */
    public boolean isDisabled() {
        return this == DISABLED || this == LOCKED || this == FROZEN || this == CANCELLED;
    }

    /**
     * 判断用户是否需要激活
     */
    public boolean needsActivation() {
        return this == PENDING_ACTIVATION;
    }

    /**
     * 判断用户是否需要审核
     */
    public boolean needsApproval() {
        return this == PENDING_APPROVAL;
    }

    /**
     * 判断用户是否已过期
     */
    public boolean isExpired() {
        return this == EXPIRED;
    }

    /**
     * 判断用户是否为临时用户
     */
    public boolean isTemporary() {
        return this == TEMPORARY;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s", code, name, description);
    }
}