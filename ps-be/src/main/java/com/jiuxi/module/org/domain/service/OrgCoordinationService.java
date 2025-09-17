package com.jiuxi.module.org.domain.service;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import com.jiuxi.module.org.domain.repo.EnterpriseRepository;
import com.jiuxi.module.org.domain.repo.OrganizationRepository;
import com.jiuxi.module.org.domain.policy.DataConsistencyPolicy;
import com.jiuxi.module.org.domain.policy.OrganizationalStructurePolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 组织协调服务
 * 负责跨聚合的业务逻辑协调和复杂业务流程处理
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Service
public class OrgCoordinationService {
    
    private final DepartmentRepository departmentRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final OrganizationRepository organizationRepository;
    
    private final DepartmentDomainService departmentDomainService;
    private final EnterpriseDomainService enterpriseDomainService;
    private final OrganizationDomainService organizationDomainService;
    
    private final OrganizationalStructurePolicy structurePolicy;
    private final DataConsistencyPolicy consistencyPolicy;
    
    public OrgCoordinationService(
            DepartmentRepository departmentRepository,
            EnterpriseRepository enterpriseRepository,
            OrganizationRepository organizationRepository,
            DepartmentDomainService departmentDomainService,
            EnterpriseDomainService enterpriseDomainService,
            OrganizationDomainService organizationDomainService,
            OrganizationalStructurePolicy structurePolicy,
            DataConsistencyPolicy consistencyPolicy) {
        this.departmentRepository = departmentRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.organizationRepository = organizationRepository;
        this.departmentDomainService = departmentDomainService;
        this.enterpriseDomainService = enterpriseDomainService;
        this.organizationDomainService = organizationDomainService;
        this.structurePolicy = structurePolicy;
        this.consistencyPolicy = consistencyPolicy;
    }
    
    /**
     * 创建完整的企业组织架构
     * @param enterprise 企业信息
     * @param organization 组织信息（可选）
     * @param rootDepartment 根部门信息
     * @param tenantId 租户ID
     * @return 创建的企业
     */
    @Transactional
    public Enterprise createEnterpriseStructure(Enterprise enterprise, Organization organization, 
                                              Department rootDepartment, String tenantId) {
        
        // 验证跨聚合数据一致性
        if (!consistencyPolicy.validateTenantConsistency(tenantId, enterprise, organization, rootDepartment)) {
            throw new IllegalArgumentException("跨聚合数据租户不一致");
        }
        
        // 1. 创建企业
        enterpriseDomainService.validateForCreate(enterprise, tenantId);
        enterprise.setTenantId(tenantId);
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        
        // 2. 创建组织（如果提供）
        Organization savedOrganization = null;
        if (organization != null) {
            organizationDomainService.validateForCreate(organization, tenantId);
            organization.setTenantId(tenantId);
            savedOrganization = organizationRepository.save(organization);
        }
        
        // 3. 创建根部门
        if (rootDepartment != null) {
            departmentDomainService.validateForCreate(rootDepartment, tenantId);
            rootDepartment.setTenantId(tenantId);
            rootDepartment.setAscnId(savedEnterprise.getEntId()); // 关联企业
            rootDepartment.setDeptLevel(1);
            rootDepartment.setDeptPath(rootDepartment.getDeptId());
            
            departmentRepository.save(rootDepartment);
        }
        
        return savedEnterprise;
    }
    
    /**
     * 批量迁移部门到新的企业
     * @param departmentIds 部门ID列表
     * @param targetEnterpriseId 目标企业ID
     * @param tenantId 租户ID
     * @return 迁移成功的部门数量
     */
    @Transactional
    public int migrateDepartmentsToEnterprise(List<String> departmentIds, String targetEnterpriseId, String tenantId) {
        
        // 验证目标企业存在
        Optional<Enterprise> targetEnterprise = enterpriseRepository.findById(targetEnterpriseId);
        if (targetEnterprise.isEmpty()) {
            throw new IllegalArgumentException("目标企业不存在: " + targetEnterpriseId);
        }
        
        // 验证企业状态
        if (targetEnterprise.get().getStatus() != EnterpriseStatus.ACTIVE) {
            throw new IllegalArgumentException("目标企业未激活，无法迁移部门");
        }
        
        int migratedCount = 0;
        
        for (String deptId : departmentIds) {
            Optional<Department> department = departmentRepository.findById(deptId);
            if (department.isEmpty()) {
                continue; // 跳过不存在的部门
            }
            
            Department dept = department.get();
            
            // 验证租户一致性
            if (!tenantId.equals(dept.getTenantId()) || !tenantId.equals(targetEnterprise.get().getTenantId())) {
                continue; // 跳过租户不匹配的部门
            }
            
            // 验证部门状态
            if (dept.getStatus() != DepartmentStatus.ACTIVE) {
                continue; // 跳过非激活状态的部门
            }
            
            // 检查企业部门数量限制
            long currentDeptCount = departmentRepository.countByTenantId(tenantId);
            if (!structurePolicy.validateEnterpriseStructure(targetEnterprise.get(), currentDeptCount + 1)) {
                throw new IllegalArgumentException("目标企业部门数量已达上限，无法继续迁移");
            }
            
            // 执行迁移
            dept.setAscnId(targetEnterpriseId);
            departmentRepository.save(dept);
            migratedCount++;
        }
        
        return migratedCount;
    }
    
    /**
     * 级联停用组织架构
     * @param organizationId 组织ID
     * @param cascadeToDepartments 是否级联到部门
     * @param cascadeToEnterprises 是否级联到企业
     * @return 操作结果
     */
    @Transactional
    public String cascadeDeactivateStructure(String organizationId, boolean cascadeToDepartments, boolean cascadeToEnterprises) {
        
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        Organization org = organization.get();
        StringBuilder result = new StringBuilder();
        
        // 1. 停用组织本身
        org.deactivate();
        organizationRepository.save(org);
        result.append("已停用组织: ").append(org.getOrganizationName()).append("\n");
        
        // 2. 级联停用子组织
        List<Organization> childOrganizations = organizationRepository.findByParentId(organizationId);
        for (Organization childOrg : childOrganizations) {
            if (childOrg.getStatus() == OrganizationStatus.ACTIVE) {
                childOrg.deactivate();
                organizationRepository.save(childOrg);
                result.append("已停用子组织: ").append(childOrg.getOrganizationName()).append("\n");
            }
        }
        
        // 3. 级联停用相关企业（如果需要）
        if (cascadeToEnterprises) {
            List<Enterprise> enterprises = enterpriseRepository.findByTenantId(org.getTenantId());
            for (Enterprise enterprise : enterprises) {
                if (enterprise.getStatus() == EnterpriseStatus.ACTIVE) {
                    enterprise.deactivate();
                    enterpriseRepository.save(enterprise);
                    result.append("已停用企业: ").append(enterprise.getEntFullName()).append("\n");
                }
            }
        }
        
        // 4. 级联停用相关部门（如果需要）
        if (cascadeToDepartments) {
            List<Department> departments = departmentRepository.findByTenantId(org.getTenantId());
            for (Department department : departments) {
                if (department.getStatus() == DepartmentStatus.ACTIVE) {
                    department.deactivate();
                    departmentRepository.save(department);
                    result.append("已停用部门: ").append(department.getDeptName()).append("\n");
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * 重建组织架构完整性
     * @param tenantId 租户ID
     * @return 修复报告
     */
    @Transactional
    public String rebuildStructureIntegrity(String tenantId) {
        StringBuilder report = new StringBuilder("组织架构完整性重建报告:\n");
        
        // 1. 修复部门路径和层级
        List<Department> allDepartments = departmentRepository.findByTenantId(tenantId);
        int pathFixedCount = 0;
        
        for (Department dept : allDepartments) {
            List<String> issues = consistencyPolicy.detectIntegrityIssues(dept);
            if (!issues.isEmpty()) {
                // 重新计算路径和层级
                String newPath = departmentDomainService.buildDeptPath(dept.getParentDeptId(), dept.getDeptId());
                Integer newLevel = departmentDomainService.calculateDeptLevel(dept.getParentDeptId());
                
                if (!newPath.equals(dept.getDeptPath()) || !newLevel.equals(dept.getDeptLevel())) {
                    dept.setDeptPath(newPath);
                    dept.setDeptLevel(newLevel);
                    departmentRepository.save(dept);
                    pathFixedCount++;
                }
            }
        }
        
        report.append("修复部门路径和层级: ").append(pathFixedCount).append("个\n");
        
        // 2. 修复组织路径和层级
        List<Organization> allOrganizations = organizationRepository.findByTenantId(tenantId);
        int orgPathFixedCount = 0;
        
        for (Organization org : allOrganizations) {
            String newPath = organizationDomainService.buildOrganizationPath(org.getParentOrganizationId(), org.getOrganizationId());
            Integer newLevel = organizationDomainService.calculateOrganizationLevel(org.getParentOrganizationId());
            
            if (!newPath.equals(org.getOrganizationPath()) || !newLevel.equals(org.getOrganizationLevel())) {
                org.setOrganizationPath(newPath);
                org.setOrganizationLevel(newLevel);
                organizationRepository.save(org);
                orgPathFixedCount++;
            }
        }
        
        report.append("修复组织路径和层级: ").append(orgPathFixedCount).append("个\n");
        
        // 3. 数据一致性评分
        int totalScore = 0;
        int scoreCount = 0;
        
        for (Department dept : allDepartments) {
            totalScore += consistencyPolicy.calculateConsistencyScore(dept);
            scoreCount++;
        }
        
        int avgScore = scoreCount > 0 ? totalScore / scoreCount : 0;
        report.append("平均数据一致性评分: ").append(avgScore).append("/100\n");
        
        return report.toString();
    }
    
    /**
     * 获取组织架构健康度报告
     * @param tenantId 租户ID
     * @return 健康度报告
     */
    public String getStructureHealthReport(String tenantId) {
        StringBuilder report = new StringBuilder("组织架构健康度报告:\n\n");
        
        // 1. 基本统计
        long orgCount = organizationRepository.countByTenantId(tenantId);
        long entCount = enterpriseRepository.countByTenantId(tenantId);
        long deptCount = departmentRepository.countByTenantId(tenantId);
        
        report.append("=== 基本统计 ===\n");
        report.append("组织数量: ").append(orgCount).append("\n");
        report.append("企业数量: ").append(entCount).append("\n");
        report.append("部门数量: ").append(deptCount).append("\n\n");
        
        // 2. 层级分析
        List<Department> departments = departmentRepository.findByTenantId(tenantId);
        int maxDepth = departments.stream().mapToInt(d -> d.getDeptLevel() != null ? d.getDeptLevel() : 0).max().orElse(0);
        double avgChildren = departments.isEmpty() ? 0 : 
            departments.stream().mapToLong(d -> departmentRepository.countDirectChildren(d.getDeptId())).average().orElse(0);
        
        report.append("=== 层级分析 ===\n");
        report.append("最大部门层级: ").append(maxDepth).append("\n");
        report.append("平均子部门数: ").append(String.format("%.2f", avgChildren)).append("\n");
        
        // 3. 架构建议
        String advice = structurePolicy.getStructureAdjustmentAdvice(maxDepth, (int) deptCount, avgChildren);
        report.append("架构建议: ").append(advice).append("\n\n");
        
        // 4. 数据质量
        List<String> qualityIssues = new java.util.ArrayList<>();
        for (Department dept : departments) {
            List<String> issues = consistencyPolicy.detectIntegrityIssues(dept);
            qualityIssues.addAll(issues);
        }
        
        report.append("=== 数据质量 ===\n");
        report.append("发现问题数量: ").append(qualityIssues.size()).append("\n");
        if (!qualityIssues.isEmpty()) {
            report.append("主要问题类型: ").append(String.join(", ", qualityIssues.subList(0, Math.min(5, qualityIssues.size())))).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * 验证跨聚合业务规则
     * @param department 部门
     * @param enterprise 企业
     * @param organization 组织
     * @return 验证结果
     */
    public boolean validateCrossAggregateBusiness(Department department, Enterprise enterprise, Organization organization) {
        
        // 1. 验证数据一致性
        if (!consistencyPolicy.validateCrossAggregateConsistency(department, enterprise, organization)) {
            return false;
        }
        
        // 2. 验证业务规则
        if (enterprise != null && enterprise.getStatus() != EnterpriseStatus.ACTIVE) {
            // 企业未激活时，其下部门也不能激活
            if (department != null && department.getStatus() == DepartmentStatus.ACTIVE) {
                return false;
            }
        }
        
        if (organization != null && organization.getStatus() != OrganizationStatus.ACTIVE) {
            // 组织未激活时，其下企业和部门也不能激活
            if ((enterprise != null && enterprise.getStatus() == EnterpriseStatus.ACTIVE) ||
                (department != null && department.getStatus() == DepartmentStatus.ACTIVE)) {
                return false;
            }
        }
        
        // 3. 验证架构规则
        if (department != null && enterprise != null) {
            long deptCount = departmentRepository.countByTenantId(department.getTenantId());
            if (!structurePolicy.validateEnterpriseStructure(enterprise, deptCount)) {
                return false;
            }
        }
        
        return true;
    }
}