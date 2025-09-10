package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.common.bean.TreeNode;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName: TpRoleService
 * @Description: 角色表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpRoleService {

    LinkedHashSet<TpRoleVO> roleAuthList(TpRoleAuthQuery query, String pid, String deptId, String rids, String jwtaid);

    IPage<TpRoleVO> queryPage(TpRoleQuery query, String pid, String rids, String jwtaid);

    List<TpRoleVO> getList(TpRoleQuery query, String jwtaid);

    int add(TpRoleVO vo, String pid, String aid, int category);

    int update(TpRoleVO vo, String pid);

    TpRoleVO view(String roleId);

    List<TpPersonRoleVO> selectByRoleId(String roleId);

    int delete(String roleId, String creator, String pid);

    List<TreeNode> authTree(String roleId, String rids, String pid);

    int roleMenus(String roleId, String menuIds);
}

