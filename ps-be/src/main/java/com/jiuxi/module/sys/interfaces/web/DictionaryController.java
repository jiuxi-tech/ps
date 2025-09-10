package com.jiuxi.module.sys.interfaces.web;

import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import com.jiuxi.module.sys.app.service.DictionaryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典管理控制器
 * 提供字典相关的RESTful API接口
 * 
 * @author System Management
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/system/dictionary")
public class DictionaryController {
    
    @Autowired
    private DictionaryApplicationService dictionaryApplicationService;
    
    /**
     * 创建字典
     * @param createDTO 字典创建DTO
     * @return 响应结果
     */
    @PostMapping
    public Map<String, Object> createDictionary(@RequestBody DictionaryCreateDTO createDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.createDictionary(createDTO, "system");
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
     * 更新字典
     * @param updateDTO 字典更新DTO
     * @return 响应结果
     */
    @PutMapping
    public Map<String, Object> updateDictionary(@RequestBody DictionaryUpdateDTO updateDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.updateDictionary(updateDTO, "system");
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
     * 删除字典
     * @param dictId 字典ID
     * @return 响应结果
     */
    @DeleteMapping("/{dictId}")
    public Map<String, Object> deleteDictionary(@PathVariable String dictId) {
        Map<String, Object> result = new HashMap<>();
        try {
            dictionaryApplicationService.deleteDictionary(dictId);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取字典
     * @param dictId 字典ID
     * @return 响应结果
     */
    @GetMapping("/{dictId}")
    public Map<String, Object> getDictionaryById(@PathVariable String dictId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.getDictionaryById(dictId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "字典不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据字典编码获取字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/code/{dictCode}")
    public Map<String, Object> getDictionaryByCode(@PathVariable String dictCode, 
                                                   @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DictionaryResponseDTO responseDTO = dictionaryApplicationService.getDictionaryByCode(dictCode, tenantId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "字典不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
}