package com.jiuxi.module.org.domain.repo;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.query.EnterpriseQuery;
import java.util.List;
import java.util.Optional;

/**
 * 企业仓储接口
 * 定义企业聚合根的持久化操作规范
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
public interface EnterpriseRepository {
    
    /**
     * 保存企业
     * @param enterprise 企业聚合根
     * @return 保存后的企业
     */
    Enterprise save(Enterprise enterprise);
    
    /**
     * 根据ID查找企业
     * @param entId 企业ID
     * @return 企业信息，如果不存在则返回空
     */
    Optional<Enterprise> findById(String entId);
    
    /**
     * 根据企业名称查找企业
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @return 企业信息，如果不存在则返回空
     */
    Optional<Enterprise> findByName(String entFullName, String tenantId);
    
    /**
     * 根据统一社会信用代码查找企业
     * @param entUnifiedCode 统一社会信用代码
     * @return 企业信息，如果不存在则返回空
     */
    Optional<Enterprise> findByUnifiedCode(String entUnifiedCode);
    
    /**
     * 根据租户ID查找企业列表
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> findByTenantId(String tenantId);
    
    /**
     * 根据企业类型查找企业列表
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> findByType(String entType, String tenantId);
    
    /**
     * 检查企业名称是否存在
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @param excludeEntId 排除的企业ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByName(String entFullName, String tenantId, String excludeEntId);
    
    /**
     * 检查统一社会信用代码是否存在
     * @param entUnifiedCode 统一社会信用代码
     * @param excludeEntId 排除的企业ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByUnifiedCode(String entUnifiedCode, String excludeEntId);
    
    /**
     * 删除企业
     * @param entId 企业ID
     */
    void deleteById(String entId);
    
    /**
     * 批量删除企业
     * @param entIds 企业ID列表
     */
    void deleteByIds(List<String> entIds);
    
    /**
     * 根据地理位置范围查找企业
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude 最小纬度
     * @param maxLatitude 最大纬度
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> findByLocationRange(String minLongitude, String maxLongitude, 
                                       String minLatitude, String maxLatitude, String tenantId);
    
    /**
     * 根据行政区划代码查找企业
     * @param addrCode 行政区划代码
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> findByAddrCode(String addrCode, String tenantId);
    
    /**
     * 统计指定租户下的企业数量
     * @param tenantId 租户ID
     * @return 企业数量
     */
    long countByTenantId(String tenantId);
    
    /**
     * 统计指定类型的企业数量
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业数量
     */
    long countByType(String entType, String tenantId);
    
    /**
     * 分页查询企业列表
     * @param tenantId 租户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 企业列表
     */
    List<Enterprise> findPage(String tenantId, int pageNum, int pageSize);
    
    /**
     * 根据关键词搜索企业（企业名称、法定代表人、联系人）
     * @param keyword 搜索关键词
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> searchByKeyword(String keyword, String tenantId);
    
    /**
     * 根据企业状态查找企业列表
     * @param status 企业状态
     * @param tenantId 租户ID
     * @return 企业列表
     */
    List<Enterprise> findByStatus(String status, String tenantId);
    
    /**
     * 批量查询企业
     * @param entIds 企业ID列表
     * @return 企业列表
     */
    List<Enterprise> findByIds(List<String> entIds);
    
    /**
     * 更新企业状态
     * @param entId 企业ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateStatus(String entId, String status);
    
    /**
     * 批量更新企业状态
     * @param entIds 企业ID列表
     * @param status 新状态
     * @return 更新成功的数量
     */
    int batchUpdateStatus(List<String> entIds, String status);
    
    /**
     * 根据查询规范查找企业列表
     * @param query 查询规范对象
     * @return 企业列表
     */
    List<Enterprise> findByQuery(EnterpriseQuery query);
    
    /**
     * 根据查询规范统计企业数量
     * @param query 查询规范对象
     * @return 企业数量
     */
    long countByQuery(EnterpriseQuery query);
    
    /**
     * 根据查询规范分页查询企业
     * @param query 查询规范对象（包含分页参数）
     * @return 企业列表
     */
    List<Enterprise> findPageByQuery(EnterpriseQuery query);
}