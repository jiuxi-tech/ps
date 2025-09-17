package com.jiuxi.module.org.infra.persistence.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.infra.persistence.entity.OrganizationPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织基础设施层装配器
 * 负责Organization聚合根与OrganizationPO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class OrganizationInfraAssembler {
    
    /**
     * 将组织聚合根转换为持久化对象
     * @param organization 组织聚合根
     * @return 组织持久化对象
     */
    public OrganizationPO toOrganizationPO(Organization organization) {
        if (organization == null) {
            return null;
        }
        
        OrganizationPO organizationPO = new OrganizationPO();
        organizationPO.setOrganizationId(organization.getOrganizationId());
        organizationPO.setOrganizationName(organization.getOrganizationName());
        organizationPO.setOrganizationShortName(organization.getOrganizationShortName());
        organizationPO.setOrganizationCode(organization.getOrganizationCode());
        organizationPO.setOrganizationType(organization.getOrganizationType() != null ? 
                organization.getOrganizationType().name() : null);
        organizationPO.setParentOrganizationId(organization.getParentOrganizationId());
        organizationPO.setOrganizationLevel(organization.getOrganizationLevel());
        organizationPO.setOrganizationPath(organization.getOrganizationPath());
        organizationPO.setCityCode(organization.getCityCode());
        organizationPO.setAddress(organization.getAddress());
        organizationPO.setContactPhone(organization.getContactPhone());
        organizationPO.setPrincipalName(organization.getPrincipalName());
        organizationPO.setPrincipalTel(organization.getPrincipalTel());
        organizationPO.setStatus(organization.getStatus() != null ? 
                organization.getStatus().name() : null);
        organizationPO.setEnabled(organization.getEnabled());
        organizationPO.setActived(organization.getActived());
        organizationPO.setDescription(organization.getDescription());
        organizationPO.setTenantId(organization.getTenantId());
        organizationPO.setCreator(organization.getCreator());
        organizationPO.setCreateTime(organization.getCreateTime());
        organizationPO.setUpdator(organization.getUpdator());
        organizationPO.setUpdateTime(organization.getUpdateTime());
        
        return organizationPO;
    }
    
    /**
     * 将持久化对象转换为组织聚合根
     * @param organizationPO 组织持久化对象
     * @return 组织聚合根
     */
    public Organization toOrganization(OrganizationPO organizationPO) {
        if (organizationPO == null) {
            return null;
        }
        
        Organization organization = new Organization();
        organization.setOrganizationId(organizationPO.getOrganizationId());
        organization.setOrganizationName(organizationPO.getOrganizationName());
        organization.setOrganizationShortName(organizationPO.getOrganizationShortName());
        organization.setOrganizationCode(organizationPO.getOrganizationCode());
        organization.setOrganizationType(organizationPO.getOrganizationType() != null ? 
                OrganizationType.valueOf(organizationPO.getOrganizationType()) : null);
        organization.setParentOrganizationId(organizationPO.getParentOrganizationId());
        organization.setOrganizationLevel(organizationPO.getOrganizationLevel());
        organization.setOrganizationPath(organizationPO.getOrganizationPath());
        organization.setCityCode(organizationPO.getCityCode());
        organization.setAddress(organizationPO.getAddress());
        organization.setContactPhone(organizationPO.getContactPhone());
        organization.setPrincipalName(organizationPO.getPrincipalName());
        organization.setPrincipalTel(organizationPO.getPrincipalTel());
        organization.setStatus(organizationPO.getStatus() != null ? 
                OrganizationStatus.valueOf(organizationPO.getStatus()) : OrganizationStatus.ACTIVE);
        organization.setEnabled(organizationPO.getEnabled());
        organization.setActived(organizationPO.getActived());
        organization.setDescription(organizationPO.getDescription());
        organization.setTenantId(organizationPO.getTenantId());
        organization.setCreator(organizationPO.getCreator());
        organization.setCreateTime(organizationPO.getCreateTime());
        organization.setUpdator(organizationPO.getUpdator());
        organization.setUpdateTime(organizationPO.getUpdateTime());
        
        return organization;
    }
    
    /**
     * 批量转换持久化对象为组织聚合根
     * @param organizationPOs 组织持久化对象列表
     * @return 组织聚合根列表
     */
    public List<Organization> toOrganizationList(List<OrganizationPO> organizationPOs) {
        if (organizationPOs == null || organizationPOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return organizationPOs.stream()
                .map(this::toOrganization)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换组织聚合根为持久化对象
     * @param organizations 组织聚合根列表
     * @return 组织持久化对象列表
     */
    public List<OrganizationPO> toOrganizationPOList(List<Organization> organizations) {
        if (organizations == null || organizations.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return organizations.stream()
                .map(this::toOrganizationPO)
                .collect(Collectors.toList());
    }
}