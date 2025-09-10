package com.jiuxi.module.sys.app.service;

import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.domain.repo.DictionaryItemRepository;
import com.jiuxi.module.sys.app.assembler.DictionaryItemAssembler;
import com.jiuxi.module.sys.app.dto.DictionaryItemCreateDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemResponseDTO;
import com.jiuxi.module.sys.app.dto.DictionaryItemUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典项应用服务
 * 负责字典项相关的应用逻辑和事务协调
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Service
public class DictionaryItemApplicationService {
    
    @Autowired
    private DictionaryItemRepository dictionaryItemRepository;
    
    @Autowired
    private DictionaryItemAssembler dictionaryItemAssembler;
    
    /**
     * 创建字典项
     * @param createDTO 字典项创建DTO
     * @param creator 创建人
     * @return 字典项响应DTO
     */
    public DictionaryItemResponseDTO createDictionaryItem(DictionaryItemCreateDTO createDTO, String creator) {
        // 转换DTO为实体
        DictionaryItem dictionaryItem = dictionaryItemAssembler.toEntity(createDTO);
        
        // 设置创建信息
        dictionaryItem.setCreator(creator);
        dictionaryItem.setCreateTime(LocalDateTime.now());
        
        // 保存字典项
        DictionaryItem savedDictionaryItem = dictionaryItemRepository.save(dictionaryItem);
        
        // 转换为响应DTO
        return dictionaryItemAssembler.toResponseDTO(savedDictionaryItem);
    }
    
    /**
     * 更新字典项
     * @param updateDTO 字典项更新DTO
     * @param updator 更新人
     * @return 字典项响应DTO
     */
    public DictionaryItemResponseDTO updateDictionaryItem(DictionaryItemUpdateDTO updateDTO, String updator) {
        // 查找现有字典项
        Optional<DictionaryItem> existingDictionaryItemOpt = dictionaryItemRepository.findById(updateDTO.getItemId());
        if (!existingDictionaryItemOpt.isPresent()) {
            throw new RuntimeException("字典项不存在");
        }
        
        // 更新字典项信息
        DictionaryItem existingDictionaryItem = existingDictionaryItemOpt.get();
        existingDictionaryItem.setDictId(updateDTO.getDictId());
        existingDictionaryItem.setItemCode(updateDTO.getItemCode());
        existingDictionaryItem.setItemName(updateDTO.getItemName());
        existingDictionaryItem.setItemValue(updateDTO.getItemValue());
        existingDictionaryItem.setItemDesc(updateDTO.getItemDesc());
        existingDictionaryItem.setStatus(updateDTO.getStatus());
        existingDictionaryItem.setOrderIndex(updateDTO.getOrderIndex());
        existingDictionaryItem.setUpdator(updator);
        existingDictionaryItem.setUpdateTime(LocalDateTime.now());
        
        // 保存更新后的字典项
        DictionaryItem updatedDictionaryItem = dictionaryItemRepository.save(existingDictionaryItem);
        
        // 转换为响应DTO
        return dictionaryItemAssembler.toResponseDTO(updatedDictionaryItem);
    }
    
    /**
     * 根据ID删除字典项
     * @param itemId 字典项ID
     */
    public void deleteDictionaryItem(String itemId) {
        dictionaryItemRepository.deleteById(itemId);
    }
    
    /**
     * 根据ID获取字典项
     * @param itemId 字典项ID
     * @return 字典项响应DTO
     */
    public DictionaryItemResponseDTO getDictionaryItemById(String itemId) {
        Optional<DictionaryItem> dictionaryItemOpt = dictionaryItemRepository.findById(itemId);
        if (!dictionaryItemOpt.isPresent()) {
            return null;
        }
        
        return dictionaryItemAssembler.toResponseDTO(dictionaryItemOpt.get());
    }
    
    /**
     * 根据字典ID获取字典项列表
     * @param dictId 字典ID
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponseDTO> getDictionaryItemsByDictId(String dictId) {
        List<DictionaryItem> dictionaryItems = dictionaryItemRepository.findByDictId(dictId);
        return dictionaryItems.stream()
                .map(dictionaryItemAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据项编码和字典ID获取字典项
     * @param itemCode 项编码
     * @param dictId 字典ID
     * @return 字典项响应DTO
     */
    public DictionaryItemResponseDTO getDictionaryItemByCodeAndDictId(String itemCode, String dictId) {
        Optional<DictionaryItem> dictionaryItemOpt = dictionaryItemRepository.findByItemCodeAndDictId(itemCode, dictId);
        if (!dictionaryItemOpt.isPresent()) {
            return null;
        }
        
        return dictionaryItemAssembler.toResponseDTO(dictionaryItemOpt.get());
    }
}