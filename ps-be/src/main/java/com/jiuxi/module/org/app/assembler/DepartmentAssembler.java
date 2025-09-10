package com.jiuxi.module.org.app.assembler;

import com.jiuxi.module.org.domain.entity.Department;
import com.jiuxi.module.org.app.dto.DepartmentResponseDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门装配器
 * 负责部门领域对象与DTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Component
public class DepartmentAssembler {
    
    /**
     * 将部门聚合根转换为响应DTO
     * @param department 部门聚合根
     * @return 部门响应DTO
     */
    public DepartmentResponseDTO toResponseDTO(Department department) {
        if (department == null) {
            return null;
        }
        
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDeptId(department.getDeptId());
        dto.setDeptName(department.getDeptName());
        dto.setDeptSimpleName(department.getDeptSimpleName());
        dto.setDeptFullName(department.getDeptFullName());
        dto.setParentDeptId(department.getParentDeptId());
        dto.setDeptPath(department.getDeptPath());
        dto.setDeptLevel(department.getDeptLevel());
        dto.setOrderIndex(department.getOrderIndex());
        dto.setStatus(department.getStatus());
        dto.setType(department.getType());
        dto.setManagerId(department.getManagerId());
        dto.setContactPhone(department.getContactPhone());
        dto.setAddress(department.getAddress());
        dto.setDescription(department.getDescription());
        dto.setCreator(department.getCreator());
        dto.setCreateTime(department.getCreateTime());
        dto.setUpdator(department.getUpdator());
        dto.setUpdateTime(department.getUpdateTime());
        dto.setTenantId(department.getTenantId());
        
        // 转换子部门列表
        if (department.getChildren() != null && !department.getChildren().isEmpty()) {
            List<DepartmentResponseDTO> childrenDTOs = department.getChildren().stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            dto.setChildren(childrenDTOs);
        } else {
            dto.setChildren(new ArrayList<>());
        }
        
        return dto;
    }
    
    /**
     * 批量转换部门列表
     * @param departments 部门列表
     * @return 部门响应DTO列表
     */
    public List<DepartmentResponseDTO> toResponseDTOs(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }
        
        return departments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}