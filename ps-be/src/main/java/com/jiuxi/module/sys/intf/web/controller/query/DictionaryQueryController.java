package com.jiuxi.module.sys.intf.web.controller.query;

import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.service.DictionaryApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理查询控制器
 * 负责字典相关的数据查询操作 (Read)
 * 基于CQRS架构设计，专注于处理查询操作
 * 
 * @author DDD Refactor - Phase 6
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/dictionary")
public class DictionaryQueryController {
    
    @Autowired
    private DictionaryApplicationService dictionaryApplicationService;
    
    /**
     * 根据ID获取字典
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/{dictId}")
    public JsonResponse getDictionaryById(@PathVariable String dictId) {
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.getDictionaryById(dictId);
            if (responseDTO != null) {
                return JsonResponse.build(200, "查询成功", responseDTO);
            } else {
                return JsonResponse.build(404, "字典不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据字典编码获取字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/code/{dictCode}")
    public JsonResponse getDictionaryByCode(@PathVariable String dictCode, 
                                           @RequestParam(required = false, defaultValue = "default") String tenantId) {
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.getDictionaryByCode(dictCode, tenantId);
            if (responseDTO != null) {
                return JsonResponse.build(200, "查询成功", responseDTO);
            } else {
                return JsonResponse.build(404, "字典不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
}