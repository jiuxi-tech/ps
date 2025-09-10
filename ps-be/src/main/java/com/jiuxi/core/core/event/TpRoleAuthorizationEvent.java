package com.jiuxi.core.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * @ClassName: TpRoleAuthorizationEvent
 * @Description: 角色 授权 事件
 * @Author: 杨攀
 * @Date: 2022/11/24 9:44
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class TpRoleAuthorizationEvent extends ApplicationEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TpRoleAuthorizationEvent(Object source) {
        super(source);
    }
}

