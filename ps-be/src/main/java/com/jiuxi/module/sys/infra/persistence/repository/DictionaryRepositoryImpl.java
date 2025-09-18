package com.jiuxi.module.sys.infra.persistence.repository;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryPO;
import com.jiuxi.module.sys.infra.persistence.mapper.DictionaryMapper;
import com.jiuxi.module.sys.infra.persistence.assembler.DictionaryPOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典仓储实现类
 * 实现字典聚合根的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Repository
public class DictionaryRepositoryImpl implements DictionaryRepository {
    
    @Autowired
    private DictionaryMapper dictionaryMapper;
    
    @Autowired
    private DictionaryPOAssembler dictionaryPOAssembler;
    
    @Override
    @Transactional
    public Dictionary save(Dictionary dictionary) {
        try {
            // 转换实体为持久化对象
            DictionaryPO dictionaryPO = dictionaryPOAssembler.toPO(dictionary);
            
            // 保存持久化对象
            dictionaryMapper.insert(dictionaryPO);
            
            // 转换回实体
            return dictionaryPOAssembler.toEntity(dictionaryPO);
        } catch (Exception e) {
            throw new RuntimeException("保存字典失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Dictionary> findById(String dictId) {
        // 根据ID查找持久化对象
        DictionaryPO dictionaryPO = dictionaryMapper.selectById(dictId);
        
        // 如果不存在，返回空Optional
        if (dictionaryPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(dictionaryPOAssembler.toEntity(dictionaryPO));
    }
    
    @Override
    @Transactional
    public void deleteById(String dictId) {
        try {
            // 逻辑删除
            dictionaryMapper.deleteById(dictId);
        } catch (Exception e) {
            throw new RuntimeException("删除字典失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Dictionary> findByDictCode(String dictCode, String tenantId) {
        // 根据字典编码和租户ID查找持久化对象
        DictionaryPO dictionaryPO = dictionaryMapper.findByDictCodeAndTenantId(dictCode, tenantId);
        
        // 如果不存在，返回空Optional
        if (dictionaryPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(dictionaryPOAssembler.toEntity(dictionaryPO));
    }
    
    // 新增的DDD查询方法实现
    
    @Override
    public Optional<Dictionary> findByDictCode(ConfigKey dictCodeObj, String tenantId) {
        // 委托给字符串版本的方法
        return findByDictCode(dictCodeObj.getValue(), tenantId);
    }
    
    @Override
    public List<Dictionary> findByDictType(ConfigType dictType, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType.getCode())
                   .eq("tenant_id", tenantId);
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findByDictStatus(ConfigStatus dictStatus, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("status", dictStatus.getCode())
                   .eq("tenant_id", tenantId);
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findByCodePrefix(String codePrefix, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现前缀查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.likeRight("dict_code", codePrefix)
                   .eq("tenant_id", tenantId);
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findByDictGroup(String dictGroup, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("dict_group", dictGroup)
                   .eq("tenant_id", tenantId);
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findByParentDictId(String parentDictId, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("parent_dict_id", parentDictId)
                   .eq("tenant_id", tenantId)
                   .orderByAsc("order_index");
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findRootDictionaries(String tenantId) {
        // 查找根级字典（parent_dict_id为空或null）
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.isNull("parent_dict_id").or().eq("parent_dict_id", ""))
                   .eq("tenant_id", tenantId)
                   .orderByAsc("order_index");
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findSystemPresetDictionaries(String tenantId) {
        // 查找系统预置字典
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("is_system_preset", true)
                   .eq("tenant_id", tenantId)
                   .orderByAsc("order_index");
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Dictionary> findActiveDictionaries(String tenantId) {
        // 查找激活状态的字典
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("status", "ACTIVE")
                   .eq("tenant_id", tenantId)
                   .orderByAsc("order_index");
        
        List<DictionaryPO> dictionaryPOs = dictionaryMapper.selectList(queryWrapper);
        return dictionaryPOs.stream()
                .map(dictionaryPOAssembler::toEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<Dictionary> batchSave(List<Dictionary> dictionaries) {
        if (dictionaries == null || dictionaries.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            // 转换为PO对象列表
            List<DictionaryPO> dictionaryPOs = dictionaries.stream()
                    .map(dictionaryPOAssembler::toPO)
                    .collect(Collectors.toList());
            
            // 批量插入（MyBatis Plus支持）
            for (DictionaryPO po : dictionaryPOs) {
                dictionaryMapper.insert(po);
            }
            
            // 转换回实体列表
            return dictionaryPOs.stream()
                    .map(dictionaryPOAssembler::toEntity)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new RuntimeException("批量保存字典失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long countByTypeAndStatus(ConfigType dictType, ConfigStatus dictStatus, String tenantId) {
        // 使用MyBatis Plus QueryWrapper实现统计
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DictionaryPO> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType.getCode())
                   .eq("status", dictStatus.getCode())
                   .eq("tenant_id", tenantId);
        
        return dictionaryMapper.selectCount(queryWrapper);
    }
    
    @Override
    public boolean existsByDictCode(String dictCode, String tenantId) {
        // 实现检查字典编码是否存在
        return findByDictCode(dictCode, tenantId).isPresent();
    }
}