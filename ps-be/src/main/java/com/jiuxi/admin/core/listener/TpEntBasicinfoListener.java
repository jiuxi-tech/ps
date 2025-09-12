package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.shared.common.constants.enums.OpertionTypeEnum;
import com.jiuxi.admin.core.event.TpEntBasicinfoEvent;
import com.jiuxi.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 企业基本信息同步监听事件
 * @ClassName: TpEntBasicinfoListener
 * @Author: pand
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpEntBasicinfoListener implements ApplicationListener<TpEntBasicinfoEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpEntBasicinfoListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpEntBasicinfoEvent event) {
        OpertionTypeEnum optionType = event.getOptionType();

        LOGGER.info("执行企业基本信息监听事件！时间：{}", CommonDateUtil.now());

        event.exceute();
    }
}
