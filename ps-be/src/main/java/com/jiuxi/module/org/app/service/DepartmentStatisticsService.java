package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门统计服务
 * 专门负责部门相关的统计分析功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
@Transactional(readOnly = true)
public class DepartmentStatisticsService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentStatisticsService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
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
     * 统计部门的所有后代部门数量（递归）
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
     * 统计部门下的用户数量
     * @param deptId 部门ID
     * @return 用户数量
     */
    public long countUsers(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return departmentRepository.countUsersByDeptId(deptId);
    }
    
    /**
     * 获取租户的部门统计概览
     * @param tenantId 租户ID
     * @return 统计概览
     */
    public DepartmentOverview getTenantDepartmentOverview(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        List<Department> allDepartments = departmentRepository.findByTenantId(tenantId);
        
        long totalCount = allDepartments.size();
        long activeCount = allDepartments.stream()
                .mapToLong(dept -> dept.getStatus() == DepartmentStatus.ACTIVE ? 1 : 0)
                .sum();
        long inactiveCount = totalCount - activeCount;
        
        // 统计根部门数量
        long rootCount = allDepartments.stream()
                .mapToLong(dept -> dept.isRoot() ? 1 : 0)
                .sum();
        
        // 统计叶子部门数量
        long leafCount = allDepartments.stream()
                .mapToLong(dept -> dept.isLeaf() ? 1 : 0)
                .sum();
        
        // 计算最大层级
        int maxLevel = allDepartments.stream()
                .mapToInt(dept -> dept.getDeptLevel() != null ? dept.getDeptLevel() : 1)
                .max()
                .orElse(1);
        
        // 计算平均层级
        double avgLevel = allDepartments.stream()
                .mapToInt(dept -> dept.getDeptLevel() != null ? dept.getDeptLevel() : 1)
                .average()
                .orElse(1.0);
        
        return new DepartmentOverview(
                totalCount, activeCount, inactiveCount,
                rootCount, leafCount, maxLevel, avgLevel
        );
    }
    
    /**
     * 获取各层级部门数量分布
     * @param tenantId 租户ID
     * @return 层级分布Map（层级 -> 数量）
     */
    public Map<Integer, Long> getLevelDistribution(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        Map<Integer, Long> distribution = new HashMap<>();
        List<Department> allDepartments = departmentRepository.findByTenantId(tenantId);
        
        for (Department dept : allDepartments) {
            Integer level = dept.getDeptLevel() != null ? dept.getDeptLevel() : 1;
            distribution.merge(level, 1L, Long::sum);
        }
        
        return distribution;
    }
    
    /**
     * 获取部门的详细统计信息
     * @param deptId 部门ID
     * @return 详细统计信息
     */
    public DepartmentStatistics getDepartmentStatistics(String deptId) {
        if (!StringUtils.hasText(deptId)) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        
        // 获取基本信息
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("部门不存在: " + deptId));
        
        // 统计子部门
        long directChildren = departmentRepository.countDirectChildren(deptId);
        long allDescendants = departmentRepository.countAllDescendants(deptId);
        
        // 统计用户
        long userCount = departmentRepository.countUsersByDeptId(deptId);
        
        // 获取层级信息
        List<Department> ancestors = departmentRepository.findAncestors(deptId);
        int ancestorCount = ancestors.size();
        
        return new DepartmentStatistics(
                deptId, department.getDeptName(), department.getDeptLevel(),
                directChildren, allDescendants, userCount, ancestorCount,
                department.isRoot(), department.isLeaf()
        );
    }
    
    /**
     * 部门统计概览
     */
    public static class DepartmentOverview {
        private final long totalCount;
        private final long activeCount;
        private final long inactiveCount;
        private final long rootCount;
        private final long leafCount;
        private final int maxLevel;
        private final double avgLevel;
        
        public DepartmentOverview(long totalCount, long activeCount, long inactiveCount,
                                 long rootCount, long leafCount, int maxLevel, double avgLevel) {
            this.totalCount = totalCount;
            this.activeCount = activeCount;
            this.inactiveCount = inactiveCount;
            this.rootCount = rootCount;
            this.leafCount = leafCount;
            this.maxLevel = maxLevel;
            this.avgLevel = avgLevel;
        }
        
        public long getTotalCount() { return totalCount; }
        public long getActiveCount() { return activeCount; }
        public long getInactiveCount() { return inactiveCount; }
        public long getRootCount() { return rootCount; }
        public long getLeafCount() { return leafCount; }
        public int getMaxLevel() { return maxLevel; }
        public double getAvgLevel() { return avgLevel; }
    }
    
    /**
     * 部门详细统计
     */
    public static class DepartmentStatistics {
        private final String deptId;
        private final String deptName;
        private final Integer level;
        private final long directChildren;
        private final long allDescendants;
        private final long userCount;
        private final int ancestorCount;
        private final boolean isRoot;
        private final boolean isLeaf;
        
        public DepartmentStatistics(String deptId, String deptName, Integer level,
                                   long directChildren, long allDescendants, long userCount,
                                   int ancestorCount, boolean isRoot, boolean isLeaf) {
            this.deptId = deptId;
            this.deptName = deptName;
            this.level = level;
            this.directChildren = directChildren;
            this.allDescendants = allDescendants;
            this.userCount = userCount;
            this.ancestorCount = ancestorCount;
            this.isRoot = isRoot;
            this.isLeaf = isLeaf;
        }
        
        public String getDeptId() { return deptId; }
        public String getDeptName() { return deptName; }
        public Integer getLevel() { return level; }
        public long getDirectChildren() { return directChildren; }
        public long getAllDescendants() { return allDescendants; }
        public long getUserCount() { return userCount; }
        public int getAncestorCount() { return ancestorCount; }
        public boolean isRoot() { return isRoot; }
        public boolean isLeaf() { return isLeaf; }
    }
}