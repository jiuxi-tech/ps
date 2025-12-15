package com.jiuxi.security.core.event;

import com.jiuxi.security.core.listener.LoginApplicationEventCollection;
import com.jiuxi.security.core.listener.LoginApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @Description: 登陆的自定义事件
 * @ClassName: LoginApplicationEvent
 * @Author: pdd
 * @Date: 2020-09-08 14:34
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class LoginApplicationEvent extends ApplicationEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginApplicationEvent.class);

    /**
     * 登陆成功/失败标识 true：登陆成功  false：登陆失败
     */
    private boolean loginSuccess;

    private String accountId;

    private LoginApplicationEventCollection loginApplicationEventService;

    public LoginApplicationEvent(Object source, LoginApplicationEventCollection loginApplicationEventService, boolean loginSuccess, String accountId) {
        super(source);
        this.loginApplicationEventService = loginApplicationEventService;
        this.loginSuccess = loginSuccess;
        this.accountId = accountId;
    }

    public void check() {
        List<LoginApplicationService> eventList = loginApplicationEventService.getEventList();
        eventList.forEach(loginApplicationService -> {
            loginApplicationService.exceute(loginSuccess, accountId);
        });
    }

}
