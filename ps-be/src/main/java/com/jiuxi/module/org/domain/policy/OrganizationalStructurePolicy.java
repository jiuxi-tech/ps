package com.jiuxi.module.org.domain.policy;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.DepartmentType;
import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import org.springframework.stereotype.Component;

/**
 * 组织架构策略
 * 定义组织架构相关的业务策略和规则
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Component
public class OrganizationalStructurePolicy {
    
    /**
     * 最大组织层级深度
     */
    public static final int MAX_ORGANIZATION_DEPTH = 10;
    
    /**
     * 最大部门层级深度
     */
    public static final int MAX_DEPARTMENT_DEPTH = 8;
    
    /**
     * 部门下最大直接子部门数量
     */
    public static final int MAX_DIRECT_CHILDREN_PER_DEPARTMENT = 20;
    
    /**
     * 企业下最大部门数量
     */
    public static final int MAX_DEPARTMENTS_PER_ENTERPRISE = 1000;
    
    /**
     * 验证组织架构深度是否合规
     * @param level 当前层级
     * @param type 架构类型（organization/department）
     * @return 是否合规
     */
    public boolean validateStructureDepth(int level, String type) {
        switch (type.toLowerCase()) {
            case "organization":
                return level <= MAX_ORGANIZATION_DEPTH;
            case "department":
                return level <= MAX_DEPARTMENT_DEPTH;
            default:
                return false;
        }
    }
    
    /**
     * 验证部门结构是否合规
     * @param department 部门
     * @param directChildrenCount 直接子部门数量
     * @return 是否合规
     */
    public boolean validateDepartmentStructure(Department department, long directChildrenCount) {
        // 检查直接子部门数量
        if (directChildrenCount > MAX_DIRECT_CHILDREN_PER_DEPARTMENT) {
            return false;
        }
        
        // 检查部门层级
        if (department.getDeptLevel() != null && department.getDeptLevel() > MAX_DEPARTMENT_DEPTH) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 验证企业组织架构是否合规
     * @param enterprise 企业
     * @param totalDepartmentCount 企业下总部门数量
     * @return 是否合规
     */
    public boolean validateEnterpriseStructure(Enterprise enterprise, long totalDepartmentCount) {
        // 检查企业下部门总数
        return totalDepartmentCount <= MAX_DEPARTMENTS_PER_ENTERPRISE;
    }
    
    /**
     * 获取推荐的组织架构模式
     * @param organizationType 组织类型
     * @param scale 规模（small/medium/large）
     * @return 推荐的架构模式
     */
    public String getRecommendedStructurePattern(OrganizationType organizationType, String scale) {
        if (organizationType == null || scale == null) {
            return "flat"; // 默认扁平化架构
        }
        
        switch (organizationType) {
            case GOVERNMENT:
                return getGovernmentStructurePattern(scale);
            case ENTERPRISE_GROUP:
                return getEnterpriseGroupStructurePattern(scale);
            case INSTITUTION:
                return getInstitutionStructurePattern(scale);
            case SOCIAL_ORGANIZATION:
                return "flat"; // 社会团体通常采用扁平化架构
            default:
                return "hierarchical"; // 默认层级化架构
        }
    }
    
    /**
     * 验证部门类型层级关系
     * @param parentType 父部门类型
     * @param childType 子部门类型
     * @return 是否符合层级关系
     */
    public boolean validateDepartmentTypeHierarchy(DepartmentType parentType, DepartmentType childType) {
        if (parentType == null || childType == null) {
            return true;
        }
        
        // 定义部门类型层级关系规则
        switch (parentType) {
            case COMPANY:
                // 公司下可以有部门、小组
                return childType == DepartmentType.DEPARTMENT || childType == DepartmentType.GROUP;
            case DEPARTMENT:
                // 部门下可以有部门、小组、虚拟部门
                return childType == DepartmentType.DEPARTMENT || 
                       childType == DepartmentType.GROUP || 
                       childType == DepartmentType.VIRTUAL;
            case GROUP:
                // 小组下只能有小组
                return childType == DepartmentType.GROUP;
            case VIRTUAL:
                // 虚拟部门下可以有任何类型
                return true;
            default:
                return false;
        }
    }
    
    /**
     * 计算推荐的管理幅度
     * @param departmentType 部门类型
     * @param departmentLevel 部门层级
     * @return 推荐的管理幅度（直接下属数量）
     */
    public int getRecommendedManagementSpan(DepartmentType departmentType, int departmentLevel) {
        // 层级越高，管理幅度越小
        int baseSpan = Math.max(3, 15 - departmentLevel * 2);
        
        // 根据部门类型调整
        switch (departmentType) {
            case COMPANY:
                return Math.min(baseSpan, 8); // 公司级管理幅度相对较小
            case DEPARTMENT:
                return Math.min(baseSpan, 12); // 部门管理幅度中等
            case GROUP:
                return Math.min(baseSpan, 10); // 小组管理幅度中等
            case VIRTUAL:
                return Math.min(baseSpan, 15); // 虚拟部门管理幅度可以较大
            default:
                return baseSpan;
        }
    }
    
    /**
     * 检查是否需要架构调整
     * @param currentDepth 当前层级深度
     * @param totalNodes 总节点数
     * @param avgChildrenPerNode 平均每节点子节点数
     * @return 调整建议
     */
    public String getStructureAdjustmentAdvice(int currentDepth, int totalNodes, double avgChildrenPerNode) {
        if (currentDepth > 6 && avgChildrenPerNode < 3) {
            return "架构过深且分支较少，建议减少层级，扁平化部分结构";
        } else if (currentDepth < 3 && avgChildrenPerNode > 10) {
            return "架构过于扁平且分支过多，建议增加中间层级";
        } else if (totalNodes > 100 && currentDepth < 4) {
            return "组织规模较大但层级较少，建议适当增加管理层级";
        } else if (totalNodes < 20 && currentDepth > 4) {
            return "组织规模较小但层级过多，建议简化组织架构";
        } else {
            return "当前组织架构基本合理";
        }
    }
    
    // 私有方法
    
    private String getGovernmentStructurePattern(String scale) {
        switch (scale.toLowerCase()) {
            case "small":
                return "functional"; // 职能型架构
            case "medium":
                return "divisional"; // 事业部制架构
            case "large":
                return "matrix"; // 矩阵型架构
            default:
                return "hierarchical";
        }
    }
    
    private String getEnterpriseGroupStructurePattern(String scale) {
        switch (scale.toLowerCase()) {
            case "small":
                return "flat"; // 扁平化架构
            case "medium":
                return "divisional"; // 事业部制架构
            case "large":
                return "holding"; // 控股型架构
            default:
                return "hierarchical";
        }
    }
    
    private String getInstitutionStructurePattern(String scale) {
        switch (scale.toLowerCase()) {
            case "small":
                return "functional"; // 职能型架构
            case "medium":
                return "professional"; // 专业型架构
            case "large":
                return "federal"; // 联邦型架构
            default:
                return "hierarchical";
        }
    }
}