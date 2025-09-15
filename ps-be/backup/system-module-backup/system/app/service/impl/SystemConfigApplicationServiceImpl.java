package com.jiuxi.module.system.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.module.system.app.service.SystemConfigApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统配置应用服务实现类（适配器模式）
 * @author DDD重构
 * @date 2025-09-12
 */
@Service
public class SystemConfigApplicationServiceImpl implements SystemConfigApplicationService {

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    @Override
    public String getConfigValue(String configKey) {
        return tpSystemConfigService.getConfigValue(configKey);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        return tpSystemConfigService.getConfigValue(configKey, defaultValue);
    }

    @Override
    public TpSystemConfig getByConfigKey(String configKey) {
        return tpSystemConfigService.getByConfigKey(configKey);
    }

    @Override
    public IPage<TpSystemConfigVO> queryPage(TpSystemConfigQuery query) {
        return tpSystemConfigService.queryPage(query);
    }

    @Override
    public void setConfigValue(String configKey, String configValue, String description) {
        tpSystemConfigService.setConfigValue(configKey, configValue, description);
    }

    @Override
    public void setConfigValue(String configKey, String configValue) {
        tpSystemConfigService.setConfigValue(configKey, configValue);
    }

    @Override
    public List<TpSystemConfig> getAllConfigs() {
        return tpSystemConfigService.getAllConfigs();
    }

    @Override
    public Map<String, String> getAllConfigsAsMap() {
        return tpSystemConfigService.getAllConfigsAsMap();
    }

    @Override
    public void updateConfigs(Map<String, String> configs) {
        tpSystemConfigService.updateConfigs(configs);
    }

    @Override
    public void deleteConfig(String configKey) {
        tpSystemConfigService.deleteConfig(configKey);
    }
}