package com.jiuxi.module.sys.intf.web.controller.command;

import com.jiuxi.module.sys.app.dto.DictionaryItemCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemUpdateDTO;
import com.jiuxi.module.sys.app.service.DictionaryItemApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典项管理命令控制器
 * 负责字典项相关的数据变更操作 (Create, Update, Delete)
 * 基于CQRS架构设计，专注于处理命令操作
 * 
 * @author DDD Refactor - Phase 6
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/dictionary/item")
public class DictionaryItemCommandController {
    
    @Autowired
    private DictionaryItemApplicationService dictionaryItemApplicationService;
    
    /**
     * 创建字典项
     * @param createDTO 字典项创建DTO
     * @return 响应结果
     */
    @PostMapping
    public JsonResponse createDictionaryItem(@RequestBody DictionaryItemCreateDTO createDTO) {
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.createDictionaryItem(createDTO, "system");
            return JsonResponse.build(200, "创建成功", responseDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新字典项
     * @param updateDTO 字典项更新DTO
     * @return 响应结果
     */
    @PutMapping
    public JsonResponse updateDictionaryItem(@RequestBody DictionaryItemUpdateDTO updateDTO) {
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.updateDictionaryItem(updateDTO, "system");
            return JsonResponse.build(200, "更新成功", responseDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除字典项
     * @param itemId 字典项ID
     * @return 响应结果
     */
    @DeleteMapping("/{itemId}")
    public JsonResponse deleteDictionaryItem(@PathVariable String itemId) {
        try {
            dictionaryItemApplicationService.deleteDictionaryItem(itemId);
            return JsonResponse.build(200, "删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除失败: " + e.getMessage());
        }
    }
}