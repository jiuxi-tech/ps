package com.jiuxi.module.sys.app.command.handler;

import com.jiuxi.module.sys.app.command.dto.DictionaryCreateCommand;
import com.jiuxi.module.sys.app.command.dto.DictionaryUpdateCommand;
import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 字典命令处理器
 * 负责处理字典相关的命令操作（CUD）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class DictionaryCommandHandler {
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    /**
     * 处理创建字典命令
     * @param command 创建命令
     * @param creator 创建人
     * @return 创建后的字典ID
     */
    @Transactional
    public String handleCreateCommand(DictionaryCreateCommand command, String creator) {
        try {
            // 创建字典实体
            Dictionary dictionary = new Dictionary();
            dictionary.setDictId(UUID.randomUUID().toString());
            dictionary.setDictCode(command.getDictCode());
            dictionary.setDictName(command.getDictName());
            dictionary.setDictDesc(command.getDictDesc());
            dictionary.setDictType(command.getDictType());
            dictionary.setStatus(command.getStatus() != null ? command.getStatus() : "ACTIVE");
            dictionary.setDictGroup(command.getDictGroup());
            dictionary.setParentDictId(command.getParentDictId());
            dictionary.setOrderIndex(command.getOrderIndex() != null ? command.getOrderIndex() : 0);
            dictionary.setCreator(creator);
            dictionary.setCreateTime(LocalDateTime.now());
            dictionary.setTenantId(command.getTenantId());
            
            // 设置DDD值对象和枚举
            if (command.getDictType() != null) {
                try {
                    dictionary.setDictTypeEnum(ConfigType.fromCode(command.getDictType()));
                } catch (Exception e) {
                    dictionary.setDictTypeEnum(ConfigType.BUSINESS);
                }
            }
            
            if (command.getStatus() != null) {
                try {
                    dictionary.setDictStatusEnum(ConfigStatus.fromCode(command.getStatus()));
                } catch (Exception e) {
                    dictionary.setDictStatusEnum(ConfigStatus.ACTIVE);
                }
            }
            
            // 保存字典
            Dictionary savedDictionary = dictionaryRepository.save(dictionary);
            
            return savedDictionary.getDictId();
        } catch (Exception e) {
            throw new RuntimeException("创建字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理更新字典命令
     * @param command 更新命令
     * @param updator 更新人
     * @return 是否更新成功
     */
    @Transactional
    public boolean handleUpdateCommand(DictionaryUpdateCommand command, String updator) {
        try {
            // 查找现有字典
            Optional<Dictionary> existingDictionaryOpt = dictionaryRepository.findById(command.getDictId());
            if (!existingDictionaryOpt.isPresent()) {
                throw new RuntimeException("字典不存在: " + command.getDictId());
            }
            
            Dictionary existingDictionary = existingDictionaryOpt.get();
            
            // 检查是否可编辑
            if (!existingDictionary.isEditable()) {
                throw new RuntimeException("字典不可编辑: " + command.getDictId());
            }
            
            // 更新字典信息
            existingDictionary.updateInfo(command.getDictName(), command.getDictDesc());
            existingDictionary.setDictCode(command.getDictCode());
            existingDictionary.setDictType(command.getDictType());
            existingDictionary.setStatus(command.getStatus());
            existingDictionary.setDictGroup(command.getDictGroup());
            existingDictionary.setParentDictId(command.getParentDictId());
            existingDictionary.setOrderIndex(command.getOrderIndex());
            existingDictionary.setUpdator(updator);
            existingDictionary.setUpdateTime(LocalDateTime.now());
            
            // 保存更新后的字典
            dictionaryRepository.save(existingDictionary);
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理启用字典命令
     * @param dictId 字典ID
     * @param operator 操作人
     * @return 是否启用成功
     */
    @Transactional
    public boolean handleEnableCommand(String dictId, String operator) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                throw new RuntimeException("字典不存在: " + dictId);
            }
            
            Dictionary dictionary = dictionaryOpt.get();
            dictionary.enable();
            dictionary.setUpdator(operator);
            dictionary.setUpdateTime(LocalDateTime.now());
            
            dictionaryRepository.save(dictionary);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("启用字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理停用字典命令
     * @param dictId 字典ID
     * @param operator 操作人
     * @return 是否停用成功
     */
    @Transactional
    public boolean handleDisableCommand(String dictId, String operator) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                throw new RuntimeException("字典不存在: " + dictId);
            }
            
            Dictionary dictionary = dictionaryOpt.get();
            dictionary.disable();
            dictionary.setUpdator(operator);
            dictionary.setUpdateTime(LocalDateTime.now());
            
            dictionaryRepository.save(dictionary);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("停用字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理删除字典命令
     * @param dictId 字典ID
     * @param operator 操作人
     * @return 是否删除成功
     */
    @Transactional
    public boolean handleDeleteCommand(String dictId, String operator) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                throw new RuntimeException("字典不存在: " + dictId);
            }
            
            Dictionary dictionary = dictionaryOpt.get();
            
            // 检查是否为系统预置字典
            if (dictionary.getIsSystemPreset() != null && dictionary.getIsSystemPreset()) {
                throw new RuntimeException("系统预置字典不允许删除: " + dictId);
            }
            
            dictionaryRepository.deleteById(dictId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理标记为系统预置字典命令
     * @param dictId 字典ID
     * @param operator 操作人
     * @return 是否标记成功
     */
    @Transactional
    public boolean handleMarkAsSystemPresetCommand(String dictId, String operator) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                throw new RuntimeException("字典不存在: " + dictId);
            }
            
            Dictionary dictionary = dictionaryOpt.get();
            dictionary.markAsSystemPreset();
            dictionary.setUpdator(operator);
            dictionary.setUpdateTime(LocalDateTime.now());
            
            dictionaryRepository.save(dictionary);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("标记系统预置字典失败: " + e.getMessage(), e);
        }
    }
}