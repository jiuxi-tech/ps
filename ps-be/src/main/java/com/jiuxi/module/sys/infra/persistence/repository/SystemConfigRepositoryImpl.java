package com.jiuxi.module.sys.infra.persistence.repository;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.infra.persistence.entity.SystemConfigPO;
import com.jiuxi.module.sys.infra.persistence.mapper.SystemConfigMapper;
import com.jiuxi.module.sys.infra.persistence.assembler.SystemConfigPOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public SystemConfig save(SystemConfig systemConfig) {
        // 转换实体为持久化对象
        SystemConfigPO systemConfigPO = systemConfigPOAssembler.toPO(systemConfig);
        
        // 保存持久化对象
        systemConfigMapper.insert(systemConfigPO);
        
        // 转换回实体
        return systemConfigPOAssembler.toEntity(systemConfigPO);
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
    public void deleteById(String configId) {
        // 逻辑删除
        systemConfigMapper.deleteById(configId);
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
}