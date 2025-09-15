package com.jiuxi.module.role.domain.repo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.*;
import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口 - 领域层
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public interface RoleRepository {
    
    /**
     * 保存角色聚合根
     * @param role 角色聚合根
     */
    void save(Role role);
    
    /**
     * 根据角色ID查找角色
     * @param roleId 角色ID
     * @return 角色聚合根，如果不存在则返回empty
     */
    Optional<Role> findById(RoleId roleId);
    
    /**
     * 根据角色名称查找角色（用于重名检查）
     * @param roleName 角色名称
     * @return 角色聚合根，如果不存在则返回empty
     */
    Optional<Role> findByRoleName(RoleName roleName);
    
    /**
     * 根据查询规格查找角色列表
     * @param spec 查询规格
     * @return 角色列表
     */
    List<Role> findBySpec(RoleSpecification spec);
    
    /**
     * 分页查询角色列表
     * @param page 分页对象
     * @param spec 查询规格
     * @return 分页结果
     */
    IPage<Role> findPage(Page<Role> page, RoleSpecification spec);
    
    /**
     * 查找用户有权限的角色列表（用于角色授权列表查询）
     * @param personId 人员ID
     * @param deptId 部门ID
     * @param roleIds 角色ID列表
     * @return 角色列表
     */
    List<Role> findAuthorizedRoles(String personId, String deptId, List<String> roleIds);
    
    /**
     * 删除角色
     * @param roleId 角色ID
     */
    void deleteById(RoleId roleId);
    
    /**
     * 统计使用该角色的用户数量
     * @param roleId 角色ID
     * @return 用户数量
     */
    long countUsersWithRole(RoleId roleId);
}