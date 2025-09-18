package com.jiuxi.module.sys.intf.web.controller.query;

import com.jiuxi.module.sys.app.dto.DictionaryItemResponseDTO;
import com.jiuxi.module.sys.app.service.DictionaryItemApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典项管理查询控制器
 * 负责字典项相关的数据查询操作 (Read)
 * 基于CQRS架构设计，专注于处理查询操作
 * 
 * @author DDD Refactor - Phase 6
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/dictionary/item")
public class DictionaryItemQueryController {
    
    @Autowired
    private DictionaryItemApplicationService dictionaryItemApplicationService;
    
    /**
     * 根据ID获取字典项
     * @param itemId 字典项ID
     * @return 响应结果
     */
    @GetMapping("/{itemId}")
    public JsonResponse getDictionaryItemById(@PathVariable String itemId) {
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.getDictionaryItemById(itemId);
            if (responseDTO != null) {
                return JsonResponse.build(200, "查询成功", responseDTO);
            } else {
                return JsonResponse.build(404, "字典项不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据字典ID获取字典项列表
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/list/{dictId}")
    public JsonResponse getDictionaryItemsByDictId(@PathVariable String dictId) {
        try {
            List<DictionaryItemResponseDTO> responseDTOs = dictionaryItemApplicationService.getDictionaryItemsByDictId(dictId);
            return JsonResponse.build(200, "查询成功", responseDTOs);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据项编码和字典ID获取字典项
     * @param itemCode 项编码
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/code/{itemCode}")
    public JsonResponse getDictionaryItemByCodeAndDictId(@PathVariable String itemCode,
                                                        @RequestParam String dictId) {
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.getDictionaryItemByCodeAndDictId(itemCode, dictId);
            if (responseDTO != null) {
                return JsonResponse.build(200, "查询成功", responseDTO);
            } else {
                return JsonResponse.build(404, "字典项不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
}