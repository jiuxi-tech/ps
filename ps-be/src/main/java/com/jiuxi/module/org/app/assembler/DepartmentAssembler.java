package com.jiuxi.module.org.app.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.app.query.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.app.query.dto.DepartmentStatisticsDTO;
import com.jiuxi.module.org.app.service.DepartmentStatisticsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门装配器
 * 负责部门聚合根与DTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
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
     * 将部门聚合根转换为简化响应DTO（仅包含关键信息）
     * @param department 部门聚合根
     * @return 部门响应DTO
     */
    public DepartmentResponseDTO toSimpleResponseDTO(Department department) {
        if (department == null) {
            return null;
        }
        
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDeptId(department.getDeptId());
        dto.setDeptName(department.getDeptName());
        dto.setDeptSimpleName(department.getDeptSimpleName());
        dto.setParentDeptId(department.getParentDeptId());
        dto.setDeptLevel(department.getDeptLevel());
        dto.setStatus(department.getStatus());
        dto.setType(department.getType());
        dto.setTenantId(department.getTenantId());
        
        return dto;
    }
    
    /**
     * 批量转换部门列表为响应DTO列表
     * @param departments 部门列表
     * @return 部门响应DTO列表
     */
    public List<DepartmentResponseDTO> toResponseDTOList(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return departments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换部门列表为简化响应DTO列表
     * @param departments 部门列表
     * @return 部门响应DTO列表
     */
    public List<DepartmentResponseDTO> toSimpleResponseDTOList(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return departments.stream()
                .map(this::toSimpleResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将部门统计信息转换为DTO
     * @param statistics 部门统计信息
     * @return 部门统计DTO
     */
    public DepartmentStatisticsDTO toStatisticsDTO(DepartmentStatisticsService.DepartmentStatistics statistics) {
        if (statistics == null) {
            return null;
        }
        
        DepartmentStatisticsDTO dto = new DepartmentStatisticsDTO();
        dto.setDeptId(statistics.getDeptId());
        dto.setDeptName(statistics.getDeptName());
        dto.setLevel(statistics.getLevel());
        dto.setDirectChildren(statistics.getDirectChildren());
        dto.setAllDescendants(statistics.getAllDescendants());
        dto.setUserCount(statistics.getUserCount());
        dto.setAncestorCount(statistics.getAncestorCount());
        dto.setRoot(statistics.isRoot());
        dto.setLeaf(statistics.isLeaf());
        
        return dto;
    }
    
    /**
     * 将部门概览信息转换为DTO
     * @param overview 部门概览信息
     * @return 租户部门概览DTO
     */
    public TenantDepartmentOverviewDTO toOverviewDTO(DepartmentStatisticsService.DepartmentOverview overview) {
        if (overview == null) {
            return null;
        }
        
        TenantDepartmentOverviewDTO dto = new TenantDepartmentOverviewDTO();
        dto.setTotalCount(overview.getTotalCount());
        dto.setActiveCount(overview.getActiveCount());
        dto.setInactiveCount(overview.getInactiveCount());
        dto.setRootCount(overview.getRootCount());
        dto.setLeafCount(overview.getLeafCount());
        dto.setMaxLevel(overview.getMaxLevel());
        dto.setAvgLevel(overview.getAvgLevel());
        
        return dto;
    }
    
    /**
     * 租户部门概览DTO（内部类）
     */
    public static class TenantDepartmentOverviewDTO {
        private long totalCount;
        private long activeCount;
        private long inactiveCount;
        private long rootCount;
        private long leafCount;
        private int maxLevel;
        private double avgLevel;
        
        // Getters and Setters
        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
        
        public long getActiveCount() { return activeCount; }
        public void setActiveCount(long activeCount) { this.activeCount = activeCount; }
        
        public long getInactiveCount() { return inactiveCount; }
        public void setInactiveCount(long inactiveCount) { this.inactiveCount = inactiveCount; }
        
        public long getRootCount() { return rootCount; }
        public void setRootCount(long rootCount) { this.rootCount = rootCount; }
        
        public long getLeafCount() { return leafCount; }
        public void setLeafCount(long leafCount) { this.leafCount = leafCount; }
        
        public int getMaxLevel() { return maxLevel; }
        public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
        
        public double getAvgLevel() { return avgLevel; }
        public void setAvgLevel(double avgLevel) { this.avgLevel = avgLevel; }
    }
}