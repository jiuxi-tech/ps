package com.jiuxi.module.org.infra.persistence.repo;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.repo.OrganizationRepository;
import com.jiuxi.module.org.domain.query.OrganizationQuery;
import com.jiuxi.module.org.infra.persistence.entity.OrganizationPO;
import com.jiuxi.module.org.infra.persistence.assembler.OrganizationInfraAssembler;
import com.jiuxi.module.org.infra.persistence.mapper.OrganizationMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 组织仓储实现类
 * 负责组织聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {
    
    private final OrganizationMapper organizationMapper;
    private final OrganizationInfraAssembler organizationInfraAssembler;
    
    public OrganizationRepositoryImpl(OrganizationMapper organizationMapper, 
                                    OrganizationInfraAssembler organizationInfraAssembler) {
        this.organizationMapper = organizationMapper;
        this.organizationInfraAssembler = organizationInfraAssembler;
    }

    @Override
    public Organization save(Organization organization) {
        OrganizationPO organizationPO = organizationInfraAssembler.toOrganizationPO(organization);
        
        if (StringUtils.hasText(organizationPO.getOrganizationId())) {
            // 更新
            organizationPO.setUpdateTime(LocalDateTime.now());
            organizationMapper.updateById(organizationPO);
        } else {
            // 新增
            organizationPO.setOrganizationId(UUID.randomUUID().toString());
            organizationPO.setCreateTime(LocalDateTime.now());
            organizationPO.setActived(1);
            organizationPO.setEnabled(1);
            organizationMapper.insert(organizationPO);
        }
        
        return organizationInfraAssembler.toOrganization(organizationPO);
    }

    @Override
    public Optional<Organization> findById(String organizationId) {
        Optional<OrganizationPO> organizationPOOpt = organizationMapper.selectById(organizationId);
        return organizationPOOpt.map(organizationInfraAssembler::toOrganization);
    }

    @Override
    public Optional<Organization> findByName(String organizationName, String tenantId) {
        Optional<OrganizationPO> organizationPOOpt = organizationMapper.selectByName(organizationName, tenantId);
        return organizationPOOpt.map(organizationInfraAssembler::toOrganization);
    }

    @Override
    public Optional<Organization> findByCode(String organizationCode) {
        Optional<OrganizationPO> organizationPOOpt = organizationMapper.selectByCode(organizationCode);
        return organizationPOOpt.map(organizationInfraAssembler::toOrganization);
    }

    @Override
    public List<Organization> findByParentId(String parentOrganizationId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByParentId(parentOrganizationId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findRootOrganizations(String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectRootOrganizations(tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findByTenantId(String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByTenantId(tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findByType(OrganizationType organizationType, String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByType(organizationType.name(), tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findByStatus(OrganizationStatus status, String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByStatus(status.name(), tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public boolean existsByName(String organizationName, String tenantId, String excludeOrgId) {
        int count = organizationMapper.countByName(organizationName, tenantId, excludeOrgId);
        return count > 0;
    }

    @Override
    public boolean existsByCode(String organizationCode, String excludeOrgId) {
        int count = organizationMapper.countByCode(organizationCode, excludeOrgId);
        return count > 0;
    }

    @Override
    public void deleteById(String organizationId) {
        organizationMapper.deleteById(organizationId, "system", LocalDateTime.now());
    }

    @Override
    public void deleteByIds(List<String> organizationIds) {
        organizationMapper.deleteByIds(organizationIds, "system", LocalDateTime.now());
    }

    @Override
    public List<Organization> findDescendants(String organizationPath) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectDescendants(organizationPath);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findAncestors(String organizationId) {
        Optional<Organization> orgOpt = findById(organizationId);
        if (!orgOpt.isPresent()) {
            return List.of();
        }
        
        Organization org = orgOpt.get();
        List<OrganizationPO> ancestorPOs = organizationMapper.selectAncestors(org.getOrganizationPath());
        return organizationInfraAssembler.toOrganizationList(ancestorPOs);
    }

    @Override
    public List<Organization> findDescendants(String organizationId, boolean includeInactive) {
        Optional<Organization> orgOpt = findById(organizationId);
        if (!orgOpt.isPresent()) {
            return List.of();
        }
        
        Organization org = orgOpt.get();
        List<OrganizationPO> descendantPOs;
        if (includeInactive) {
            descendantPOs = organizationMapper.selectAllDescendants(org.getOrganizationPath());
        } else {
            descendantPOs = organizationMapper.selectActiveDescendants(org.getOrganizationPath());
        }
        return organizationInfraAssembler.toOrganizationList(descendantPOs);
    }

    @Override
    public List<Organization> findByLevel(Integer level, String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByLevel(level, tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findOrganizationTree(String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectOrganizationTree(tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public long countDirectChildren(String organizationId) {
        return organizationMapper.countDirectChildren(organizationId);
    }

    @Override
    public long countAllDescendants(String organizationId) {
        Optional<Organization> orgOpt = findById(organizationId);
        if (!orgOpt.isPresent()) {
            return 0;
        }
        
        Organization org = orgOpt.get();
        return organizationMapper.countAllDescendants(org.getOrganizationPath());
    }

    @Override
    public boolean isAncestor(String ancestorOrgId, String descendantOrgId) {
        Optional<Organization> ancestorOpt = findById(ancestorOrgId);
        Optional<Organization> descendantOpt = findById(descendantOrgId);
        
        if (!ancestorOpt.isPresent() || !descendantOpt.isPresent()) {
            return false;
        }
        
        Organization ancestor = ancestorOpt.get();
        Organization descendant = descendantOpt.get();
        
        // 通过路径判断祖先关系
        return StringUtils.hasText(descendant.getOrganizationPath()) && 
               StringUtils.hasText(ancestor.getOrganizationPath()) &&
               descendant.getOrganizationPath().startsWith(ancestor.getOrganizationPath() + "/");
    }

    @Override
    public List<Organization> findByCityCode(String cityCode, String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByCityCode(cityCode, tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public long countByTenantId(String tenantId) {
        return organizationMapper.countByTenantId(tenantId);
    }

    @Override
    public long countByType(OrganizationType organizationType, String tenantId) {
        return organizationMapper.countByType(organizationType.name(), tenantId);
    }

    @Override
    public List<Organization> findPage(String tenantId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<OrganizationPO> organizationPOs = organizationMapper.selectPage(tenantId, offset, pageSize);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> searchByKeyword(String keyword, String tenantId) {
        List<OrganizationPO> organizationPOs = organizationMapper.searchByKeyword(keyword, tenantId);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public List<Organization> findByIds(List<String> organizationIds) {
        List<OrganizationPO> organizationPOs = organizationMapper.selectByIds(organizationIds);
        return organizationInfraAssembler.toOrganizationList(organizationPOs);
    }

    @Override
    public boolean updateStatus(String organizationId, OrganizationStatus status) {
        Integer enabled = OrganizationStatus.ACTIVE.equals(status) ? 1 : 0;
        Integer actived = OrganizationStatus.INACTIVE.equals(status) ? 0 : 1;
        int result = organizationMapper.updateStatus(organizationId, status.name(), enabled, actived);
        return result > 0;
    }

    @Override
    public int batchUpdateStatus(List<String> organizationIds, OrganizationStatus status) {
        Integer enabled = OrganizationStatus.ACTIVE.equals(status) ? 1 : 0;
        Integer actived = OrganizationStatus.INACTIVE.equals(status) ? 0 : 1;
        return organizationMapper.batchUpdateStatus(organizationIds, status.name(), enabled, actived);
    }

    @Override
    public void updatePathAndLevel(String organizationId, String organizationPath, Integer level) {
        organizationMapper.updatePathAndLevel(organizationId, organizationPath, level);
    }

    @Override
    public void batchUpdatePathAndLevel(List<Organization> organizations) {
        List<OrganizationPO> organizationPOs = organizationInfraAssembler.toOrganizationPOList(organizations);
        organizationMapper.batchUpdatePathAndLevel(organizationPOs);
    }

    @Override
    public List<Organization> findByQuery(OrganizationQuery query) {
        // TODO: 实现复杂查询逻辑，需要根据OrganizationQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("findByQuery method not implemented yet");
    }

    @Override
    public long countByQuery(OrganizationQuery query) {
        // TODO: 实现复杂查询统计逻辑，需要根据OrganizationQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("countByQuery method not implemented yet");
    }

    @Override
    public List<Organization> findPageByQuery(OrganizationQuery query) {
        // TODO: 实现复杂查询分页逻辑，需要根据OrganizationQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("findPageByQuery method not implemented yet");
    }
}