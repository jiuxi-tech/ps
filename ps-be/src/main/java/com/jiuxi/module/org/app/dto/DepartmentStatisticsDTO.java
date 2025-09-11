package com.jiuxi.module.org.app.dto;

import java.util.Map;

/**
 * 部门统计DTO
 * 用于返回部门统计信息
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class DepartmentStatisticsDTO {
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 部门层级
     */
    private Integer level;
    
    /**
     * 直接子部门数量
     */
    private long directChildren;
    
    /**
     * 所有后代部门数量
     */
    private long allDescendants;
    
    /**
     * 用户数量
     */
    private long userCount;
    
    /**
     * 祖先部门数量
     */
    private int ancestorCount;
    
    /**
     * 是否为根部门
     */
    private boolean isRoot;
    
    /**
     * 是否为叶子部门
     */
    private boolean isLeaf;
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public long getDirectChildren() {
        return directChildren;
    }
    
    public void setDirectChildren(long directChildren) {
        this.directChildren = directChildren;
    }
    
    public long getAllDescendants() {
        return allDescendants;
    }
    
    public void setAllDescendants(long allDescendants) {
        this.allDescendants = allDescendants;
    }
    
    public long getUserCount() {
        return userCount;
    }
    
    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
    
    public int getAncestorCount() {
        return ancestorCount;
    }
    
    public void setAncestorCount(int ancestorCount) {
        this.ancestorCount = ancestorCount;
    }
    
    public boolean isRoot() {
        return isRoot;
    }
    
    public void setRoot(boolean root) {
        isRoot = root;
    }
    
    public boolean isLeaf() {
        return isLeaf;
    }
    
    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}

/**
 * 租户部门概览DTO
 */
class TenantDepartmentOverviewDTO {
    
    /**
     * 部门总数
     */
    private long totalCount;
    
    /**
     * 激活部门数量
     */
    private long activeCount;
    
    /**
     * 停用部门数量
     */
    private long inactiveCount;
    
    /**
     * 根部门数量
     */
    private long rootCount;
    
    /**
     * 叶子部门数量
     */
    private long leafCount;
    
    /**
     * 最大层级
     */
    private int maxLevel;
    
    /**
     * 平均层级
     */
    private double avgLevel;
    
    /**
     * 层级分布（层级 -> 数量）
     */
    private Map<Integer, Long> levelDistribution;
    
    public long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    
    public long getActiveCount() {
        return activeCount;
    }
    
    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }
    
    public long getInactiveCount() {
        return inactiveCount;
    }
    
    public void setInactiveCount(long inactiveCount) {
        this.inactiveCount = inactiveCount;
    }
    
    public long getRootCount() {
        return rootCount;
    }
    
    public void setRootCount(long rootCount) {
        this.rootCount = rootCount;
    }
    
    public long getLeafCount() {
        return leafCount;
    }
    
    public void setLeafCount(long leafCount) {
        this.leafCount = leafCount;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    public double getAvgLevel() {
        return avgLevel;
    }
    
    public void setAvgLevel(double avgLevel) {
        this.avgLevel = avgLevel;
    }
    
    public Map<Integer, Long> getLevelDistribution() {
        return levelDistribution;
    }
    
    public void setLevelDistribution(Map<Integer, Long> levelDistribution) {
        this.levelDistribution = levelDistribution;
    }
}