package com.jiuxi.module.system.domain.repository;

import com.jiuxi.module.system.domain.entity.Dictionary;
import com.jiuxi.module.system.domain.entity.DictionaryStatus;
import com.jiuxi.module.system.domain.entity.DictionaryType;

import java.util.List;
import java.util.Optional;

/**
 * @InterfaceName: DictionaryRepository
 * @Description: 字典管理仓储接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface DictionaryRepository {
    
    /**
     * 保存字典
     */
    Dictionary save(Dictionary dictionary);
    
    /**
     * 根据ID查找字典
     */
    Optional<Dictionary> findById(String dictId);
    
    /**
     * 根据字典编码查找字典
     */
    Optional<Dictionary> findByDictCode(String dictCode);
    
    /**
     * 根据字典类型查找字典列表
     */
    List<Dictionary> findByDictType(DictionaryType dictType);
    
    /**
     * 根据状态查找字典列表
     */
    List<Dictionary> findByStatus(DictionaryStatus status);
    
    /**
     * 查找所有启用的字典
     */
    List<Dictionary> findAllEnabled();
    
    /**
     * 根据字典名称模糊查询
     */
    List<Dictionary> findByDictNameContaining(String dictName);
    
    /**
     * 检查字典编码是否存在
     */
    boolean existsByDictCode(String dictCode);
    
    /**
     * 检查字典编码是否存在（排除指定ID）
     */
    boolean existsByDictCodeAndIdNot(String dictCode, String dictId);
    
    /**
     * 根据ID删除字典
     */
    void deleteById(String dictId);
    
    /**
     * 批量删除字典
     */
    void deleteByIds(List<String> dictIds);
    
    /**
     * 统计字典总数
     */
    long count();
    
    /**
     * 根据条件统计字典数量
     */
    long countByStatus(DictionaryStatus status);
    
    /**
     * 分页查询字典
     */
    List<Dictionary> findWithPagination(int page, int size);
    
    /**
     * 根据条件分页查询字典
     */
    List<Dictionary> findByConditionsWithPagination(DictionaryType dictType, DictionaryStatus status, String keyword, int page, int size);
}