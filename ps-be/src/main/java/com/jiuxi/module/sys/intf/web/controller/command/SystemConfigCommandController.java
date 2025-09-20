package com.jiuxi.module.sys.intf.web.controller.command;

import com.jiuxi.module.sys.app.service.SystemConfigApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统配置管理命令控制器
 * 负责系统配置相关的数据变更操作 (Create, Update, Delete)
 * 基于CQRS架构设计，专注于处理命令操作
 * 
 * @author DDD Refactor - Phase 6 (Separated from SystemConfigController)
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/config")
@Authorization
public class SystemConfigCommandController {

    @Autowired
    private SystemConfigApplicationService systemConfigApplicationService;

    /**
     * 删除配置
     * 支持JSON格式请求体和表单参数两种方式
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestBody(required = false) Map<String, String> requestBody,
                             @RequestParam(required = false) String configKeys,
                             @RequestParam(required = false) String configKey) {
        
        String keys;
        
        // 优先使用JSON请求体参数
        if (requestBody != null && !requestBody.isEmpty()) {
            keys = requestBody.get("configKeys");
            if (keys == null) {
                keys = requestBody.get("configKey");
            }
        } else {
            // 使用表单参数，优先使用 configKeys，其次使用 configKey
            keys = configKeys;
            if (keys == null) {
                keys = configKey;
            }
        }
        
        if (keys == null || keys.trim().isEmpty()) {
            return JsonResponse.buildFailure("缺少必要参数: configKeys 或 configKey");
        }
        
        List<String> keyList = Arrays.asList(keys.split(","));
        for (String key : keyList) {
            systemConfigApplicationService.deleteConfig(key.trim());
        }
        return JsonResponse.buildSuccess();
    }

    /**
     * 新增配置
     * 支持JSON格式请求体和表单参数两种方式
     */
    @RequestMapping("/add")
    public JsonResponse add(@RequestBody(required = false) Map<String, String> requestBody,
                          @RequestParam(required = false) String configKey, 
                          @RequestParam(required = false) String configValue, 
                          @RequestParam(required = false) String description) {
        
        String key, value, desc;
        
        // 优先使用JSON请求体参数
        if (requestBody != null && !requestBody.isEmpty()) {
            key = requestBody.get("configKey");
            value = requestBody.get("configValue");
            desc = requestBody.get("description");
        } else {
            // 使用表单参数
            key = configKey;
            value = configValue;
            desc = description;
        }
        
        if (key == null || key.trim().isEmpty()) {
            return JsonResponse.buildFailure("缺少必要参数: configKey");
        }
        if (value == null) {
            return JsonResponse.buildFailure("缺少必要参数: configValue");
        }
        
        systemConfigApplicationService.setConfigValue(key, value, desc);
        return JsonResponse.buildSuccess();
    }

    /**
     * 保存或更新配置
     */
    @RequestMapping("/save")
    public JsonResponse save(@RequestParam String configKey, 
                           @RequestParam String configValue, 
                           @RequestParam(required = false) String description) {
        systemConfigApplicationService.setConfigValue(configKey, configValue, description);
        return JsonResponse.buildSuccess();
    }

    /**
     * 批量更新配置
     */
    @RequestMapping("/batch-update")
    public JsonResponse batchUpdate(@RequestBody Map<String, String> configs) {
        systemConfigApplicationService.updateConfigs(configs);
        return JsonResponse.buildSuccess();
    }
    /**
     * 更新系统配置
     * 支持JSON格式请求体和表单参数两种方式
     */
    @RequestMapping("/update")
    public JsonResponse update(@RequestBody(required = false) Map<String, String> requestBody,
                             @RequestParam(required = false) String configKey, 
                             @RequestParam(required = false) String configValue, 
                             @RequestParam(required = false) String description) {
        
        String key, value, desc;
        
        // 优先使用JSON请求体参数
        if (requestBody != null && !requestBody.isEmpty()) {
            key = requestBody.get("configKey");
            value = requestBody.get("configValue");
            desc = requestBody.get("description");
        } else {
            // 使用表单参数
            key = configKey;
            value = configValue;
            desc = description;
        }
        
        if (key == null || key.trim().isEmpty()) {
            return JsonResponse.buildFailure("缺少必要参数: configKey");
        }
        if (value == null) {
            return JsonResponse.buildFailure("缺少必要参数: configValue");
        }
        
        systemConfigApplicationService.setConfigValue(key, value, desc);
        return JsonResponse.buildSuccess();
    }
}