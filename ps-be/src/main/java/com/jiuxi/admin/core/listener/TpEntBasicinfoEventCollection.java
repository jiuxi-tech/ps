package com.jiuxi.admin.core.listener;

import com.jiuxi.admin.core.listener.service.TpEntBasicinfoEventService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @ClassName: TpEntBasicinfoEventCollection
 * @Author: pand
 * @Date: 2022-03-13 14:34
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class TpEntBasicinfoEventCollection {

    private List<TpEntBasicinfoEventService> eventList = new ArrayList<>();

    public List<TpEntBasicinfoEventService> getEventList() {
        return eventList;
    }

    /**
     * 动态添加事件
     *
     * @param event
     */
    public void addEvent(TpEntBasicinfoEventService event) {
        this.eventList.add(event);
    }
}
