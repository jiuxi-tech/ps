package com.jiuxi.admin.core.event;

import com.jiuxi.shared.common.constants.enums.OpertionTypeEnum;
import com.jiuxi.admin.core.bean.entity.TpEntBasicinfo;
import com.jiuxi.admin.core.listener.TpEntBasicinfoEventCollection;
import com.jiuxi.admin.core.listener.service.TpEntBasicinfoEventService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @Description: 企业基本信息操作的监听事件
 * @ClassName: TpEntBasicinfoEvent
 * @Author: pand
 * @Date: 2021-06-08 17:14
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpEntBasicinfoEvent extends ApplicationEvent {

    private TpEntBasicinfoEventCollection tpEntBasicinfoEventCollection;

    private TpEntBasicinfo tpEntBasicinfo;

    private OpertionTypeEnum optionType;


    /**
     * 监听事件
     *
     * @param source:
     * @param tpEntBasicinfoEventCollection: 接口定义，需要实现
     * @param tpEntBasicinfo:                企业基本信息
     * @param optionType:                    操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpEntBasicinfoEvent(Object source, TpEntBasicinfoEventCollection tpEntBasicinfoEventCollection, TpEntBasicinfo tpEntBasicinfo, OpertionTypeEnum optionType) {
        super(source);
        this.tpEntBasicinfoEventCollection = tpEntBasicinfoEventCollection;
        this.tpEntBasicinfo = tpEntBasicinfo;
        this.optionType = optionType;
    }


    public void exceute() {
        List<TpEntBasicinfoEventService> eventList = tpEntBasicinfoEventCollection.getEventList();
        eventList.forEach(tpEntBasicinfoEventService -> {
            switch (optionType) {
                case ADD:
                    // 新增
                    tpEntBasicinfoEventService.add(tpEntBasicinfo);
                    break;
                case UPDATE:
                    // 修改
                    tpEntBasicinfoEventService.update(tpEntBasicinfo);
                    break;
                case DELETE:
                    // 删除
                    tpEntBasicinfoEventService.delete(tpEntBasicinfo.getEntId());
                    break;
                default:
                    throw new TopinfoRuntimeException(-1, "企业基本信息发布事件，操作类型异常！");
            }

        });
    }


    public OpertionTypeEnum getOptionType() {
        return optionType;
    }

    /*public TpEntBasicinfoEventService getTpEntBasicinfoEventService() {
        return tpEntBasicinfoEventService;
    }*/

    /*public void add() {
        tpEntBasicinfoEventService.add(tpEntBasicinfo);
    }

    public void update() {
        tpEntBasicinfoEventService.update(tpEntBasicinfo);
    }

    public void delete() {
        tpEntBasicinfoEventService.delete(tpEntBasicinfo.getEntId());
    }*/
}
