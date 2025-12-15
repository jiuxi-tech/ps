package com.jiuxi.admin.core.event;

import com.jiuxi.admin.core.bean.entity.TpCity;
import com.jiuxi.admin.core.listener.service.TpCityEventService;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 企业基本信息操作的监听事件
 * @ClassName: TpEntBasicinfoEvent
 * @Author: pdd
 * @Date: 2021-06-08 17:14
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpCityEvent extends ApplicationEvent {

    private TpCityEventService tpCityEventService;

    private TpCity tpCity;

    private int optionType;


    /**
     * 监听事件
     *
     * @param source:
     * @param tpCityEventService: 接口定义，需要实现
     * @param tpCity:             行政区划基本信息
     * @param optionType:         操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpCityEvent(Object source, TpCityEventService tpCityEventService, TpCity tpCity, int optionType) {
        super(source);
        this.tpCityEventService = tpCityEventService;
        this.tpCity = tpCity;
        this.optionType = optionType;
    }

    public TpCityEventService getTpCityEventService() {
        return tpCityEventService;
    }

    public int getOptionType() {
        return optionType;
    }

    public void add() {
        tpCityEventService.add(tpCity);
    }

    public void update() {
        tpCityEventService.update(tpCity);
    }

    public void delete() {
        tpCityEventService.delete(tpCity.getCityId());
    }
}
