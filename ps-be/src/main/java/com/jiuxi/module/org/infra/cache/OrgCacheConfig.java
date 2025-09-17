package com.jiuxi.module.org.infra.cache;

import org.springframework.context.annotation.Configuration;

/**
 * 组织模块缓存配置
 * 为组织、企业、部门等实体提供缓存支持
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Configuration
public class OrgCacheConfig {
    
    /**
     * 缓存名称常量
     */
    public static final String DEPARTMENT_CACHE = "org:department";
    public static final String ENTERPRISE_CACHE = "org:enterprise";
    public static final String ORGANIZATION_CACHE = "org:organization";
    public static final String DEPT_TREE_CACHE = "org:dept:tree";
    public static final String ORG_TREE_CACHE = "org:org:tree";
    public static final String DEPT_CHILDREN_CACHE = "org:dept:children";
    public static final String ORG_CHILDREN_CACHE = "org:org:children";
}