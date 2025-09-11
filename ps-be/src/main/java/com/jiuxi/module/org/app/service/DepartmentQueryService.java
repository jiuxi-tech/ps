package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.app.assembler.DepartmentAssembler;
import com.jiuxi.module.org.app.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.domain.entity.Department;
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