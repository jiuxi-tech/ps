package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import java.util.Optional;

/**
 * 字典仓储接口
 * 定义字典聚合根的持久化操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
public interface DictionaryRepository {
    
    /**
     * 保存字典
     * @param dictionary 字典聚合根
     * @return 保存后的字典
     */
    Dictionary save(Dictionary dictionary);
    
    /**
     * 根据ID查找字典
     * @param dictId 字典ID
     * @return 字典Optional
     */
    Optional<Dictionary> findById(String dictId);
    
    /**
     * 根据ID删除字典
     * @param dictId 字典ID
     */
    void deleteById(String dictId);
    
    /**
     * 根据字典编码查找字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典Optional
     */
    Optional<Dictionary> findByDictCode(String dictCode, String tenantId);
}