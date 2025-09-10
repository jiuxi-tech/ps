package com.jiuxi.module.sys.infra.persistence.repository;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryPO;
import com.jiuxi.module.sys.infra.persistence.mapper.DictionaryMapper;
import com.jiuxi.module.sys.infra.persistence.assembler.DictionaryPOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Dictionary save(Dictionary dictionary) {
        // 转换实体为持久化对象
        DictionaryPO dictionaryPO = dictionaryPOAssembler.toPO(dictionary);
        
        // 保存持久化对象
        dictionaryMapper.insert(dictionaryPO);
        
        // 转换回实体
        return dictionaryPOAssembler.toEntity(dictionaryPO);
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
    public void deleteById(String dictId) {
        // 逻辑删除
        dictionaryMapper.deleteById(dictId);
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
}