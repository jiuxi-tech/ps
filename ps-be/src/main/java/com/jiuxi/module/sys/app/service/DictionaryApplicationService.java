package com.jiuxi.module.sys.app.service;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import com.jiuxi.module.sys.app.assembler.DictionaryAssembler;
import com.jiuxi.module.sys.app.dto.DictionaryCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    // 新增的DDD领域功能方法
    
    /**
     * 根据字典类型获取字典列表
     * @param dictTypeCode 字典类型代码
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getDictionariesByType(String dictTypeCode, String tenantId) {
        try {
            ConfigType dictType = ConfigType.fromCode(dictTypeCode);
            List<Dictionary> dictionaries = dictionaryRepository.findByDictType(dictType, tenantId);
            return dictionaries.stream()
                    .map(dictionaryAssembler::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典状态获取字典列表
     * @param dictStatusCode 字典状态代码
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getDictionariesByStatus(String dictStatusCode, String tenantId) {
        try {
            ConfigStatus dictStatus = ConfigStatus.fromCode(dictStatusCode);
            List<Dictionary> dictionaries = dictionaryRepository.findByDictStatus(dictStatus, tenantId);
            return dictionaries.stream()
                    .map(dictionaryAssembler::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典编码前缀获取字典列表
     * @param codePrefix 字典编码前缀
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getDictionariesByCodePrefix(String codePrefix, String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findByCodePrefix(codePrefix, tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据字典组获取字典列表
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getDictionariesByGroup(String dictGroup, String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findByDictGroup(dictGroup, tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据上级字典ID获取子字典列表
     * @param parentDictId 上级字典ID
     * @param tenantId 租户ID
     * @return 子字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getChildDictionaries(String parentDictId, String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findByParentDictId(parentDictId, tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取根级字典列表
     * @param tenantId 租户ID
     * @return 根级字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getRootDictionaries(String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findRootDictionaries(tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取系统预置字典列表
     * @param tenantId 租户ID
     * @return 系统预置字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getSystemPresetDictionaries(String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findSystemPresetDictionaries(tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取激活状态的字典列表
     * @param tenantId 租户ID
     * @return 激活字典响应DTO列表
     */
    public List<DictionaryResponseDTO> getActiveDictionaries(String tenantId) {
        List<Dictionary> dictionaries = dictionaryRepository.findActiveDictionaries(tenantId);
        return dictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量保存字典
     * @param dictionaries 字典创建DTO列表
     * @param creator 创建人
     * @return 保存后的字典响应DTO列表
     */
    @Transactional
    public List<DictionaryResponseDTO> batchCreateDictionaries(List<DictionaryCreateDTO> dictionaries, String creator) {
        List<Dictionary> dictionaryEntities = dictionaries.stream()
                .map(dto -> {
                    Dictionary dictionary = dictionaryAssembler.toEntity(dto);
                    dictionary.setCreator(creator);
                    dictionary.setCreateTime(LocalDateTime.now());
                    return dictionary;
                })
                .collect(Collectors.toList());
        
        List<Dictionary> savedDictionaries = dictionaryRepository.batchSave(dictionaryEntities);
        return savedDictionaries.stream()
                .map(dictionaryAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 按类型和状态统计字典数量
     * @param dictTypeCode 字典类型代码
     * @param dictStatusCode 字典状态代码  
     * @param tenantId 租户ID
     * @return 字典数量
     */
    public long countDictionariesByTypeAndStatus(String dictTypeCode, String dictStatusCode, String tenantId) {
        try {
            ConfigType dictType = ConfigType.fromCode(dictTypeCode);
            ConfigStatus dictStatus = ConfigStatus.fromCode(dictStatusCode);
            return dictionaryRepository.countByTypeAndStatus(dictType, dictStatus, tenantId);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 检查字典编码是否存在
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    public boolean existsByDictCode(String dictCode, String tenantId) {
        return dictionaryRepository.existsByDictCode(dictCode, tenantId);
    }
}