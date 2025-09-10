package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.DictionaryItem;
import java.util.List;
import java.util.Optional;

/**
 * 字典项仓储接口
 * 定义字典项实体的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
public interface DictionaryItemRepository {
    
    /**
     * 保存字典项
     * @param dictionaryItem 字典项实体
     * @return 保存后的字典项
     */
    DictionaryItem save(DictionaryItem dictionaryItem);
    
    /**
     * 根据ID查找字典项
     * @param itemId 字典项ID
     * @return 字典项Optional
     */
    Optional<DictionaryItem> findById(String itemId);
    
    /**
     * 根据ID删除字典项
     * @param itemId 字典项ID
     */
    void deleteById(String itemId);
    
    /**
     * 根据字典ID查找字典项列表
     * @param dictId 字典ID
     * @return 字典项列表
     */
    List<DictionaryItem> findByDictId(String dictId);
    
    /**
     * 根据项编码和字典ID查找字典项
     * @param itemCode 项编码
     * @param dictId 字典ID
     * @return 字典项Optional
     */
    Optional<DictionaryItem> findByItemCodeAndDictId(String itemCode, String dictId);
}