package com.jiuxi.module.role.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpRole;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 角色权限数据访问层接口
 * @author DDD重构
 * @date 2025-09-12
 */
@Mapper
public interface RoleMapper {

    /**
     * 查询当前登录人拥有的角色
     * @param query 查询条件
     * @return 角色列表
     */
    LinkedHashSet<TpRoleVO> selectRoleAuthList(@Param("query") TpRoleAuthQuery query);

    /**
     * 分页查询角色列表
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpRoleVO> selectPage(Page<TpRoleVO> page, @Param("query") TpRoleQuery query);

    /**
     * 查询角色列表（不分页）
     * @param query 查询条件
     * @return 角色列表
     */
    List<TpRoleVO> selectList(@Param("query") TpRoleQuery query);

    /**
     * 新增角色
     * @param role 角色对象
     * @return 影响行数
     */
    int save(TpRole role);

    /**
     * 更新角色信息
     * @param role 角色对象
     * @return 影响行数
     */
    int update(TpRole role);

    /**
     * 查看角色详情
     * @param roleId 角色ID
     * @return 角色详情
     */
    TpRoleVO view(@Param("roleId") String roleId);

    /**
     * 根据角色ID查询角色人员关系
     * @param roleId 角色ID
     * @return 角色人员关系列表
     */
    List<TpPersonRoleVO> selectByRoleId(@Param("roleId") String roleId);

    /**
     * 删除角色
     * @param roleId 角色ID
     * @return 影响行数
     */
    int delete(@Param("roleId") String roleId);

    /**
     * 获取权限树
     * @param roleId 角色ID
     * @param rids 角色ID集合
     * @param pid 人员ID
     * @return 权限树
     */
    List<TreeNode> selectAuthTree(@Param("roleId") String roleId, @Param("rids") String rids, @Param("pid") String pid);

    /**
     * 删除角色菜单关系
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleMenus(@Param("roleId") String roleId);

    /**
     * 批量插入角色菜单关系
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     * @return 影响行数
     */
    int insertRoleMenus(@Param("roleId") String roleId, @Param("menuIds") List<String> menuIds);
}