package com.jiuxi.module.role.domain.event;

import com.jiuxi.module.role.domain.model.vo.RoleId;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 角色更新事件
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleUpdatedEvent {
    
    private final RoleId roleId;
    private final String roleName;
    private final String updater;
    private final LocalDateTime occurredOn;
    
    public RoleUpdatedEvent(RoleId roleId, String roleName, String updater) {
        this.roleId = Objects.requireNonNull(roleId, "角色ID不能为空");
        this.roleName = Objects.requireNonNull(roleName, "角色名称不能为空");
        this.updater = Objects.requireNonNull(updater, "更新者不能为空");
        this.occurredOn = LocalDateTime.now();
    }
    
    public RoleId getRoleId() {
        return roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public String getUpdater() {
        return updater;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleUpdatedEvent that = (RoleUpdatedEvent) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(occurredOn, that.occurredOn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roleId, occurredOn);
    }
    
    @Override
    public String toString() {
        return "RoleUpdatedEvent{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", updater='" + updater + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}