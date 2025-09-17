package com.jiuxi.module.org.infra.persistence.mapper;

import com.jiuxi.module.org.infra.persistence.entity.EnterprisePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 企业持久化映射器
 * 基于DDD重构的Enterprise持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Mapper
public interface EnterpriseNewMapper {
    
    /**
     * 插入企业
     * @param enterprise 企业持久化对象
     * @return 影响行数
     */
    int insert(EnterprisePO enterprise);
    
    /**
     * 根据ID更新企业
     * @param enterprise 企业持久化对象
     * @return 影响行数
     */
    int updateById(EnterprisePO enterprise);
    
    /**
     * 根据ID查询企业
     * @param entId 企业ID
     * @return 企业持久化对象
     */
    Optional<EnterprisePO> selectById(@Param("entId") String entId);
    
    /**
     * 根据企业名称查询企业
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @return 企业持久化对象
     */
    Optional<EnterprisePO> selectByName(@Param("entFullName") String entFullName, @Param("tenantId") String tenantId);
    
    /**
     * 根据统一社会信用代码查询企业
     * @param entUnifiedCode 统一社会信用代码
     * @return 企业持久化对象
     */
    Optional<EnterprisePO> selectByUnifiedCode(@Param("entUnifiedCode") String entUnifiedCode);
    
    /**
     * 根据租户ID查询企业列表
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> selectByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 根据企业类型查询企业列表
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> selectByType(@Param("entType") String entType, @Param("tenantId") String tenantId);
    
    /**
     * 检查企业名称是否存在
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @param excludeEntId 排除的企业ID
     * @return 存在的数量
     */
    int countByName(@Param("entFullName") String entFullName, @Param("tenantId") String tenantId, @Param("excludeEntId") String excludeEntId);
    
    /**
     * 检查统一社会信用代码是否存在
     * @param entUnifiedCode 统一社会信用代码
     * @param excludeEntId 排除的企业ID
     * @return 存在的数量
     */
    int countByUnifiedCode(@Param("entUnifiedCode") String entUnifiedCode, @Param("excludeEntId") String excludeEntId);
    
    /**
     * 逻辑删除企业
     * @param entId 企业ID
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteById(@Param("entId") String entId, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量逻辑删除企业
     * @param entIds 企业ID列表
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteByIds(@Param("entIds") List<String> entIds, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 根据地理位置范围查询企业
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude 最小纬度
     * @param maxLatitude 最大纬度
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> selectByLocationRange(@Param("minLongitude") String minLongitude, 
                                           @Param("maxLongitude") String maxLongitude,
                                           @Param("minLatitude") String minLatitude, 
                                           @Param("maxLatitude") String maxLatitude,
                                           @Param("tenantId") String tenantId);
    
    /**
     * 根据行政区划查询企业
     * @param addrCode 行政区划代码
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> selectByAddrCode(@Param("addrCode") String addrCode, @Param("tenantId") String tenantId);
    
    /**
     * 统计租户企业数量
     * @param tenantId 租户ID
     * @return 企业数量
     */
    long countByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 统计企业类型数量
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业数量
     */
    long countByType(@Param("entType") String entType, @Param("tenantId") String tenantId);
    
    /**
     * 分页查询企业
     * @param tenantId 租户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 企业列表
     */
    List<EnterprisePO> selectPage(@Param("tenantId") String tenantId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据关键词搜索企业
     * @param keyword 搜索关键词
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);
    
    /**
     * 根据状态查询企业
     * @param enabled 启用状态
     * @param actived 有效状态
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<EnterprisePO> selectByStatus(@Param("enabled") Integer enabled, @Param("actived") Integer actived, @Param("tenantId") String tenantId);
    
    /**
     * 批量查询企业
     * @param entIds 企业ID列表
     * @return 企业列表
     */
    List<EnterprisePO> selectByIds(@Param("entIds") List<String> entIds);
    
    /**
     * 更新企业状态
     * @param entId 企业ID
     * @param enabled 启用状态
     * @param actived 有效状态
     * @return 是否更新成功
     */
    int updateStatus(@Param("entId") String entId, @Param("enabled") Integer enabled, @Param("actived") Integer actived);
    
    /**
     * 批量更新企业状态
     * @param entIds 企业ID列表
     * @param enabled 启用状态
     * @param actived 有效状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("entIds") List<String> entIds, @Param("enabled") Integer enabled, @Param("actived") Integer actived);
}