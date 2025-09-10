package com.jiuxi.module.sys.app.assembler;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import org.springframework.stereotype.Component;

/**
 * 字典装配器
 * 负责Dictionary实体和DTO之间的转换
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Component
public class DictionaryAssembler {
    
    /**
     * 将DictionaryCreateDTO转换为Dictionary实体
     * @param dto 字典创建DTO
     * @return 字典实体
     */
    public Dictionary toEntity(DictionaryCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Dictionary dictionary = new Dictionary();
        dictionary.setDictCode(dto.getDictCode());
        dictionary.setDictName(dto.getDictName());
        dictionary.setDictDesc(dto.getDictDesc());
        dictionary.setDictType(dto.getDictType());
        dictionary.setOrderIndex(dto.getOrderIndex());
        dictionary.setTenantId(dto.getTenantId());
        
        return dictionary;
    }
    
    /**
     * 将DictionaryUpdateDTO转换为Dictionary实体
     * @param dto 字典更新DTO
     * @return 字典实体
     */
    public Dictionary toEntity(DictionaryUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Dictionary dictionary = new Dictionary();
        dictionary.setDictId(dto.getDictId());
        dictionary.setDictCode(dto.getDictCode());
        dictionary.setDictName(dto.getDictName());
        dictionary.setDictDesc(dto.getDictDesc());
        dictionary.setDictType(dto.getDictType());
        dictionary.setStatus(dto.getStatus());
        dictionary.setOrderIndex(dto.getOrderIndex());
        dictionary.setUpdator(dto.getUpdator());
        
        return dictionary;
    }
    
    /**
     * 将Dictionary实体转换为DictionaryResponseDTO
     * @param dictionary 字典实体
     * @return 字典响应DTO
     */
    public DictionaryResponseDTO toResponseDTO(Dictionary dictionary) {
        if (dictionary == null) {
            return null;
        }
        
        DictionaryResponseDTO dto = new DictionaryResponseDTO();
        dto.setDictId(dictionary.getDictId());
        dto.setDictCode(dictionary.getDictCode());
        dto.setDictName(dictionary.getDictName());
        dto.setDictDesc(dictionary.getDictDesc());
        dto.setDictType(dictionary.getDictType());
        dto.setStatus(dictionary.getStatus());
        dto.setOrderIndex(dictionary.getOrderIndex());
        dto.setCreator(dictionary.getCreator());
        dto.setCreateTime(dictionary.getCreateTime());
        dto.setUpdator(dictionary.getUpdator());
        dto.setUpdateTime(dictionary.getUpdateTime());
        dto.setTenantId(dictionary.getTenantId());
        
        return dto;
    }
}