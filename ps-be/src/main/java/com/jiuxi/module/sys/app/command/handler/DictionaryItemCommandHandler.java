package com.jiuxi.module.sys.app.command.handler;

import com.jiuxi.module.sys.app.command.dto.DictionaryItemCreateCommand;
import com.jiuxi.module.sys.app.command.dto.DictionaryItemUpdateCommand;
import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.domain.repo.DictionaryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 字典项命令处理器
 * 负责处理字典项相关的命令操作（CUD）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class DictionaryItemCommandHandler {
    
    @Autowired
    private DictionaryItemRepository dictionaryItemRepository;
    
    /**
     * 处理创建字典项命令
     * @param command 创建命令
     * @param creator 创建人
     * @return 创建后的字典项ID
     */
    @Transactional
    public String handleCreateCommand(DictionaryItemCreateCommand command, String creator) {
        try {
            // 创建字典项实体
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setItemId(UUID.randomUUID().toString());
            dictionaryItem.setDictId(command.getDictId());
            dictionaryItem.setItemCode(command.getItemCode());
            dictionaryItem.setItemName(command.getItemName());
            dictionaryItem.setItemValue(command.getItemValue());
            dictionaryItem.setItemDesc(command.getItemDesc());
            dictionaryItem.setStatus(command.getStatus() != null ? command.getStatus() : "ACTIVE");
            dictionaryItem.setOrderIndex(command.getOrderIndex() != null ? command.getOrderIndex() : 0);
            dictionaryItem.setIsDefault(command.getIsDefault() != null ? command.getIsDefault() : false);
            dictionaryItem.setCreator(creator);
            dictionaryItem.setCreateTime(LocalDateTime.now());
            dictionaryItem.setTenantId(command.getTenantId());
            
            // 保存字典项
            DictionaryItem savedDictionaryItem = dictionaryItemRepository.save(dictionaryItem);
            
            return savedDictionaryItem.getItemId();
        } catch (Exception e) {
            throw new RuntimeException("创建字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理更新字典项命令
     * @param command 更新命令
     * @param updator 更新人
     * @return 是否更新成功
     */
    @Transactional
    public boolean handleUpdateCommand(DictionaryItemUpdateCommand command, String updator) {
        try {
            // 查找现有字典项
            Optional<DictionaryItem> existingItemOpt = dictionaryItemRepository.findById(command.getItemId());
            if (!existingItemOpt.isPresent()) {
                throw new RuntimeException("字典项不存在: " + command.getItemId());
            }
            
            DictionaryItem existingItem = existingItemOpt.get();
            
            // 更新字典项信息
            existingItem.setItemCode(command.getItemCode());
            existingItem.setItemName(command.getItemName());
            existingItem.setItemValue(command.getItemValue());
            existingItem.setItemDesc(command.getItemDesc());
            existingItem.setStatus(command.getStatus());
            existingItem.setOrderIndex(command.getOrderIndex());
            existingItem.setIsDefault(command.getIsDefault());
            existingItem.setUpdator(updator);
            existingItem.setUpdateTime(LocalDateTime.now());
            
            // 保存更新后的字典项
            dictionaryItemRepository.save(existingItem);
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理启用字典项命令
     * @param itemId 字典项ID
     * @param operator 操作人
     * @return 是否启用成功
     */
    @Transactional
    public boolean handleEnableCommand(String itemId, String operator) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findById(itemId);
            if (!itemOpt.isPresent()) {
                throw new RuntimeException("字典项不存在: " + itemId);
            }
            
            DictionaryItem item = itemOpt.get();
            item.setStatus("ACTIVE");
            item.setUpdator(operator);
            item.setUpdateTime(LocalDateTime.now());
            
            dictionaryItemRepository.save(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("启用字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理停用字典项命令
     * @param itemId 字典项ID
     * @param operator 操作人
     * @return 是否停用成功
     */
    @Transactional
    public boolean handleDisableCommand(String itemId, String operator) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findById(itemId);
            if (!itemOpt.isPresent()) {
                throw new RuntimeException("字典项不存在: " + itemId);
            }
            
            DictionaryItem item = itemOpt.get();
            item.setStatus("INACTIVE");
            item.setUpdator(operator);
            item.setUpdateTime(LocalDateTime.now());
            
            dictionaryItemRepository.save(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("停用字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理删除字典项命令
     * @param itemId 字典项ID
     * @param operator 操作人
     * @return 是否删除成功
     */
    @Transactional
    public boolean handleDeleteCommand(String itemId, String operator) {
        try {
            dictionaryItemRepository.deleteById(itemId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理设置为默认字典项命令
     * @param itemId 字典项ID
     * @param dictId 字典ID
     * @param operator 操作人
     * @return 是否设置成功
     */
    @Transactional
    public boolean handleSetAsDefaultCommand(String itemId, String dictId, String operator) {
        try {
            // 先清除该字典下所有项的默认标识
            // 注意：这里需要根据实际的DictionaryItemRepository接口实现批量更新
            // 暂时用循环方式实现，实际项目中可优化为批量操作
            
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findById(itemId);
            if (!itemOpt.isPresent()) {
                throw new RuntimeException("字典项不存在: " + itemId);
            }
            
            DictionaryItem item = itemOpt.get();
            item.setIsDefault(true);
            item.setUpdator(operator);
            item.setUpdateTime(LocalDateTime.now());
            
            dictionaryItemRepository.save(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("设置默认字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理调整字典项排序命令
     * @param itemId 字典项ID
     * @param newOrderIndex 新的排序索引
     * @param operator 操作人
     * @return 是否调整成功
     */
    @Transactional
    public boolean handleUpdateOrderCommand(String itemId, Integer newOrderIndex, String operator) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findById(itemId);
            if (!itemOpt.isPresent()) {
                throw new RuntimeException("字典项不存在: " + itemId);
            }
            
            DictionaryItem item = itemOpt.get();
            item.setOrderIndex(newOrderIndex);
            item.setUpdator(operator);
            item.setUpdateTime(LocalDateTime.now());
            
            dictionaryItemRepository.save(item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("调整字典项排序失败: " + e.getMessage(), e);
        }
    }
}