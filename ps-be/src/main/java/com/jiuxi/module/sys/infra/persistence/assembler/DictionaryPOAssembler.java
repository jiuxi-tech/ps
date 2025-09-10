package com.jiuxi.module.sys.infra.persistence.assembler;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryPO;
import org.springframework.stereotype.Component;

/**
 * 字典PO装配器
 * 负责Dictionary实体和DictionaryPO持久化对象之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class DictionaryPOAssembler {
    
    /**
     * 将Dictionary实体转换为DictionaryPO持久化对象
     * @param dictionary 字典实体
     * @return 字典持久化对象
     */
    public DictionaryPO toPO(Dictionary dictionary) {
        if (dictionary == null) {
            return null;
        }
        
        DictionaryPO dictionaryPO = new DictionaryPO();
        dictionaryPO.setDictId(dictionary.getDictId());
        dictionaryPO.setDictCode(dictionary.getDictCode());
        dictionaryPO.setDictName(dictionary.getDictName());
        dictionaryPO.setDictDesc(dictionary.getDictDesc());
        dictionaryPO.setDictType(dictionary.getDictType());
        dictionaryPO.setStatus(dictionary.getStatus());
        dictionaryPO.setOrderIndex(dictionary.getOrderIndex());
        dictionaryPO.setCreator(dictionary.getCreator());
        dictionaryPO.setCreateTime(dictionary.getCreateTime());
        dictionaryPO.setUpdator(dictionary.getUpdator());
        dictionaryPO.setUpdateTime(dictionary.getUpdateTime());
        dictionaryPO.setTenantId(dictionary.getTenantId());
        dictionaryPO.setDeleted(0); // 默认未删除
        
        return dictionaryPO;
    }
    
    /**
     * 将DictionaryPO持久化对象转换为Dictionary实体
     * @param dictionaryPO 字典持久化对象
     * @return 字典实体
     */
    public Dictionary toEntity(DictionaryPO dictionaryPO) {
        if (dictionaryPO == null) {
            return null;
        }
        
        Dictionary dictionary = new Dictionary();
        dictionary.setDictId(dictionaryPO.getDictId());
        dictionary.setDictCode(dictionaryPO.getDictCode());
        dictionary.setDictName(dictionaryPO.getDictName());
        dictionary.setDictDesc(dictionaryPO.getDictDesc());
        dictionary.setDictType(dictionaryPO.getDictType());
        dictionary.setStatus(dictionaryPO.getStatus());
        dictionary.setOrderIndex(dictionaryPO.getOrderIndex());
        dictionary.setCreator(dictionaryPO.getCreator());
        dictionary.setCreateTime(dictionaryPO.getCreateTime());
        dictionary.setUpdator(dictionaryPO.getUpdator());
        dictionary.setUpdateTime(dictionaryPO.getUpdateTime());
        dictionary.setTenantId(dictionaryPO.getTenantId());
        
        return dictionary;
    }
}