package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpMediorgQuery;
import com.jiuxi.admin.core.bean.vo.TpMediorgVO;
import com.jiuxi.admin.core.service.TpMediorgService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: TpMediorgController
 * @Description: 中介表
 * @Author pand
 * @Date 2021-05-25 17:32:41
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/mediorg")
@Authorization
public class TpMediorgController {

    @Autowired
    private TpMediorgService tpMediorgService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpMediorgQuery query) {
        IPage<TpMediorgVO> page = tpMediorgService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String mediorgId) {
        TpMediorgVO vo = tpMediorgService.view(mediorgId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse add(TpMediorgVO tpMediorg, String jwtpid) {
        String mediorgId = tpMediorgService.add(tpMediorg, jwtpid);
        return JsonResponse.buildSuccess(mediorgId);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(TpMediorgVO tpMediorg, String jwtpid) {
        tpMediorgService.update(tpMediorg, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String mediorgId) {
        tpMediorgService.deleteById(mediorgId);
        return JsonResponse.buildSuccess();
    }

}
