package com.jiuxi.module.role.domain.repo;

import com.jiuxi.module.role.domain.model.entity.Permission;
import com.jiuxi.module.role.domain.model.vo.PermissionId;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import java.util.List;
import java.util.Optional;

/**
 * 权限仓储接口 - 领域层
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public interface PermissionRepository {
    
    /**
     * 根据权限ID查找权限
     * @param permissionId 权限ID
     * @return 权限实体
     */
    Optional<Permission> findById(PermissionId permissionId);
    
    /**
     * 根据菜单ID查找权限
     * @param menuId 菜单ID
     * @return 权限实体
     */
    Optional<Permission> findByMenuId(String menuId);
    
    /**
     * 查找所有权限
     * @return 权限列表
     */
    List<Permission> findAll();
    
    /**
     * 根据父权限ID查找子权限列表
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> findByParentId(String parentId);
    
    /**
     * 查找根权限列表（无父权限）
     * @return 根权限列表
     */
    List<Permission> findRootPermissions();
    
    /**
     * 根据角色ID查找该角色拥有的权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> findByRoleId(RoleId roleId);
    
    /**
     * 根据权限ID列表批量查找权限
     * @param permissionIds 权限ID列表
     * @return 权限列表
     */
    List<Permission> findByIds(List<PermissionId> permissionIds);
    
    /**
     * 根据菜单ID列表批量查找权限
     * @param menuIds 菜单ID列表
     * @return 权限列表
     */
    List<Permission> findByMenuIds(List<String> menuIds);
    
    /**
     * 构建完整的权限树（用于权限管理界面）
     * @return 权限树节点列表
     */
    List<PermissionTreeNode> buildPermissionTree();
    
    /**
     * 构建指定角色的权限树（用于权限分配界面）
     * @param roleId 角色ID
     * @return 权限树节点列表，包含角色已分配的权限标记
     */
    List<PermissionTreeNode> buildRolePermissionTree(RoleId roleId);
    
    /**
     * 检查权限是否存在
     * @param permissionId 权限ID
     * @return 是否存在
     */
    boolean existsById(PermissionId permissionId);
    
    /**
     * 检查菜单是否已关联权限
     * @param menuId 菜单ID
     * @return 是否已关联
     */
    boolean existsByMenuId(String menuId);
}