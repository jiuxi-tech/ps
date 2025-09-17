package com.jiuxi.module.org.infra.persistence.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;
import com.jiuxi.module.org.infra.persistence.entity.EnterprisePO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 企业基础设施层装配器
 * 负责Enterprise聚合根与EnterprisePO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class EnterpriseInfraAssembler {
    
    /**
     * 将企业聚合根转换为持久化对象
     * @param enterprise 企业聚合根
     * @return 企业持久化对象
     */
    public EnterprisePO toEnterprisePO(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }
        
        EnterprisePO enterprisePO = new EnterprisePO();
        enterprisePO.setEntId(enterprise.getEntId());
        enterprisePO.setEntFullName(enterprise.getEntFullName());
        enterprisePO.setEntSimpleName(enterprise.getEntSimpleName());
        enterprisePO.setEntUnifiedCode(enterprise.getEntUnifiedCode());
        enterprisePO.setEntType(enterprise.getEntType());
        enterprisePO.setEntDesc(enterprise.getEntDesc());
        enterprisePO.setLegalRepr(enterprise.getLegalRepr());
        enterprisePO.setLegalReprTel(enterprise.getLegalReprTel());
        enterprisePO.setLinkPsnName(enterprise.getLinkPsnName());
        enterprisePO.setLinkPsnTel(enterprise.getLinkPsnTel());
        enterprisePO.setRegFund(enterprise.getRegFund());
        enterprisePO.setEntAddrCode(enterprise.getEntAddrCode());
        enterprisePO.setEntAddr(enterprise.getEntAddr());
        enterprisePO.setLongitude(enterprise.getLongitude());
        enterprisePO.setLatitude(enterprise.getLatitude());
        enterprisePO.setGeoCode(enterprise.getGeoCode());
        enterprisePO.setProdAddrCode(enterprise.getProdAddrCode());
        enterprisePO.setProdAddr(enterprise.getProdAddr());
        enterprisePO.setIndustryTypeCode(enterprise.getIndustryTypeCode());
        enterprisePO.setLineCode(enterprise.getLineCode());
        enterprisePO.setScaleType(enterprise.getScaleType());
        enterprisePO.setEnabled(enterprise.getEnabled());
        enterprisePO.setActived(enterprise.getActived());
        enterprisePO.setTenantId(enterprise.getTenantId());
        enterprisePO.setCreator(enterprise.getCreator());
        enterprisePO.setCreateTime(enterprise.getCreateTime());
        enterprisePO.setUpdator(enterprise.getUpdator());
        enterprisePO.setUpdateTime(enterprise.getUpdateTime());
        
        return enterprisePO;
    }
    
    /**
     * 将持久化对象转换为企业聚合根
     * @param enterprisePO 企业持久化对象
     * @return 企业聚合根
     */
    public Enterprise toEnterprise(EnterprisePO enterprisePO) {
        if (enterprisePO == null) {
            return null;
        }
        
        Enterprise enterprise = new Enterprise();
        enterprise.setEntId(enterprisePO.getEntId());
        enterprise.setEntFullName(enterprisePO.getEntFullName());
        enterprise.setEntSimpleName(enterprisePO.getEntSimpleName());
        enterprise.setEntUnifiedCode(enterprisePO.getEntUnifiedCode());
        enterprise.setEntType(enterprisePO.getEntType());
        enterprise.setEntDesc(enterprisePO.getEntDesc());
        enterprise.setLegalRepr(enterprisePO.getLegalRepr());
        enterprise.setLegalReprTel(enterprisePO.getLegalReprTel());
        enterprise.setLinkPsnName(enterprisePO.getLinkPsnName());
        enterprise.setLinkPsnTel(enterprisePO.getLinkPsnTel());
        enterprise.setRegFund(enterprisePO.getRegFund());
        enterprise.setEntAddrCode(enterprisePO.getEntAddrCode());
        enterprise.setEntAddr(enterprisePO.getEntAddr());
        enterprise.setLongitude(enterprisePO.getLongitude());
        enterprise.setLatitude(enterprisePO.getLatitude());
        enterprise.setGeoCode(enterprisePO.getGeoCode());
        enterprise.setProdAddrCode(enterprisePO.getProdAddrCode());
        enterprise.setProdAddr(enterprisePO.getProdAddr());
        enterprise.setIndustryTypeCode(enterprisePO.getIndustryTypeCode());
        enterprise.setLineCode(enterprisePO.getLineCode());
        enterprise.setScaleType(enterprisePO.getScaleType());
        enterprise.setEnabled(enterprisePO.getEnabled());
        enterprise.setActived(enterprisePO.getActived());
        enterprise.setTenantId(enterprisePO.getTenantId());
        enterprise.setCreator(enterprisePO.getCreator());
        enterprise.setCreateTime(enterprisePO.getCreateTime());
        enterprise.setUpdator(enterprisePO.getUpdator());
        enterprise.setUpdateTime(enterprisePO.getUpdateTime());
        
        // 从状态字段推断EnterpriseStatus
        if (enterprisePO.getActived() != null && enterprisePO.getActived() == 0) {
            enterprise.setStatus(EnterpriseStatus.CANCELLED);
        } else if (enterprisePO.getEnabled() != null && enterprisePO.getEnabled() == 0) {
            enterprise.setStatus(EnterpriseStatus.INACTIVE);
        } else {
            enterprise.setStatus(EnterpriseStatus.ACTIVE);
        }
        
        return enterprise;
    }
    
    /**
     * 批量转换持久化对象为企业聚合根
     * @param enterprisePOs 企业持久化对象列表
     * @return 企业聚合根列表
     */
    public List<Enterprise> toEnterpriseList(List<EnterprisePO> enterprisePOs) {
        if (enterprisePOs == null || enterprisePOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return enterprisePOs.stream()
                .map(this::toEnterprise)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换企业聚合根为持久化对象
     * @param enterprises 企业聚合根列表
     * @return 企业持久化对象列表
     */
    public List<EnterprisePO> toEnterprisePOList(List<Enterprise> enterprises) {
        if (enterprises == null || enterprises.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return enterprises.stream()
                .map(this::toEnterprisePO)
                .collect(Collectors.toList());
    }
}