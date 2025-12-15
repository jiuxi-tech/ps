package com.jiuxi.admin.core.event;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoEventService;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 部门基本信息操作的监听事件, 废弃原因：add,update,delete方法中的if控制方法，不在框架中控制，需要业务上自己控制。
 * @ClassName: TpDeptBasicinfoEvent
 * @Author: pdd
 * @Date: 2021-06-08 17:14
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Deprecated
public class TpDeptBasicinfoEvent extends ApplicationEvent {

    private TpDeptBasicinfoEventService tpDeptBasicinfoEventService;

    private TpDeptBasicinfo tpDeptBasicinfo;

    private int optionType;


    /**
     * 监听事件
     *
     * @param source:
     * @param tpDeptBasicinfoEventService: 接口定义，需要实现
     * @param tpDeptBasicinfo:             部门基本信息
     * @param optionType:                  操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpDeptBasicinfoEvent(Object source, TpDeptBasicinfoEventService tpDeptBasicinfoEventService, TpDeptBasicinfo tpDeptBasicinfo, int optionType) {
        super(source);
        this.tpDeptBasicinfoEventService = tpDeptBasicinfoEventService;
        this.tpDeptBasicinfo = tpDeptBasicinfo;
        this.optionType = optionType;
    }

    public TpDeptBasicinfoEventService getTpDeptBasicinfoEventService() {
        return tpDeptBasicinfoEventService;
    }

    public int getOptionType() {
        return optionType;
    }

    public void add() {
        int category = tpDeptBasicinfo.getCategory();
        String deptLevelCode = tpDeptBasicinfo.getDeptLevelcode();
        // 只有政府的部门，企业/中介 的非顶级部门（层级code大于等于25位）进行推送
        if (category == TpConstant.Category.ORG || (category == TpConstant.Category.ENT && deptLevelCode.length() >= 25) || (category == TpConstant.Category.MED && deptLevelCode.length() >= 25)) {
            tpDeptBasicinfoEventService.add(tpDeptBasicinfo);
        }
    }

    public void update() {
        int category = tpDeptBasicinfo.getCategory();
        String deptLevelCode = tpDeptBasicinfo.getDeptLevelcode();
        // 只有政府的部门，企业/中介 的非顶级部门（层级code大于等于25位）进行推送
        if (category == TpConstant.Category.ORG || (category == TpConstant.Category.ENT && deptLevelCode.length() >= 25) || (category == TpConstant.Category.MED && deptLevelCode.length() >= 25)) {
            tpDeptBasicinfoEventService.update(tpDeptBasicinfo);
        }
    }

    public void delete() {
        int category = tpDeptBasicinfo.getCategory();
        String deptLevelCode = tpDeptBasicinfo.getDeptLevelcode();
        // 只有政府的部门，企业/中介 的非顶级部门（层级code大于等于25位）进行推送
        if (category == TpConstant.Category.ORG || (category == TpConstant.Category.ENT && deptLevelCode.length() >= 25) || (category == TpConstant.Category.MED && deptLevelCode.length() >= 25)) {
            tpDeptBasicinfoEventService.delete(tpDeptBasicinfo.getDeptId());
        }
    }
}
