package com.jiuxi.module.role.domain.model.vo;

import java.util.Objects;

/**
 * 角色状态值对象
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleStatus {
    
    public static final RoleStatus ENABLED = new RoleStatus(1, "启用");
    public static final RoleStatus DISABLED = new RoleStatus(0, "禁用");
    
    private final Integer code;
    private final String description;
    
    private RoleStatus(Integer code, String description) {
        this.code = Objects.requireNonNull(code, "状态码不能为空");
        this.description = Objects.requireNonNull(description, "状态描述不能为空");
    }
    
    public static RoleStatus of(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("状态码不能为空");
        }
        switch (code) {
            case 1:
                return ENABLED;
            case 0:
                return DISABLED;
            default:
                throw new IllegalArgumentException("无效的角色状态码: " + code);
        }
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 检查角色是否启用
     */
    public boolean isEnabled() {
        return this.equals(ENABLED);
    }
    
    /**
     * 检查角色是否禁用
     */
    public boolean isDisabled() {
        return this.equals(DISABLED);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleStatus that = (RoleStatus) o;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return "RoleStatus{" + "code=" + code + ", description='" + description + '\'' + '}';
    }
}