package com.jiuxi.module.org.app.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.app.query.dto.EnterpriseResponseDTO;
import org.springframework.stereotype.Component;

/**
 * 企业装配器
 * 负责企业聚合根与DTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class EnterpriseAssembler {
    
    /**
     * 将企业聚合根转换为响应DTO
     * @param enterprise 企业聚合根
     * @return 企业响应DTO
     */
    public EnterpriseResponseDTO toResponseDTO(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }
        
        EnterpriseResponseDTO dto = new EnterpriseResponseDTO();
        dto.setEntId(enterprise.getEntId());
        dto.setEntFullName(enterprise.getEntFullName());
        dto.setEntSimpleName(enterprise.getEntSimpleName());
        dto.setEntUnifiedCode(enterprise.getEntUnifiedCode());
        dto.setEntType(enterprise.getEntType());
        dto.setEntPhone(enterprise.getLegalReprTel());
        dto.setEntEmail(""); // 暂时为空，需要后续完善
        dto.setEntAddress(enterprise.getEntAddr());
        dto.setEntLegalPerson(enterprise.getLegalRepr());
        dto.setEntManager(enterprise.getLinkPsnName());
        dto.setEntDescription(enterprise.getEntDesc());
        dto.setEntLongitude(enterprise.getLongitude());
        dto.setEntLatitude(enterprise.getLatitude());
        dto.setEntStatus(enterprise.getStatus() != null ? enterprise.getStatus().name() : null);
        dto.setTenantId(enterprise.getTenantId());
        dto.setCreator(enterprise.getCreator());
        dto.setCreateTime(enterprise.getCreateTime());
        dto.setUpdator(enterprise.getUpdator());
        dto.setUpdateTime(enterprise.getUpdateTime());
        
        return dto;
    }
    
    /**
     * 将企业聚合根转换为简化响应DTO（仅包含关键信息）
     * @param enterprise 企业聚合根
     * @return 企业响应DTO
     */
    public EnterpriseResponseDTO toSimpleResponseDTO(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }
        
        EnterpriseResponseDTO dto = new EnterpriseResponseDTO();
        dto.setEntId(enterprise.getEntId());
        dto.setEntFullName(enterprise.getEntFullName());
        dto.setEntSimpleName(enterprise.getEntSimpleName());
        dto.setEntUnifiedCode(enterprise.getEntUnifiedCode());
        dto.setEntType(enterprise.getEntType());
        dto.setEntStatus(enterprise.getStatus() != null ? enterprise.getStatus().name() : null);
        dto.setTenantId(enterprise.getTenantId());
        
        return dto;
    }
    
    /**
     * 批量转换企业列表为响应DTO列表
     * @param enterprises 企业列表
     * @return 企业响应DTO列表
     */
    public java.util.List<EnterpriseResponseDTO> toResponseDTOList(java.util.List<Enterprise> enterprises) {
        if (enterprises == null || enterprises.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return enterprises.stream()
                .map(this::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 批量转换企业列表为简化响应DTO列表
     * @param enterprises 企业列表
     * @return 企业响应DTO列表
     */
    public java.util.List<EnterpriseResponseDTO> toSimpleResponseDTOList(java.util.List<Enterprise> enterprises) {
        if (enterprises == null || enterprises.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return enterprises.stream()
                .map(this::toSimpleResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}