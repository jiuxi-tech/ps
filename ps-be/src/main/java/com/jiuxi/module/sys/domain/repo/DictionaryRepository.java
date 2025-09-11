package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import java.util.List;
import java.util.Optional;

/**
 * 字典仓储接口
 * 定义字典聚合根的持久化操作，增强支持DDD模式
 * 
 * @author System Management (Enhanced by DDD Refactor)
 * @date 2025-09-06
 * @version 2.0 - Enhanced with DDD query methods
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
    
    // 新增的DDD领域查询方法
    
    /**
     * 根据字典编码值对象查找字典
     * @param dictCodeObj 字典编码值对象
     * @param tenantId 租户ID
     * @return 字典 Optional
     */
    Optional<Dictionary> findByDictCode(ConfigKey dictCodeObj, String tenantId);
    
    /**
     * 根据字典类型查找字典列表
     * @param dictType 字典类型
     * @param tenantId 租户ID
     * @return 字典列表
     */
    List<Dictionary> findByDictType(ConfigType dictType, String tenantId);
    
    /**
     * 根据字典状态查找字典列表
     * @param dictStatus 字典状态
     * @param tenantId 租户ID
     * @return 字典列表
     */
    List<Dictionary> findByDictStatus(ConfigStatus dictStatus, String tenantId);
    
    /**
     * 根据字典编码前缀查找字典列表
     * @param codePrefix 字典编码前缀
     * @param tenantId 租户ID
     * @return 字典列表
     */
    List<Dictionary> findByCodePrefix(String codePrefix, String tenantId);
    
    /**
     * 根据字典组查找字典列表
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 字典列表
     */
    List<Dictionary> findByDictGroup(String dictGroup, String tenantId);
    
    /**
     * 根据上级字典ID查找子字典列表
     * @param parentDictId 上级字典ID
     * @param tenantId 租户ID
     * @return 子字典列表
     */
    List<Dictionary> findByParentDictId(String parentDictId, String tenantId);
    
    /**
     * 查找根级字典列表
     * @param tenantId 租户ID
     * @return 根级字典列表
     */
    List<Dictionary> findRootDictionaries(String tenantId);
    
    /**
     * 查找系统预置字典
     * @param tenantId 租户ID
     * @return 系统预置字典列表
     */
    List<Dictionary> findSystemPresetDictionaries(String tenantId);
    
    /**
     * 查找激活状态的字典
     * @param tenantId 租户ID
     * @return 激活字典列表
     */
    List<Dictionary> findActiveDictionaries(String tenantId);
    
    /**
     * 批量保存字典
     * @param dictionaries 字典列表
     * @return 保存后的字典列表
     */
    List<Dictionary> batchSave(List<Dictionary> dictionaries);
    
    /**
     * 按字典类型和状态统计数量
     * @param dictType 字典类型
     * @param dictStatus 字典状态
     * @param tenantId 租户ID
     * @return 字典数量
     */
    long countByTypeAndStatus(ConfigType dictType, ConfigStatus dictStatus, String tenantId);
    
    /**
     * 检查字典编码是否存在
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    boolean existsByDictCode(String dictCode, String tenantId);
}