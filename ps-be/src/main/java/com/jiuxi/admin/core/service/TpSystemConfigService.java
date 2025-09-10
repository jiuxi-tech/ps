package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;

import java.util.List;
import java.util.Map;

/**
 * 系统配置服务接口
 * @author system
 * @date 2025-01-20
 */
public interface TpSystemConfigService {

    /**
     * 获取配置值
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);

    /**
     * 获取配置值，如果不存在返回默认值
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 根据配置键获取配置对象
     * @param configKey 配置键
     * @return 配置对象
     */
    TpSystemConfig getByConfigKey(String configKey);

    /**
     * 分页查询配置
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpSystemConfigVO> queryPage(TpSystemConfigQuery query);

    /**
     * 设置配置值
     * @param configKey 配置键
     * @param configValue 配置值
     * @param description 描述
     */
    void setConfigValue(String configKey, String configValue, String description);

    /**
     * 设置配置值（不更新描述）
     * @param configKey 配置键
     * @param configValue 配置值
     */
    void setConfigValue(String configKey, String configValue);

    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<TpSystemConfig> getAllConfigs();

    /**
     * 获取所有配置（Map格式）
     * @return 配置Map
     */
    Map<String, String> getAllConfigsAsMap();

    /**
     * 批量更新配置
     * @param configs 配置Map
     */
    void updateConfigs(Map<String, String> configs);

    /**
     * 删除配置
     * @param configKey 配置键
     */
    void deleteConfig(String configKey);
}