package com.jiuxi.module.org.app.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.app.query.dto.OrganizationResponseDTO;
import org.springframework.stereotype.Component;

/**
 * 组织装配器
 * 负责组织聚合根与DTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class OrganizationAssembler {
    
    /**
     * 将组织聚合根转换为响应DTO
     * @param organization 组织聚合根
     * @return 组织响应DTO
     */
    public OrganizationResponseDTO toResponseDTO(Organization organization) {
        if (organization == null) {
            return null;
        }
        
        OrganizationResponseDTO dto = new OrganizationResponseDTO();
        dto.setOrganizationId(organization.getOrganizationId());
        dto.setOrganizationName(organization.getOrganizationName());
        dto.setOrganizationCode(organization.getOrganizationCode());
        dto.setOrganizationType(organization.getOrganizationType());
        dto.setParentOrganizationId(organization.getParentOrganizationId());
        dto.setOrganizationPath(organization.getOrganizationPath());
        dto.setLevel(organization.getOrganizationLevel());
        dto.setDescription(organization.getDescription());
        // 暂时移除orderIndex，Organization类中没有此字段
        dto.setStatus(organization.getStatus());
        dto.setTenantId(organization.getTenantId());
        dto.setCreator(organization.getCreator());
        dto.setCreateTime(organization.getCreateTime());
        dto.setUpdator(organization.getUpdator());
        dto.setUpdateTime(organization.getUpdateTime());
        
        return dto;
    }
    
    /**
     * 将组织聚合根转换为简化响应DTO（仅包含关键信息）
     * @param organization 组织聚合根
     * @return 组织响应DTO
     */
    public OrganizationResponseDTO toSimpleResponseDTO(Organization organization) {
        if (organization == null) {
            return null;
        }
        
        OrganizationResponseDTO dto = new OrganizationResponseDTO();
        dto.setOrganizationId(organization.getOrganizationId());
        dto.setOrganizationName(organization.getOrganizationName());
        dto.setOrganizationCode(organization.getOrganizationCode());
        dto.setOrganizationType(organization.getOrganizationType());
        dto.setParentOrganizationId(organization.getParentOrganizationId());
        dto.setLevel(organization.getOrganizationLevel());
        dto.setStatus(organization.getStatus());
        dto.setTenantId(organization.getTenantId());
        
        return dto;
    }
    
    /**
     * 批量转换组织列表为响应DTO列表
     * @param organizations 组织列表
     * @return 组织响应DTO列表
     */
    public java.util.List<OrganizationResponseDTO> toResponseDTOList(java.util.List<Organization> organizations) {
        if (organizations == null || organizations.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return organizations.stream()
                .map(this::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 批量转换组织列表为简化响应DTO列表
     * @param organizations 组织列表
     * @return 组织响应DTO列表
     */
    public java.util.List<OrganizationResponseDTO> toSimpleResponseDTOList(java.util.List<Organization> organizations) {
        if (organizations == null || organizations.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return organizations.stream()
                .map(this::toSimpleResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}