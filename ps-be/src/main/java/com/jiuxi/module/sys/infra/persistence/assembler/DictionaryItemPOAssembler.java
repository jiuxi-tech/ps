package com.jiuxi.module.sys.infra.persistence.assembler;

import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryItemPO;
import org.springframework.stereotype.Component;

/**
 * 字典项PO装配器
 * 负责DictionaryItem实体和DictionaryItemPO持久化对象之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class DictionaryItemPOAssembler {
    
    /**
     * 将DictionaryItem实体转换为DictionaryItemPO持久化对象
     * @param dictionaryItem 字典项实体
     * @return 字典项持久化对象
     */
    public DictionaryItemPO toPO(DictionaryItem dictionaryItem) {
        if (dictionaryItem == null) {
            return null;
        }
        
        DictionaryItemPO dictionaryItemPO = new DictionaryItemPO();
        dictionaryItemPO.setItemId(dictionaryItem.getItemId());
        dictionaryItemPO.setDictId(dictionaryItem.getDictId());
        dictionaryItemPO.setItemCode(dictionaryItem.getItemCode());
        dictionaryItemPO.setItemName(dictionaryItem.getItemName());
        dictionaryItemPO.setItemValue(dictionaryItem.getItemValue());
        dictionaryItemPO.setItemDesc(dictionaryItem.getItemDesc());
        dictionaryItemPO.setStatus(dictionaryItem.getStatus());
        dictionaryItemPO.setOrderIndex(dictionaryItem.getOrderIndex());
        dictionaryItemPO.setCreator(dictionaryItem.getCreator());
        dictionaryItemPO.setCreateTime(dictionaryItem.getCreateTime());
        dictionaryItemPO.setUpdator(dictionaryItem.getUpdator());
        dictionaryItemPO.setUpdateTime(dictionaryItem.getUpdateTime());
        dictionaryItemPO.setTenantId(dictionaryItem.getTenantId());
        dictionaryItemPO.setDeleted(0); // 默认未删除
        
        return dictionaryItemPO;
    }
    
    /**
     * 将DictionaryItemPO持久化对象转换为DictionaryItem实体
     * @param dictionaryItemPO 字典项持久化对象
     * @return 字典项实体
     */
    public DictionaryItem toEntity(DictionaryItemPO dictionaryItemPO) {
        if (dictionaryItemPO == null) {
            return null;
        }
        
        DictionaryItem dictionaryItem = new DictionaryItem();
        dictionaryItem.setItemId(dictionaryItemPO.getItemId());
        dictionaryItem.setDictId(dictionaryItemPO.getDictId());
        dictionaryItem.setItemCode(dictionaryItemPO.getItemCode());
        dictionaryItem.setItemName(dictionaryItemPO.getItemName());
        dictionaryItem.setItemValue(dictionaryItemPO.getItemValue());
        dictionaryItem.setItemDesc(dictionaryItemPO.getItemDesc());
        dictionaryItem.setStatus(dictionaryItemPO.getStatus());
        dictionaryItem.setOrderIndex(dictionaryItemPO.getOrderIndex());
        dictionaryItem.setCreator(dictionaryItemPO.getCreator());
        dictionaryItem.setCreateTime(dictionaryItemPO.getCreateTime());
        dictionaryItem.setUpdator(dictionaryItemPO.getUpdator());
        dictionaryItem.setUpdateTime(dictionaryItemPO.getUpdateTime());
        dictionaryItem.setTenantId(dictionaryItemPO.getTenantId());
        
        return dictionaryItem;
    }
}