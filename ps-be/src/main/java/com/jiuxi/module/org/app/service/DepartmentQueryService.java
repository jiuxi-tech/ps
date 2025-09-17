package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.app.assembler.DepartmentAssembler;
import com.jiuxi.module.org.app.query.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 部门查询服务
 * 专门负责部门的复杂查询和统计功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentAssembler departmentAssembler;
    
    public DepartmentQueryService(DepartmentRepository departmentRepository,
                                 DepartmentAssembler departmentAssembler) {
        this.departmentRepository = departmentRepository;
        this.departmentAssembler = departmentAssembler;
    }
    
    /**
     * 根据左右值编码查询子部门（高效树形查询）
     * @param leftValue 左值
     * @param rightValue 右值
     * @param tenantId 租户ID
     * @return 子部门列表
     */
    public List<DepartmentResponseDTO> findByLeftRightValue(Integer leftValue, Integer rightValue, String tenantId) {
        if (leftValue == null || rightValue == null || leftValue >= rightValue) {
            throw new IllegalArgumentException("左右值参数无效");
        }
        
        List<Department> departments = departmentRepository.findByLeftRightValue(leftValue, rightValue, tenantId);
        return departments.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询部门的完整祖先链
     * @param deptId 部门ID
     * @return 祖先部门列表（从顶级到直接父部门）
     */
    public List<DepartmentResponseDTO> findAncestorChain(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        
        List<Department> ancestors = departmentRepository.findAncestors(deptId);
        return ancestors.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询部门的完整子树
     * @param deptId 部门ID
     * @param includeInactive 是否包含停用部门
     * @return 完整子树（包含多层级）
     */
    public List<DepartmentResponseDTO> findCompleteSubTree(String deptId, boolean includeInactive) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        
        List<Department> descendants = departmentRepository.findDescendants(deptId, includeInactive);
        return descendants.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询指定层级的所有部门
     * @param level 部门层级
     * @param tenantId 租户ID
     * @return 指定层级的部门列表
     */
    public List<DepartmentResponseDTO> findDepartmentsByLevel(Integer level, String tenantId) {
        if (level == null || level < 1) {
            throw new IllegalArgumentException("层级参数无效，必须大于0");
        }
        
        List<Department> departments = departmentRepository.findByLevel(level, tenantId);
        return departments.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询租户的最大部门层级
     * @param tenantId 租户ID
     * @return 最大层级
     */
    public Integer findMaxDepartmentLevel(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        List<Department> allDepts = departmentRepository.findByTenantId(tenantId);
        return allDepts.stream()
                .mapToInt(dept -> dept.getDeptLevel() != null ? dept.getDeptLevel() : 1)
                .max()
                .orElse(1);
    }
    
    /**
     * 验证部门移动操作是否合法
     * @param deptId 要移动的部门ID
     * @param targetParentId 目标父部门ID
     * @return 验证结果和原因
     */
    public DepartmentMoveValidationResult validateDepartmentMove(String deptId, String targetParentId) {
        if (!StringUtils.hasText(deptId)) {
            return new DepartmentMoveValidationResult(false, "部门ID不能为空");
        }
        
        // 检查部门是否存在
        Optional<Department> deptOpt = departmentRepository.findById(deptId);
        if (deptOpt.isEmpty()) {
            return new DepartmentMoveValidationResult(false, "部门不存在");
        }
        
        // 如果目标父部门为空，表示移动到根级别
        if (!StringUtils.hasText(targetParentId)) {
            return new DepartmentMoveValidationResult(true, "可以移动到根级别");
        }
        
        // 检查目标父部门是否存在
        Optional<Department> targetParentOpt = departmentRepository.findById(targetParentId);
        if (targetParentOpt.isEmpty()) {
            return new DepartmentMoveValidationResult(false, "目标父部门不存在");
        }
        
        // 检查是否会形成循环引用（部门不能移动到自己或自己的子部门下）
        if (deptId.equals(targetParentId)) {
            return new DepartmentMoveValidationResult(false, "不能将部门移动到自己下面");
        }
        
        boolean isDescendant = departmentRepository.isAncestor(deptId, targetParentId);
        if (isDescendant) {
            return new DepartmentMoveValidationResult(false, "不能将部门移动到自己的子部门下面");
        }
        
        return new DepartmentMoveValidationResult(true, "可以移动");
    }
    
    /**
     * 根据ID查询部门
     * @param deptId 部门ID
     * @return 部门响应
     */
    public Optional<DepartmentResponseDTO> findDepartmentById(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.findById(deptId)
                .map(departmentAssembler::toResponseDTO);
    }
    
    /**
     * 根据ID查询部门（兼容控制器调用）
     * @param deptId 部门ID
     * @return 部门响应
     */
    public DepartmentResponseDTO getDepartmentById(String deptId) {
        return findDepartmentById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("部门不存在: " + deptId));
    }
    
    /**
     * 查询部门树
     * @param tenantId 租户ID
     * @return 部门树
     */
    public List<DepartmentResponseDTO> findDepartmentTree(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Department> departments = departmentRepository.findDepartmentTree(tenantId);
        return departments.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询部门树（兼容控制器调用）
     * @param tenantId 租户ID
     * @return 部门树
     */
    public List<DepartmentResponseDTO> getDepartmentTree(String tenantId) {
        return findDepartmentTree(tenantId);
    }
    
    /**
     * 根据父部门查询子部门
     * @param parentDeptId 父部门ID
     * @return 子部门列表
     */
    public List<DepartmentResponseDTO> findChildDepartments(String parentDeptId) {
        if (!StringUtils.hasText(parentDeptId)) {
            throw new IllegalArgumentException("父部门ID不能为空");
        }
        List<Department> children = departmentRepository.findByParentId(parentDeptId);
        return children.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据父部门查询子部门（兼容控制器调用）
     * @param parentDeptId 父部门ID
     * @return 子部门列表
     */
    public List<DepartmentResponseDTO> getChildDepartments(String parentDeptId) {
        return findChildDepartments(parentDeptId);
    }
    
    /**
     * 获取根部门列表
     * @param tenantId 租户ID
     * @return 根部门列表
     */
    public List<DepartmentResponseDTO> getRootDepartments(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Department> rootDepartments = departmentRepository.findRootDepartments();
        return rootDepartments.stream()
                .filter(dept -> tenantId.equals(dept.getTenantId()))
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取部门用户数量
     * @param deptId 部门ID
     * @return 用户数量
     */
    public long getDepartmentUserCount(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.countUsersByDeptId(deptId);
    }
    
    /**
     * 查询部门的所有祖先部门
     * @param deptId 部门ID
     * @return 祖先部门列表
     */
    public List<DepartmentResponseDTO> findAncestorDepartments(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        List<Department> ancestors = departmentRepository.findAncestors(deptId);
        return ancestors.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询部门的所有后代部门
     * @param deptId 部门ID
     * @param includeInactive 是否包含停用部门
     * @return 后代部门列表
     */
    public List<DepartmentResponseDTO> findDescendantDepartments(String deptId, boolean includeInactive) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        List<Department> descendants = departmentRepository.findDescendants(deptId, includeInactive);
        return descendants.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 统计部门的直接子部门数量
     * @param deptId 部门ID
     * @return 直接子部门数量
     */
    public long countDirectChildren(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.countDirectChildren(deptId);
    }
    
    /**
     * 统计部门的所有后代部门数量
     * @param deptId 部门ID
     * @return 所有后代部门数量
     */
    public long countAllDescendants(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.countAllDescendants(deptId);
    }
    
    /**
     * 检查部门是否为另一个部门的祖先
     * @param ancestorDeptId 祖先部门ID
     * @param descendantDeptId 后代部门ID
     * @return 是否为祖先关系
     */
    public boolean isAncestorDepartment(String ancestorDeptId, String descendantDeptId) {
        if (!StringUtils.hasText(ancestorDeptId) || !StringUtils.hasText(descendantDeptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.isAncestor(ancestorDeptId, descendantDeptId);
    }
    
    /**
     * 批量查询部门及其后代部门
     * @param deptIds 部门ID列表
     * @param includeDescendants 是否包含后代部门
     * @return 部门列表
     */
    public List<DepartmentResponseDTO> findDepartmentsWithChildren(List<String> deptIds, boolean includeDescendants) {
        if (deptIds == null || deptIds.isEmpty()) {
            throw new IllegalArgumentException("部门ID列表不能为空");
        }
        List<Department> departments = departmentRepository.findDepartmentsWithChildren(deptIds, includeDescendants);
        return departments.stream()
                .map(departmentAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据部门编号查询部门
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @return 部门响应
     */
    public Optional<DepartmentResponseDTO> findDepartmentByNo(String deptNo, String tenantId) {
        if (!StringUtils.hasText(deptNo) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("部门编号和租户ID不能为空");
        }
        return departmentRepository.findByDeptNo(deptNo, tenantId)
                .map(departmentAssembler::toResponseDTO);
    }
    
    /**
     * 检查部门编号是否存在
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @param excludeDeptId 排除的部门ID
     * @return 是否存在
     */
    public boolean existsByDeptNo(String deptNo, String tenantId, String excludeDeptId) {
        if (!StringUtils.hasText(deptNo) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("部门编号和租户ID不能为空");
        }
        return departmentRepository.existsByDeptNo(deptNo, tenantId, excludeDeptId);
    }
    
    /**
     * 部门移动验证结果
     */
    public static class DepartmentMoveValidationResult {
        private final boolean valid;
        private final String message;
        
        public DepartmentMoveValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}