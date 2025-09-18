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
     * 批量删除配置
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestParam String configKeys) {
        List<String> keyList = Arrays.asList(configKeys.split(","));
        for (String configKey : keyList) {
            systemConfigApplicationService.deleteConfig(configKey.trim());
        }
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
}