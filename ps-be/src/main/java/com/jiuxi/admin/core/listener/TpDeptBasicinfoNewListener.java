package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.event.TpDeptBasicinfoEvent;
import com.jiuxi.admin.core.event.TpDeptBasicinfoNewEvent;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoEventService;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoNewEventService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 部门基本信息同步监听事件
 * @ClassName: TpDeptBasicinfoListener
 * @Author: pand
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpDeptBasicinfoNewListener implements ApplicationListener<TpDeptBasicinfoNewEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDeptBasicinfoNewListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpDeptBasicinfoNewEvent event) {
        int optionType = event.getOptionType();
        TpDeptBasicinfoNewEventService tpCityEventService = event.getTpDeptBasicinfoNewEventService();

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
