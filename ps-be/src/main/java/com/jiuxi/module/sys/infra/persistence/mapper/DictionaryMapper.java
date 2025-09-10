package com.jiuxi.module.sys.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

/**
 * 字典Mapper接口
 * 定义字典持久化对象的数据库操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<DictionaryPO> {
    
    /**
     * 根据字典编码和租户ID查找字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典持久化对象
     */
    DictionaryPO findByDictCodeAndTenantId(@Param("dictCode") String dictCode, @Param("tenantId") String tenantId);
}