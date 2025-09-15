package com.jiuxi.module.system.domain.repository;

import com.jiuxi.module.system.domain.entity.DictionaryItem;
import com.jiuxi.module.system.domain.entity.DictionaryStatus;

import java.util.List;
import java.util.Optional;

/**
 * @InterfaceName: DictionaryItemRepository
 * @Description: 字典项仓储接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface DictionaryItemRepository {
    
    /**
     * 保存字典项
     */
    DictionaryItem save(DictionaryItem dictionaryItem);
    
    /**
     * 根据ID查找字典项
     */
    Optional<DictionaryItem> findById(String itemId);
    
    /**
     * 根据字典ID查找所有字典项
     */
    List<DictionaryItem> findByDictId(String dictId);
    
    /**
     * 根据字典ID和状态查找字典项
     */
    List<DictionaryItem> findByDictIdAndStatus(String dictId, DictionaryStatus status);
    
    /**
     * 根据字典ID查找启用的字典项
     */
    List<DictionaryItem> findEnabledByDictId(String dictId);
    
    /**
     * 根据字典ID和键值查找字典项
     */
    Optional<DictionaryItem> findByDictIdAndItemKey(String dictId, String itemKey);
    
    /**
     * 根据父级ID查找子字典项
     */
    List<DictionaryItem> findByParentId(String parentId);
    
    /**
     * 根据字典ID查找根级字典项（无父级）
     */
    List<DictionaryItem> findRootItemsByDictId(String dictId);
    
    /**
     * 检查字典项键值是否存在
     */
    boolean existsByDictIdAndItemKey(String dictId, String itemKey);
    
    /**
     * 检查字典项键值是否存在（排除指定ID）
     */
    boolean existsByDictIdAndItemKeyAndIdNot(String dictId, String itemKey, String itemId);
    
    /**
     * 根据字典ID和项值模糊查询
     */
    List<DictionaryItem> findByDictIdAndItemValueContaining(String dictId, String itemValue);
    
    /**
     * 根据字典ID获取最大排序号
     */
    Integer getMaxSortOrderByDictId(String dictId);
    
    /**
     * 根据父级ID获取最大排序号
     */
    Integer getMaxSortOrderByParentId(String parentId);
    
    /**
     * 根据ID删除字典项
     */
    void deleteById(String itemId);
    
    /**
     * 根据字典ID删除所有字典项
     */
    void deleteByDictId(String dictId);
    
    /**
     * 批量删除字典项
     */
    void deleteByIds(List<String> itemIds);
    
    /**
     * 统计字典项总数
     */
    long count();
    
    /**
     * 根据字典ID统计字典项数量
     */
    long countByDictId(String dictId);
    
    /**
     * 根据字典ID和状态统计字典项数量
     */
    long countByDictIdAndStatus(String dictId, DictionaryStatus status);
    
    /**
     * 分页查询字典项
     */
    List<DictionaryItem> findByDictIdWithPagination(String dictId, int page, int size);
}