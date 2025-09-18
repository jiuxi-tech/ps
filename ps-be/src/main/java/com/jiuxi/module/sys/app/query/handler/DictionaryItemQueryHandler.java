package com.jiuxi.module.sys.app.query.handler;

import com.jiuxi.module.sys.app.query.dto.DictionaryItemResponse;
import com.jiuxi.module.sys.app.assembler.DictionaryItemAssembler;
import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import com.jiuxi.module.sys.domain.repo.DictionaryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典项查询处理器
 * 负责处理字典项相关的查询操作（R）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class DictionaryItemQueryHandler {
    
    @Autowired
    private DictionaryItemRepository dictionaryItemRepository;
    
    @Autowired
    private DictionaryItemAssembler dictionaryItemAssembler;
    
    /**
     * 根据ID查询字典项
     * @param itemId 字典项ID
     * @return 字典项响应DTO
     */
    public DictionaryItemResponse handleGetByIdQuery(String itemId) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findById(itemId);
            if (!itemOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(itemOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("查询字典项失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据字典ID查询字典项列表
     * @param dictId 字典ID
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetByDictIdQuery(String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典ID和租户ID查询字典项列表
     * @param dictId 字典ID
     * @param tenantId 租户ID
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetByDictIdAndTenantQuery(String dictId, String tenantId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .filter(item -> tenantId.equals(item.getTenantId()))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典ID和状态查询字典项列表
     * @param dictId 字典ID
     * @param status 状态
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetByDictIdAndStatusQuery(String dictId, String status) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .filter(item -> status.equals(item.getStatus()))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典编码查询字典项列表
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetByDictCodeQuery(String dictCode, String tenantId) {
        try {
            // 简化实现：返回空列表，需要实际实现时再扩展
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典编码和状态查询字典项列表
     * @param dictCode 字典编码
     * @param status 状态
     * @param tenantId 租户ID
     * @return 字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetByDictCodeAndStatusQuery(String dictCode, String status, String tenantId) {
        try {
            // 简化实现：返回空列表，需要实际实现时再扩展
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询激活状态的字典项列表
     * @param dictId 字典ID
     * @return 激活字典项响应DTO列表
     */
    public List<DictionaryItemResponse> handleGetActiveItemsQuery(String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .filter(item -> "ACTIVE".equals(item.getStatus()))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典ID查询默认字典项
     * @param dictId 字典ID
     * @return 默认字典项响应DTO
     */
    public DictionaryItemResponse handleGetDefaultItemQuery(String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            Optional<DictionaryItem> itemOpt = items.stream()
                    .filter(item -> Boolean.TRUE.equals(item.getIsDefault()))
                    .findFirst();
            if (!itemOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(itemOpt.get());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 根据项目代码和字典ID查询字典项
     * @param itemCode 项目代码
     * @param dictId 字典ID
     * @return 字典项响应DTO
     */
    public DictionaryItemResponse handleGetByCodeAndDictIdQuery(String itemCode, String dictId) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findByItemCodeAndDictId(itemCode, dictId);
            if (!itemOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(itemOpt.get());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 根据项目值和字典ID查询字典项
     * @param itemValue 项目值
     * @param dictId 字典ID
     * @return 字典项响应DTO
     */
    public DictionaryItemResponse handleGetByValueAndDictIdQuery(String itemValue, String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            Optional<DictionaryItem> itemOpt = items.stream()
                    .filter(item -> itemValue.equals(item.getItemValue()))
                    .findFirst();
            if (!itemOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(itemOpt.get());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 统计字典下的项目数量
     * @param dictId 字典ID
     * @return 项目数量
     */
    public long handleCountByDictIdQuery(String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 统计字典下指定状态的项目数量
     * @param dictId 字典ID
     * @param status 状态
     * @return 项目数量
     */
    public long handleCountByDictIdAndStatusQuery(String dictId, String status) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .filter(item -> status.equals(item.getStatus()))
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 检查字典项代码在字典中是否存在
     * @param itemCode 字典项代码
     * @param dictId 字典ID
     * @return 是否存在
     */
    public boolean handleExistsByCodeAndDictIdQuery(String itemCode, String dictId) {
        try {
            Optional<DictionaryItem> itemOpt = dictionaryItemRepository.findByItemCodeAndDictId(itemCode, dictId);
            return itemOpt.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查字典项值在字典中是否存在
     * @param itemValue 字典项值
     * @param dictId 字典ID
     * @return 是否存在
     */
    public boolean handleExistsByValueAndDictIdQuery(String itemValue, String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .anyMatch(item -> itemValue.equals(item.getItemValue()));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 查询字典项的最大排序索引
     * @param dictId 字典ID
     * @return 最大排序索引
     */
    public Integer handleGetMaxOrderIndexQuery(String dictId) {
        try {
            List<DictionaryItem> items = dictionaryItemRepository.findByDictId(dictId);
            return items.stream()
                    .mapToInt(item -> item.getOrderIndex() != null ? item.getOrderIndex() : 0)
                    .max()
                    .orElse(0);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 转换DictionaryItem实体为响应DTO
     * @param dictionaryItem 字典项实体
     * @return 字典项响应DTO
     */
    private DictionaryItemResponse convertToResponse(DictionaryItem dictionaryItem) {
        return dictionaryItemAssembler.toResponse(dictionaryItem);
    }
}