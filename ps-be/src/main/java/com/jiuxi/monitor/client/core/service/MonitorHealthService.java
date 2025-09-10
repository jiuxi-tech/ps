package com.jiuxi.monitor.client.core.service;

/**
 * @ClassName: MonitorOuterServer
 * @Description: 外围服务接口
 * @Author 杨占锐
 * @Date 2024/11/20 17:09
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorHealthService {

    /**
     * 获取服务名称
     *
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/20 17:10
     */
    String getServerName();

    /**
     * 服务器是否健康
     *
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/20 17:10
     */
    boolean isHealth();
}
