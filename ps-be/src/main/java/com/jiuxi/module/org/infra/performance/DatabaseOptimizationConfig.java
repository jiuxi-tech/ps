package com.jiuxi.module.org.infra.performance;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库性能优化配置
 * 提供数据库层面的性能优化参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Configuration
@ConfigurationProperties(prefix = "jiuxi.org.database.optimization")
public class DatabaseOptimizationConfig {
    
    /**
     * 批量操作配置
     */
    private BatchConfig batch = new BatchConfig();
    
    /**
     * 查询优化配置
     */
    private QueryConfig query = new QueryConfig();
    
    /**
     * 连接池优化配置
     */
    private ConnectionPoolConfig connectionPool = new ConnectionPoolConfig();
    
    /**
     * 索引提示配置
     */
    private IndexHintConfig indexHint = new IndexHintConfig();
    
    public static class BatchConfig {
        /**
         * 批量插入大小
         */
        private int insertBatchSize = 500;
        
        /**
         * 批量更新大小
         */
        private int updateBatchSize = 200;
        
        /**
         * 批量删除大小
         */
        private int deleteBatchSize = 1000;
        
        /**
         * 批量查询大小
         */
        private int selectBatchSize = 1000;
        
        // Getters and Setters
        public int getInsertBatchSize() { return insertBatchSize; }
        public void setInsertBatchSize(int insertBatchSize) { this.insertBatchSize = insertBatchSize; }
        
        public int getUpdateBatchSize() { return updateBatchSize; }
        public void setUpdateBatchSize(int updateBatchSize) { this.updateBatchSize = updateBatchSize; }
        
        public int getDeleteBatchSize() { return deleteBatchSize; }
        public void setDeleteBatchSize(int deleteBatchSize) { this.deleteBatchSize = deleteBatchSize; }
        
        public int getSelectBatchSize() { return selectBatchSize; }
        public void setSelectBatchSize(int selectBatchSize) { this.selectBatchSize = selectBatchSize; }
    }
    
    public static class QueryConfig {
        /**
         * 树查询最大深度
         */
        private int maxTreeDepth = 10;
        
        /**
         * 分页查询最大大小
         */
        private int maxPageSize = 1000;
        
        /**
         * 查询超时时间（秒）
         */
        private int queryTimeout = 30;
        
        /**
         * 是否启用查询结果缓存
         */
        private boolean enableResultCache = true;
        
        /**
         * 慢查询阈值（毫秒）
         */
        private long slowQueryThreshold = 1000;
        
        // Getters and Setters
        public int getMaxTreeDepth() { return maxTreeDepth; }
        public void setMaxTreeDepth(int maxTreeDepth) { this.maxTreeDepth = maxTreeDepth; }
        
        public int getMaxPageSize() { return maxPageSize; }
        public void setMaxPageSize(int maxPageSize) { this.maxPageSize = maxPageSize; }
        
        public int getQueryTimeout() { return queryTimeout; }
        public void setQueryTimeout(int queryTimeout) { this.queryTimeout = queryTimeout; }
        
        public boolean isEnableResultCache() { return enableResultCache; }
        public void setEnableResultCache(boolean enableResultCache) { this.enableResultCache = enableResultCache; }
        
        public long getSlowQueryThreshold() { return slowQueryThreshold; }
        public void setSlowQueryThreshold(long slowQueryThreshold) { this.slowQueryThreshold = slowQueryThreshold; }
    }
    
    public static class ConnectionPoolConfig {
        /**
         * 最小连接数
         */
        private int minimumIdle = 5;
        
        /**
         * 最大连接数
         */
        private int maximumPoolSize = 20;
        
        /**
         * 连接超时时间（毫秒）
         */
        private long connectionTimeout = 30000;
        
        /**
         * 空闲连接超时时间（毫秒）
         */
        private long idleTimeout = 600000;
        
        /**
         * 连接最大生命周期（毫秒）
         */
        private long maxLifetime = 1800000;
        
        /**
         * 连接泄漏检测阈值（毫秒）
         */
        private long leakDetectionThreshold = 60000;
        
        // Getters and Setters
        public int getMinimumIdle() { return minimumIdle; }
        public void setMinimumIdle(int minimumIdle) { this.minimumIdle = minimumIdle; }
        
        public int getMaximumPoolSize() { return maximumPoolSize; }
        public void setMaximumPoolSize(int maximumPoolSize) { this.maximumPoolSize = maximumPoolSize; }
        
        public long getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(long connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        
        public long getIdleTimeout() { return idleTimeout; }
        public void setIdleTimeout(long idleTimeout) { this.idleTimeout = idleTimeout; }
        
        public long getMaxLifetime() { return maxLifetime; }
        public void setMaxLifetime(long maxLifetime) { this.maxLifetime = maxLifetime; }
        
        public long getLeakDetectionThreshold() { return leakDetectionThreshold; }
        public void setLeakDetectionThreshold(long leakDetectionThreshold) { this.leakDetectionThreshold = leakDetectionThreshold; }
    }
    
    public static class IndexHintConfig {
        /**
         * 是否启用索引提示
         */
        private boolean enableIndexHints = true;
        
        /**
         * 部门表主要查询索引
         */
        private String departmentPrimaryIndex = "idx_dept_tenant_parent";
        
        /**
         * 部门树查询索引
         */
        private String departmentTreeIndex = "idx_dept_path";
        
        /**
         * 企业表主要查询索引
         */
        private String enterprisePrimaryIndex = "idx_ent_tenant_type";
        
        /**
         * 企业地理位置查询索引
         */
        private String enterpriseLocationIndex = "idx_ent_location";
        
        /**
         * 组织表主要查询索引
         */
        private String organizationPrimaryIndex = "idx_org_tenant_parent";
        
        /**
         * 组织树查询索引
         */
        private String organizationTreeIndex = "idx_org_path";
        
        // Getters and Setters
        public boolean isEnableIndexHints() { return enableIndexHints; }
        public void setEnableIndexHints(boolean enableIndexHints) { this.enableIndexHints = enableIndexHints; }
        
        public String getDepartmentPrimaryIndex() { return departmentPrimaryIndex; }
        public void setDepartmentPrimaryIndex(String departmentPrimaryIndex) { this.departmentPrimaryIndex = departmentPrimaryIndex; }
        
        public String getDepartmentTreeIndex() { return departmentTreeIndex; }
        public void setDepartmentTreeIndex(String departmentTreeIndex) { this.departmentTreeIndex = departmentTreeIndex; }
        
        public String getEnterprisePrimaryIndex() { return enterprisePrimaryIndex; }
        public void setEnterprisePrimaryIndex(String enterprisePrimaryIndex) { this.enterprisePrimaryIndex = enterprisePrimaryIndex; }
        
        public String getEnterpriseLocationIndex() { return enterpriseLocationIndex; }
        public void setEnterpriseLocationIndex(String enterpriseLocationIndex) { this.enterpriseLocationIndex = enterpriseLocationIndex; }
        
        public String getOrganizationPrimaryIndex() { return organizationPrimaryIndex; }
        public void setOrganizationPrimaryIndex(String organizationPrimaryIndex) { this.organizationPrimaryIndex = organizationPrimaryIndex; }
        
        public String getOrganizationTreeIndex() { return organizationTreeIndex; }
        public void setOrganizationTreeIndex(String organizationTreeIndex) { this.organizationTreeIndex = organizationTreeIndex; }
    }
    
    // Main class getters and setters
    public BatchConfig getBatch() { return batch; }
    public void setBatch(BatchConfig batch) { this.batch = batch; }
    
    public QueryConfig getQuery() { return query; }
    public void setQuery(QueryConfig query) { this.query = query; }
    
    public ConnectionPoolConfig getConnectionPool() { return connectionPool; }
    public void setConnectionPool(ConnectionPoolConfig connectionPool) { this.connectionPool = connectionPool; }
    
    public IndexHintConfig getIndexHint() { return indexHint; }
    public void setIndexHint(IndexHintConfig indexHint) { this.indexHint = indexHint; }
}