package com.jiuxi.module.org.infra.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 组织模块缓存服务
 * 提供缓存管理和缓存失效处理，特别针对层级结构数据
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
public class OrgCacheService {
    
    private final CacheManager cacheManager;
    
    public OrgCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
     * 清除部门相关缓存
     * 当部门数据变更时，需要清除相关的缓存
     * 
     * @param deptId 部门ID
     * @param parentDeptId 父部门ID
     * @param tenantId 租户ID
     */
    public void evictDepartmentCache(String deptId, String parentDeptId, String tenantId) {
        // 清除单个部门缓存
        evictCache(OrgCacheConfig.DEPARTMENT_CACHE, deptId);
        
        // 清除部门树缓存
        evictCache(OrgCacheConfig.DEPT_TREE_CACHE, tenantId);
        
        // 清除父部门的子部门缓存
        if (StringUtils.hasText(parentDeptId)) {
            evictCache(OrgCacheConfig.DEPT_CHILDREN_CACHE, parentDeptId);
        }
        
        // 清除当前部门的子部门缓存
        evictCache(OrgCacheConfig.DEPT_CHILDREN_CACHE, deptId);
    }
    
    /**
     * 清除企业相关缓存
     * 
     * @param entId 企业ID
     * @param tenantId 租户ID
     */
    public void evictEnterpriseCache(String entId, String tenantId) {
        // 清除单个企业缓存
        evictCache(OrgCacheConfig.ENTERPRISE_CACHE, entId);
        
        // 清除租户级别的企业列表缓存
        evictCache(OrgCacheConfig.ENTERPRISE_CACHE, "tenant:" + tenantId);
    }
    
    /**
     * 清除组织相关缓存
     * 当组织数据变更时，需要清除相关的缓存
     * 
     * @param organizationId 组织ID
     * @param parentOrganizationId 父组织ID
     * @param tenantId 租户ID
     */
    public void evictOrganizationCache(String organizationId, String parentOrganizationId, String tenantId) {
        // 清除单个组织缓存
        evictCache(OrgCacheConfig.ORGANIZATION_CACHE, organizationId);
        
        // 清除组织树缓存
        evictCache(OrgCacheConfig.ORG_TREE_CACHE, tenantId);
        
        // 清除父组织的子组织缓存
        if (StringUtils.hasText(parentOrganizationId)) {
            evictCache(OrgCacheConfig.ORG_CHILDREN_CACHE, parentOrganizationId);
        }
        
        // 清除当前组织的子组织缓存
        evictCache(OrgCacheConfig.ORG_CHILDREN_CACHE, organizationId);
    }
    
    /**
     * 批量清除部门缓存
     * 
     * @param deptIds 部门ID列表
     * @param tenantId 租户ID
     */
    public void evictDepartmentCacheBatch(List<String> deptIds, String tenantId) {
        if (deptIds == null || deptIds.isEmpty()) {
            return;
        }
        
        // 批量清除部门缓存
        for (String deptId : deptIds) {
            evictCache(OrgCacheConfig.DEPARTMENT_CACHE, deptId);
            evictCache(OrgCacheConfig.DEPT_CHILDREN_CACHE, deptId);
        }
        
        // 清除部门树缓存
        evictCache(OrgCacheConfig.DEPT_TREE_CACHE, tenantId);
    }
    
    /**
     * 批量清除组织缓存
     * 
     * @param organizationIds 组织ID列表
     * @param tenantId 租户ID
     */
    public void evictOrganizationCacheBatch(List<String> organizationIds, String tenantId) {
        if (organizationIds == null || organizationIds.isEmpty()) {
            return;
        }
        
        // 批量清除组织缓存
        for (String organizationId : organizationIds) {
            evictCache(OrgCacheConfig.ORGANIZATION_CACHE, organizationId);
            evictCache(OrgCacheConfig.ORG_CHILDREN_CACHE, organizationId);
        }
        
        // 清除组织树缓存
        evictCache(OrgCacheConfig.ORG_TREE_CACHE, tenantId);
    }
    
    /**
     * 清除租户相关的所有缓存
     * 
     * @param tenantId 租户ID
     */
    public void evictTenantCache(String tenantId) {
        // 清除树缓存
        evictCache(OrgCacheConfig.DEPT_TREE_CACHE, tenantId);
        evictCache(OrgCacheConfig.ORG_TREE_CACHE, tenantId);
        
        // 清除租户级别的缓存
        evictCache(OrgCacheConfig.ENTERPRISE_CACHE, "tenant:" + tenantId);
        evictCache(OrgCacheConfig.DEPARTMENT_CACHE, "tenant:" + tenantId);
        evictCache(OrgCacheConfig.ORGANIZATION_CACHE, "tenant:" + tenantId);
    }
    
    /**
     * 清除层级结构缓存
     * 当层级结构发生变化时（如移动部门/组织），需要清除相关的层级缓存
     * 
     * @param entityType 实体类型 (dept/org)
     * @param tenantId 租户ID
     * @param affectedIds 受影响的ID列表
     */
    public void evictHierarchyCache(String entityType, String tenantId, Set<String> affectedIds) {
        if ("dept".equals(entityType)) {
            evictCache(OrgCacheConfig.DEPT_TREE_CACHE, tenantId);
            if (affectedIds != null) {
                for (String id : affectedIds) {
                    evictCache(OrgCacheConfig.DEPT_CHILDREN_CACHE, id);
                }
            }
        } else if ("org".equals(entityType)) {
            evictCache(OrgCacheConfig.ORG_TREE_CACHE, tenantId);
            if (affectedIds != null) {
                for (String id : affectedIds) {
                    evictCache(OrgCacheConfig.ORG_CHILDREN_CACHE, id);
                }
            }
        }
    }
    
    /**
     * 通用缓存清除方法
     * 
     * @param cacheName 缓存名称
     * @param key 缓存键
     */
    private void evictCache(String cacheName, String key) {
        if (!StringUtils.hasText(key)) {
            return;
        }
        
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
    
    /**
     * 清除整个缓存
     * 
     * @param cacheName 缓存名称
     */
    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
    
    /**
     * 预热部门树缓存
     * 在系统启动或数据变更后，预先加载常用的部门树数据
     * 
     * @param tenantId 租户ID
     */
    public void warmupDepartmentTreeCache(String tenantId) {
        // 这里可以调用实际的部门树查询方法来预热缓存
        // 由于需要避免循环依赖，实际的预热逻辑应该由Service层调用
    }
    
    /**
     * 预热组织树缓存
     * 
     * @param tenantId 租户ID
     */
    public void warmupOrganizationTreeCache(String tenantId) {
        // 这里可以调用实际的组织树查询方法来预热缓存
        // 由于需要避免循环依赖，实际的预热逻辑应该由Service层调用
    }
}