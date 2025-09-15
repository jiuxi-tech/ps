package com.jiuxi.module.role.domain.event;

import com.jiuxi.module.role.domain.model.vo.RoleId;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 角色删除事件
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleDeletedEvent {
    
    private final RoleId roleId;
    private final String roleName;
    private final String deleter;
    private final LocalDateTime occurredOn;
    
    public RoleDeletedEvent(RoleId roleId, String roleName, String deleter) {
        this.roleId = Objects.requireNonNull(roleId, "角色ID不能为空");
        this.roleName = Objects.requireNonNull(roleName, "角色名称不能为空");
        this.deleter = Objects.requireNonNull(deleter, "删除者不能为空");
        this.occurredOn = LocalDateTime.now();
    }
    
    public RoleId getRoleId() {
        return roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public String getDeleter() {
        return deleter;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDeletedEvent that = (RoleDeletedEvent) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(occurredOn, that.occurredOn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roleId, occurredOn);
    }
    
    @Override
    public String toString() {
        return "RoleDeletedEvent{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", deleter='" + deleter + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}