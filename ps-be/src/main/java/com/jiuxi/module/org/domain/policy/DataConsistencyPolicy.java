package com.jiuxi.module.org.domain.policy;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.aggregate.Organization;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据一致性策略
 * 定义跨聚合的数据一致性规则和验证策略
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Component
public class DataConsistencyPolicy {
    
    // 租户ID格式验证
    private static final Pattern TENANT_ID_PATTERN = Pattern.compile("^[A-Z0-9]{6,20}$");
    
    // ID格式验证（UUID格式或自定义格式）
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{8,64}$");
    
    /**
     * 验证租户一致性
     * @param tenantId 租户ID
     * @param entities 实体列表
     * @return 是否一致
     */
    public boolean validateTenantConsistency(String tenantId, Object... entities) {
        if (!StringUtils.hasText(tenantId)) {
            return false;
        }
        
        // 验证租户ID格式
        if (!TENANT_ID_PATTERN.matcher(tenantId).matches()) {
            return false;
        }
        
        // 验证所有实体的租户ID一致性
        for (Object entity : entities) {
            String entityTenantId = extractTenantId(entity);
            if (!tenantId.equals(entityTenantId)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 验证部门路径一致性
     * @param department 部门
     * @return 是否一致
     */
    public boolean validateDepartmentPathConsistency(Department department) {
        if (department == null) {
            return false;
        }
        
        String deptPath = department.getDeptPath();
        Integer deptLevel = department.getDeptLevel();
        
        if (!StringUtils.hasText(deptPath) || deptLevel == null) {
            return false;
        }
        
        // 验证路径和层级的一致性
        String[] pathSegments = deptPath.split("/");
        return pathSegments.length == deptLevel;
    }
    
    /**
     * 验证部门嵌套集合一致性
     * @param department 部门
     * @return 是否一致
     */
    public boolean validateNestedSetConsistency(Department department) {
        if (department == null) {
            return false;
        }
        
        Integer leftValue = department.getLeftValue();
        Integer rightValue = department.getRightValue();
        
        if (leftValue == null || rightValue == null) {
            return true; // 如果没有设置左右值，不进行验证
        }
        
        // 左值必须小于右值
        if (leftValue >= rightValue) {
            return false;
        }
        
        // 右值减左值必须大于等于1（如果有子节点，差值会更大）
        return (rightValue - leftValue) >= 1;
    }
    
    /**
     * 验证组织层级一致性
     * @param organization 组织
     * @param parentOrganization 父组织
     * @return 是否一致
     */
    public boolean validateOrganizationHierarchyConsistency(Organization organization, Organization parentOrganization) {
        if (organization == null) {
            return false;
        }
        
        if (parentOrganization == null) {
            // 如果没有父组织，应该是根组织（层级为1）
            return organization.getOrganizationLevel() == 1;
        }
        
        // 子组织的层级应该等于父组织层级+1
        return organization.getOrganizationLevel() == (parentOrganization.getOrganizationLevel() + 1);
    }
    
    /**
     * 验证跨聚合引用一致性
     * @param department 部门
     * @param enterprise 所属企业
     * @param organization 所属组织
     * @return 是否一致
     */
    public boolean validateCrossAggregateConsistency(Department department, Enterprise enterprise, Organization organization) {
        if (department == null) {
            return false;
        }
        
        // 验证租户一致性
        String deptTenantId = department.getTenantId();
        
        if (enterprise != null) {
            if (!deptTenantId.equals(enterprise.getTenantId())) {
                return false;
            }
        }
        
        if (organization != null) {
            if (!deptTenantId.equals(organization.getTenantId())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 验证ID格式一致性
     * @param id ID值
     * @param type ID类型（用于日志）
     * @return 是否符合格式
     */
    public boolean validateIdFormat(String id, String type) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        
        return ID_PATTERN.matcher(id).matches();
    }
    
    /**
     * 验证状态一致性
     * @param enabledFlag 启用标志
     * @param activedFlag 有效标志
     * @param statusEnum 状态枚举
     * @return 是否一致
     */
    public boolean validateStatusConsistency(Integer enabledFlag, Integer activedFlag, Enum<?> statusEnum) {
        if (statusEnum == null) {
            return enabledFlag == null && activedFlag == null;
        }
        
        String statusName = statusEnum.name();
        
        // 根据状态枚举验证标志位的一致性
        switch (statusName) {
            case "ACTIVE":
                return Integer.valueOf(1).equals(enabledFlag) && Integer.valueOf(1).equals(activedFlag);
            case "INACTIVE":
                return Integer.valueOf(0).equals(enabledFlag) && Integer.valueOf(1).equals(activedFlag);
            case "DELETED":
            case "CANCELLED":
                return Integer.valueOf(0).equals(activedFlag);
            default:
                return true; // 未知状态不做验证
        }
    }
    
    /**
     * 检测数据完整性问题
     * @param department 部门
     * @return 问题列表
     */
    public List<String> detectIntegrityIssues(Department department) {
        List<String> issues = new java.util.ArrayList<>();
        
        if (department == null) {
            issues.add("部门对象为空");
            return issues;
        }
        
        // 检查必填字段
        if (!StringUtils.hasText(department.getDeptId())) {
            issues.add("部门ID为空");
        }
        
        if (!StringUtils.hasText(department.getDeptName())) {
            issues.add("部门名称为空");
        }
        
        if (!StringUtils.hasText(department.getTenantId())) {
            issues.add("租户ID为空");
        }
        
        // 检查路径一致性
        if (!validateDepartmentPathConsistency(department)) {
            issues.add("部门路径与层级不一致");
        }
        
        // 检查嵌套集合一致性
        if (!validateNestedSetConsistency(department)) {
            issues.add("部门嵌套集合左右值不一致");
        }
        
        // 检查状态一致性
        if (!validateStatusConsistency(department.getEnabled(), null, department.getStatus())) {
            issues.add("部门状态标志位不一致");
        }
        
        // 检查ID格式
        if (!validateIdFormat(department.getDeptId(), "department")) {
            issues.add("部门ID格式不正确");
        }
        
        if (StringUtils.hasText(department.getParentDeptId()) && 
            !validateIdFormat(department.getParentDeptId(), "parentDepartment")) {
            issues.add("父部门ID格式不正确");
        }
        
        return issues;
    }
    
    /**
     * 检测企业数据完整性问题
     * @param enterprise 企业
     * @return 问题列表
     */
    public List<String> detectEnterpriseIntegrityIssues(Enterprise enterprise) {
        List<String> issues = new java.util.ArrayList<>();
        
        if (enterprise == null) {
            issues.add("企业对象为空");
            return issues;
        }
        
        // 检查必填字段
        if (!StringUtils.hasText(enterprise.getEntId())) {
            issues.add("企业ID为空");
        }
        
        if (!StringUtils.hasText(enterprise.getEntFullName())) {
            issues.add("企业名称为空");
        }
        
        if (!StringUtils.hasText(enterprise.getTenantId())) {
            issues.add("租户ID为空");
        }
        
        // 检查状态一致性
        if (!validateStatusConsistency(enterprise.getEnabled(), enterprise.getActived(), enterprise.getStatus())) {
            issues.add("企业状态标志位不一致");
        }
        
        // 检查ID格式
        if (!validateIdFormat(enterprise.getEntId(), "enterprise")) {
            issues.add("企业ID格式不正确");
        }
        
        return issues;
    }
    
    /**
     * 计算数据一致性评分
     * @param department 部门
     * @return 评分（0-100）
     */
    public int calculateConsistencyScore(Department department) {
        if (department == null) {
            return 0;
        }
        
        int totalChecks = 8;
        int passedChecks = 0;
        
        // 各项检查
        if (StringUtils.hasText(department.getDeptId())) passedChecks++;
        if (StringUtils.hasText(department.getDeptName())) passedChecks++;
        if (StringUtils.hasText(department.getTenantId())) passedChecks++;
        if (validateDepartmentPathConsistency(department)) passedChecks++;
        if (validateNestedSetConsistency(department)) passedChecks++;
        if (validateStatusConsistency(department.getEnabled(), null, department.getStatus())) passedChecks++;
        if (validateIdFormat(department.getDeptId(), "department")) passedChecks++;
        if (department.getParentDeptId() == null || validateIdFormat(department.getParentDeptId(), "parent")) passedChecks++;
        
        return (passedChecks * 100) / totalChecks;
    }
    
    // 私有方法
    
    /**
     * 从实体中提取租户ID
     */
    private String extractTenantId(Object entity) {
        if (entity instanceof Department) {
            return ((Department) entity).getTenantId();
        } else if (entity instanceof Enterprise) {
            return ((Enterprise) entity).getTenantId();
        } else if (entity instanceof Organization) {
            return ((Organization) entity).getTenantId();
        }
        return null;
    }
}