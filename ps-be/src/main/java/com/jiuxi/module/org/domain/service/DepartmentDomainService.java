package com.jiuxi.module.org.domain.service;

import com.jiuxi.module.org.domain.entity.Department;
import com.jiuxi.module.org.domain.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 部门领域服务
 * 负责部门相关的业务规则和领域逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
public class DepartmentDomainService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentDomainService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    /**
     * 验证部门创建规则
     * @param department 待创建的部门
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForCreate(Department department, String tenantId) {
        // 验证必填字段
        if (!StringUtils.hasText(department.getDeptName())) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        // 验证部门名称长度
        if (department.getDeptName().length() > 100) {
            throw new IllegalArgumentException("部门名称长度不能超过100个字符");
        }
        
        // 验证部门名称唯一性
        if (departmentRepository.existsByName(department.getDeptName(), tenantId, null)) {
            throw new IllegalArgumentException("部门名称已存在: " + department.getDeptName());
        }
        
        // 验证父部门存在性
        if (StringUtils.hasText(department.getParentDeptId())) {
            Optional<Department> parentDept = departmentRepository.findById(department.getParentDeptId());
            if (parentDept.isEmpty()) {
                throw new IllegalArgumentException("父部门不存在: " + department.getParentDeptId());
            }
            
            // 验证父部门状态
            if (parentDept.get().getStatus() != DepartmentStatus.ACTIVE) {
                throw new IllegalArgumentException("父部门已停用，不能添加子部门");
            }
            
            // 验证父部门租户
            if (!tenantId.equals(parentDept.get().getTenantId())) {
                throw new IllegalArgumentException("父部门与当前部门不属于同一租户");
            }
        }
    }
    
    /**
     * 验证部门更新规则
     * @param department 待更新的部门
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForUpdate(Department department, String tenantId) {
        // 验证部门是否存在
        if (!StringUtils.hasText(department.getDeptId())) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        
        Optional<Department> existingDept = departmentRepository.findById(department.getDeptId());
        if (existingDept.isEmpty()) {
            throw new IllegalArgumentException("部门不存在: " + department.getDeptId());
        }
        
        // 验证必填字段
        if (!StringUtils.hasText(department.getDeptName())) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        
        // 验证部门名称长度
        if (department.getDeptName().length() > 100) {
            throw new IllegalArgumentException("部门名称长度不能超过100个字符");
        }
        
        // 验证部门名称唯一性（排除自己）
        if (departmentRepository.existsByName(department.getDeptName(), tenantId, department.getDeptId())) {
            throw new IllegalArgumentException("部门名称已存在: " + department.getDeptName());
        }
        
        // 验证父部门变更合法性
        if (StringUtils.hasText(department.getParentDeptId())) {
            // 不能将自己设为父部门
            if (department.getDeptId().equals(department.getParentDeptId())) {
                throw new IllegalArgumentException("部门不能将自己设为父部门");
            }
            
            // 验证父部门存在性
            Optional<Department> parentDept = departmentRepository.findById(department.getParentDeptId());
            if (parentDept.isEmpty()) {
                throw new IllegalArgumentException("父部门不存在: " + department.getParentDeptId());
            }
            
            // 验证不能将父部门设为自己的子部门（防止循环引用）
            if (isDescendant(department.getDeptId(), department.getParentDeptId())) {
                throw new IllegalArgumentException("不能将子部门设为父部门，会造成循环引用");
            }
        }
    }
    
    /**
     * 验证部门删除规则
     * @param deptId 部门ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForDelete(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        
        // 验证部门是否存在
        Optional<Department> department = departmentRepository.findById(deptId);
        if (department.isEmpty()) {
            throw new IllegalArgumentException("部门不存在: " + deptId);
        }
        
        // 验证是否有子部门
        List<Department> children = departmentRepository.findByParentId(deptId);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("部门下存在子部门，不能删除");
        }
        
        // 验证是否有用户
        long userCount = departmentRepository.countUsersByDeptId(deptId);
        if (userCount > 0) {
            throw new IllegalArgumentException("部门下存在用户，不能删除");
        }
    }
    
    /**
     * 计算部门层级
     * @param parentDeptId 父部门ID
     * @return 部门层级
     */
    public Integer calculateDeptLevel(String parentDeptId) {
        if (!StringUtils.hasText(parentDeptId)) {
            return 1; // 根部门层级为1
        }
        
        Optional<Department> parentDept = departmentRepository.findById(parentDeptId);
        if (parentDept.isPresent()) {
            return parentDept.get().getDeptLevel() + 1;
        }
        
        return 1;
    }
    
    /**
     * 构建部门路径
     * @param parentDeptId 父部门ID
     * @param currentDeptId 当前部门ID
     * @return 部门路径
     */
    public String buildDeptPath(String parentDeptId, String currentDeptId) {
        if (!StringUtils.hasText(parentDeptId)) {
            return currentDeptId; // 根部门路径为自己的ID
        }
        
        Optional<Department> parentDept = departmentRepository.findById(parentDeptId);
        if (parentDept.isPresent()) {
            return parentDept.get().getDeptPath() + "/" + currentDeptId;
        }
        
        return currentDeptId;
    }
    
    /**
     * 检查是否为子部门（包括子部门的子部门）
     * @param ancestorDeptId 祖先部门ID
     * @param descendantDeptId 后代部门ID
     * @return 是否为子部门
     */
    private boolean isDescendant(String ancestorDeptId, String descendantDeptId) {
        if (!StringUtils.hasText(descendantDeptId)) {
            return false;
        }
        
        Optional<Department> dept = departmentRepository.findById(descendantDeptId);
        if (dept.isEmpty()) {
            return false;
        }
        
        String parentId = dept.get().getParentDeptId();
        while (StringUtils.hasText(parentId)) {
            if (ancestorDeptId.equals(parentId)) {
                return true;
            }
            
            Optional<Department> parentDept = departmentRepository.findById(parentId);
            if (parentDept.isEmpty()) {
                break;
            }
            
            parentId = parentDept.get().getParentDeptId();
        }
        
        return false;
    }
}