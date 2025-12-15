package com.jiuxi.admin.core.event;

import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.listener.service.TpPersonBasicinfoEventService;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 人员操作的监听事件
 * @ClassName: PersonBasicinfoEvent
 * @Author: pdd
 * @Date: 2021-06-08 11:09
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpPersonBasicinfoEvent extends ApplicationEvent {

    private TpPersonBasicinfoEventService tpPersonBasicinfoEventService;

    private TpPersonBasicinfo tpPersonBasicinfo;

    private int optionType;

    /**
     * 监听事件
     *
     * @param source:
     * @param tpPersonBasicinfoEventService: 接口定义，需要实现
     * @param tpPersonBasicinfo:             人员信息
     * @param optionType:                    操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpPersonBasicinfoEvent(Object source, TpPersonBasicinfoEventService tpPersonBasicinfoEventService, TpPersonBasicinfo tpPersonBasicinfo, int optionType) {
        super(source);
        this.tpPersonBasicinfoEventService = tpPersonBasicinfoEventService;
        this.tpPersonBasicinfo = tpPersonBasicinfo;
        this.optionType = optionType;
    }

    public TpPersonBasicinfoEventService getTpPersonBasicinfoEventService() {
        return tpPersonBasicinfoEventService;
    }

    public int getOptionType() {
        return optionType;
    }

    public void add() {
        tpPersonBasicinfoEventService.add(tpPersonBasicinfo);
    }

    public void update() {
        tpPersonBasicinfoEventService.update(tpPersonBasicinfo);
    }

    public void delete() {
        tpPersonBasicinfoEventService.delete(tpPersonBasicinfo.getPersonId());
    }

}
