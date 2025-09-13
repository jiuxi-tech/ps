package com.jiuxi.admin.core.mapper;

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
 * @ClassName: TpMenuMapper
 * @Description: 菜单表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMenuMapper {

    /**
     * 查询所有节点
     *
     * @param creator:
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpMenuVO>
     * @author pand
     * @date 2020-11-26 17:01
     */
    LinkedHashSet<TreeNode> selectByCreator(@Param("creator") String creator, @Param("enabled") String enabled);

    /**
     * 根据父节点id查询下级书结构
     *
     * @param menuPid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-11-26 17:02
     */
    List<TreeNode> selectChildren(@Param("menuPid") String menuPid);

    int save(TpMenu bean);

    int update(TpMenu bean);

    TpMenuVO view(String menuId);

    int deleteByMenuId(TpMenu bean);

    LinkedHashSet<MenuTreeNode> selectByRoldIds(@Param("menuSource") Integer menuSource, @Param("roleIds") List<String> roleIds);

    LinkedHashSet<TreeNode> authSelectByRoldIds(@Param("roleIds") List<String> roleIds);

    LinkedHashSet<MenuTreeNode> selectAll(Integer menuSource);

    int countChildren(@Param("menuPid") String menuPid);

    /**
     * 超管查询所有菜单  【注： pid 取的是 menuTreePid】
     *
     * @return java.util.Set<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2024/5/16 14:59
     */
    List<TreeNode> listAll(@Param("menuId") String menuId);

    /**
     * 根据菜单id，查询所有下级
     *
     * @param menuId
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpMenuVO>
     * @author 杨占锐
     * @date 2024/5/16 17:34
     */
    List<TpMenuVO> listChildrenByMenuId(@Param("menuId") String menuId);
}
