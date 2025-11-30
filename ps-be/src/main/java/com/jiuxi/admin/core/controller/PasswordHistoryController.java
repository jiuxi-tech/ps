package com.jiuxi.admin.core.controller;

import com.jiuxi.admin.core.bean.query.TpPasswordHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpPasswordHistoryVO;
import com.jiuxi.admin.core.service.PasswordHistoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 密码修改历史控制器
 *
 * @author system
 * @date 2025-12-01
 */
@RestController
@RequestMapping("/sys/password-history")
public class PasswordHistoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordHistoryController.class);

    @Autowired
    private PasswordHistoryService passwordHistoryService;

    /**
     * 分页查询密码历史列表
     *
     * @param query 查询条件
     * @return 分页数据
     */
    @GetMapping("/list")
    public JsonResponse list(TpPasswordHistoryQuery query) {
        try {
            Page<TpPasswordHistoryVO> page = passwordHistoryService.list(query);
            return JsonResponse.buildSuccess(page);
        } catch (Exception e) {
            LOGGER.error("查询密码历史列表失败", e);
            return JsonResponse.buildFailure(e.getMessage());
        }
    }

    /**
     * 查看密码历史详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    @GetMapping("/view")
    public JsonResponse view(@RequestParam String historyId) {
        try {
            TpPasswordHistoryVO vo = passwordHistoryService.view(historyId);
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            LOGGER.error("查看密码历史详情失败: historyId={}", historyId, e);
            return JsonResponse.buildFailure(e.getMessage());
        }
    }
}
