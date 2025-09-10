package com.jiuxi.module.sys.interfaces.web;

import com.jiuxi.module.sys.app.dto.DictionaryItemCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemUpdateDTO;
import com.jiuxi.module.sys.app.service.DictionaryItemApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典项管理控制器
 * 提供字典项相关的RESTful API接口
 * 
 * @author System Management
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/system/dictionary/item")
public class DictionaryItemController {
    
    @Autowired
    private DictionaryItemApplicationService dictionaryItemApplicationService;
    
    /**
     * 创建字典项
     * @param createDTO 字典项创建DTO
     * @return 响应结果
     */
    @PostMapping
    public Map<String, Object> createDictionaryItem(@RequestBody DictionaryItemCreateDTO createDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.createDictionaryItem(createDTO, "system");
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", responseDTO);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新字典项
     * @param updateDTO 字典项更新DTO
     * @return 响应结果
     */
    @PutMapping
    public Map<String, Object> updateDictionaryItem(@RequestBody DictionaryItemUpdateDTO updateDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.updateDictionaryItem(updateDTO, "system");
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", responseDTO);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除字典项
     * @param itemId 字典项ID
     * @return 响应结果
     */
    @DeleteMapping("/{itemId}")
    public Map<String, Object> deleteDictionaryItem(@PathVariable String itemId) {
        Map<String, Object> result = new HashMap<>();
        try {
            dictionaryItemApplicationService.deleteDictionaryItem(itemId);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取字典项
     * @param itemId 字典项ID
     * @return 响应结果
     */
    @GetMapping("/{itemId}")
    public Map<String, Object> getDictionaryItemById(@PathVariable String itemId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.getDictionaryItemById(itemId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "字典项不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据字典ID获取字典项列表
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/list/{dictId}")
    public Map<String, Object> getDictionaryItemsByDictId(@PathVariable String dictId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DictionaryItemResponseDTO> responseDTOs = dictionaryItemApplicationService.getDictionaryItemsByDictId(dictId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", responseDTOs);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据项编码和字典ID获取字典项
     * @param itemCode 项编码
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/code/{itemCode}")
    public Map<String, Object> getDictionaryItemByCodeAndDictId(@PathVariable String itemCode,
                                                               @RequestParam String dictId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryItemResponseDTO responseDTO = dictionaryItemApplicationService.getDictionaryItemByCodeAndDictId(itemCode, dictId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "字典项不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
}