package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpAgentQuery;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.bean.vo.TpAgentVO;
import com.jiuxi.admin.core.service.TpAgentService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.validation.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @ClassName: TpAgentController
 * @Description: 代办表
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/agent")
public class TpAgentController {

    @Autowired
    private TpAgentService tpAgentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpAgentQuery query, String jwtpid) {
        IPage<TpAgentVO> page = tpAgentService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 代办发送
     */
    @RequestMapping("/send")
    public JsonResponse send(@Validated(value = AddGroup.class) TpAgentVO tpAgent, String jwtpid) {
        String agId = tpAgentService.send(tpAgent, jwtpid);
        return JsonResponse.buildSuccess(agId);
    }

    /**
     * 消息提醒
     */
    @RequestMapping("/remindlist")
    public JsonResponse remindlist(String jwtaid, String jwtdid, String jwtpid) {

        List<TpAgentVO> list = tpAgentService.remindlist(jwtaid, jwtdid, jwtpid);

        return JsonResponse.buildSuccess(list);
    }

    /**
     * 代办详情
     */
    @RequestMapping("/view")
    public JsonResponse view(String agId, String jwtpid) {
        TpAgentDealVO vo = tpAgentService.view(agId, jwtpid);
        return JsonResponse.buildSuccess(vo);
    }

}
