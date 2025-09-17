package com.jiuxi.module.org.domain.service;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.repo.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 组织领域服务
 * 负责组织相关的业务规则和领域逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Service
public class OrganizationDomainService {
    
    private final OrganizationRepository organizationRepository;
    
    // 组织代码正则表达式（统一社会信用代码或组织机构代码）
    private static final Pattern ORG_CODE_PATTERN = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$|^[A-Z0-9]{8}-[A-Z0-9]$");
    
    // 联系电话正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$|^0\\d{2,3}-?\\d{7,8}$");
    
    public OrganizationDomainService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
    
    /**
     * 验证组织创建规则
     * @param organization 待创建的组织
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForCreate(Organization organization, String tenantId) {
        // 验证必填字段
        validateRequiredFields(organization, tenantId);
        
        // 验证组织名称唯一性
        if (organizationRepository.existsByName(organization.getOrganizationName(), tenantId, null)) {
            throw new IllegalArgumentException("组织名称已存在: " + organization.getOrganizationName());
        }
        
        // 验证组织代码唯一性
        if (StringUtils.hasText(organization.getOrganizationCode())) {
            if (organizationRepository.existsByCode(organization.getOrganizationCode(), null)) {
                throw new IllegalArgumentException("组织代码已存在: " + organization.getOrganizationCode());
            }
        }
        
        // 验证父组织存在性和合法性
        if (StringUtils.hasText(organization.getParentOrganizationId())) {
            validateParentOrganization(organization.getParentOrganizationId(), tenantId);
        }
        
        // 验证格式规范
        validateFormat(organization);
        
        // 验证组织层级深度
        validateOrganizationDepth(organization);
    }
    
    /**
     * 验证组织更新规则
     * @param organization 待更新的组织
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForUpdate(Organization organization, String tenantId) {
        // 验证组织是否存在
        if (!StringUtils.hasText(organization.getOrganizationId())) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        
        Optional<Organization> existingOrg = organizationRepository.findById(organization.getOrganizationId());
        if (existingOrg.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organization.getOrganizationId());
        }
        
        // 验证必填字段
        validateRequiredFields(organization, tenantId);
        
        // 验证组织名称唯一性（排除自己）
        if (organizationRepository.existsByName(organization.getOrganizationName(), tenantId, organization.getOrganizationId())) {
            throw new IllegalArgumentException("组织名称已存在: " + organization.getOrganizationName());
        }
        
        // 验证组织代码唯一性（排除自己）
        if (StringUtils.hasText(organization.getOrganizationCode())) {
            if (organizationRepository.existsByCode(organization.getOrganizationCode(), organization.getOrganizationId())) {
                throw new IllegalArgumentException("组织代码已存在: " + organization.getOrganizationCode());
            }
        }
        
        // 验证父组织变更合法性
        if (StringUtils.hasText(organization.getParentOrganizationId())) {
            // 不能将自己设为父组织
            if (organization.getOrganizationId().equals(organization.getParentOrganizationId())) {
                throw new IllegalArgumentException("组织不能将自己设为父组织");
            }
            
            // 验证父组织存在性
            validateParentOrganization(organization.getParentOrganizationId(), tenantId);
            
            // 验证不能将父组织设为自己的子组织（防止循环引用）
            if (isDescendant(organization.getOrganizationId(), organization.getParentOrganizationId())) {
                throw new IllegalArgumentException("不能将子组织设为父组织，会造成循环引用");
            }
        }
        
        // 验证格式规范
        validateFormat(organization);
        
        // 验证状态变更合法性
        validateStatusChange(existingOrg.get(), organization);
    }
    
    /**
     * 验证组织删除规则
     * @param organizationId 组织ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForDelete(String organizationId) {
        if (!StringUtils.hasText(organizationId)) {
            throw new IllegalArgumentException("组织ID不能为空");
        }
        
        // 验证组织是否存在
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        // 验证是否有子组织
        List<Organization> children = organizationRepository.findByParentId(organizationId);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("组织下存在子组织，不能删除");
        }
        
        // 检查组织状态，只有停用状态的组织才能删除
        if (organization.get().getStatus() == OrganizationStatus.ACTIVE) {
            throw new IllegalArgumentException("组织处于活跃状态，不能删除。请先停用组织。");
        }
    }
    
    /**
     * 计算组织层级
     * @param parentOrganizationId 父组织ID
     * @return 组织层级
     */
    public Integer calculateOrganizationLevel(String parentOrganizationId) {
        if (!StringUtils.hasText(parentOrganizationId)) {
            return 1; // 根组织层级为1
        }
        
        Optional<Organization> parentOrg = organizationRepository.findById(parentOrganizationId);
        if (parentOrg.isPresent()) {
            return parentOrg.get().getOrganizationLevel() + 1;
        }
        
        return 1;
    }
    
    /**
     * 构建组织路径
     * @param parentOrganizationId 父组织ID
     * @param currentOrganizationId 当前组织ID
     * @return 组织路径
     */
    public String buildOrganizationPath(String parentOrganizationId, String currentOrganizationId) {
        if (!StringUtils.hasText(parentOrganizationId)) {
            return currentOrganizationId; // 根组织路径为自己的ID
        }
        
        Optional<Organization> parentOrg = organizationRepository.findById(parentOrganizationId);
        if (parentOrg.isPresent()) {
            return parentOrg.get().getOrganizationPath() + "/" + currentOrganizationId;
        }
        
        return currentOrganizationId;
    }
    
    /**
     * 生成组织编号
     * @param organizationType 组织类型
     * @param tenantId 租户ID
     * @return 组织编号
     */
    public String generateOrganizationCode(OrganizationType organizationType, String tenantId) {
        String prefix = "ORG";
        
        if (organizationType != null) {
            switch (organizationType) {
                case GOVERNMENT:
                    prefix = "GOV";
                    break;
                case ENTERPRISE_GROUP:
                    prefix = "EGP";
                    break;
                case INSTITUTION:
                    prefix = "INS";
                    break;
                case SOCIAL_ORGANIZATION:
                    prefix = "SOC";
                    break;
                case OTHER:
                default:
                    prefix = "ORG";
                    break;
            }
        }
        
        // 获取当前租户下的组织数量，用于生成序号
        long count = organizationRepository.countByTenantId(tenantId);
        String sequence = String.format("%06d", count + 1);
        
        return prefix + tenantId.substring(0, Math.min(3, tenantId.length())).toUpperCase() + sequence;
    }
    
    /**
     * 验证组织类型层级关系
     * @param parentType 父组织类型
     * @param childType 子组织类型
     * @return 是否符合层级关系
     */
    public boolean validateTypeHierarchy(OrganizationType parentType, OrganizationType childType) {
        if (parentType == null || childType == null) {
            return true; // 如果类型为空，不进行验证
        }
        
        // 定义组织类型层级关系规则
        switch (parentType) {
            case GOVERNMENT:
                // 政府机构下可以有政府机构、事业单位
                return childType == OrganizationType.GOVERNMENT || 
                       childType == OrganizationType.INSTITUTION;
            case ENTERPRISE_GROUP:
                // 企业集团下可以有企业集团、其他
                return childType == OrganizationType.ENTERPRISE_GROUP || 
                       childType == OrganizationType.OTHER;
            case INSTITUTION:
                // 事业单位下可以有事业单位、其他
                return childType == OrganizationType.INSTITUTION || 
                       childType == OrganizationType.OTHER;
            case SOCIAL_ORGANIZATION:
                // 社会团体下可以有社会团体、其他
                return childType == OrganizationType.SOCIAL_ORGANIZATION || 
                       childType == OrganizationType.OTHER;
            case OTHER:
                // 其他类型下可以有任何类型
                return true;
            default:
                return false;
        }
    }
    
    // 私有方法
    
    /**
     * 验证必填字段
     */
    private void validateRequiredFields(Organization organization, String tenantId) {
        if (!StringUtils.hasText(organization.getOrganizationName())) {
            throw new IllegalArgumentException("组织名称不能为空");
        }
        
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        if (organization.getOrganizationType() == null) {
            throw new IllegalArgumentException("组织类型不能为空");
        }
        
        // 验证字段长度
        if (organization.getOrganizationName().length() > 200) {
            throw new IllegalArgumentException("组织名称长度不能超过200个字符");
        }
        
        if (StringUtils.hasText(organization.getOrganizationShortName()) && 
            organization.getOrganizationShortName().length() > 100) {
            throw new IllegalArgumentException("组织简称长度不能超过100个字符");
        }
        
        if (StringUtils.hasText(organization.getAddress()) && 
            organization.getAddress().length() > 500) {
            throw new IllegalArgumentException("组织地址长度不能超过500个字符");
        }
    }
    
    /**
     * 验证格式规范
     */
    private void validateFormat(Organization organization) {
        // 验证组织代码格式
        if (StringUtils.hasText(organization.getOrganizationCode())) {
            if (!ORG_CODE_PATTERN.matcher(organization.getOrganizationCode()).matches()) {
                throw new IllegalArgumentException("组织代码格式不正确");
            }
        }
        
        // 验证联系电话格式
        if (StringUtils.hasText(organization.getContactPhone())) {
            if (!PHONE_PATTERN.matcher(organization.getContactPhone()).matches()) {
                throw new IllegalArgumentException("联系电话格式不正确");
            }
        }
        
        if (StringUtils.hasText(organization.getPrincipalTel())) {
            if (!PHONE_PATTERN.matcher(organization.getPrincipalTel()).matches()) {
                throw new IllegalArgumentException("负责人电话格式不正确");
            }
        }
        
        // 验证负责人姓名格式
        if (StringUtils.hasText(organization.getPrincipalName())) {
            if (organization.getPrincipalName().length() < 2 || organization.getPrincipalName().length() > 50) {
                throw new IllegalArgumentException("负责人姓名长度应在2-50个字符之间");
            }
        }
    }
    
    /**
     * 验证父组织存在性和合法性
     */
    private void validateParentOrganization(String parentOrganizationId, String tenantId) {
        Optional<Organization> parentOrg = organizationRepository.findById(parentOrganizationId);
        if (parentOrg.isEmpty()) {
            throw new IllegalArgumentException("父组织不存在: " + parentOrganizationId);
        }
        
        // 验证父组织状态
        if (parentOrg.get().getStatus() != OrganizationStatus.ACTIVE) {
            throw new IllegalArgumentException("父组织已停用，不能添加子组织");
        }
        
        // 验证父组织租户
        if (!tenantId.equals(parentOrg.get().getTenantId())) {
            throw new IllegalArgumentException("父组织与当前组织不属于同一租户");
        }
    }
    
    /**
     * 验证组织层级深度
     */
    private void validateOrganizationDepth(Organization organization) {
        int level = calculateOrganizationLevel(organization.getParentOrganizationId());
        
        // 限制组织层级深度不超过10级
        if (level > 10) {
            throw new IllegalArgumentException("组织层级不能超过10级");
        }
        
        organization.setOrganizationLevel(level);
    }
    
    /**
     * 验证状态变更合法性
     */
    private void validateStatusChange(Organization existingOrg, Organization newOrg) {
        OrganizationStatus oldStatus = existingOrg.getStatus();
        OrganizationStatus newStatus = newOrg.getStatus();
        
        if (oldStatus == newStatus) {
            return; // 状态未变更
        }
        
        // 定义状态转换规则
        switch (oldStatus) {
            case ACTIVE:
                // 活跃状态可以转为：停用
                if (newStatus != OrganizationStatus.INACTIVE) {
                    throw new IllegalArgumentException("组织处于活跃状态，只能转为停用状态");
                }
                break;
            case INACTIVE:
                // 停用状态可以转为：活跃
                if (newStatus != OrganizationStatus.ACTIVE) {
                    throw new IllegalArgumentException("组织处于停用状态，只能转为活跃状态");
                }
                break;
            default:
                throw new IllegalArgumentException("未知的组织状态: " + oldStatus);
        }
    }
    
    /**
     * 检查是否为子组织（包括子组织的子组织）
     */
    private boolean isDescendant(String ancestorOrgId, String descendantOrgId) {
        if (!StringUtils.hasText(descendantOrgId)) {
            return false;
        }
        
        Optional<Organization> org = organizationRepository.findById(descendantOrgId);
        if (org.isEmpty()) {
            return false;
        }
        
        String parentId = org.get().getParentOrganizationId();
        while (StringUtils.hasText(parentId)) {
            if (ancestorOrgId.equals(parentId)) {
                return true;
            }
            
            Optional<Organization> parentOrg = organizationRepository.findById(parentId);
            if (parentOrg.isEmpty()) {
                break;
            }
            
            parentId = parentOrg.get().getParentOrganizationId();
        }
        
        return false;
    }
}