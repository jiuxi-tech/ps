package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpMessageReadQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.service.TpMessageReadService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * @ClassName: TpMessageReadController
 * @Description: 消息、代办 已读表
 * @Author pand
 * @Date 2021-05-28 15:04:39
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/messageread")
public class TpMessageReadController {

    @Autowired
    private TpMessageReadService tpMessageReadService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpMessageReadQuery query, String jwtpid) {
        query.setPersonId(jwtpid);
        IPage<TpMessageReadVO> page = tpMessageReadService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String[] personIds) {
        tpMessageReadService.deleteByIds(Arrays.asList(personIds));
        return JsonResponse.buildSuccess();
    }

}
