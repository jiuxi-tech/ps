package com.jiuxi.security.core.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 事件集合
 * @ClassName: LoginApplicationEventService
 * @Author: pand
 * @Date: 2020-10-20 15:52
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class LoginApplicationEventCollection {

    private List<LoginApplicationService> eventList = new ArrayList<>();

    public List<LoginApplicationService> getEventList() {
        return eventList;
    }

    /**
     * 动态添加事件
     * @param event
     */
    public void addEvent(LoginApplicationService event) {
        this.eventList.add(event);
    }
}
