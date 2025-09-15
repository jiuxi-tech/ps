package com.jiuxi.module.sys.interfaces.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.module.sys.app.service.SystemConfigApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统配置管理控制器
 * 整合了原system模块的SystemConfigManagementController功能
 * 
 * @author DDD重构 (Merged from system module)
 * @date 2025-09-12
 */
@RestController
@RequestMapping("/sys/config")
@Authorization
public class SystemConfigController {

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
     * 批量更新配置
     */
    @RequestMapping("/batch-update")
    public JsonResponse batchUpdate(@RequestBody Map<String, String> configs) {
        systemConfigApplicationService.updateConfigs(configs);
        return JsonResponse.buildSuccess();
    }

    /**
     * 测试配置接口
     */
    @RequestMapping("/test_config_management")
    public JsonResponse testConfigManagement() {
        return JsonResponse.buildSuccess("系统配置管理功能正常");
    }
}