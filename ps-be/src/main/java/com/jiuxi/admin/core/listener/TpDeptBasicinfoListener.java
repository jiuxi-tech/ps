package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.event.TpDeptBasicinfoEvent;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoEventService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 部门基本信息同步监听事件，废弃原因：TpDeptBasicinfoEvent类的add,update,delete方法中的if控制方法，不在框架中控制，需要业务上自己控制。
 * @ClassName: TpDeptBasicinfoListener
 * @Author: pand
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Deprecated
public class TpDeptBasicinfoListener implements ApplicationListener<TpDeptBasicinfoEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDeptBasicinfoListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpDeptBasicinfoEvent event) {
        int optionType = event.getOptionType();
        TpDeptBasicinfoEventService tpCityEventService = event.getTpDeptBasicinfoEventService();

        if (null == tpCityEventService) {
            return;
        }

        LOGGER.info("执行部门基本信息监听事件！时间：{}", CommonDateUtil.now());
        switch (optionType) {
            case 1:
                // 新增
                event.add();
                break;
            case 2:
                // 修改
                event.update();
                break;
            case 3:
                // 删除
                event.delete();
                break;
            default:
                throw new TopinfoRuntimeException(-1, "部门基本信息发布事件，操作类型异常！");
        }
    }
}
