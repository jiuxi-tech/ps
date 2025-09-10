package com.jiuxi.monitor.server.core.service;

import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;

/**
 * @ClassName: MonitorCacheService
 * @Description: 监控缓存服务
 * @Author 杨占锐
 * @Date 2024/11/18 10:13
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorCacheService {

    /**
     * 查询客户端基本信息
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientBaseInfo
     * @author 杨占锐
     * @date 2024/11/19 13:28
     */
    TpMonitorClientVO getClientBaseInfo(String clientId);

    /**
     * 清空客户端基本信息缓存
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientBaseInfo
     * @author 杨占锐
     * @date 2024/11/19 13:28
     */
    void clearClientInfo(String clientId);

    /**
     * 添加心跳信息到缓存中
     *
     * @param info
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:30
     */
    void setHeartbeat(ClientHeartbeatInfo info);

    /**
     * 获取心跳信息
     *
     * @param clientId
     * @return com.jiuxi.monitor.server.bean.ClientHeartbeatInfo
     * @author 杨占锐
     * @date 2024/11/19 15:51
     */
    ClientHeartbeatInfo getHeartbeat(String clientId);

    /**
     * 查询服务资源报警配置信息
     *
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:30
     */
    TpMonitorConfigVO getConfig();

    /**
     * 删除服务资源报警配置信息
     *
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:30
     */
    void clearAlarmConfig();


}
