package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TpSystemConfigController
 * @Description: 系统配置管理控制器
 * @Author: System
 * @Date: 2024-01-26
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/config")
@Authorization
public class TpSystemConfigController {

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    /**
     * 分页查询配置列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpSystemConfigQuery query) {
        IPage<TpSystemConfigVO> page = tpSystemConfigService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 获取所有配置（Map格式）
     */
    @RequestMapping("/all")
    public JsonResponse getAllConfigs() {
        Map<String, String> configs = tpSystemConfigService.getAllConfigsAsMap();
        return JsonResponse.buildSuccess(configs);
    }

    /**
     * 根据配置键获取配置值
     */
    @RequestMapping("/get")
    public JsonResponse getConfig(@RequestParam String configKey) {
        String value = tpSystemConfigService.getConfigValue(configKey);
        return JsonResponse.buildSuccess(value);
    }

    /**
     * 查看配置详情
     */
    @RequestMapping("/view")
    public JsonResponse view(@RequestParam String configKey) {
        TpSystemConfig config = tpSystemConfigService.getByConfigKey(configKey);
        return JsonResponse.buildSuccess(config);
    }

    /**
     * 新增配置
     */
    @RequestMapping("/add")
    public JsonResponse add(@RequestBody TpSystemConfig config) {
        tpSystemConfigService.setConfigValue(config.getConfigKey(), config.getConfigValue(), config.getDescription());
        return JsonResponse.buildSuccess();
    }

    /**
     * 更新配置
     */
    @RequestMapping("/update")
    public JsonResponse update(@RequestBody TpSystemConfig config) {
        tpSystemConfigService.setConfigValue(config.getConfigKey(), config.getConfigValue(), config.getDescription());
        return JsonResponse.buildSuccess();
    }

    /**
     * 批量更新配置
     */
    @RequestMapping("/batch-update")
    public JsonResponse batchUpdate(@RequestBody Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            tpSystemConfigService.setConfigValue(entry.getKey(), entry.getValue());
        }
        return JsonResponse.buildSuccess();
    }

    /**
     * 保存配置（新增或更新）
     */
    @RequestMapping("/save")
    public JsonResponse save(@RequestBody TpSystemConfig config) {
        tpSystemConfigService.setConfigValue(config.getConfigKey(), config.getConfigValue(), config.getDescription());
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除配置
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestBody String[] configKeys) {
        for (String configKey : configKeys) {
            tpSystemConfigService.deleteConfig(configKey);
        }
        return JsonResponse.buildSuccess();
    }

    /**
     * 根据配置键删除单个配置
     */
    @RequestMapping("/delete/{configKey}")
    public JsonResponse deleteByKey(@PathVariable String configKey) {
        tpSystemConfigService.deleteConfig(configKey);
        return JsonResponse.buildSuccess();
    }

    /**
     * 重置配置缓存
     */
    @RequestMapping("/refresh-cache")
    public JsonResponse refreshCache() {
        // 这里可以添加缓存刷新逻辑
        return JsonResponse.buildSuccess("缓存刷新成功");
    }
}