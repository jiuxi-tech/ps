package com.jiuxi.module.sys.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.sys.infra.persistence.entity.DictionaryItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 字典项Mapper接口
 * 定义字典项持久化对象的数据库操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Mapper
public interface DictionaryItemMapper extends BaseMapper<DictionaryItemPO> {
    
    /**
     * 根据字典ID查找字典项列表
     * @param dictId 字典ID
     * @return 字典项持久化对象列表
     */
    List<DictionaryItemPO> findByDictId(@Param("dictId") String dictId);
    
    /**
     * 根据项编码和字典ID查找字典项
     * @param itemCode 项编码
     * @param dictId 字典ID
     * @return 字典项持久化对象
     */
    DictionaryItemPO findByItemCodeAndDictId(@Param("itemCode") String itemCode, @Param("dictId") String dictId);
}