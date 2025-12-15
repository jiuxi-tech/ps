package com.jiuxi.admin.core.event;

import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoNewEventService;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 部门基本信息操作的监听事件
 * @ClassName: TpDeptBasicinfoEvent
 * @Author: pdd
 * @Date: 2021-06-08 17:14
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpDeptBasicinfoNewEvent extends ApplicationEvent {

    private TpDeptBasicinfoNewEventService tpDeptBasicinfoNewEventService;

    private TpDeptBasicinfo tpDeptBasicinfo;

    private int optionType;


    /**
     * 监听事件
     *
     * @param source:
     * @param tpDeptBasicinfoNewEventService: 接口定义，需要实现
     * @param tpDeptBasicinfo:                部门基本信息
     * @param optionType:                     操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpDeptBasicinfoNewEvent(Object source, TpDeptBasicinfoNewEventService tpDeptBasicinfoNewEventService, TpDeptBasicinfo tpDeptBasicinfo, int optionType) {
        super(source);
        this.tpDeptBasicinfoNewEventService = tpDeptBasicinfoNewEventService;
        this.tpDeptBasicinfo = tpDeptBasicinfo;
        this.optionType = optionType;
    }

    public TpDeptBasicinfoNewEventService getTpDeptBasicinfoNewEventService() {
        return tpDeptBasicinfoNewEventService;
    }

    public int getOptionType() {
        return optionType;
    }

    public void add() {
        tpDeptBasicinfoNewEventService.add(tpDeptBasicinfo);
    }

    public void update() {
        tpDeptBasicinfoNewEventService.update(tpDeptBasicinfo);
    }

    public void delete() {
        tpDeptBasicinfoNewEventService.delete(tpDeptBasicinfo.getDeptId());
    }
}
