package com.jiuxi.admin.core.service.domain.authorization;

import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.common.bean.JsonResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * 权限管理领域服务接口
 * 
 * 定义权限管理相关的核心业务逻辑，包括：
 * - 角色权限管理
 * - 菜单权限控制
 * - 数据权限验证
 * - 权限继承和传播
 * 
 * @author 系统重构
 * @since 2.2.2
 */
public interface AuthorizationDomainService {

    /**
     * 创建角色
     * 包含角色权限验证和初始化
     * 
     * @param roleVO 角色信息
     * @return 创建结果
     */
    JsonResponse createRole(TpRoleVO roleVO);

    /**
     * 更新角色信息
     * 确保权限变更的一致性
     * 
     * @param roleVO 角色信息
     * @return 更新结果
     */
    JsonResponse updateRole(TpRoleVO roleVO);

    /**
     * 删除角色
     * 处理角色关联的用户和权限
     * 
     * @param roleId 角色ID
     * @return 删除结果
     */
    JsonResponse deleteRole(String roleId);

    /**
     * 分页查询角色
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    Page<TpRoleVO> pageRoles(TpRoleQuery query);

    /**
     * 分配角色菜单权限
     * 
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 分配结果
     */
    JsonResponse assignRoleMenus(String roleId, List<String> menuIds);

    /**
     * 获取角色菜单权限
     * 
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<TpMenuVO> getRoleMenus(String roleId);

    /**
     * 获取用户菜单权限
     * 基于用户的所有角色计算菜单权限
     * 
     * @param personId 用户ID
     * @return 菜单树
     */
    List<TpMenuVO> getUserMenuTree(String personId);

    /**
     * 验证用户菜单权限
     * 
     * @param personId 用户ID
     * @param menuCode 菜单代码
     * @return 是否有权限
     */
    boolean hasMenuPermission(String personId, String menuCode);

    /**
     * 验证用户操作权限
     * 
     * @param personId 用户ID
     * @param operation 操作代码
     * @return 是否有权限
     */
    boolean hasOperationPermission(String personId, String operation);

    /**
     * 获取用户数据权限
     * 
     * @param personId 用户ID
     * @param resourceType 资源类型
     * @return 数据权限配置
     */
    List<TpDataPermissionsVO> getUserDataPermissions(String personId, String resourceType);

    /**
     * 验证用户数据权限
     * 
     * @param personId 用户ID
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @return 是否有权限
     */
    boolean hasDataPermission(String personId, String resourceType, String resourceId);

    /**
     * 获取用户权限范围
     * 返回用户可访问的数据范围（如部门、区域等）
     * 
     * @param personId 用户ID
     * @return 权限范围
     */
    Set<String> getUserPermissionScope(String personId);

    /**
     * 刷新用户权限缓存
     * 
     * @param personId 用户ID
     * @return 刷新结果
     */
    JsonResponse refreshUserPermissions(String personId);

    /**
     * 获取菜单树结构
     * 
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    List<TpMenuVO> getMenuTree(String parentId);

    /**
     * 创建菜单
     * 
     * @param menuVO 菜单信息
     * @return 创建结果
     */
    JsonResponse createMenu(TpMenuVO menuVO);

    /**
     * 更新菜单
     * 
     * @param menuVO 菜单信息
     * @return 更新结果
     */
    JsonResponse updateMenu(TpMenuVO menuVO);

    /**
     * 删除菜单
     * 
     * @param menuId 菜单ID
     * @return 删除结果
     */
    JsonResponse deleteMenu(String menuId);
}