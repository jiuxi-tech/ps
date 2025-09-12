package com.jiuxi.shared.infrastructure.messaging.event;

import org.springframework.context.ApplicationEvent;

/**
 * 角色授权事件
 * @author DDD重构
 * @date 2025-09-12
 */
public class TpRoleAuthorizationEvent extends ApplicationEvent {

    private String message;
    private String roleId;
    private String menuIds;
    private String operatorId;

    public TpRoleAuthorizationEvent(String message) {
        super(message);
        this.message = message;
    }

    public TpRoleAuthorizationEvent(Object source, String roleId, String menuIds, String operatorId) {
        super(source);
        this.roleId = roleId;
        this.menuIds = menuIds;
        this.operatorId = operatorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}