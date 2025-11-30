package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpApiCallLogQuery;
import com.jiuxi.admin.core.bean.vo.TpApiCallLogVO;
import com.jiuxi.admin.core.service.TpApiCallLogService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API调用日志Controller
 *
 * @author system
 * @date 2025-11-30
 */
@RestController
@RequestMapping("/sys/api-call-log")
@Authorization
public class TpApiCallLogController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "logId";

    @Autowired
    private TpApiCallLogService tpApiCallLogService;

    /**
     * API调用日志列表（分页查询）
     * 注：忽略租户ID，查询所有日志
     */
    @RequestMapping("/list")
    public JsonResponse list(TpApiCallLogQuery query, String jwtpid) {
        // jwtpid 仅用于 buildPassKey，不作为查询条件
        IPage<TpApiCallLogVO> page = tpApiCallLogService.queryPage(query, jwtpid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 查看API调用日志详情
     */
    @RequestMapping(value = "/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String logId) {
        TpApiCallLogVO vo = tpApiCallLogService.view(logId);
        return JsonResponse.buildSuccess(vo);
    }
}
