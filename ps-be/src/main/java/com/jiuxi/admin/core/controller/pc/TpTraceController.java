package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuxi.admin.core.bean.vo.TpTraceVO;
import com.jiuxi.admin.core.bean.entity.TpTrace;
import com.jiuxi.admin.core.bean.query.TpTraceQuery;
import com.jiuxi.admin.core.service.TpTraceService;


/**
 * @ClassName: TpTraceController
 * @Description: 修改痕迹表
 * @Author yangp
 * @Date 2021-02-26 15:48:55
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/core/tptrace")
public class TpTraceController {

    @Autowired
    private TpTraceService tpTraceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpTraceQuery query) {
        IPage<TpTraceVO> page = tpTraceService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String id){
        // TODO
        return JsonResponse.buildSuccess();
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse add(@RequestBody TpTrace tpTrace){
        // TODO
        return JsonResponse.buildSuccess();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(@RequestBody TpTrace tpTrace){
        // TODO
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestBody String[] ids){
        // TODO
        return JsonResponse.buildSuccess();
    }

}
