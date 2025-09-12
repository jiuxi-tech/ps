package com.jiuxi.module.system.interfaces.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.module.system.app.service.SystemConfigApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SystemConfigManagementController
 * @Description: 系统配置管理控制器
 * @Author: DDD重构
 * @Date: 2025-09-12
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/config")
@Authorization
public class SystemConfigManagementController {

    @Autowired
    private SystemConfigApplicationService systemConfigApplicationService;

    /**
     * 分页查询配置列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpSystemConfigQuery query) {
        IPage<TpSystemConfigVO> page = systemConfigApplicationService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 获取所有配置（Map格式）
     */
    @RequestMapping("/all")
    public JsonResponse getAllConfigs() {
        Map<String, String> configs = systemConfigApplicationService.getAllConfigsAsMap();
        return JsonResponse.buildSuccess(configs);
    }

    /**
     * 根据配置键获取配置值 (向后兼容)
     */
    @RequestMapping("/get")
    public JsonResponse getConfig(@RequestParam String configKey) {
        String value = systemConfigApplicationService.getConfigValue(configKey);
        return JsonResponse.buildSuccess(value);
    }

    /**
     * 查看配置详情
     */
    @RequestMapping("/view")
    public JsonResponse view(@RequestParam String configKey) {
        TpSystemConfig config = systemConfigApplicationService.getByConfigKey(configKey);
        return JsonResponse.buildSuccess(config);
    }

    /**
     * 根据键获取配置值
     */
    @RequestMapping("/value/{configKey}")
    public JsonResponse getConfigValue(@PathVariable String configKey) {
        String value = systemConfigApplicationService.getConfigValue(configKey);
        return JsonResponse.buildSuccess(value);
    }

    /**
     * 设置配置值
     */
    @PostMapping("/setValue")
    public JsonResponse setConfigValue(@RequestParam String configKey, 
                                      @RequestParam String configValue,
                                      @RequestParam(required = false) String description) {
        systemConfigApplicationService.setConfigValue(configKey, configValue, description);
        return JsonResponse.buildSuccess();
    }

    /**
     * 批量更新配置
     */
    @PostMapping("/batchUpdate")
    public JsonResponse updateConfigs(@RequestBody Map<String, String> configs) {
        systemConfigApplicationService.updateConfigs(configs);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除配置
     */
    @PostMapping("/delete")
    public JsonResponse deleteConfig(@RequestParam String configKey) {
        systemConfigApplicationService.deleteConfig(configKey);
        return JsonResponse.buildSuccess();
    }
}