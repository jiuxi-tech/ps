package com.jiuxi.module.org.domain.repo;

import com.jiuxi.module.org.domain.entity.Department;
import java.util.List;
import java.util.Optional;

/**
 * 部门仓储接口
 * 定义部门聚合根的持久化操作规范
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public interface DepartmentRepository {
    
    /**
     * 保存部门
     * @param department 部门聚合根
     * @return 保存后的部门
     */
    Department save(Department department);
    
    /**
     * 根据ID查找部门
     * @param deptId 部门ID
     * @return 部门信息，如果不存在则返回空
     */
    Optional<Department> findById(String deptId);
    
    /**
     * 根据父部门ID查找子部门列表
     * @param parentDeptId 父部门ID
     * @return 子部门列表
     */
    List<Department> findByParentId(String parentDeptId);
    
    /**
     * 查找所有根部门（顶级部门）
     * @return 根部门列表
     */
    List<Department> findRootDepartments();
    
    /**
     * 根据租户ID查找部门列表
     * @param tenantId 租户ID
     * @return 部门列表
     */
    List<Department> findByTenantId(String tenantId);
    
    /**
     * 根据部门名称查找部门
     * @param deptName 部门名称
     * @param tenantId 租户ID
     * @return 部门信息，如果不存在则返回空
     */
    Optional<Department> findByName(String deptName, String tenantId);
    
    /**
     * 检查部门名称是否存在
     * @param deptName 部门名称
     * @param tenantId 租户ID
     * @param excludeDeptId 排除的部门ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByName(String deptName, String tenantId, String excludeDeptId);
    
    /**
     * 删除部门
     * @param deptId 部门ID
     */
    void deleteById(String deptId);
    
    /**
     * 批量删除部门
     * @param deptIds 部门ID列表
     */
    void deleteByIds(List<String> deptIds);
    
    /**
     * 根据部门路径查找所有子部门（包括子部门的子部门）
     * @param deptPath 部门路径
     * @return 子部门列表
     */
    List<Department> findDescendants(String deptPath);
    
    /**
     * 统计部门下的用户数量
     * @param deptId 部门ID
     * @return 用户数量
     */
    long countUsersByDeptId(String deptId);
    
    /**
     * 获取部门树形结构
     * @param tenantId 租户ID
     * @return 部门树
     */
    List<Department> findDepartmentTree(String tenantId);
}