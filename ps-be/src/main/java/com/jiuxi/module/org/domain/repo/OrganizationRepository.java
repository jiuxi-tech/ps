package com.jiuxi.module.org.domain.repo;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.query.OrganizationQuery;
import java.util.List;
import java.util.Optional;

/**
 * 组织仓储接口
 * 定义组织聚合根的持久化操作规范
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
public interface OrganizationRepository {
    
    /**
     * 保存组织
     * @param organization 组织聚合根
     * @return 保存后的组织
     */
    Organization save(Organization organization);
    
    /**
     * 根据ID查找组织
     * @param organizationId 组织ID
     * @return 组织信息，如果不存在则返回空
     */
    Optional<Organization> findById(String organizationId);
    
    /**
     * 根据组织名称查找组织
     * @param organizationName 组织名称
     * @param tenantId 租户ID
     * @return 组织信息，如果不存在则返回空
     */
    Optional<Organization> findByName(String organizationName, String tenantId);
    
    /**
     * 根据组织代码查找组织
     * @param organizationCode 组织代码
     * @return 组织信息，如果不存在则返回空
     */
    Optional<Organization> findByCode(String organizationCode);
    
    /**
     * 根据父组织ID查找子组织列表
     * @param parentOrganizationId 父组织ID
     * @return 子组织列表
     */
    List<Organization> findByParentId(String parentOrganizationId);
    
    /**
     * 查找所有根组织（顶级组织）
     * @param tenantId 租户ID
     * @return 根组织列表
     */
    List<Organization> findRootOrganizations(String tenantId);
    
    /**
     * 根据租户ID查找组织列表
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> findByTenantId(String tenantId);
    
    /**
     * 根据组织类型查找组织列表
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> findByType(OrganizationType organizationType, String tenantId);
    
    /**
     * 根据组织状态查找组织列表
     * @param status 组织状态
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> findByStatus(OrganizationStatus status, String tenantId);
    
    /**
     * 检查组织名称是否存在
     * @param organizationName 组织名称
     * @param tenantId 租户ID
     * @param excludeOrgId 排除的组织ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByName(String organizationName, String tenantId, String excludeOrgId);
    
    /**
     * 检查组织代码是否存在
     * @param organizationCode 组织代码
     * @param excludeOrgId 排除的组织ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByCode(String organizationCode, String excludeOrgId);
    
    /**
     * 删除组织
     * @param organizationId 组织ID
     */
    void deleteById(String organizationId);
    
    /**
     * 批量删除组织
     * @param organizationIds 组织ID列表
     */
    void deleteByIds(List<String> organizationIds);
    
    /**
     * 根据组织路径查找所有子组织（包括子组织的子组织）
     * @param organizationPath 组织路径
     * @return 子组织列表
     */
    List<Organization> findDescendants(String organizationPath);
    
    /**
     * 查询指定组织的所有祖先组织
     * @param organizationId 组织ID
     * @return 祖先组织列表（按层级从高到低排序）
     */
    List<Organization> findAncestors(String organizationId);
    
    /**
     * 查询指定组织的所有后代组织
     * @param organizationId 组织ID
     * @param includeInactive 是否包含停用组织
     * @return 后代组织列表
     */
    List<Organization> findDescendants(String organizationId, boolean includeInactive);
    
    /**
     * 根据层级查询组织
     * @param level 组织层级
     * @param tenantId 租户ID
     * @return 指定层级的组织列表
     */
    List<Organization> findByLevel(Integer level, String tenantId);
    
    /**
     * 获取组织树形结构
     * @param tenantId 租户ID
     * @return 组织树
     */
    List<Organization> findOrganizationTree(String tenantId);
    
    /**
     * 查询组织的直接子组织数量
     * @param organizationId 组织ID
     * @return 直接子组织数量
     */
    long countDirectChildren(String organizationId);
    
    /**
     * 查询组织的所有子组织数量（包括多层级）
     * @param organizationId 组织ID
     * @return 所有子组织数量
     */
    long countAllDescendants(String organizationId);
    
    /**
     * 检查组织是否为另一个组织的祖先
     * @param ancestorOrgId 祖先组织ID
     * @param descendantOrgId 后代组织ID
     * @return 是否为祖先关系
     */
    boolean isAncestor(String ancestorOrgId, String descendantOrgId);
    
    /**
     * 根据行政区划代码查找组织
     * @param cityCode 行政区划代码
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> findByCityCode(String cityCode, String tenantId);
    
    /**
     * 统计指定租户下的组织数量
     * @param tenantId 租户ID
     * @return 组织数量
     */
    long countByTenantId(String tenantId);
    
    /**
     * 统计指定类型的组织数量
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织数量
     */
    long countByType(OrganizationType organizationType, String tenantId);
    
    /**
     * 分页查询组织列表
     * @param tenantId 租户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 组织列表
     */
    List<Organization> findPage(String tenantId, int pageNum, int pageSize);
    
    /**
     * 根据关键词搜索组织（组织名称、负责人姓名）
     * @param keyword 搜索关键词
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> searchByKeyword(String keyword, String tenantId);
    
    /**
     * 批量查询组织
     * @param organizationIds 组织ID列表
     * @return 组织列表
     */
    List<Organization> findByIds(List<String> organizationIds);
    
    /**
     * 更新组织状态
     * @param organizationId 组织ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateStatus(String organizationId, OrganizationStatus status);
    
    /**
     * 批量更新组织状态
     * @param organizationIds 组织ID列表
     * @param status 新状态
     * @return 更新成功的数量
     */
    int batchUpdateStatus(List<String> organizationIds, OrganizationStatus status);
    
    /**
     * 更新组织路径和层级
     * @param organizationId 组织ID
     * @param organizationPath 组织路径
     * @param level 组织层级
     */
    void updatePathAndLevel(String organizationId, String organizationPath, Integer level);
    
    /**
     * 批量更新组织路径和层级
     * @param organizations 组织列表（包含路径和层级信息）
     */
    void batchUpdatePathAndLevel(List<Organization> organizations);
    
    /**
     * 根据查询规范查找组织列表
     * @param query 查询规范对象
     * @return 组织列表
     */
    List<Organization> findByQuery(OrganizationQuery query);
    
    /**
     * 根据查询规范统计组织数量
     * @param query 查询规范对象
     * @return 组织数量
     */
    long countByQuery(OrganizationQuery query);
    
    /**
     * 根据查询规范分页查询组织
     * @param query 查询规范对象（包含分页参数）
     * @return 组织列表
     */
    List<Organization> findPageByQuery(OrganizationQuery query);
}