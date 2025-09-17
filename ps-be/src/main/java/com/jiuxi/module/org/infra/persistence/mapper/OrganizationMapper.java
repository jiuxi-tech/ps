package com.jiuxi.module.org.infra.persistence.mapper;

import com.jiuxi.module.org.infra.persistence.entity.OrganizationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 组织持久化映射器
 * 基于DDD重构的Organization持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Mapper
public interface OrganizationMapper {
    
    /**
     * 插入组织
     * @param organization 组织持久化对象
     * @return 影响行数
     */
    int insert(OrganizationPO organization);
    
    /**
     * 根据ID更新组织
     * @param organization 组织持久化对象
     * @return 影响行数
     */
    int updateById(OrganizationPO organization);
    
    /**
     * 根据ID查询组织
     * @param organizationId 组织ID
     * @return 组织持久化对象
     */
    Optional<OrganizationPO> selectById(@Param("organizationId") String organizationId);
    
    /**
     * 根据组织名称查询组织
     * @param organizationName 组织名称
     * @param tenantId 租户ID
     * @return 组织持久化对象
     */
    Optional<OrganizationPO> selectByName(@Param("organizationName") String organizationName, @Param("tenantId") String tenantId);
    
    /**
     * 根据组织代码查询组织
     * @param organizationCode 组织代码
     * @return 组织持久化对象
     */
    Optional<OrganizationPO> selectByCode(@Param("organizationCode") String organizationCode);
    
    /**
     * 根据父组织ID查询子组织
     * @param parentOrganizationId 父组织ID
     * @return 子组织列表
     */
    List<OrganizationPO> selectByParentId(@Param("parentOrganizationId") String parentOrganizationId);
    
    /**
     * 根据租户ID查询根组织
     * @param tenantId 租户ID
     * @return 根组织列表
     */
    List<OrganizationPO> selectRootOrganizations(@Param("tenantId") String tenantId);
    
    /**
     * 根据租户ID查询所有组织
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationPO> selectByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 根据组织类型查询组织
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationPO> selectByType(@Param("organizationType") String organizationType, @Param("tenantId") String tenantId);
    
    /**
     * 根据组织状态查询组织
     * @param status 组织状态
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationPO> selectByStatus(@Param("status") String status, @Param("tenantId") String tenantId);
    
    /**
     * 检查组织名称是否存在
     * @param organizationName 组织名称
     * @param tenantId 租户ID
     * @param excludeOrgId 排除的组织ID
     * @return 存在的数量
     */
    int countByName(@Param("organizationName") String organizationName, @Param("tenantId") String tenantId, @Param("excludeOrgId") String excludeOrgId);
    
    /**
     * 检查组织代码是否存在
     * @param organizationCode 组织代码
     * @param excludeOrgId 排除的组织ID
     * @return 存在的数量
     */
    int countByCode(@Param("organizationCode") String organizationCode, @Param("excludeOrgId") String excludeOrgId);
    
    /**
     * 逻辑删除组织
     * @param organizationId 组织ID
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteById(@Param("organizationId") String organizationId, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量逻辑删除组织
     * @param organizationIds 组织ID列表
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteByIds(@Param("organizationIds") List<String> organizationIds, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 根据组织路径查询后代组织
     * @param organizationPath 组织路径前缀
     * @return 后代组织列表
     */
    List<OrganizationPO> selectDescendants(@Param("organizationPath") String organizationPath);
    
    /**
     * 查询指定组织的祖先组织
     * @param organizationPath 组织路径
     * @return 祖先组织列表
     */
    List<OrganizationPO> selectAncestors(@Param("organizationPath") String organizationPath);
    
    /**
     * 查询所有后代组织（包含停用）
     * @param organizationPath 组织路径前缀
     * @return 后代组织列表
     */
    List<OrganizationPO> selectAllDescendants(@Param("organizationPath") String organizationPath);
    
    /**
     * 查询激活的后代组织
     * @param organizationPath 组织路径前缀
     * @return 激活的后代组织列表
     */
    List<OrganizationPO> selectActiveDescendants(@Param("organizationPath") String organizationPath);
    
    /**
     * 根据层级查询组织
     * @param level 组织层级
     * @param tenantId 租户ID
     * @return 指定层级的组织列表
     */
    List<OrganizationPO> selectByLevel(@Param("level") Integer level, @Param("tenantId") String tenantId);
    
    /**
     * 获取组织树形结构
     * @param tenantId 租户ID
     * @return 组织列表（按层级排序）
     */
    List<OrganizationPO> selectOrganizationTree(@Param("tenantId") String tenantId);
    
    /**
     * 统计直接子组织数量
     * @param organizationId 组织ID
     * @return 直接子组织数量
     */
    long countDirectChildren(@Param("organizationId") String organizationId);
    
    /**
     * 统计所有后代组织数量
     * @param organizationPath 组织路径前缀
     * @return 所有后代组织数量
     */
    long countAllDescendants(@Param("organizationPath") String organizationPath);
    
    /**
     * 根据行政区划查询组织
     * @param cityCode 行政区划代码
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationPO> selectByCityCode(@Param("cityCode") String cityCode, @Param("tenantId") String tenantId);
    
    /**
     * 统计租户组织数量
     * @param tenantId 租户ID
     * @return 组织数量
     */
    long countByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 统计组织类型数量
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织数量
     */
    long countByType(@Param("organizationType") String organizationType, @Param("tenantId") String tenantId);
    
    /**
     * 分页查询组织
     * @param tenantId 租户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 组织列表
     */
    List<OrganizationPO> selectPage(@Param("tenantId") String tenantId, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据关键词搜索组织
     * @param keyword 搜索关键词
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationPO> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);
    
    /**
     * 批量查询组织
     * @param organizationIds 组织ID列表
     * @return 组织列表
     */
    List<OrganizationPO> selectByIds(@Param("organizationIds") List<String> organizationIds);
    
    /**
     * 更新组织状态
     * @param organizationId 组织ID
     * @param status 组织状态
     * @param enabled 启用状态
     * @param actived 有效状态
     * @return 影响行数
     */
    int updateStatus(@Param("organizationId") String organizationId, @Param("status") String status, @Param("enabled") Integer enabled, @Param("actived") Integer actived);
    
    /**
     * 批量更新组织状态
     * @param organizationIds 组织ID列表
     * @param status 组织状态
     * @param enabled 启用状态
     * @param actived 有效状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("organizationIds") List<String> organizationIds, @Param("status") String status, @Param("enabled") Integer enabled, @Param("actived") Integer actived);
    
    /**
     * 更新组织路径和层级
     * @param organizationId 组织ID
     * @param organizationPath 组织路径
     * @param level 层级
     * @return 影响行数
     */
    int updatePathAndLevel(@Param("organizationId") String organizationId, @Param("organizationPath") String organizationPath, @Param("level") Integer level);
    
    /**
     * 批量更新组织路径和层级
     * @param organizations 组织列表
     * @return 影响行数
     */
    int batchUpdatePathAndLevel(@Param("organizations") List<OrganizationPO> organizations);
}