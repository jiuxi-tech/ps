package com.jiuxi.module.sys.intf.web.controller.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.module.sys.app.service.SystemConfigApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置管理查询控制器
 * 负责系统配置相关的数据查询操作 (Read)
 * 基于CQRS架构设计，专注于处理查询操作
 * 
 * @author DDD Refactor - Phase 6 (Separated from SystemConfigController)
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/config")
@Authorization
public class SystemConfigQueryController {

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
     * 查看配置详情
     */
    @RequestMapping("/view")
    public JsonResponse view(@RequestParam String configKey) {
        TpSystemConfig config = systemConfigApplicationService.getByConfigKey(configKey);
        return JsonResponse.buildSuccess(config);
    }

    /**
     * 根据键获取配置值（路径参数方式）
     */
    @RequestMapping("/value/{configKey}")
    public JsonResponse getConfigValue(@PathVariable String configKey) {
        String value = systemConfigApplicationService.getConfigValue(configKey);
        return JsonResponse.buildSuccess(value);
    }

    /**
     * 根据键获取配置值（查询参数方式）
     */
    @RequestMapping("/get")
    public JsonResponse getConfigByKey(@RequestParam String configKey) {
        String value = systemConfigApplicationService.getConfigValue(configKey);
        return JsonResponse.buildSuccess(value);
    }

    /**
     * 获取所有配置
     */
    @RequestMapping("/all")
    public JsonResponse getAllConfigs() {
        Map<String, String> configs = systemConfigApplicationService.getAllConfigsAsMap();
        return JsonResponse.buildSuccess(configs);
    }

    /**
     * 测试配置接口
     */
    @RequestMapping("/test_config_management")
    public JsonResponse testConfigManagement() {
        return JsonResponse.buildSuccess("系统配置管理功能正常");
    }
}