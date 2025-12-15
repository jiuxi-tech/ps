package com.jiuxi.monitor.server.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.core.service.MonitorServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;


import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.query.TpMonitorClientQuery;
import com.jiuxi.monitor.server.core.service.TpMonitorClientService;

import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: TpMonitorClientController
 * @Description: 监控服务控制器
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/monitor")
public class TpMonitorClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMonitorClientController.class);

    @Autowired
    private MonitorServerService monitorServerService;

    @Autowired
    private TpMonitorClientService tpMonitorClientService;

    /**
     * 接收心跳信息
     *
     * @param heartbeatInfo
     * @param request
     * @return com.jiuxi.common.bean.JsonResponse
     * @author jiuxx
     * @date 2024/11/18 9:48
     */
    @RequestMapping("/heartbeat")
    public JsonResponse heartbeat(@RequestBody ClientHeartbeatInfo heartbeatInfo, HttpServletRequest request) {

        if (null == heartbeatInfo) {
            return JsonResponse.buildFailure();
        }

        LOGGER.info("接收到心跳信息，systemDesc: {}", heartbeatInfo.getSystemDesc());

        monitorServerService.handleHeartbeat(heartbeatInfo, request);

        LOGGER.info("接收到心跳信息处理完成，systemDesc: {}", heartbeatInfo.getSystemDesc());
        return JsonResponse.buildSuccess();
    }

    /**
     * 列表
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/list")
    @Authorization
    public JsonResponse list(TpMonitorClientQuery query, String jwtpid) {
        IPage<TpMonitorClientVO> page = tpMonitorClientService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/view")
    public JsonResponse view(String clientId) {
        TpMonitorClientVO vo = tpMonitorClientService.view(clientId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 查看心跳信息
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/view-heartbeat")
    public JsonResponse viewHeartbeat(String clientId) {
        ClientHeartbeatInfo heartbeatInfo = tpMonitorClientService.viewHeartbeat(clientId);
        return JsonResponse.buildSuccess(heartbeatInfo);
    }

    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/update")
    public JsonResponse update(TpMonitorClientVO tpMonitorClient, String jwtpid) {
        tpMonitorClientService.update(tpMonitorClient, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除-单条数据
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String clientId, String jwtpid) {
        tpMonitorClientService.deleteByIds(Arrays.asList(clientId), jwtpid);
        return JsonResponse.buildSuccess();
    }


}
