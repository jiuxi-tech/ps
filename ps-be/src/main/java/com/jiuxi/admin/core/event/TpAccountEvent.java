package com.jiuxi.admin.core.event;

import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.listener.service.TpAccountEventService;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 账号操作的监听事件
 * @ClassName: TpAccountBasicinfoEvent
 * @Author: pand
 * @Date: 2021-06-08 11:09
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpAccountEvent extends ApplicationEvent {

    private TpAccountEventService tpAccountEventService;

    private TpAccount tpAccount;

    private int optionType;

    /**
     * 监听事件
     *
     * @param source:
     * @param tpAccountEventService: 接口定义，需要实现
     * @param tpAccount:             账号信息
     * @param optionType:            操作类型 1：新增  2：修改   3：删除
     * @return null
     * @author pand
     * @date 2021-06-08 13:18
     */
    public TpAccountEvent(Object source, TpAccountEventService tpAccountEventService, TpAccount tpAccount, int optionType) {
        super(source);
        this.tpAccountEventService = tpAccountEventService;
        this.tpAccount = tpAccount;
        this.optionType = optionType;
    }

    public TpAccountEventService getTpAccountEventService() {
        return tpAccountEventService;
    }

    public int getOptionType() {
        return optionType;
    }

    public void add() {
        tpAccountEventService.add(tpAccount);
    }

    public void update() {
        tpAccountEventService.update(tpAccount);
    }

    public void updatePwd() {
        tpAccountEventService.updatePwd(tpAccount);
    }

    public void lockedPwd() {
        tpAccountEventService.lockedPwd(tpAccount);
    }

    public void enabledPwd() {
        tpAccountEventService.enabledPwd(tpAccount);
    }

    public void delete() {
        tpAccountEventService.delete(tpAccount.getAccountId());
    }

}
