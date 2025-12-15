package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.event.TpPersonBasicinfoEvent;
import com.jiuxi.admin.core.listener.service.TpPersonBasicinfoEventService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 人员信息同步监听事件
 * @ClassName: TpPersonBasicinfoListener
 * @Author: pdd
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpPersonBasicinfoListener implements ApplicationListener<TpPersonBasicinfoEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpPersonBasicinfoListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpPersonBasicinfoEvent event) {
        int optionType = event.getOptionType();
        TpPersonBasicinfoEventService tpPersonBasicinfoEventService = event.getTpPersonBasicinfoEventService();

        if (null == tpPersonBasicinfoEventService) {
            return;
        }

        LOGGER.info("执行人员信息监听事件！时间：{}", CommonDateUtil.now());
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
                throw new TopinfoRuntimeException(-1, "人员信息发布事件，操作类型异常！");
        }
    }
}
