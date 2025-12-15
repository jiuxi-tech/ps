package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpAgentDealQuery;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.service.TpAgentDealService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * @ClassName: TpAgentDealController
 * @Description: 代办处理表
 * @Author pand
 * @Date 2021-06-03 14:28:23
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/agentdeal")
public class TpAgentDealController {

    @Autowired
    private TpAgentDealService tpAgentDealService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpAgentDealQuery query) {
        IPage<TpAgentDealVO> page = tpAgentDealService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse add(TpAgentDealVO tpAgentDeal, String jwtpid) {
        String personId = tpAgentDealService.add(tpAgentDeal, jwtpid);
        return JsonResponse.buildSuccess(personId);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(TpAgentDealVO tpAgentDeal, String jwtpid) {
        tpAgentDealService.update(tpAgentDeal, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String[] personIds, String jwtpid) {
        tpAgentDealService.deleteByIds(Arrays.asList(personIds), jwtpid);
        return JsonResponse.buildSuccess();
    }

}
