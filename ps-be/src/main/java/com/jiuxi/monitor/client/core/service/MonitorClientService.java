package com.jiuxi.monitor.client.core.service;

/**
 * @ClassName: MonitorClientService
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/14 17:44
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface MonitorClientService {

    /**
     * 发送心跳
     *
     * @param
     * @return void
     * @author jiuxx
     * @date 2024/11/14 18:24
     */
    void heartbeat();

}
