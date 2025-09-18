package com.jiuxi.module.sys.infra.persistence.repository;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.infra.persistence.entity.SystemConfigPO;
import com.jiuxi.module.sys.infra.persistence.mapper.SystemConfigMapper;
import com.jiuxi.module.sys.infra.persistence.assembler.SystemConfigPOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统配置仓储实现类
 * 实现系统配置聚合根的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Repository
public class SystemConfigRepositoryImpl implements SystemConfigRepository {
    
    @Autowired
    private SystemConfigMapper systemConfigMapper;
    
    @Autowired
    private SystemConfigPOAssembler systemConfigPOAssembler;
    
    @Override
    @Transactional
    public SystemConfig save(SystemConfig systemConfig) {
        try {
            // 转换实体为持久化对象
            SystemConfigPO systemConfigPO = systemConfigPOAssembler.toPO(systemConfig);
            
            // 保存持久化对象
            systemConfigMapper.insert(systemConfigPO);
            
            // 转换回实体
            return systemConfigPOAssembler.toEntity(systemConfigPO);
        } catch (Exception e) {
            throw new RuntimeException("保存系统配置失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<SystemConfig> findById(String configId) {
        // 根据ID查找持久化对象
        SystemConfigPO systemConfigPO = systemConfigMapper.selectById(configId);
        
        // 如果不存在，返回空Optional
        if (systemConfigPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(systemConfigPOAssembler.toEntity(systemConfigPO));
    }
    
    @Override
    @Transactional
    public void deleteById(String configId) {
        try {
            // 逻辑删除
            systemConfigMapper.deleteById(configId);
        } catch (Exception e) {
            throw new RuntimeException("删除系统配置失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<SystemConfig> findByConfigKey(String configKey, String tenantId) {
        // 根据配置键和租户ID查找持久化对象
        SystemConfigPO systemConfigPO = systemConfigMapper.findByConfigKeyAndTenantId(configKey, tenantId);
        
        // 如果不存在，返回空Optional
        if (systemConfigPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(systemConfigPOAssembler.toEntity(systemConfigPO));
    }
    
    // 新增的DDD查询方法实现
    
    @Override
    public Optional<SystemConfig> findByConfigKey(ConfigKey configKey, String tenantId) {
        // 委托给字符串版本的方法
        return findByConfigKey(configKey.getValue(), tenantId);
    }
    
    @Override
    public List<SystemConfig> findByConfigType(ConfigType configType, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("config_type", configType.getCode())
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> findByConfigStatus(ConfigStatus configStatus, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("config_status", configStatus.getCode())
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> findByKeyPrefix(String keyPrefix, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现前缀查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.likeRight("config_key", keyPrefix)
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> findByConfigGroup(String configGroup, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("config_group", configGroup)
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> findSystemLevelConfigs(String tenantId) {
        // 查找系统级配置
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("is_system_level", true)
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<SystemConfig> findActiveConfigs(String tenantId) {
        // 查找激活状态的配置
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("config_status", "ACTIVE")
                   .eq("tenant_id", tenantId);
        
        List<SystemConfigPO> systemConfigPOs = systemConfigMapper.selectList(queryWrapper);
        return systemConfigPOs.stream()
                .map(systemConfigPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<SystemConfig> batchSave(List<SystemConfig> systemConfigs) {
        if (systemConfigs == null || systemConfigs.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            // 转换为PO对象列表
            List<SystemConfigPO> systemConfigPOs = systemConfigs.stream()
                    .map(systemConfigPOAssembler::toPO)
                    .collect(Collectors.toList());
            
            // 批量插入（MyBatis Plus支持）
            for (SystemConfigPO po : systemConfigPOs) {
                systemConfigMapper.insert(po);
            }
            
            // 转换回实体列表
            return systemConfigPOs.stream()
                    .map(systemConfigPOAssembler::toEntity)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new RuntimeException("批量保存系统配置失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long countByTypeAndStatus(ConfigType configType, ConfigStatus configStatus, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现统计
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SystemConfigPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("config_type", configType.getCode())
                   .eq("config_status", configStatus.getCode())
                   .eq("tenant_id", tenantId);
        
        return systemConfigMapper.selectCount(queryWrapper);
    }
    
    @Override
    public boolean existsByConfigKey(String configKey, String tenantId) {
        // 实现检查配置键是否存在
        return findByConfigKey(configKey, tenantId).isPresent();
    }
}