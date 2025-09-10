package com.jiuxi.module.sys.app.service;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import com.jiuxi.module.sys.app.assembler.DictionaryAssembler;
import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 字典应用服务
 * 负责字典相关的应用逻辑和事务协调
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Service
public class DictionaryApplicationService {
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    @Autowired
    private DictionaryAssembler dictionaryAssembler;
    
    /**
     * 创建字典
     * @param createDTO 字典创建DTO
     * @param creator 创建人
     * @return 字典响应DTO
     */
    public DictionaryResponseDTO createDictionary(DictionaryCreateDTO createDTO, String creator) {
        // 转换DTO为实体
        Dictionary dictionary = dictionaryAssembler.toEntity(createDTO);
        
        // 设置创建信息
        dictionary.setCreator(creator);
        dictionary.setCreateTime(LocalDateTime.now());
        
        // 保存字典
        Dictionary savedDictionary = dictionaryRepository.save(dictionary);
        
        // 转换为响应DTO
        return dictionaryAssembler.toResponseDTO(savedDictionary);
    }
    
    /**
     * 更新字典
     * @param updateDTO 字典更新DTO
     * @param updator 更新人
     * @return 字典响应DTO
     */
    public DictionaryResponseDTO updateDictionary(DictionaryUpdateDTO updateDTO, String updator) {
        // 查找现有字典
        Optional<Dictionary> existingDictionaryOpt = dictionaryRepository.findById(updateDTO.getDictId());
        if (!existingDictionaryOpt.isPresent()) {
            throw new RuntimeException("字典不存在");
        }
        
        // 更新字典信息
        Dictionary existingDictionary = existingDictionaryOpt.get();
        existingDictionary.setDictCode(updateDTO.getDictCode());
        existingDictionary.setDictName(updateDTO.getDictName());
        existingDictionary.setDictDesc(updateDTO.getDictDesc());
        existingDictionary.setDictType(updateDTO.getDictType());
        existingDictionary.setStatus(updateDTO.getStatus());
        existingDictionary.setOrderIndex(updateDTO.getOrderIndex());
        existingDictionary.setUpdator(updator);
        existingDictionary.setUpdateTime(LocalDateTime.now());
        
        // 保存更新后的字典
        Dictionary updatedDictionary = dictionaryRepository.save(existingDictionary);
        
        // 转换为响应DTO
        return dictionaryAssembler.toResponseDTO(updatedDictionary);
    }
    
    /**
     * 根据ID删除字典
     * @param dictId 字典ID
     */
    public void deleteDictionary(String dictId) {
        dictionaryRepository.deleteById(dictId);
    }
    
    /**
     * 根据ID获取字典
     * @param dictId 字典ID
     * @return 字典响应DTO
     */
    public DictionaryResponseDTO getDictionaryById(String dictId) {
        Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
        if (!dictionaryOpt.isPresent()) {
            return null;
        }
        
        return dictionaryAssembler.toResponseDTO(dictionaryOpt.get());
    }
    
    /**
     * 根据字典编码获取字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典响应DTO
     */
    public DictionaryResponseDTO getDictionaryByCode(String dictCode, String tenantId) {
        Optional<Dictionary> dictionaryOpt = dictionaryRepository.findByDictCode(dictCode, tenantId);
        if (!dictionaryOpt.isPresent()) {
            return null;
        }
        
        return dictionaryAssembler.toResponseDTO(dictionaryOpt.get());
    }
}