package com.jiuxi.monitor.server.core.service;

import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: MonitorServerService
 * @Description:  监控服务端接口
 * @Author yangzr
 * @Date 2024/11/18 9:49
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface MonitorServerService {

    /**
     * 处理接收到的心跳信息
     *
     * @param heartbeatInfo
     * @param request
     * @return void
     * @author jiuxx
     * @date 2024/11/19 14:35
     */
    void handleHeartbeat(ClientHeartbeatInfo heartbeatInfo, HttpServletRequest request);

    /**
     * 离线报警
     *
     * @param clientVO
     * @param configVO
     * @return void
     * @author jiuxx
     * @date 2024/11/19 15:13
     */
    void offlineAlarm(TpMonitorClientVO clientVO, TpMonitorConfigVO configVO);
}
