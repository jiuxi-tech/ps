package com.jiuxi.security.core.listener;

import com.jiuxi.security.core.event.LoginApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @Description: 登陆的自定义监听器
 * @ClassName: LoginApplicationLisenter
 * @Author: pand
 * @Date: 2020-09-08 14:39
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class LoginApplicationLisenter implements ApplicationListener<LoginApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginApplicationLisenter.class);

    /**
     * 处理监听到的事件
     *
     * @param loginApplicationEvent: 监听到的自定义事件
     * @return void
     * @author pand
     * @date 2020-09-08 14:40
     */
    @Override
    @Async(value = "taskExecutor")
    public void onApplicationEvent(LoginApplicationEvent loginApplicationEvent) {
        loginApplicationEvent.check();
    }
}
