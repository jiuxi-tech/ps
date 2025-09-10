package com.jiuxi.module.sys.app.assembler;

import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.app.dto.DictionaryItemCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemUpdateDTO;
import org.springframework.stereotype.Component;

/**
 * 字典项装配器
 * 负责DictionaryItem实体和DTO之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class DictionaryItemAssembler {
    
    /**
     * 将DictionaryItemCreateDTO转换为DictionaryItem实体
     * @param dto 字典项创建DTO
     * @return 字典项实体
     */
    public DictionaryItem toEntity(DictionaryItemCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        DictionaryItem dictionaryItem = new DictionaryItem();
        dictionaryItem.setDictId(dto.getDictId());
        dictionaryItem.setItemCode(dto.getItemCode());
        dictionaryItem.setItemName(dto.getItemName());
        dictionaryItem.setItemValue(dto.getItemValue());
        dictionaryItem.setItemDesc(dto.getItemDesc());
        dictionaryItem.setOrderIndex(dto.getOrderIndex());
        dictionaryItem.setTenantId(dto.getTenantId());
        
        return dictionaryItem;
    }
    
    /**
     * 将DictionaryItemUpdateDTO转换为DictionaryItem实体
     * @param dto 字典项更新DTO
     * @return 字典项实体
     */
    public DictionaryItem toEntity(DictionaryItemUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        DictionaryItem dictionaryItem = new DictionaryItem();
        dictionaryItem.setItemId(dto.getItemId());
        dictionaryItem.setDictId(dto.getDictId());
        dictionaryItem.setItemCode(dto.getItemCode());
        dictionaryItem.setItemName(dto.getItemName());
        dictionaryItem.setItemValue(dto.getItemValue());
        dictionaryItem.setItemDesc(dto.getItemDesc());
        dictionaryItem.setStatus(dto.getStatus());
        dictionaryItem.setOrderIndex(dto.getOrderIndex());
        dictionaryItem.setUpdator(dto.getUpdator());
        
        return dictionaryItem;
    }
    
    /**
     * 将DictionaryItem实体转换为DictionaryItemResponseDTO
     * @param dictionaryItem 字典项实体
     * @return 字典项响应DTO
     */
    public DictionaryItemResponseDTO toResponseDTO(DictionaryItem dictionaryItem) {
        if (dictionaryItem == null) {
            return null;
        }
        
        DictionaryItemResponseDTO dto = new DictionaryItemResponseDTO();
        dto.setItemId(dictionaryItem.getItemId());
        dto.setDictId(dictionaryItem.getDictId());
        dto.setItemCode(dictionaryItem.getItemCode());
        dto.setItemName(dictionaryItem.getItemName());
        dto.setItemValue(dictionaryItem.getItemValue());
        dto.setItemDesc(dictionaryItem.getItemDesc());
        dto.setStatus(dictionaryItem.getStatus());
        dto.setOrderIndex(dictionaryItem.getOrderIndex());
        dto.setCreator(dictionaryItem.getCreator());
        dto.setCreateTime(dictionaryItem.getCreateTime());
        dto.setUpdator(dictionaryItem.getUpdator());
        dto.setUpdateTime(dictionaryItem.getUpdateTime());
        dto.setTenantId(dictionaryItem.getTenantId());
        
        return dto;
    }
}