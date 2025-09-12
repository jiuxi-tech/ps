package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.event.TpCityEvent;
import com.jiuxi.admin.core.listener.service.TpCityEventService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 行政区划信息同步监听事件
 * @ClassName: TpCityListener
 * @Author: pand
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpCityListener implements ApplicationListener<TpCityEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpCityListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpCityEvent event) {
        int optionType = event.getOptionType();
        TpCityEventService tpCityEventService = event.getTpCityEventService();

        if (null == tpCityEventService) {
            return;
        }

        LOGGER.info("执行行政区划信息监听事件！时间：{}", CommonDateUtil.now());
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
                throw new TopinfoRuntimeException(-1, "行政区划信息发布事件，操作类型异常！");
        }
    }
}
