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
    
    /**
     * 根据左右值编码查询子部门（包括所有层级的子部门）
     * @param leftValue 左值
     * @param rightValue 右值
     * @param tenantId 租户ID
     * @return 子部门列表
     */
    List<Department> findByLeftRightValue(Integer leftValue, Integer rightValue, String tenantId);
    
    /**
     * 查询指定部门的所有祖先部门
     * @param deptId 部门ID
     * @return 祖先部门列表（按层级从高到低排序）
     */
    List<Department> findAncestors(String deptId);
    
    /**
     * 查询指定部门的所有后代部门
     * @param deptId 部门ID
     * @param includeInactive 是否包含停用部门
     * @return 后代部门列表
     */
    List<Department> findDescendants(String deptId, boolean includeInactive);
    
    /**
     * 根据层级查询部门
     * @param level 部门层级
     * @param tenantId 租户ID
     * @return 指定层级的部门列表
     */
    List<Department> findByLevel(Integer level, String tenantId);
    
    /**
     * 查询部门的直接子部门数量
     * @param deptId 部门ID
     * @return 直接子部门数量
     */
    long countDirectChildren(String deptId);
    
    /**
     * 查询部门的所有子部门数量（包括多层级）
     * @param deptId 部门ID
     * @return 所有子部门数量
     */
    long countAllDescendants(String deptId);
    
    /**
     * 检查部门是否为另一个部门的祖先
     * @param ancestorDeptId 祖先部门ID
     * @param descendantDeptId 后代部门ID
     * @return 是否为祖先关系
     */
    boolean isAncestor(String ancestorDeptId, String descendantDeptId);
    
    /**
     * 批量查询部门及其子部门
     * @param deptIds 部门ID列表
     * @param includeDescendants 是否包含子部门
     * @return 部门列表
     */
    List<Department> findDepartmentsWithChildren(List<String> deptIds, boolean includeDescendants);
    
    /**
     * 根据部门编号查询部门
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @return 部门信息
     */
    Optional<Department> findByDeptNo(String deptNo, String tenantId);
    
    /**
     * 检查部门编号是否存在
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @param excludeDeptId 排除的部门ID
     * @return 是否存在
     */
    boolean existsByDeptNo(String deptNo, String tenantId, String excludeDeptId);
    
    /**
     * 更新部门的左右值编码
     * @param deptId 部门ID
     * @param leftValue 左值
     * @param rightValue 右值
     */
    void updateLeftRightValue(String deptId, Integer leftValue, Integer rightValue);
    
    /**
     * 批量更新部门的左右值编码
     * @param departments 部门列表（包含左右值信息）
     */
    void batchUpdateLeftRightValue(List<Department> departments);
}