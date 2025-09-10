package com.jiuxi.module.sys.infra.persistence.repository;

import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.domain.repo.DictionaryItemRepository;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryItemPO;
import com.jiuxi.module.sys.infra.persistence.mapper.DictionaryItemMapper;
import com.jiuxi.module.sys.infra.persistence.assembler.DictionaryItemPOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典项仓储实现类
 * 实现字典项实体的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Repository
public class DictionaryItemRepositoryImpl implements DictionaryItemRepository {
    
    @Autowired
    private DictionaryItemMapper dictionaryItemMapper;
    
    @Autowired
    private DictionaryItemPOAssembler dictionaryItemPOAssembler;
    
    @Override
    public DictionaryItem save(DictionaryItem dictionaryItem) {
        // 转换实体为持久化对象
        DictionaryItemPO dictionaryItemPO = dictionaryItemPOAssembler.toPO(dictionaryItem);
        
        // 保存持久化对象
        dictionaryItemMapper.insert(dictionaryItemPO);
        
        // 转换回实体
        return dictionaryItemPOAssembler.toEntity(dictionaryItemPO);
    }
    
    @Override
    public Optional<DictionaryItem> findById(String itemId) {
        // 根据ID查找持久化对象
        DictionaryItemPO dictionaryItemPO = dictionaryItemMapper.selectById(itemId);
        
        // 如果不存在，返回空Optional
        if (dictionaryItemPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(dictionaryItemPOAssembler.toEntity(dictionaryItemPO));
    }
    
    @Override
    public void deleteById(String itemId) {
        // 逻辑删除
        dictionaryItemMapper.deleteById(itemId);
    }
    
    @Override
    public List<DictionaryItem> findByDictId(String dictId) {
        // 根据字典ID查找字典项持久化对象列表
        List<DictionaryItemPO> dictionaryItemPOs = dictionaryItemMapper.findByDictId(dictId);
        
        // 转换为实体列表
        return dictionaryItemPOs.stream()
                .map(dictionaryItemPOAssembler::toEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<DictionaryItem> findByItemCodeAndDictId(String itemCode, String dictId) {
        // 根据项编码和字典ID查找字典项持久化对象
        DictionaryItemPO dictionaryItemPO = dictionaryItemMapper.findByItemCodeAndDictId(itemCode, dictId);
        
        // 如果不存在，返回空Optional
        if (dictionaryItemPO == null) {
            return Optional.empty();
        }
        
        // 转换为实体并返回Optional
        return Optional.of(dictionaryItemPOAssembler.toEntity(dictionaryItemPO));
    }
}