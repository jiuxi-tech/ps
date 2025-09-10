package com.jiuxi.monitor.server.core.controller;


import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.TpMonitorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TpMonitorConfigController
 * @Description: 监控配置, 只会存在一条数据
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/monitor-config")
@Authorization
public class TpMonitorConfigController {

    @Autowired
    private TpMonitorConfigService tpMonitorConfigService;

    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/update")
    public JsonResponse update(TpMonitorConfigVO tpMonitorConfig, String jwtpid) {

        tpMonitorConfigService.update(tpMonitorConfig, jwtpid);
        return JsonResponse.buildSuccess();
    }


    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @RequestMapping("/view")
    public JsonResponse view() {

        TpMonitorConfigVO configVO = tpMonitorConfigService.getOne();
        return JsonResponse.buildSuccess(configVO);
    }
}
