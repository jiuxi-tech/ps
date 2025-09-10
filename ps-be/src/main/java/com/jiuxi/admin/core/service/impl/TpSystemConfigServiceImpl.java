package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpSystemConfigVO;
import com.jiuxi.admin.core.mapper.TpSystemConfigMapper;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.config.ConfigChangeEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置服务实现类
 * @author system
 * @date 2025-01-20
 */
@Service
public class TpSystemConfigServiceImpl implements TpSystemConfigService {

    @Override
    public void setConfigValue(String configKey, String configValue) {
        setConfigValue(configKey, configValue, null);
    }

    @Autowired
    private TpSystemConfigMapper configMapper;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String getConfigValue(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            return null;
        }
        return configMapper.getConfigValue(configKey);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    @Override
    public TpSystemConfig getByConfigKey(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            return null;
        }
        return configMapper.selectById(configKey);
    }

    @Override
    public IPage<TpSystemConfigVO> queryPage(TpSystemConfigQuery query) {
        // 简化实现：直接获取所有配置，然后手动分页
        List<TpSystemConfig> allConfigs = configMapper.selectList();
        
        // 手动过滤
        List<TpSystemConfig> filteredConfigs = allConfigs.stream()
            .filter(config -> {
                if (StringUtils.hasText(query.getConfigKey()) && 
                    !config.getConfigKey().contains(query.getConfigKey())) {
                    return false;
                }
                if (StringUtils.hasText(query.getConfigValue()) && 
                    (config.getConfigValue() == null || !config.getConfigValue().contains(query.getConfigValue()))) {
                    return false;
                }
                if (StringUtils.hasText(query.getDescription()) && 
                    (config.getDescription() == null || !config.getDescription().contains(query.getDescription()))) {
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
        
        // 手动分页
        int current = query.getPageNum();
        int size = query.getPageSize();
        int total = filteredConfigs.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<TpSystemConfig> pageConfigs = start < total ? 
            filteredConfigs.subList(start, end) : 
            new ArrayList<>();
        
        // 转换为VO
        Page<TpSystemConfigVO> voPage = new Page<>(current, size, total);
        List<TpSystemConfigVO> voList = pageConfigs.stream().map(config -> {
            TpSystemConfigVO vo = new TpSystemConfigVO();
            BeanUtils.copyProperties(config, vo);
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public void setConfigValue(String configKey, String configValue, String description) {
        if (!StringUtils.hasText(configKey)) {
            return;
        }

        TpSystemConfig config = configMapper.selectById(configKey);
        String oldValue = null;
        String operation;
        
        if (config == null) {
            // 新增配置
            config = new TpSystemConfig();
            config.setConfigKey(configKey);
            config.setConfigValue(configValue);
            config.setDescription(description);
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
            configMapper.insert(config);
            operation = "ADD";
        } else {
            // 更新配置
            oldValue = config.getConfigValue();
            config.setConfigValue(configValue);
            if (StringUtils.hasText(description)) {
                config.setDescription(description);
            }
            config.setUpdateTime(LocalDateTime.now());
            configMapper.updateById(config);
            operation = "UPDATE";
        }
        
        // 发布配置变更事件
        try {
            ConfigChangeEvent event = new ConfigChangeEvent(this, configKey, oldValue, configValue, operation);
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            // 记录日志但不影响主流程
            System.err.println("发布配置变更事件失败: " + e.getMessage());
        }
    }

    @Override
    public List<TpSystemConfig> getAllConfigs() {
        return configMapper.selectList();
    }

    @Override
    public Map<String, String> getAllConfigsAsMap() {
        List<TpSystemConfig> configs = getAllConfigs();
        return configs.stream()
                .collect(Collectors.toMap(
                        TpSystemConfig::getConfigKey,
                        config -> config.getConfigValue() != null ? config.getConfigValue() : "",
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public void updateConfigs(Map<String, String> configs) {
        if (configs == null || configs.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : configs.entrySet()) {
            setConfigValue(entry.getKey(), entry.getValue(), null);
        }
    }

    @Override
    public void deleteConfig(String configKey) {
        if (StringUtils.hasText(configKey)) {
            // 获取删除前的配置值
            TpSystemConfig config = configMapper.selectById(configKey);
            String oldValue = config != null ? config.getConfigValue() : null;
            
            // 执行删除
            configMapper.deleteById(configKey);
            
            // 发布配置变更事件
            try {
                ConfigChangeEvent event = new ConfigChangeEvent(this, configKey, oldValue, null, "DELETE");
                eventPublisher.publishEvent(event);
            } catch (Exception e) {
                // 记录日志但不影响主流程
                System.err.println("发布配置变更事件失败: " + e.getMessage());
            }
        }
    }
}