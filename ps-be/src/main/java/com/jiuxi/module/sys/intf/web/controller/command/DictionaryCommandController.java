package com.jiuxi.module.sys.intf.web.controller.command;

import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import com.jiuxi.module.sys.app.service.DictionaryApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理命令控制器
 * 负责字典相关的数据变更操作 (Create, Update, Delete)
 * 基于CQRS架构设计，专注于处理命令操作
 * 
 * @author DDD Refactor - Phase 6
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/dictionary")
public class DictionaryCommandController {
    
    @Autowired
    private DictionaryApplicationService dictionaryApplicationService;
    
    /**
     * 创建字典
     * @param createDTO 字典创建DTO
     * @return 响应结果
     */
    @PostMapping
    public JsonResponse createDictionary(@RequestBody DictionaryCreateDTO createDTO) {
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.createDictionary(createDTO, "system");
            return JsonResponse.build(200, "创建成功", responseDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新字典
     * @param updateDTO 字典更新DTO
     * @return 响应结果
     */
    @PutMapping
    public JsonResponse updateDictionary(@RequestBody DictionaryUpdateDTO updateDTO) {
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.updateDictionary(updateDTO, "system");
            return JsonResponse.build(200, "更新成功", responseDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除字典
     * @param dictId 字典ID
     * @return 响应结果
     */
    @DeleteMapping("/{dictId}")
    public JsonResponse deleteDictionary(@PathVariable String dictId) {
        try {
            dictionaryApplicationService.deleteDictionary(dictId);
            return JsonResponse.build(200, "删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除失败: " + e.getMessage());
        }
    }
}