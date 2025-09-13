package com.jiuxi.module.system.app.service;

import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.common.bean.TreeNode;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单应用服务接口
 * @author DDD重构
 * @date 2025-09-12
 */
public interface SystemMenuService {

    /**
     * 菜单树
     * 只有超级管理员具有操作菜单的权限，查询所有菜单树。默认展开
     * 进入该功能，menuId传空，获取全部菜单树。menuId不为空，查询下级菜单树。
     *
     * @param menuId:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-11-26 14:12
     */
    List<TreeNode> tree(String menuId);

    /**
     * 根据人员查询用户拥有的菜单
     *
     * @param pid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-07 16:58
     */
    List<MenuTreeNode> mainTree(String pid, String rids);

    /**
     * 根据人员查询用户拥有的菜单
     *
     * @param pid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-07 16:58
     */
    Set<MenuTreeNode> mainAppTree(String pid, String rids);

    /**
     * 同步查询下两级，通过第二级判断下一级是否有叶子结点
     *
     * @param menuId:
     * @param jwtpid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-01 17:48
     */
    List<TreeNode> asyncTree(String menuId, String jwtpid);

    /**
     * 新增菜单信息
     * 1。判断新增的节点是否是根节点，如果不是根节点，需要将新增节点的父节点变更为非叶子结点。
     * 2。新增节点是根节点，设置menuPid为-1
     *
     * @param vo: 菜单信息
     * @return com.jiuxi.admin.core.bean.vo.TpMenuVO
     * @author pand
     * @date 2020-11-26 14:48
     */
    TpMenuVO save(TpMenuVO vo, String pid);

    /**
     * 更新菜单信息
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    TpMenuVO update(TpMenuVO vo, String pid);

    /**
     * 查看菜单信息
     *
     * @param menuId:
     * @return com.jiuxi.admin.core.bean.vo.TpMenuVO
     * @author pand
     * @date 2020-11-26 14:48
     */
    TpMenuVO view(String menuId);

    /**
     * 逻辑删除
     *
     * @param menuId:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-11-26 15:22
     */
    int delete(String menuId, String pid);
}