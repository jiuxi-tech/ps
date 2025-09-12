package com.jiuxi.module.sys.interfaces.web;

import com.jiuxi.module.sys.app.dto.SystemConfigCreateDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigResponseDTO;
import com.jiuxi.module.sys.app.dto.SystemConfigUpdateDTO;
import com.jiuxi.module.sys.app.service.SystemConfigApplicationService;
import com.jiuxi.module.sys.app.service.ParameterConfigApplicationService;
import com.jiuxi.module.sys.domain.service.SystemConfigCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    
    @Autowired
    private ParameterConfigApplicationService parameterConfigApplicationService;
    
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
    
    // 新增的DDD功能接口
    
    /**
     * 根据配置类型获取配置列表
     * @param configType 配置类型
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/type/{configType}")
    public Map<String, Object> getSystemConfigsByType(@PathVariable String configType,
                                                     @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SystemConfigResponseDTO> configs = systemConfigApplicationService.getSystemConfigsByType(configType, tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", configs);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据配置组获取配置列表
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/group/{configGroup}")
    public Map<String, Object> getSystemConfigsByGroup(@PathVariable String configGroup,
                                                      @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SystemConfigResponseDTO> configs = systemConfigApplicationService.getSystemConfigsByGroup(configGroup, tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", configs);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取系统级配置
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/system-level")
    public Map<String, Object> getSystemLevelConfigs(@RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SystemConfigResponseDTO> configs = systemConfigApplicationService.getSystemLevelConfigs(tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", configs);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取激活状态的配置
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/active")
    public Map<String, Object> getActiveConfigs(@RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SystemConfigResponseDTO> configs = systemConfigApplicationService.getActiveConfigs(tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", configs);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 手动热更新配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @PostMapping("/hot-reload/{configKey}")
    public Map<String, Object> hotReloadConfig(@PathVariable String configKey,
                                              @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = systemConfigApplicationService.hotReloadConfig(configKey, tenantId);
            if (success) {
                result.put("code", 200);
                result.put("message", "热更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "热更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "热更新失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量热更新配置
     * @param configKeys 配置键列表
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @PostMapping("/hot-reload/batch")
    public Map<String, Object> batchHotReloadConfigs(@RequestBody List<String> configKeys,
                                                    @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int successCount = systemConfigApplicationService.batchHotReloadConfigs(configKeys, tenantId);
            result.put("code", 200);
            result.put("message", "批量热更新完成");
            result.put("data", Map.of("total", configKeys.size(), "success", successCount));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "批量热更新失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取缓存统计信息
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/cache/stats")
    public Map<String, Object> getCacheStats(@RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SystemConfigCacheService.CacheStats stats = systemConfigApplicationService.getCacheStats(tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", stats);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 清理缓存
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @DeleteMapping("/cache")
    public Map<String, Object> clearCache(@RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            systemConfigApplicationService.clearCache(tenantId);
            result.put("code", 200);
            result.put("message", "缓存清理成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "缓存清理失败: " + e.getMessage());
        }
        return result;
    }
    
    // 参数配置相关接口
    
    /**
     * 获取参数值
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @GetMapping("/param/{paramKey}")
    public Map<String, Object> getParameter(@PathVariable String paramKey,
                                           @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String value = parameterConfigApplicationService.getStringParameter(paramKey, tenantId, null);
            if (value != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", Map.of("key", paramKey, "value", value));
            } else {
                result.put("code", 404);
                result.put("message", "参数不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 设置参数值
     * @param paramKey 参数键
     * @param paramValue 参数值
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @PostMapping("/param/{paramKey}")
    public Map<String, Object> setParameter(@PathVariable String paramKey,
                                           @RequestBody Map<String, String> request,
                                           @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String paramValue = request.get("value");
            boolean success = parameterConfigApplicationService.setStringParameter(paramKey, paramValue, tenantId, "admin");
            if (success) {
                result.put("code", 200);
                result.put("message", "参数设置成功");
            } else {
                result.put("code", 400);
                result.put("message", "参数设置失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "参数设置失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量获取参数
     * @param paramKeys 参数键列表
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @PostMapping("/param/batch/get")
    public Map<String, Object> getBatchParameters(@RequestBody List<String> paramKeys,
                                                 @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, String> parameters = parameterConfigApplicationService.getBatchParameters(paramKeys, tenantId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", parameters);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量设置参数
     * @param parameters 参数键值对
     * @param tenantId 租户ID
     * @return 响应结果
     */
    @PostMapping("/param/batch/set")
    public Map<String, Object> setBatchParameters(@RequestBody Map<String, String> parameters,
                                                 @RequestParam(required = false, defaultValue = "default") String tenantId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int successCount = parameterConfigApplicationService.setBatchParameters(parameters, tenantId, "admin");
            result.put("code", 200);
            result.put("message", "批量设置完成");
            result.put("data", Map.of("total", parameters.size(), "success", successCount));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "批量设置失败: " + e.getMessage());
        }
        return result;
    }
}