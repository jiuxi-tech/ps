package com.jiuxi.monitor.client.core.service;

/**
 * @ClassName: MonitorClientService
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/14 17:44
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorClientService {

    /**
     * 发送心跳
     *
     * @param
     * @return void
     * @author 杨占锐
     * @date 2024/11/14 18:24
     */
    void heartbeat();

}
