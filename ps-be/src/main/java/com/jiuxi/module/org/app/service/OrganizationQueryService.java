package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.repo.OrganizationRepository;
import com.jiuxi.module.org.app.query.dto.OrganizationResponseDTO;
import com.jiuxi.module.org.app.assembler.OrganizationAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织查询服务
 * 专门负责组织的查询、统计和分析功能
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryService {
    
    private final OrganizationRepository organizationRepository;
    private final OrganizationAssembler organizationAssembler;
    
    public OrganizationQueryService(OrganizationRepository organizationRepository,
                                   OrganizationAssembler organizationAssembler) {
        this.organizationRepository = organizationRepository;
        this.organizationAssembler = organizationAssembler;
    }
    
    /**
     * 根据ID查询组织
     * @param organizationId 组织ID
     * @return 组织响应
     */
    public Optional<OrganizationResponseDTO> findOrganizationById(String organizationId) {
        if (!StringUtils.hasText(organizationId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        return organizationRepository.findById(organizationId)
                .map(organizationAssembler::toResponseDTO);
    }
    
    /**
     * 根据组织名称查询组织
     * @param organizationName 组织名称
     * @param tenantId 租户ID
     * @return 组织响应
     */
    public Optional<OrganizationResponseDTO> findOrganizationByName(String organizationName, String tenantId) {
        if (!StringUtils.hasText(organizationName) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("组织名称和租户ID不能为空");
        }
        return organizationRepository.findByName(organizationName, tenantId)
                .map(organizationAssembler::toResponseDTO);
    }
    
    /**
     * 查询组织树
     * @param tenantId 租户ID
     * @return 组织树
     */
    public List<OrganizationResponseDTO> findOrganizationTree(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Organization> organizations = organizationRepository.findOrganizationTree(tenantId);
        return organizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据父组织查询子组织
     * @param parentOrganizationId 父组织ID
     * @return 子组织列表
     */
    public List<OrganizationResponseDTO> findChildOrganizations(String parentOrganizationId) {
        if (!StringUtils.hasText(parentOrganizationId)) {
            throw new IllegalArgumentException("父组织ID不能为空");
        }
        List<Organization> children = organizationRepository.findByParentId(parentOrganizationId);
        return children.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取根组织列表
     * @param tenantId 租户ID
     * @return 根组织列表
     */
    public List<OrganizationResponseDTO> getRootOrganizations(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Organization> rootOrganizations = organizationRepository.findRootOrganizations(tenantId);
        return rootOrganizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据租户ID查询组织列表
     * @param tenantId 租户ID
     * @return 组织列表
     */
    public List<OrganizationResponseDTO> findOrganizationsByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Organization> organizations = organizationRepository.findByTenantId(tenantId);
        return organizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据组织类型查询组织
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织列表
     */
    public List<OrganizationResponseDTO> findOrganizationsByType(OrganizationType organizationType, String tenantId) {
        if (organizationType == null || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("组织类型和租户ID不能为空");
        }
        List<Organization> organizations = organizationRepository.findByType(organizationType, tenantId);
        return organizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据状态查询组织
     * @param status 组织状态
     * @param tenantId 租户ID
     * @return 组织列表
     */
    public List<OrganizationResponseDTO> findOrganizationsByStatus(OrganizationStatus status, String tenantId) {
        if (status == null || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("组织状态和租户ID不能为空");
        }
        List<Organization> organizations = organizationRepository.findByStatus(status, tenantId);
        return organizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询组织的所有后代组织
     * @param organizationPath 组织路径
     * @return 后代组织列表
     */
    public List<OrganizationResponseDTO> findDescendantOrganizations(String organizationPath) {
        if (!StringUtils.hasText(organizationPath)) {
            throw new IllegalArgumentException("组织路径不能为空");
        }
        List<Organization> descendants = organizationRepository.findDescendants(organizationPath);
        return descendants.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询组织的所有祖先组织
     * @param organizationId 组织ID
     * @return 祖先组织列表
     */
    public List<OrganizationResponseDTO> findAncestorOrganizations(String organizationId) {
        if (!StringUtils.hasText(organizationId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        List<Organization> ancestors = organizationRepository.findAncestors(organizationId);
        return ancestors.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据层级查询组织
     * @param level 组织层级
     * @param tenantId 租户ID
     * @return 组织列表
     */
    public List<OrganizationResponseDTO> findOrganizationsByLevel(Integer level, String tenantId) {
        if (level == null || level < 1 || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("组织层级必须大于0，租户ID不能为空");
        }
        List<Organization> organizations = organizationRepository.findByLevel(level, tenantId);
        return organizations.stream()
                .map(organizationAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 统计直接子组织数量
     * @param organizationId 组织ID
     * @return 直接子组织数量
     */
    public long countDirectChildren(String organizationId) {
        if (!StringUtils.hasText(organizationId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        return organizationRepository.countDirectChildren(organizationId);
    }
    
    /**
     * 统计所有后代组织数量
     * @param organizationId 组织ID
     * @return 所有后代组织数量
     */
    public long countAllDescendants(String organizationId) {
        if (!StringUtils.hasText(organizationId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        return organizationRepository.countAllDescendants(organizationId);
    }
    
    /**
     * 检查是否为祖先关系
     * @param ancestorOrgId 祖先组织ID
     * @param descendantOrgId 后代组织ID
     * @return 是否为祖先关系
     */
    public boolean isAncestor(String ancestorOrgId, String descendantOrgId) {
        if (!StringUtils.hasText(ancestorOrgId) || !StringUtils.hasText(descendantOrgId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        return organizationRepository.isAncestor(ancestorOrgId, descendantOrgId);
    }
    
    /**
     * 统计租户的组织数量
     * @param tenantId 租户ID
     * @return 组织数量
     */
    public long countOrganizationsByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        return organizationRepository.countByTenantId(tenantId);
    }
    
    /**
     * 统计指定类型的组织数量
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织数量
     */
    public long countOrganizationsByType(OrganizationType organizationType, String tenantId) {
        if (organizationType == null || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("组织类型和租户ID不能为空");
        }
        return organizationRepository.countByType(organizationType, tenantId);
    }
}