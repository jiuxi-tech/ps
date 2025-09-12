package com.jiuxi.monitor.server.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;


import com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO;
import com.jiuxi.monitor.server.core.bean.query.TpSendMailRecordQuery;
import com.jiuxi.monitor.server.core.service.TpSendMailRecordService;


/**
 * @ClassName: TpSendMailRecordController
 * @Description: 邮件发送记录表
 * @Author yangzr
 * @Date 2024-11-20 10:36:59
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/mail-record")

public class TpSendMailRecordController {

    @Autowired
    private TpSendMailRecordService tpSendMailRecordService;

    /**
     * 列表
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @RequestMapping("/list")
    @Authorization
    public JsonResponse list(TpSendMailRecordQuery query, String jwtpid) {
        IPage<TpSendMailRecordVO> page = tpSendMailRecordService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @RequestMapping("/view")
    public JsonResponse view(String recordId) {
        TpSendMailRecordVO vo = tpSendMailRecordService.view(recordId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 删除-单条数据
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String recordId, String jwtpid) {
        tpSendMailRecordService.deleteByIds(Arrays.asList(recordId), jwtpid);
        return JsonResponse.buildSuccess();
    }


}
