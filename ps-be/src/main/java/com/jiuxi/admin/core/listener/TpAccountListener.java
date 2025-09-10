package com.jiuxi.admin.core.listener;

import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.event.TpAccountEvent;
import com.jiuxi.admin.core.listener.service.TpAccountEventService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

/**
 * @Description: 账号信息同步监听事件
 * @ClassName: TpPersonBasicinfoListener
 * @Author: pand
 * @Date: 2021-06-08 11:17
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class TpAccountListener implements ApplicationListener<TpAccountEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpAccountListener.class);

    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(TpAccountEvent event) {
        int optionType = event.getOptionType();
        TpAccountEventService tpAccountEventService = event.getTpAccountEventService();

        if (null == tpAccountEventService) {
            return;
        }

        LOGGER.info("执行账号信息监听事件！时间：{}", CommonDateUtil.now());
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
                // 修改密码
                event.updatePwd();
                break;
            case 4:
                // 冻结/解冻 密码
                event.lockedPwd();
                break;
            case 5:
                // 启动/停用 密码
                event.enabledPwd();
                break;
            case 6:
                // 删除
                event.delete();
                break;
            default:
                throw new TopinfoRuntimeException(-1, "账号信息发布事件，操作类型异常！");
        }
    }
}
