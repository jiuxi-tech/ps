package com.jiuxi.module.system.domain.service;

import com.jiuxi.module.system.domain.entity.Dictionary;
import com.jiuxi.module.system.domain.entity.DictionaryItem;
import com.jiuxi.module.system.domain.entity.DictionaryType;

import java.util.List;

/**
 * @InterfaceName: DictionaryDomainService
 * @Description: 字典管理领域服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface DictionaryDomainService {
    
    /**
     * 创建字典
     */
    Dictionary createDictionary(String dictCode, String dictName, DictionaryType dictType, String description);
    
    /**
     * 验证字典编码唯一性
     */
    void validateDictCodeUniqueness(String dictCode, String excludeId);
    
    /**
     * 验证字典是否可以删除
     */
    void validateDictionaryDeletable(String dictId);
    
    /**
     * 批量启用字典
     */
    void enableDictionaries(List<String> dictIds);
    
    /**
     * 批量禁用字典
     */
    void disableDictionaries(List<String> dictIds);
    
    /**
     * 复制字典及其字典项
     */
    Dictionary copyDictionary(String sourceDictId, String newDictCode, String newDictName);
    
    /**
     * 为字典添加字典项
     */
    DictionaryItem addDictionaryItem(String dictId, String itemKey, String itemValue, String description);
    
    /**
     * 验证字典项键值唯一性
     */
    void validateItemKeyUniqueness(String dictId, String itemKey, String excludeId);
    
    /**
     * 验证字典项是否可以删除
     */
    void validateDictionaryItemDeletable(String itemId);
    
    /**
     * 批量启用字典项
     */
    void enableDictionaryItems(List<String> itemIds);
    
    /**
     * 批量禁用字典项
     */
    void disableDictionaryItems(List<String> itemIds);
    
    /**
     * 调整字典项排序
     */
    void adjustItemSortOrder(String itemId, Integer newSortOrder);
    
    /**
     * 移动字典项到指定父级
     */
    void moveDictionaryItem(String itemId, String newParentId);
    
    /**
     * 构建字典项树形结构
     */
    List<DictionaryItem> buildDictionaryItemTree(String dictId);
    
    /**
     * 验证字典项层级深度
     */
    void validateItemHierarchyDepth(String parentId, int maxDepth);
    
    /**
     * 检查字典项循环引用
     */
    void checkItemCircularReference(String itemId, String parentId);
    
    /**
     * 获取字典项的完整路径
     */
    String getDictionaryItemPath(String itemId);
    
    /**
     * 同步字典缓存
     */
    void syncDictionaryCache(String dictId);
    
    /**
     * 清理字典缓存
     */
    void clearDictionaryCache();
}