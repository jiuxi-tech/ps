package com.jiuxi.module.auth.domain.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据权限实体
 * 定义具体的数据访问权限规则
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class DataPermission {
    
    /**
     * 数据权限ID
     */
    private String dataPermissionId;
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限描述
     */
    private String permissionDesc;
    
    /**
     * 数据权限范围
     */
    private DataScope dataScope;
    
    /**
     * 自定义部门列表（当dataScope为CUSTOM时使用）
     */
    private List<String> customDeptIds;
    
    /**
     * 数据过滤SQL条件
     */
    private String filterCondition;
    
    /**
     * 影响的数据表
     */
    private List<String> affectedTables;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造器
    public DataPermission() {
        this.customDeptIds = new ArrayList<>();
        this.affectedTables = new ArrayList<>();
        this.enabled = true;
        this.dataScope = DataScope.SELF;
    }
    
    public DataPermission(String permissionName, DataScope dataScope) {
        this();
        this.permissionName = permissionName;
        this.dataScope = dataScope;
    }
    
    // 业务方法
    
    /**
     * 添加自定义部门
     */
    public void addCustomDept(String deptId) {
        if (deptId != null && !this.customDeptIds.contains(deptId)) {
            this.customDeptIds.add(deptId);
        }
    }
    
    /**
     * 移除自定义部门
     */
    public void removeCustomDept(String deptId) {
        this.customDeptIds.remove(deptId);
    }
    
    /**
     * 清空自定义部门
     */
    public void clearCustomDepts() {
        this.customDeptIds.clear();
    }
    
    /**
     * 添加影响的数据表
     */
    public void addAffectedTable(String tableName) {
        if (tableName != null && !this.affectedTables.contains(tableName)) {
            this.affectedTables.add(tableName);
        }
    }
    
    /**
     * 移除影响的数据表
     */
    public void removeAffectedTable(String tableName) {
        this.affectedTables.remove(tableName);
    }
    
    /**
     * 启用数据权限
     */
    public void enable() {
        this.enabled = true;
    }
    
    /**
     * 停用数据权限
     */
    public void disable() {
        this.enabled = false;
    }
    
    /**
     * 检查是否启用
     */
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.enabled);
    }
    
    /**
     * 检查是否是全部数据权限
     */
    public boolean isAllDataScope() {
        return DataScope.ALL.equals(this.dataScope);
    }
    
    /**
     * 检查是否是部门数据权限
     */
    public boolean isDeptDataScope() {
        return DataScope.DEPT.equals(this.dataScope);
    }
    
    /**
     * 检查是否是部门及以下数据权限
     */
    public boolean isDeptAndChildDataScope() {
        return DataScope.DEPT_AND_CHILD.equals(this.dataScope);
    }
    
    /**
     * 检查是否是仅本人数据权限
     */
    public boolean isSelfDataScope() {
        return DataScope.SELF.equals(this.dataScope);
    }
    
    /**
     * 检查是否是自定义数据权限
     */
    public boolean isCustomDataScope() {
        return DataScope.CUSTOM.equals(this.dataScope);
    }
    
    /**
     * 生成数据过滤条件
     * @param userId 当前用户ID
     * @param deptId 当前用户部门ID
     * @param tableName 表名
     * @return SQL WHERE条件
     */
    public String generateFilterCondition(String userId, String deptId, String tableName) {
        if (!isEnabled()) {
            return "1=0"; // 禁用时返回false条件
        }
        
        switch (this.dataScope) {
            case ALL:
                return "1=1"; // 允许查看所有数据
                
            case SELF:
                return tableName + ".creator = '" + userId + "'";
                
            case DEPT:
                return tableName + ".dept_id = '" + deptId + "'";
                
            case DEPT_AND_CHILD:
                // 需要结合部门树查询，这里返回基础条件
                return tableName + ".dept_id IN (SELECT dept_id FROM tp_dept_basicinfo " +
                       "WHERE dept_path LIKE (SELECT CONCAT(dept_path, '%') FROM tp_dept_basicinfo WHERE dept_id = '" + deptId + "'))";
                
            case CUSTOM:
                if (customDeptIds.isEmpty()) {
                    return "1=0"; // 没有自定义部门时禁止访问
                }
                return tableName + ".dept_id IN ('" + String.join("','", customDeptIds) + "')";
                
            default:
                return "1=0"; // 默认禁止访问
        }
    }
    
    /**
     * 验证数据权限配置的有效性
     */
    public boolean isValid() {
        if (permissionName == null || permissionName.trim().isEmpty()) {
            return false;
        }
        
        if (dataScope == null) {
            return false;
        }
        
        // 自定义权限必须有自定义部门
        if (DataScope.CUSTOM.equals(dataScope) && customDeptIds.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    // Getters and Setters
    public String getDataPermissionId() {
        return dataPermissionId;
    }
    
    public void setDataPermissionId(String dataPermissionId) {
        this.dataPermissionId = dataPermissionId;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getPermissionDesc() {
        return permissionDesc;
    }
    
    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }
    
    public DataScope getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
    }
    
    public List<String> getCustomDeptIds() {
        return customDeptIds;
    }
    
    public void setCustomDeptIds(List<String> customDeptIds) {
        this.customDeptIds = customDeptIds != null ? customDeptIds : new ArrayList<>();
    }
    
    public String getFilterCondition() {
        return filterCondition;
    }
    
    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }
    
    public List<String> getAffectedTables() {
        return affectedTables;
    }
    
    public void setAffectedTables(List<String> affectedTables) {
        this.affectedTables = affectedTables != null ? affectedTables : new ArrayList<>();
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPermission that = (DataPermission) o;
        return Objects.equals(dataPermissionId, that.dataPermissionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dataPermissionId);
    }
}