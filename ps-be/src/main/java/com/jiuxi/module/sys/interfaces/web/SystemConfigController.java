package com.jiuxi.module.sys.interfaces.web;

import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
import com.jiuxi.module.sys.app.service.SystemConfigApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置管理控制器
 * 提供系统配置相关的RESTful API接口
 * 
 * @author System Management
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    
    @Autowired
    private SystemConfigApplicationService systemConfigApplicationService;
    
    /**
     * 创建系统配置
     * @param createDTO 系统配置创建DTO
     * @return 响应结果
     */
    @PostMapping
    public Map<String, Object> createSystemConfig(@RequestBody SystemConfigCreateDTO createDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            SystemConfigResponseDTO responseDTO = systemConfigApplicationService.createSystemConfig(createDTO, "system");
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
     * 更新系统配置
     * @param updateDTO 系统配置更新DTO
     * @return 响应结果
     */
    @PutMapping
    public Map<String, Object> updateSystemConfig(@RequestBody SystemConfigUpdateDTO updateDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            SystemConfigResponseDTO responseDTO = systemConfigApplicationService.updateSystemConfig(updateDTO, "system");
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
     * 删除系统配置
     * @param configId 配置ID
     * @return 响应结果
     */
    @DeleteMapping("/{configId}")
    public Map<String, Object> deleteSystemConfig(@PathVariable String configId) {
        Map<String, Object> result = new HashMap<>();
        try {
            systemConfigApplicationService.deleteSystemConfig(configId);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取系统配置
     * @param configId 配置ID
     * @return 响应结果
     */
    @GetMapping("/{configId}")
    public Map<String, Object> getSystemConfigById(@PathVariable String configId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SystemConfigResponseDTO responseDTO = systemConfigApplicationService.getSystemConfigById(configId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "系统配置不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据配置键获取系统配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/key/{configKey}")
    public Map<String, Object> getSystemConfigByKey(@PathVariable String configKey,
                                                   @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SystemConfigResponseDTO responseDTO = systemConfigApplicationService.getSystemConfigByKey(configKey, tenantId);
            if (responseDTO != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", responseDTO);
            } else {
                result.put("code", 404);
                result.put("message", "系统配置不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
}