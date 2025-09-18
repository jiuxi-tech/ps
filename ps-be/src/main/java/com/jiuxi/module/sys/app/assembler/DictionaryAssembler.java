package com.jiuxi.module.sys.app.assembler;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import com.jiuxi.module.sys.app.command.dto.DictionaryCreateCommand;
import com.jiuxi.module.sys.app.command.dto.DictionaryUpdateCommand;
import com.jiuxi.module.sys.app.query.dto.DictionaryResponse;
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
    
    // CQRS 兼容方法
    
    /**
     * 将DictionaryCreateCommand转换为Dictionary实体
     * @param command 字典创建命令
     * @return 字典实体
     */
    public Dictionary toEntity(DictionaryCreateCommand command) {
        if (command == null) {
            return null;
        }
        
        Dictionary dictionary = new Dictionary();
        dictionary.setDictCode(command.getDictCode());
        dictionary.setDictName(command.getDictName());
        dictionary.setDictDesc(command.getDictDesc());
        dictionary.setDictType(command.getDictType());
        dictionary.setStatus(command.getStatus());
        dictionary.setDictGroup(command.getDictGroup());
        dictionary.setParentDictId(command.getParentDictId());
        dictionary.setOrderIndex(command.getOrderIndex());
        dictionary.setTenantId(command.getTenantId());
        
        return dictionary;
    }
    
    /**
     * 将DictionaryUpdateCommand转换为Dictionary实体
     * @param command 字典更新命令
     * @return 字典实体
     */
    public Dictionary toEntity(DictionaryUpdateCommand command) {
        if (command == null) {
            return null;
        }
        
        Dictionary dictionary = new Dictionary();
        dictionary.setDictId(command.getDictId());
        dictionary.setDictCode(command.getDictCode());
        dictionary.setDictName(command.getDictName());
        dictionary.setDictDesc(command.getDictDesc());
        dictionary.setDictType(command.getDictType());
        dictionary.setStatus(command.getStatus());
        dictionary.setDictGroup(command.getDictGroup());
        dictionary.setParentDictId(command.getParentDictId());
        dictionary.setOrderIndex(command.getOrderIndex());
        dictionary.setUpdator(command.getUpdator());
        
        return dictionary;
    }
    
    /**
     * 将Dictionary实体转换为DictionaryResponse（新的CQRS响应）
     * @param dictionary 字典实体
     * @return 字典响应
     */
    public DictionaryResponse toResponse(Dictionary dictionary) {
        if (dictionary == null) {
            return null;
        }
        
        DictionaryResponse response = new DictionaryResponse();
        response.setDictId(dictionary.getDictId());
        response.setDictCode(dictionary.getDictCode());
        response.setDictName(dictionary.getDictName());
        response.setDictDesc(dictionary.getDictDesc());
        response.setDictType(dictionary.getDictType());
        response.setStatus(dictionary.getStatus());
        response.setDictGroup(dictionary.getDictGroup());
        response.setParentDictId(dictionary.getParentDictId());
        response.setOrderIndex(dictionary.getOrderIndex());
        response.setCreator(dictionary.getCreator());
        response.setCreateTime(dictionary.getCreateTime());
        response.setUpdator(dictionary.getUpdator());
        response.setUpdateTime(dictionary.getUpdateTime());
        response.setTenantId(dictionary.getTenantId());
        
        return response;
    }
}