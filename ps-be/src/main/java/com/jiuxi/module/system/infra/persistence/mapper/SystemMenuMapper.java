package com.jiuxi.module.system.infra.persistence.mapper;

import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.admin.core.bean.entity.TpMenu;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单数据访问层接口
 * @author DDD重构
 * @date 2025-09-12
 */
@Mapper
public interface SystemMenuMapper {

    /**
     * 查询所有节点
     * @param creator
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpMenuVO>
     */
    List<TpMenuVO> selectList(@Param("creator") String creator);

    /**
     * 根据父菜单ID查询子菜单
     * @param menuPid 父菜单ID
     * @return 子菜单列表
     */
    List<TreeNode> selectChildren(@Param("menuPid") String menuPid);

    /**
     * 根据人员ID和角色ID查询菜单
     * @param pid 人员ID
     * @param rids 角色ID集合
     * @return 菜单列表
     */
    List<MenuTreeNode> selectByPersonAndRoles(@Param("pid") String pid, @Param("rids") String rids);

    /**
     * 根据人员ID和角色ID查询APP菜单
     * @param pid 人员ID
     * @param rids 角色ID集合
     * @return APP菜单列表
     */
    Set<MenuTreeNode> selectAppMenuByPersonAndRoles(@Param("pid") String pid, @Param("rids") String rids);

    /**
     * 异步查询菜单树
     * @param menuId 菜单ID
     * @param jwtpid 用户ID
     * @return 菜单树节点
     */
    List<TreeNode> selectAsyncTree(@Param("menuId") String menuId, @Param("jwtpid") String jwtpid);

    /**
     * 保存菜单
     * @param menu 菜单对象
     * @return 影响行数
     */
    int save(TpMenu menu);

    /**
     * 更新菜单
     * @param menu 菜单对象
     * @return 影响行数
     */
    int update(TpMenu menu);

    /**
     * 根据菜单ID查询菜单详情
     * @param menuId 菜单ID
     * @return 菜单详情
     */
    TpMenuVO view(@Param("menuId") String menuId);

    /**
     * 删除菜单
     * @param menuId 菜单ID
     * @return 影响行数
     */
    int delete(@Param("menuId") String menuId);

    /**
     * 查询子菜单数量
     * @param menuId 菜单ID
     * @return 子菜单数量
     */
    int selectChildrenCount(@Param("menuId") String menuId);

    /**
     * 更新父菜单的叶子节点标识
     * @param menuPid 父菜单ID
     * @param isLeaf 是否为叶子节点
     * @return 影响行数
     */
    int updateParentLeaf(@Param("menuPid") String menuPid, @Param("isLeaf") int isLeaf);
}