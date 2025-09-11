package com.jiuxi.module.org.infra.persistence.mapper;

import com.jiuxi.module.org.infra.persistence.entity.DepartmentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 部门持久化映射器
 * 基于现有的 TpDeptBasicinfoMapper 进行适配
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Mapper
public interface DepartmentMapper {
    
    /**
     * 插入部门
     * @param department 部门持久化对象
     * @return 影响行数
     */
    int insert(DepartmentPO department);
    
    /**
     * 根据ID更新部门
     * @param department 部门持久化对象
     * @return 影响行数
     */
    int updateById(DepartmentPO department);
    
    /**
     * 根据ID查询部门
     * @param deptId 部门ID
     * @return 部门持久化对象
     */
    Optional<DepartmentPO> selectById(@Param("deptId") String deptId);
    
    /**
     * 根据父部门ID查询子部门
     * @param parentDeptId 父部门ID
     * @return 子部门列表
     */
    List<DepartmentPO> selectByParentId(@Param("parentDeptId") String parentDeptId);
    
    /**
     * 根据租户ID查询根部门
     * @param tenantId 租户ID
     * @return 根部门列表
     */
    List<DepartmentPO> selectRootDepartments(@Param("tenantId") String tenantId);
    
    /**
     * 根据租户ID查询所有部门
     * @param tenantId 租户ID
     * @return 部门列表
     */
    List<DepartmentPO> selectByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 根据部门名称查询部门
     * @param deptName 部门名称
     * @param tenantId 租户ID
     * @return 部门持久化对象
     */
    Optional<DepartmentPO> selectByName(@Param("deptName") String deptName, @Param("tenantId") String tenantId);
    
    /**
     * 检查部门名称是否存在
     * @param deptName 部门名称
     * @param tenantId 租户ID
     * @param excludeDeptId 排除的部门ID
     * @return 存在的数量
     */
    int countByName(@Param("deptName") String deptName, @Param("tenantId") String tenantId, @Param("excludeDeptId") String excludeDeptId);
    
    /**
     * 逻辑删除部门
     * @param deptId 部门ID
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteById(@Param("deptId") String deptId, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量逻辑删除部门
     * @param deptIds 部门ID列表
     * @param updator 更新人
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int deleteByIds(@Param("deptIds") List<String> deptIds, @Param("updator") String updator, @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 根据部门路径查询所有子部门
     * @param deptPath 部门路径前缀
     * @return 子部门列表
     */
    List<DepartmentPO> selectDescendants(@Param("deptPath") String deptPath);
    
    /**
     * 统计部门下的用户数量
     * @param deptId 部门ID
     * @return 用户数量
     */
    long countUsersByDeptId(@Param("deptId") String deptId);
    
    /**
     * 获取部门树形结构
     * @param tenantId 租户ID
     * @return 部门列表（按层级排序）
     */
    List<DepartmentPO> selectDepartmentTree(@Param("tenantId") String tenantId);
    
    /**
     * 根据左右值编码查询子部门
     * @param leftValue 左值
     * @param rightValue 右值
     * @param tenantId 租户ID
     * @return 子部门列表
     */
    List<DepartmentPO> selectByLeftRightValue(@Param("leftValue") Integer leftValue, @Param("rightValue") Integer rightValue, @Param("tenantId") String tenantId);
    
    /**
     * 查询指定部门的祖先部门
     * @param deptPath 部门路径
     * @return 祖先部门列表
     */
    List<DepartmentPO> selectAncestors(@Param("deptPath") String deptPath);
    
    /**
     * 查询所有后代部门（包含停用）
     * @param deptPath 部门路径前缀
     * @return 后代部门列表
     */
    List<DepartmentPO> selectAllDescendants(@Param("deptPath") String deptPath);
    
    /**
     * 查询激活的后代部门
     * @param deptPath 部门路径前缀
     * @return 激活的后代部门列表
     */
    List<DepartmentPO> selectActiveDescendants(@Param("deptPath") String deptPath);
    
    /**
     * 根据层级查询部门
     * @param level 部门层级
     * @param tenantId 租户ID
     * @return 指定层级的部门列表
     */
    List<DepartmentPO> selectByLevel(@Param("level") Integer level, @Param("tenantId") String tenantId);
    
    /**
     * 统计直接子部门数量
     * @param deptId 部门ID
     * @return 直接子部门数量
     */
    long countDirectChildren(@Param("deptId") String deptId);
    
    /**
     * 统计所有后代部门数量
     * @param deptPath 部门路径前缀
     * @return 所有后代部门数量
     */
    long countAllDescendants(@Param("deptPath") String deptPath);
    
    /**
     * 查询部门及其后代部门
     * @param deptIds 部门ID列表
     * @return 部门及后代部门列表
     */
    List<DepartmentPO> selectDepartmentsWithDescendants(@Param("deptIds") List<String> deptIds);
    
    /**
     * 查询部门及其直接子部门
     * @param deptIds 部门ID列表
     * @return 部门及直接子部门列表
     */
    List<DepartmentPO> selectDepartmentsWithDirectChildren(@Param("deptIds") List<String> deptIds);
    
    /**
     * 根据部门编号查询部门
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @return 部门持久化对象
     */
    Optional<DepartmentPO> selectByDeptNo(@Param("deptNo") String deptNo, @Param("tenantId") String tenantId);
    
    /**
     * 检查部门编号是否存在
     * @param deptNo 部门编号
     * @param tenantId 租户ID
     * @param excludeDeptId 排除的部门ID
     * @return 存在的数量
     */
    int countByDeptNo(@Param("deptNo") String deptNo, @Param("tenantId") String tenantId, @Param("excludeDeptId") String excludeDeptId);
    
    /**
     * 更新部门的左右值编码
     * @param deptId 部门ID
     * @param leftValue 左值
     * @param rightValue 右值
     */
    void updateLeftRightValue(@Param("deptId") String deptId, @Param("leftValue") Integer leftValue, @Param("rightValue") Integer rightValue);
    
    /**
     * 批量更新部门的左右值编码
     * @param departments 部门列表
     */
    void batchUpdateLeftRightValue(@Param("departments") List<DepartmentPO> departments);
}